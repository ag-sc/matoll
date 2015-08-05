package de.citec.sc.matoll.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

//import learning.SVMClassifier;



import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.evaluation.LexiconEvaluation;
import de.citec.sc.matoll.io.Config;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.patterns.PatternLibrary;
import de.citec.sc.matoll.preprocessor.ModelPreprocessor;
import de.citec.sc.matoll.utils.StanfordLemmatizer;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import de.citec.sc.matoll.classifiers.WEKAclassifier;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.utils.Learning;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Matoll {
 
        
	private static Logger logger = LogManager.getLogger(Matoll.class.getName());
        
        
        
	/**
         * 
         * @param args
         * @throws IOException
         * @throws ParserConfigurationException
         * @throws SAXException
         * @throws InstantiationException
         * @throws IllegalAccessException
         * @throws ClassNotFoundException 
         */
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
			
		//Logger logger = LogManager.getLogger(Matoll.class.getName());

                

		String directory;
		String mode = "train";
		String gold_standard_lexicon;
		String model_file = "model";
		String output_lexicon;
		String configFile;
		Language language;
		String classi;
		Config config = null;
		boolean coreference = false;
		int no_entries = 1000;
		String output;
		double frequency = 2.0;
		
		HashMap<String,Double> maxima; 
		maxima = new HashMap<String,Double>();
		
		
		//Provenance provenance;
		
		Pattern p = Pattern.compile("^--(\\w+)=(\\w+|\\d+)$");
		  
		Matcher matcher;
		 
		if (args.length < 3)
		{
			System.out.print("Usage: Matoll --mode=train/test <DIRECTORY> <CONFIG>\n");
			return;
		
		}
		
//		Classifier classifier;
		
		directory = args[1];
		configFile = args[2];
		
		config = new Config();
		
		config.loadFromFile(configFile);
		
		classi = config.getClassifier();
	
		gold_standard_lexicon = config.getGoldStandardLexicon();
		
		model_file = config.getModel();
		
		output_lexicon = config.getOutputLexicon();
		output = config.getOutput();
		coreference = config.getCoreference();
		
		language = config.getLanguage();
				
		for (int i=0; i < args.length; i++)
		{
			matcher = p.matcher(args[i]);
				 
			if (matcher.matches())
			{
			    if (i== 0 && matcher.group(1).equals("mode"))
			    {
			    	mode = matcher.group(2);
			    	System.out.print("Starting MATOLL with mode: "+mode+"\n");
			    	System.out.print("Language: "+language+"\n");
			    	System.out.print("Processing directory: "+directory+"\n");
			    	System.out.print("Using gold standard: "+gold_standard_lexicon+"\n");
			    	System.out.print("Using model file: "+model_file+"\n");
			    	System.out.print("Output lexicon: "+output_lexicon+"\n");
			    	System.out.print("Output: "+output+"\n");
			    	System.out.print("Using coreference: "+coreference+"\n");
			    	
			    }
			    else
			    {
			    	System.out.print("Usage: Matoll --mode=train/test <DIRECTORY> <CONFIG>\n");
			    	return;
			    }
			}		
		}

//		
		LexiconLoader loader = new LexiconLoader();
//		
//		logger.info("Loading lexicon from: "+gold_standard_lexicon+"\n");
//		
		Lexicon gold = loader.loadFromFile(gold_standard_lexicon);
		
		File folder = new File(directory);
		                
		
		// Creating preprocessor
		
		ModelPreprocessor preprocessor = new ModelPreprocessor(language);
                preprocessor.setCoreferenceResolution(coreference);
				
		Lexicon automatic_lexicon = new Lexicon();
                if(config.getBaseUri()!=null) automatic_lexicon.setBaseURI(config.getBaseUri());
		

		PatternLibrary library = new PatternLibrary();
                if(language == Language.EN){
                    StanfordLemmatizer sl = new StanfordLemmatizer(language);
                    library.setLemmatizer(sl);
                }
		
		
		library.setPatterns(config.getPatterns());
		
	
		String subj;
		String obj;
		
		String reference = null;
		
		List<Model> sentences;
		File[] files = folder.listFiles();
                
                ForkJoinPool commonPool = ForkJoinPool.commonPool();
                System.out.println(commonPool.getParallelism());   
                /*
                add -Djava.util.concurrent.ForkJoinPool.common.parallelism=5 in run.sh
                */

                List<File> list_files = new ArrayList<>();
                list_files.addAll(Arrays.asList(files));
//                list_files.parallelStream()
//                        .filter(f->f.isFile()&&f.toString().endsWith(".ttl"))
//                        .map((File f)->{
//                            return createLexicon(f,preprocessor,library);
//                        })
//                        .forEach(automatic_lexicon::addLexicon);
                System.out.println(list_files.size()+" files");
                
                List<Lexicon> lexicon_list= list_files.parallelStream()
                        .filter(f->f.isFile()&&f.toString().endsWith(".ttl"))
                        .map((File f)->{
                            return createLexicon(f,preprocessor,library);
                        })
                        .collect(Collectors.toList());
                lexicon_list.stream().forEach((l) -> {
                    automatic_lexicon.addLexicon(l);
            });
                       
                
//                int file_counter = 0;
//                /*
//                TODO: run this loop parralel and put together final lexicon afterwards;
//                */
//		for (final File file : files) {
//			
//			if (file.isFile() && file.toString().endsWith(".ttl")) {
//                            
//                                file_counter+=1;
//
//				logger.info("Processing: "+file.toString()+"  "+file_counter+"/"+files.length);
//                                
//								
//			
//			
//			}
//		}
		
		logger.info("Extracted all entries \n");
		logger.info("Lexicon contains "+Integer.toString(automatic_lexicon.getEntries().size())+" entries\n");
		
		LexiconSerialization serializer = new LexiconSerialization(language,library.getPatternSparqlMapping());
		
                
                Model model = ModelFactory.createDefaultModel();
		
		serializer.serialize(automatic_lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File(output_lexicon.replace(".lex","_beforeTraining.ttl")));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
                
                out.close();
		
                
                WEKAclassifier classifier = new WEKAclassifier(language);
		
		logger.info("Starting "+mode+"\n");	
		boolean predict = true;
		if (mode.equals("train"))
		{
                    /*
                    during training only entries with a frequency of at least 2 are considered (per sense)
                    */
                    Learning.doTraining(automatic_lexicon,gold,maxima,language, classifier,2);
		
		}
                else{
                    Learning.doPrediction(automatic_lexicon, gold, classifier, output, language);
                }
		writeByReference(automatic_lexicon);
                logger.info("Write lexicon to "+output_lexicon+"\n");
                model = ModelFactory.createDefaultModel();

                serializer.serialize(automatic_lexicon, model);

                out = new FileOutputStream(new File(output_lexicon.replace(".lex",".ttl")));

                RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;

                out.close();
                
		
		
			
	}
        
        private static Lexicon createLexicon(File file,ModelPreprocessor preprocessor,PatternLibrary library) {
            Model model = RDFDataMgr.loadModel(file.toString());
            
            Lexicon lexicon = new Lexicon();
            try{
                List<Model> sentences = getSentences(model);

                for (Model sentence: sentences)
                {
                    //System.out.println(sentence.toString());
                    String obj = getObject(sentence);

                    String subj = getSubject(sentence);

                    String reference = getReference(sentence);

                    preprocessor.preprocess(sentence,subj,obj);

                    //logger.info("Extract lexical entry for: "+sentence.toString()+"\n");	
                    library.extractLexicalEntries(sentence, lexicon);
                }
                model.close();
                // FileOutputStream output = new FileOutputStream(new File(file.toString().replaceAll(".ttl", "_pci.ttl")));

                // RDFDataMgr.write(output, model, RDFFormat.TURTLE) ;
                return lexicon;
            }
            catch(Exception e){
                return lexicon;
            }
			 
            
        }
        
        
        private static String test123(String input){
            return input+"321";
        }
        /**
         * 
         * @param lexicon
         * @throws IOException 
         */
	private static void writeByReference(Lexicon lexicon) throws IOException {
		List<LexicalEntry> entries;
		FileWriter writer;
		Set<Reference> references = lexicon.getReferences();
		
		
		for (Reference ref: references)
		{
			String filename = ref.toString().replaceAll("http:\\/\\/","").replaceAll("\\/","_").replaceAll("\\.","_")+".lex";
			System.out.println("Write lexicon for reference "+ref.toString()+" to "+filename);
			writer = new FileWriter(filename);
			entries = lexicon.getEntriesForReference(ref.toString());
			
			for (LexicalEntry entry: entries)
			{
				writer.write(entry.toString()+"\n");
				writer.flush();
			}
			
			writer.close();
			
			
			
		}
	}
//        /**
//         * 
//         * @param vector
//         * @param max
//         * @return 
//         */
//	private static FeatureVector normalize(FeatureVector vector, HashMap<String,Double> max) {
//		
//		HashMap<String,Double> map;
//		
//		map = vector.getFeatureMap();
//		
//		for (String feature: map.keySet())
//		{
//			map.put(feature, new Double(map.get(feature).doubleValue() / max.get(feature).doubleValue()));
//		}
//		
//		return vector;
//		
//	}
        /**
         * 
         * @param feature
         * @param value
         * @param map 
         */
	private static void updateMaximum(String feature, Double value, HashMap<String,Double> map) {
		
		
		if (map.containsKey(feature))
		{
		
			if (value > map.get(feature))
			{
				map.put(feature, value);
			}
		}
		else
		{
			map.put(feature, value);
		}
		
	}
        /**
         * 
         * @param model
         * @return 
         */
	private static String getReference(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("own:reference"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        return stmt.getObject().toString();
	    }
		
		return null;
	}
        
        /**
         * 
         * @param model
         * @return
         * @throws FileNotFoundException 
         */
	private static List<Model> getSentences(Model model) throws FileNotFoundException {
		
		// get all ?res <conll:sentence> 
		
		List<Model> sentences = new ArrayList<Model>();
		
		StmtIterator iter, iter2, iter3;
		
		Statement stmt, stmt2, stmt3;
		
		Resource resource;
		
		Resource token;
		
		iter = model.listStatements(null,model.getProperty("conll:language"), (RDFNode) null);
		
		while (iter.hasNext()) {
					
			Model sentence = ModelFactory.createDefaultModel();
			
			stmt = iter.next();
			
			resource = stmt.getSubject();
			
			iter2 = model.listStatements(resource , null, (RDFNode) null);
			
			while (iter2.hasNext())
			{
				stmt2 = iter2.next();
				
				sentence.add(stmt2);
				
			}
			
			iter2 = model.listStatements(null , model.getProperty("own:partOf"), (RDFNode) resource);
			
			while (iter2.hasNext())
			{
				stmt2 = iter2.next();
				
				token = stmt2.getSubject();
				
				iter3 = model.listStatements(token , null, (RDFNode) null);
				
				while (iter3.hasNext())
				{
					stmt3 = iter3.next();
					
					sentence.add(stmt3);
					
				}
			}
			
			sentences.add(sentence);
			
			// RDFDataMgr.write(new FileOutputStream(new File(resource+".ttl")), sentence, RDFFormat.TURTLE) ;
			
		}
		

		return sentences;
		
	}

        /**
         * 
         * @param model
         * @return 
         */
	private static String getSubject(Model model) {
		
		StmtIterator iter = model.listStatements(null,model.getProperty("own:subj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
			
	        return stmt.getObject().toString();
	    }
		
		return null;
	}
        
        /**
         * 
         * @param model
         * @return 
         */
	private static String getObject(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("own:obj"), (RDFNode) null);
		
		Statement stmt;
		
		while (iter.hasNext()) {
						
			stmt = iter.next();
	        
	        return stmt.getObject().toString();
	    }
		
		return null;
	}

}
