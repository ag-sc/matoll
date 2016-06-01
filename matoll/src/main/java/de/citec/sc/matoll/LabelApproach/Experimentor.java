package de.citec.sc.matoll.LabelApproach;



import com.google.common.collect.Sets;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class Experimentor {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String path_annotatedFiles = "resources/annotatedAdjectives/";
		String path_raw_files = "resources/plainAdjectives/";
		String path_to_write_arff = "adjective.arff";
                String path_to_tagger_model ="resources/english-caseless-left3words-distsim.tagger";
                
                //classifier
		RandomForest classifier = new RandomForest();
                //J48 classifier = new J48();
                //SMO classifier = new SMO();
                
                String classifier_name = classifier.getClass().toString().replace("class ", "");
                Set<String> feature_list = new HashSet<String>();
                feature_list.add("NAF");
                feature_list.add("Trigrams");
                feature_list.add("Bigrams");
                feature_list.add("PR");
                feature_list.add("POSPR");
                feature_list.add("AR");
                feature_list.add("PAP");
                feature_list.add("NOF");
                feature_list.add("PP");
                feature_list.add("NLD");
                feature_list.add("AP");
                feature_list.add("AFP");
                feature_list.add("ALP");
                List<String> output = new ArrayList<>();
                Set<Set<String>> combinations_feature = Sets.powerSet(feature_list);
                int set_min = 6;
                
                Set<Set<String>> combinations_feature_new = new HashSet<>();
                for(Set<String> s : combinations_feature){
                    if(s.size()>=set_min) combinations_feature_new.add(s);
                }
                
                int counter = 0;
                int number_combinations = combinations_feature_new.size();
                MaxentTagger tagger = new MaxentTagger(path_to_tagger_model);
                for(Set<String> combination : combinations_feature_new){
                    counter+=1;
                    LabelFeature label_feature = new LabelFeature();
                    label_feature.setFeature(combination);
                    String feature_name = "feature";
                    for(String s: combination) feature_name += "_"+s;

                    File f = new File("resources/arff_combinations/"+feature_name+".arff");
                    if(!f.exists()) { 
                        /*
                         * Overall Feature
                         */
                        HashSet<String> posAdj = new HashSet<String>();
                        HashSet<String> pos = new HashSet<String>();
                        HashSet<String> label_3 = new HashSet<String>();
                        HashSet<String> label_2 = new HashSet<String>();

                        try {
                                GenerateArff.run(path_annotatedFiles, path_raw_files, "resources/arff_combinations/"+feature_name+".arff",label_3,label_2,pos,posAdj,tagger,label_feature);
                        } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                    }
                    
                    System.out.println("create "+counter+"/"+number_combinations);
                    
                }
                counter = 0;
                
                for(Set<String> combination : combinations_feature_new){
                    counter+=1;
                    LabelFeature label_feature = new LabelFeature();
                    label_feature.setFeature(combination);
                    String feature_name = "feature";
                    for(String s: combination) feature_name += "_"+s;
                    
                    try {

                        BufferedReader bReader = readDataFile("resources/arff_combinations/"+feature_name+".arff");
                        Instances train = new Instances(bReader);
                        train.setClassIndex(train.numAttributes() -1); //last attribute is the class attribute


                        Evaluation eval=new Evaluation(train);
                        eval.crossValidateModel(classifier, train, 10, new Random(1));
                        System.out.println(counter+"/"+number_combinations+"  evaluated "+combination.toString()+":"+Double.toString(eval.pctCorrect()));
                        output.add(combination.toString()+"\t"+Double.toString(eval.pctCorrect()));

                    } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();

                    }
                }
            PrintWriter writer;
            try {
                    writer = new PrintWriter(classifier_name+"_"+set_min+"_results_feature_combinations.tsv");
                    for(String s : output){
                        writer.write(s+"\n");
                    }
                    writer.close();
            } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }  
		
	}




        public static BufferedReader readDataFile(String filename) {
            BufferedReader inputReader = null;

            try {
                inputReader = new BufferedReader(new FileReader(filename));
            } catch (FileNotFoundException ex) {
                System.err.println("File not found: " + filename);
            }

            return inputReader;
        }

    
}