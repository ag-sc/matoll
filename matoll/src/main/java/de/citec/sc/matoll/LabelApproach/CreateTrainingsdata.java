package de.citec.sc.matoll.LabelApproach;




import java.io.IOException;
import java.util.HashSet;

import static de.citec.sc.matoll.core.Language.EN;

import de.citec.sc.matoll.utils.OntologyImporter;
import de.citec.sc.matoll.utils.StanfordLemmatizer;
import de.citec.sc.matoll.utils.Wordnet;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.FileInputStream;
import java.io.PrintWriter;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;


import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
//import weka.classifiers.trees.J48;

/*
 * Good description for using Weka with Java:
 * http://stackoverflow.com/questions/21674522/get-prediction-percentage-in-weka-using-own-java-code-and-a-model/21678307#21678307
 */
public class CreateTrainingsdata {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String path_to_write_arff = "adjective.arff";
		String path_weka_model = path_to_write_arff.replace(".arff", ".model");
		String path_to_wordnet = "/Users/swalter/Backup/Software/WordNet-3.0";
		String path_to_objects = "/Users/swalter/Downloads/tmp_extractPropertiesWithData/results/ontology/";
		/*
		 * TODO: Automatically import via maven
		 */
		
		//String path_to_tagger_model ="resources/english-left3words/english-caseless-left3words-distsim.tagger";
                String path_to_tagger_model ="resources/english-caseless-left3words-distsim.tagger";
		SMO smo = new SMO();
//                J48 smo = new J48();
                smo.setOptions(weka.core.Utils.splitOptions("-M"));
                Classifier cls = smo; 
                
                final StanfordLemmatizer sl = new StanfordLemmatizer(EN);
	
                String path_to_input_file = "../dbpedia_2014.owl";
//                String path_to_input_file = "test.txt";
                Set<String> properties = new HashSet<>();
                Set<String> classes = new HashSet<>();
                if(path_to_input_file.endsWith(".txt")){
                    properties = loadPropertyList(path_to_input_file);
                }
                else{
                    OntologyImporter importer = new OntologyImporter(path_to_input_file,"RDF/XML");
                    properties = importer.getProperties();
                    classes = importer.getClasses();
                }
		
		
		ExtractData adjectiveExtractor = new ExtractData(path_to_wordnet);
		
		MaxentTagger tagger = new MaxentTagger(path_to_tagger_model);
		Morphology mp = new Morphology();
                
                Wordnet wordnet = new Wordnet(path_to_wordnet);
		int min = 1;
                int max = properties.size()-1;
                List<String> tmp_properties = new ArrayList<>();
                for(String p:properties)tmp_properties.add(p);
                Set<String> random_properties = new HashSet<>();
                ExtractData ed = new ExtractData(path_to_wordnet);
                Random rn = new Random();
                for(int i = 0;i<200;i++)
                   try{
                       String uri = tmp_properties.get(rn.nextInt(max - min + 1) + min);
                    Set<String> resultSet = new HashSet<>();
                    List<AdjectiveObject> adjectiveObjects = ed.start(path_to_objects, uri, tagger, mp);
                    for(AdjectiveObject adjectiveObject : adjectiveObjects){
                        String result = "\t"+adjectiveObject.getAdjectiveTerm()+"\t"+adjectiveObject.getObject()+"\t"+adjectiveObject.getObjectURI()+"\t"+uri+"\n";
                        resultSet.add(result);
                    }
                    List<String> result_tmp = new ArrayList<>();
                    List<String> results = new ArrayList<>();
                    for(String x:resultSet)result_tmp.add(x);
                    int local_min = 1;
                    int local_max = result_tmp.size()-1;
                    Random rndm = new Random();
                    if(local_max>20){
                        for(int j = 0;j<20;j++){
                         results.add(result_tmp.get(rndm.nextInt(local_max - local_min + 1) + local_min));
                        }
                        PrintWriter writer = new PrintWriter("/Users/swalter/Git/matoll/matoll/resources/toAnnotate/"+uri.replace("/", "_").replace(":","")+".tsv");                
                        for(String x :results) writer.write(x);
                        writer.close();
                    }
                    
                    
//                    List<String> result = createOutput(ed.getRawObjects(path_to_objects, uri, "en"),uri);
//                    for(String x :result) System.out.println(x);
                   }
                catch(Exception e){e.printStackTrace();}
	

		
		
		
		
		
		 
		 
		 
		
	}


        
         private static boolean isAlpha(String label) {
            if(label.length()<=2) return false;
            label = cleanTerm(label);
            label = label.replace("_","");
            char[] chars = label.toCharArray();
            
            for (char c : chars) {
                if(!Character.isLetter(c)) {
                    return false;
                }
            }

            return true;
        }
         
         private static String cleanTerm(String input){
            String output = input.replace("ü", "ue")
                    .replace("ö", "oe")
                    .replace("ß", "ss")
                    .replace("-", "_")
                    .replace(" ", "_")
                    .replace("\"", "")
                    .replace("+", "_");
            return output;
        }



        
        private static String frag(String uri) {
            
            uri = uri.replace("=","");
            if (uri.contains("[")){
                uri = uri.split("\\[")[0];
            }
            uri = uri.replace("!","");
            uri = uri.replace("?","");
            uri = uri.replace("*","");
            uri = uri.replace(",","");
            uri = uri.replace("(","");
            uri = uri.replace(")","");
            uri = uri.replace("|"," ");
            String  pattern =  ".+(/|#)(\\w+)";
            Matcher matcher = (Pattern.compile(pattern)).matcher(uri);
        
            while (matcher.find()) {
                  return matcher.group(2).replace(" ","_").replace(".","");
            }
            
            return uri.replace(" ","_").replace(".","");
        }
        
            
    	private static Set<String> loadPropertyList(String pathToProperties) throws IOException {
            Set<String> properties = new HashSet<>();
            String properties_raw = "";
            /*
             * each line contains one property
             */
            FileInputStream inputStream = new FileInputStream(pathToProperties);
	    try {
	        properties_raw = IOUtils.toString(inputStream);
	    } finally {
	        inputStream.close();
	    }
	    
	    for(String p: properties_raw.split("\n")){
                properties.add(p);
	    }
            
            return properties;
		
	}

//    private static List<String> createOutput(List<List<String>> rawObjects, String uri) {
//        for(List<String> tmp : rawObjects){
//            String property_object = tmp.get(0);
//            if(property_object.contains(" ")){
//
//                String[] object_tmp = property_object.split(" ");
//                for(String term : object_tmp){
//                        //System.out.println("term:"+term);
//                        if((wordnet.checkForAdjective(term.toLowerCase())|| dbnaray_adjective_list.containsKey(term.toLowerCase()))
//                                        &&!stopwords.contains(term.toLowerCase())){
//                                adjectiveobject.setAdjectiveTerm(term);
//                                adjectiveobject.setAdjective(true);
//                                adjectiveobject.setSublabel(getSublabel(term));
//                                adjectiveobject.setPattern(property_object.replace(term, "<adj>"));
//                                adjectiveobject.setPos_Pattern(getPosPattern(term,tagger));
//                                adjectiveobject.setPos_adj_Pattern(getPosPattern(property_object.replace(term, "<adj>"),tagger));
//                        }
//                }
//            }
//        }
//        
//                
//        else {
//                if((wordnet.checkForAdjective(property_object.toLowerCase())|| dbnaray_adjective_list.containsKey(property_object.toLowerCase()))
//                            &&!stopwords.contains(property_object.toLowerCase())){
//                        adjectiveobject.setAdjectiveTerm(property_object);
//                        adjectiveobject.setAdjective(true);
//                        adjectiveobject.setSublabel(getSublabel(property_object));
//                        adjectiveobject.setPattern("<adj>");
//                        adjectiveobject.setPos_adj_Pattern("<adj>");
//                        adjectiveobject.setPos_Pattern(getPosPattern(property_object,tagger));
//                }
//        }
//    }



}