/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.classifiers;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Provenance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author swalter
 */
public class WEKAclassifier {
    SMO smo;
    Classifier cls;
    Language Language;
    
    public WEKAclassifier(Language language) throws Exception{
        this.smo = new SMO();
        this.smo.setOptions(weka.core.Utils.splitOptions("-M"));
        this.cls = smo; 
        this.Language=language;
    }

    public void train(List<Provenance> provenances) throws IOException {
        String path = "matoll"+Language.toString()+".arff";
        writeVectors(provenances,path);
        Instances inst = new Instances(new BufferedReader(new FileReader(path)));
         inst.setClassIndex(inst.numAttributes() - 1);
         try {
                cls.buildClassifier(inst);
                // serialize model
                 ObjectOutputStream oos = new ObjectOutputStream(
                                            new FileOutputStream(path.replace(".arff", ".model")));
                 oos.writeObject(cls);
                 oos.flush();
                 oos.close();
        } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
    }
    


    public HashMap<Integer, Double> predict(Provenance provenance) throws IOException, Exception {
        
        /*
        we want predict that the entry is true
        */
        provenance.setAnnotation(1);
        List<Provenance> tmp_prov = new ArrayList<Provenance>();
        tmp_prov.add(provenance);
        writeVectors(tmp_prov,"tmp.arff");
        
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File("tmp.arff"));
        Instances structure = loader.getStructure();
        structure.setClassIndex(structure.numAttributes() - 1);
        HashMap<Integer,Double> hm = new HashMap<Integer,Double>();
        
        Instance current;
	while ((current = loader.getNextInstance(structure)) != null){
             /*
             * value_to_predict
             * can be only 0 or 1, as only two classes are given 
             */

            double value=cls.classifyInstance(current);
            double[] percentage=cls.distributionForInstance(current);

            List<String> result = new ArrayList<String>();
            int prediction = (int)value;
            double distribution = percentage[(int) value];
            hm.put(prediction, distribution);
        }        
        return hm;
    }
    

    public void saveModel(String file) throws IOException {
        System.out.println("Automatically saved during training");
    }


    public void loadModel(String file) throws IOException, Exception {
        this.cls = (Classifier) weka.core.SerializationHelper.read(file);
    }

    private void writeVectors(List<Provenance> provenance, String path) {
        String output ="@relation matoll\n"
            +"@attribute 'normalizedFrequency' numeric\n"
            +"@attribute 'averageLenght' numeric\n"
            +"@attribute 'overallRatioLabel' numeric\n"
            +"@attribute 'class' {0,1}\n"
            +"@data\n";
        
        output = provenance.stream().map((prov) -> prov.getFrequency().toString()
                +","+prov.getAvaerage_lenght().toString()
                +","+prov.getOveralLabelRatio().toString()
                +","+prov.getAnnotation().toString()+"\n").reduce(output, String::concat);
        
        
        PrintWriter writer;
        try {
                writer = new PrintWriter(path);
                writer.println(output);
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        
       
    }
    
    
}
