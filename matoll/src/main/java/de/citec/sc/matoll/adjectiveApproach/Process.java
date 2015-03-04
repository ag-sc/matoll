package de.citec.sc.matoll.adjectiveApproach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.utils.OntologyImporter;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/*
 * Good description for using Weka with Java:
 * http://stackoverflow.com/questions/21674522/get-prediction-percentage-in-weka-using-own-java-code-and-a-model/21678307#21678307
 */
public class Process {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String path_annotatedFiles = "/Users/swalter/Documents/annotatedAdjectives/";
		String path_raw_files = "/Users/swalter/Documents/plainAdjectives/";
		String path_to_write_arff = "/Users/swalter/Desktop/adjective.arff";
		String path_weka_model = path_to_write_arff.replace(".arff", ".model");
		String path_to_wordnet = "/Users/swalter/Software/WordNet-3.0";
		String path_to_objects = "/Users/swalter/Desktop/Resources/";
		String path_to_tagger_model = "/Users/swalter/Software/stanford_models/english-caseless-left3words-distsim.tagger";
		Classifier cls = new SMO();
		
		OntologyImporter importer = new OntologyImporter("/Users/swalter/Downloads/dbpedia_2014.owl","RDF/XML");
		
		ExtractData adjectiveExtractor = new ExtractData(path_to_wordnet);
		
		MaxentTagger tagger = new MaxentTagger(path_to_tagger_model);
		Morphology mp = new Morphology();
		
		/*
		 * Overall Feature
		 */
		HashSet<String> posAdj = new HashSet<String>();
		HashSet<String> pos = new HashSet<String>();
		HashSet<String> label_3 = new HashSet<String>();
		HashSet<String> label_2 = new HashSet<String>();
		
		
		/*
		 * Lexicon
		 */
		Lexicon lexicon = new Lexicon();
		
		
		/*
		 * Generate ARFF File (Training)
		 */
		System.out.println("Generate ARFF File (Training)");
		try {
			GenerateArff.run(path_annotatedFiles, path_raw_files, path_to_write_arff,label_3,label_2,pos,posAdj);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(label_3.size());
		System.out.println(label_2.size());
		System.out.println(pos.size());
		System.out.println(posAdj.size());
		
		System.out.println("Generate model");
		/*
		 * Generate model
		 */
		try {
			generateModel(cls,path_to_write_arff);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Load model for prediction");
		/*
		 * Load model for prediction
		 */
		Prediction prediction = new Prediction(path_weka_model);
		
		
		/*
		 * Get Test Data features
		 */
		String arff_prefix = createArffPrefix(label_2, label_3, pos, posAdj);
		
		System.out.println("Done preprosessing");
		int counter = 0;
		int uri_counter = 0;
		HashSet<String> properties = importer.getProperties();
		for(String uri:properties){
			uri_counter+=1;
			System.out.println("URI:"+uri);
			System.out.println(uri_counter+"/"+properties.size());
			try{
				List<AdjectiveObject> object_list = adjectiveExtractor.start(path_to_objects, uri, tagger, mp);
				System.out.println(object_list.size());
				for(AdjectiveObject adjectiveObject : object_list){
					/*
					 * ignore "adjectives", which start with a digit
					 */
					if(adjectiveObject.isAdjective() && !Character.isDigit(adjectiveObject.getAdjectiveTerm().charAt(0))){
						String tmp = "/tmp/tmp.arff";
						writeSingleArffFile(tmp,arff_prefix,adjectiveObject,label_2, label_3, pos, posAdj);
						/*
						 * Load instances to predict.
						 */
						 ArffLoader loader = new ArffLoader();
						 loader.setFile(new File(tmp));
						 Instances structure = loader.getStructure();
						 structure.setClassIndex(structure.numAttributes() - 1);
						 
						 Instance current;
						 while ((current = loader.getNextInstance(structure)) != null){
							 /*
							  * predict
							  */
							 List<String> result = prediction.predict(current);
							 if(result.get(0).equals("1")){
								 counter+=1;
								 System.out.println("Add to lexica");
								 System.out.println("Adjective:"+adjectiveObject.getAdjectiveTerm());
								 System.out.println("Object:"+adjectiveObject.getObject());
								 System.out.println("Frequency:"+adjectiveObject.getFrequency());
								 System.out.println();
								 lexicon.addEntry(getLexicalEntry(lexicon,adjectiveObject.getAdjectiveTerm(),adjectiveObject.getObject(),uri,
										 adjectiveObject.getFrequency(),result.get(1)));
							 }						 
						 }
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("/Users/swalter/Desktop/adjectives.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		System.out.println("Properties:"+Integer.toString(properties.size()));
		System.out.println("Created entries:"+Integer.toString(counter));
		System.out.println("Average Entries per Property:"+Double.toString((double) counter/properties.size()));
		
		
		 
		 
		 
		
	}

	private static LexicalEntry getLexicalEntry(Lexicon lexicon,String adjective, String object, String uri, int frequency, String distribution) {
LexicalEntry entry;
		
		entry = lexicon.createNewEntry(adjective);
		
		entry.setCanonicalForm(adjective);
		
		Sense sense = new Sense();
		
		/*
		 * TODO object should be a URI and not a label
		 */
		sense.setReference(new Restriction(object,uri));
		
		entry.setSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicativeFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeSubject","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.setSyntacticBehaviour(behaviour);
		
		Provenance provenance = new Provenance();
		
		//provenance.setAgent("Distribution");
		//provenance.setConfidence(Double.valueOf(distribution));
		
		provenance.setAgent("Frequency");
		provenance.setConfidence((double) frequency);
		
		entry.setProvenance(provenance);
		
		entry = lexicon.createNewEntry(adjective);
		
		entry.setSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");

		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectiveAttributiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#attributiveArg","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.setSyntacticBehaviour(behaviour);
		
		
		
		entry.setProvenance(provenance);
		
		lexicon.addEntry(entry);
		
		return entry;
	}

	private static void writeSingleArffFile(String path, String arff_prefix,
			AdjectiveObject adjectiveobject,HashSet<String> subLabelList,
			HashSet<String> subLabelList_2,
			HashSet<String> posPatternList,HashSet<String> posAdjPatternList) {
		String line = Double.toString(adjectiveobject.getNormalizedFrequency())
				+","+Double.toString(adjectiveobject.getNormalizedObjectFrequency())
				+","+Double.toString(adjectiveobject.getNormalizedObjectOccurrences())
				+","+Double.toString(adjectiveobject.getRatio())
				+","+Double.toString(adjectiveobject.getRatio_pattern())
				+","+Double.toString(adjectiveobject.getRatio_pos_pattern())
				//+","+Double.toString(adjectiveobject.getEntropy())
				+","+Integer.toString(adjectiveobject.getPosition());
		if(adjectiveobject.isFirstPosition())line+=","+"1";
		else line+=","+"0";
		if(adjectiveobject.isLastPosition())line+=","+"1";
		else line+=","+"0";
		line+=","+Double.toString(adjectiveobject.getNld());
		for(String label:subLabelList){
			if(adjectiveobject.getSublabel().equals(label))line+=","+"1";
			else line+=","+"0";
		}
		for(String label:subLabelList_2){
			if(adjectiveobject.getSublabel_2().equals(label))line+=","+"1";
			else line+=","+"0";
		}
		for(String pospattern:posPatternList){
			if(adjectiveobject.getPos_Pattern().equals(pospattern))line+=","+"1";
			else line+=","+"0";
		}
		for(String posadjpattern:posAdjPatternList){
			if(adjectiveobject.getPos_adj_Pattern().equals(posadjpattern))line+=","+"1";
			else line+=","+"0";
		}
		/*
		 * we test always, if the line is true
		 */
		line+=",1";
		arff_prefix+=line;
		PrintWriter writer;
		try {
			writer = new PrintWriter(path);
			writer.println(arff_prefix);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private static String createArffPrefix(HashSet<String> subLabelList,HashSet<String> subLabelList_2,
			HashSet<String> posPatternList,HashSet<String> posAdjPatternList) {
		String first_line =""
				+"@relation adjectives\n"
				+"@attribute 'normalizedFrequency' numeric\n"
				+"@attribute 'normalizedObjectFrequency' numeric\n"
				+"@attribute 'normalizedObjectOccourences' numeric\n"
				+"@attribute 'ratio' numeric\n"
				+"@attribute 'ratioPattern' numeric\n"
				+"@attribute 'ratioPosPattern' numeric\n"
				//+"@attribute 'entropy' numeric\n"
				+"@attribute 'position' numeric\n"
				+"@attribute 'firstPosition' {0,1}\n"
				+"@attribute 'lastPosition' {0,1}\n"
				+"@attribute 'nld' numeric\n";
			int counter=0;
			for(String label:subLabelList){
				counter+=1;
				first_line+="@attribute 'l"+Integer.toString(counter)+"' {0,1}\n";
			}
			for(String label:subLabelList_2){
				counter+=1;
				first_line+="@attribute 'l2"+Integer.toString(counter)+"' {0,1}\n";
			}
			for(String label:posPatternList){
				counter+=1;
				first_line+="@attribute 'p"+Integer.toString(counter)+"' {0,1}\n";
			}
			for(String label:posAdjPatternList){
				counter+=1;
				first_line+="@attribute 'pa"+Integer.toString(counter)+"' {0,1}\n";
			}
			
			first_line+="@attribute 'class' {0,1}\n"
					+"@data\n";
			
			
			return first_line;
	}

	private static void generateModel(Classifier cls,String path_to_arff) throws FileNotFoundException, IOException {
		 Instances inst = new Instances(new BufferedReader(new FileReader(path_to_arff)));
		 inst.setClassIndex(inst.numAttributes() - 1);
		 try {
			cls.buildClassifier(inst);
			// serialize model
			 ObjectOutputStream oos = new ObjectOutputStream(
			                            new FileOutputStream(path_to_arff.replace(".arff", ".model")));
			 oos.writeObject(cls);
			 oos.flush();
			 oos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		
	}

}