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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author swalter
 */
public class WEKAclassifier {
//    SMO smo;
    Logistic log;
    Classifier cls;
    Language Language;
    
    public WEKAclassifier(Language language) throws Exception{
//        this.smo = new SMO();
//        this.smo.setOptions(weka.core.Utils.splitOptions("-M"));
//        this.cls = smo; 
        
        this.log = new Logistic();
//        this.log.setOptions(weka.core.Utils.splitOptions("-M"));
        this.cls = log; 
        this.Language=language;
    }

    public void train(List<Provenance> provenances,Set<String> pattern_lookup,Set<String> pos_lookup) throws IOException {
        String path = "matoll"+Language.toString()+".arff";
        writeVectors(provenances,path,pattern_lookup,pos_lookup);
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
    


    public HashMap<Integer, Double> predict(Provenance provenance,Set<String> pattern_lookup,Set<String> pos_lookup) throws IOException, Exception {
        
        /*
        we want predict that the entry is true
        */
        provenance.setAnnotation(1);
        List<Provenance> tmp_prov = new ArrayList<Provenance>();
        tmp_prov.add(provenance);
        writeVectors(tmp_prov,"tmp.arff",pattern_lookup, pos_lookup);
        
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

    private void writeVectors(List<Provenance> provenance, String path,Set<String> pattern_lookup,Set<String> pos_lookup) {
        String output ="@relation matoll\n"
            +"@attribute 'normalizedFrequency' numeric\n"
            +"@attribute 'averageLenght' numeric\n"
            +"@attribute 'overallRatioLabel' numeric\n"
            +"@attribute 'numberPattern' numeric\n"
            +"@attribute 'overallRatioEntries' numeric\n";
        output = pattern_lookup.stream().map((p) -> "@attribute '"+p+"' {0,1}\n").reduce(output, String::concat);
        output = pos_lookup.stream().map((s) -> "@attribute 'pos_"+s.split("#")[1]+"' {0,1}\n").reduce(output, String::concat);
        output+="@attribute 'class' {0,1}\n"
            +"@data\n";
        
        for(Provenance prov:provenance){
            output+=prov.getFrequency().toString()
                +","+prov.getAvaerage_lenght().toString()
                +","+prov.getOveralLabelRatio().toString()
                +","+prov.getPatternset().size()
                +","+prov.getOverallPropertyEntryRatio();
            HashSet<String> patterns = prov.getPatternset();
            for(String p:pattern_lookup){
                if(patterns.contains(p)) output+=",1";
                else output+=",0";
            }
            for(String p:pos_lookup){
                if(prov.getPOS().equals(p)) output+=",1";
                else output+=",0";
            }
            output+=","+prov.getAnnotation().toString()+"\n";
            
        }
            
        
        
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
