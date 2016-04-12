package de.citec.sc.matoll.process;

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
import gnu.trove.map.hash.TObjectIntHashMap;

import javax.xml.parsers.ParserConfigurationException;

//import learning.SVMClassifier;



import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.io.Config;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.patterns.PatternLibrary;
import de.citec.sc.matoll.preprocessor.ModelPreprocessor;
import de.citec.sc.matoll.utils.StanfordLemmatizer;
import de.citec.sc.matoll.classifiers.WEKAclassifier;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.utils.Learning;
import de.citec.sc.matoll.utils.Stopwords;
import java.io.PrintWriter;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import org.xml.sax.SAXException;

import java.util.HashSet;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class Matoll {
 
        
//	private static Logger logger = LogManager.getLogger(Matoll.class.getName());
        
        
        
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
			
                

		String directory;
		String mode = "train";
		String gold_standard_lexicon;
		String output_lexicon;
		String configFile;
		Language language;
		String classi;
		boolean coreference = false;
		int no_entries = 1000;
		String output;
		double frequency = 2.0;
                
                Stopwords stopwords=new Stopwords();
		
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
		
		final Config config = new Config();
		
		config.loadFromFile(configFile);
		
		classi = config.getClassifier();
	
		gold_standard_lexicon = config.getGoldStandardLexicon();
		
		String model_file = config.getModel();
		
		output_lexicon = config.getOutputLexicon();
		output = config.getOutput();
		coreference = config.getCoreference();
		
		language = config.getLanguage();
                
                final StanfordLemmatizer sl = new StanfordLemmatizer(language);
				
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
		
		                
		
		// Creating preprocessor
//		
		ModelPreprocessor preprocessor = new ModelPreprocessor(language);
                preprocessor.setCoreferenceResolution(coreference);
                switch (language) {
                    
                    case EN: 
                        Set<String> dep = new HashSet<>();
                        dep.add("prep");
                        dep.add("appos");
                        dep.add("nn");
                        dep.add("dobj");
                        dep.add("pobj");
                        dep.add("num");
                        preprocessor.setDEP(dep);
                        break;
                        
                    case DE: 
                        dep = new HashSet<>();
                        dep.add("pp");
                        dep.add("pn");
                        dep.add("obja");
                        dep.add("objd");
                        dep.add("app");
                        preprocessor.setDEP(dep);
                        break;
                        
                    case ES: 
                        dep = new HashSet<>();
                        dep.add("MOD");
                        dep.add("COMP");
                        dep.add("DO");
                        dep.add("OBLC");
                        dep.add("BYAG");
                        preprocessor.setDEP(dep);
                        break;
                        
                        
                    case JA: 
                        break;
                        //TODO
                                                    
                }
				
		Lexicon automatic_lexicon = new Lexicon();
                automatic_lexicon.setBaseURI(config.getBaseUri());
		
                
		PatternLibrary library = new PatternLibrary();
                if(language == Language.EN){
                    //sl = new StanfordLemmatizer(language);
                    library.setLemmatizer(sl);
                }
		
		
		library.setPatterns(config.getPatterns());
		
	
		String subj;
		String obj;
		
		String reference = null;
		
		List<Model> sentences = new ArrayList<>();
                
                List<File> list_files = new ArrayList<>();
                 
                if(config.getFiles().isEmpty()){
                    File folder = new File(directory);
                    File[] files = folder.listFiles();
                    for(File file : files){
                        if (file.toString().contains(".ttl")) list_files.add(file);
                    }
                }
                else{
                    list_files.addAll(config.getFiles());
                }
		                
                int sentence_counter = 0;
                TObjectIntHashMap<String> freq = new TObjectIntHashMap<String>();
                
                
                for(File file:list_files){
                    Model model = RDFDataMgr.loadModel(file.toString());
                    sentences.clear();
                    sentences = getSentences(model);
                    for(Model sentence: sentences){
                        obj = getObject(sentence);
                        subj = getSubject(sentence);
                        if (!stopwords.isStopword(obj, language) 
                                && !stopwords.isStopword(subj, language) 
                                && !subj.equals(obj) 
                                && !subj.contains(obj) 
                                && !obj.contains(subj)) {
                            reference = getReference(sentence);
                            if(!reference.equals("http://dbpedia.org/ontology/type")&&!reference.equals("http://dbpedia.org/ontology/isPartOf")){
                                preprocessor.preprocess(sentence,subj,obj,language);
                                freq.adjustOrPutValue(reference, 1, 1);
                                sentence_counter+=1;
                                library.extractLexicalEntries(sentence, automatic_lexicon);
                            }
                        }
                    }
                    model.close();
                }
            
                System.out.println("Extracted entries");
                if(config.doStatistics()){
                    System.out.println("do some statistics now");
                    absolutNumberEntriesByReference(automatic_lexicon,freq,language);
                    Map<String, Double> overall_pattern_distribution = getOverallDistributionPattern(automatic_lexicon);
                    for(String key: overall_pattern_distribution.keySet()){
                        System.out.println(key+":"+overall_pattern_distribution.get(key));
                    }
                    for(Reference ref:automatic_lexicon.getReferences()){
                        getDistributionPatternPerPreference(automatic_lexicon,ref.getURI(),language);
                    }
                }
                
                
                System.out.println("Calculate normalized confidence");
                calculateConfidence(automatic_lexicon,freq);  
                normalizeConfidence(automatic_lexicon);
                
		
//		logger.info("Extracted all entries \n");
//		logger.info("Lexicon contains "+Integer.toString(automatic_lexicon.getEntries().size())+" entries\n");
		
		LexiconSerialization serializer = new LexiconSerialization(library.getPatternSparqlMapping(),config.removeStopwords());
		
                extportTSV(automatic_lexicon,output_lexicon);
                
                Model model = ModelFactory.createDefaultModel();
		
		serializer.serialize(automatic_lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File(output_lexicon.replace(".lex","_beforeTraining.ttl")));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
                
                out.close();
		
                System.out.println("Actual number used sentences:"+Integer.toString(sentence_counter));
                
                
//                WEKAclassifier classifier = new WEKAclassifier(language);
//		
////		logger.info("Starting "+mode+"\n");	
//		boolean predict = true;
//		if (mode.equals("train"))
//		{
//                    /*
//                    during training only entries with a frequency of at least 2 are considered (per sense)
//                    */
//                    Learning.doTraining(automatic_lexicon,gold,maxima,language, classifier,2);
//		
//		}
//                else{
//                    Learning.doPrediction(automatic_lexicon, gold, classifier, output, language);
//                }
////		writeByReference(automatic_lexicon,language);

		
		
			
	}
        
        
        

        /**
         * 
         * @param lexicon
         * @throws IOException 
         */
	private static void writeByReference(Lexicon lexicon, Language language) throws IOException {
		List<LexicalEntry> entries;
		FileWriter writer;
		Set<Reference> references = lexicon.getReferences();
		
		
		for (Reference ref: references)
		{
			String filename = language.toString()+"_"+ref.toString().replaceAll("http:\\/\\/","").replaceAll("\\/","_").replaceAll("\\.","_")+".lex";
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
        
        /**
         * 
         * @param lexicon
         * @throws IOException 
         */
	private static void absolutNumberEntriesByReference(Lexicon lexicon,TObjectIntHashMap<String> freq, Language language) throws IOException {
            Map<String,Integer> absolut_nubers = new HashMap<>();
            for(LexicalEntry entry : lexicon.getEntries()){
                for(Sense sense:entry.getSenseBehaviours().keySet()){
                    Provenance prov = entry.getProvenance(sense);
                    String uri = sense.getReference().getURI();
                    if(absolut_nubers.containsKey(uri)){
                        int value = absolut_nubers.get(uri);
                        absolut_nubers.put(uri, value+prov.getFrequency());
                    }
                    else 
                        absolut_nubers.put(uri, prov.getFrequency());
                }
            }
            String filename = language.toString()+"_statistics.tsv";
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("uri\t#entries\t#numberSentences\n");
                
                for (String key: absolut_nubers.keySet())
                {
                    if(freq.contains(key)){
                        int number_sentences = freq.get(key);
                        writer.write(key+"\t"+absolut_nubers.get(key)+"\t"+number_sentences+"\n");
                        writer.flush();
                    }
                    
                }
            }
	}


        /**
         * 
         * @param model
         * @return 
         */
	private static String getReference(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("conll:reference"), (RDFNode) null);
		Statement stmt;
		while (iter.hasNext()) {
			stmt = iter.next();
                   return stmt.getObject().toString();
                }
		
		return null;
	}
        
        private static Map<String,Double> getOverallDistributionPattern(Lexicon lexicon){
            Map<String,Double> results = new HashMap<>();
            Map<String,Integer> tmp_results = new HashMap<>();
            int absolut_number = 0;
            
            for(LexicalEntry entry:lexicon.getEntries()){
                for(Sense sense : entry.getSenseBehaviours().keySet()){
                    Provenance prov = entry.getProvenance(sense);
                    absolut_number+=prov.getFrequency();
                    for(String pattern_name: prov.getPatternset()){
                        if(tmp_results.containsKey(pattern_name)){
                            int value = tmp_results.get(pattern_name);
                            tmp_results.put(pattern_name, value+prov.getFrequency());
                        }
                        else
                            tmp_results.put(pattern_name, prov.getFrequency());
                        /*
                        numbers are not 100%correct, as one provenance can be created by different pattern and the distinction of the contribution of each pattern is then lost.
                        */
                    }
                }
            }
            for(String key : tmp_results.keySet()){
                double value = (tmp_results.get(key)+0.0)/absolut_number;
                results.put(key, value);
            }
            
            
            return results;
        }
        
        
        
        private static void getDistributionPatternPerPreference(Lexicon lexicon,String uri, Language language) throws IOException{
            Map<String,Double> results = new HashMap<>();
            Map<String,Integer> tmp_results = new HashMap<>();
            int absolut_number = 0;
            
            for(LexicalEntry entry:lexicon.getEntriesForReference(uri)){
                for(Sense sense : entry.getSenseBehaviours().keySet()){
                    Provenance prov = entry.getProvenance(sense);
                    if(uri.equals(sense.getReference().getURI())){
                        absolut_number+=prov.getFrequency();
                        for(String pattern_name: prov.getPatternset()){
                            if(tmp_results.containsKey(pattern_name)){
                                int value = tmp_results.get(pattern_name);
                                tmp_results.put(pattern_name, value+prov.getFrequency());
                            }
                            else
                                tmp_results.put(pattern_name, prov.getFrequency());
                            /*
                            numbers are not 100%correct, as one provenance can be created by different pattern and the distinction of the contribution of each pattern is then lost.
                            */
                        }
                    }
                }
            }
            for(String key : tmp_results.keySet()){
                double value = (tmp_results.get(key)+0.0)/absolut_number;
                results.put(key, value);
            }

            String filename = uri.replaceAll("http:\\/\\/","").replaceAll("\\/","_").replaceAll("\\.","_")+"_"+language.toString()+"_statistics.tsv";
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("pattern_name\t#distribution\n");
                
                for (String key: results.keySet())
                {
                    writer.write(key+"\t"+results.get(key)+"\n");
                    writer.flush();
                    
                    
                }
            }
            
            
            
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

    private static void calculateConfidence(Lexicon automatic_lexicon, TObjectIntHashMap<String> freq) {
        for(LexicalEntry entry: automatic_lexicon.getEntries()){
            int value_2 = getFrequencySameCanonicalForm(entry.getCanonicalForm(),automatic_lexicon);
            for(Sense sense : entry.getSenseBehaviours().keySet()){
                Provenance prov = entry.getProvenance(sense);
                String uri = sense.getReference().getURI();
                double value_1 = (prov.getFrequency()+0.0)/freq.get(uri);
                double value_3 = (prov.getFrequency()+0.0)/value_2;
                /*
                Harmonic mean between the ratio of Frequency of the sense over the number of sentences in the property 
                and the ratio of frequency of the sense over the number of senses of other entries with the same cannonical form
                */
                prov.setConfidence((2*value_1*value_3)/(value_1+value_3));
            }
        }
    }

    private static int getFrequencySameCanonicalForm(String canonicalForm, Lexicon automatic_lexicon) {
        int value = 0;
        for(LexicalEntry entry: automatic_lexicon.getEntriesWithCanonicalForm(canonicalForm)){
            for(Sense sense:entry.getSenseBehaviours().keySet()){
                Provenance prov = entry.getProvenance(sense);
                value+=prov.getFrequency();
            }
        }
        return value;
    }

    /*
    normalizes all confidence values by the highest confidence value in the lexicon
    */
    private static void normalizeConfidence(Lexicon automatic_lexicon) {
        double highestConfidence = 0.0;
        for(LexicalEntry entry: automatic_lexicon.getEntries()){
            for(Sense sense : entry.getSenseBehaviours().keySet()){
                Provenance prov = entry.getProvenance(sense);
                if(prov.getConfidence()>highestConfidence) highestConfidence = prov.getConfidence();
            }
        }
        
        System.out.println("Greatest confidence is:"+highestConfidence);
        
        for(LexicalEntry entry: automatic_lexicon.getEntries()){
            for(Sense sense : entry.getSenseBehaviours().keySet()){
                Provenance prov = entry.getProvenance(sense);
                double confidence = prov.getConfidence();
                double normalised_confidence = confidence/highestConfidence;
                prov.setConfidence(normalised_confidence);
            }
        }
        
    }
    
    private static void extportTSV(Lexicon lexicon, String path){
        Map<String,Double> hm_double = new HashMap<>();
        Map<String,Integer> hm_int = new HashMap<>();
        for(LexicalEntry entry : lexicon.getEntries()){
            for(Sense sense:entry.getSenseBehaviours().keySet()){
                Reference ref = sense.getReference();
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    SimpleReference reference = (SimpleReference) ref;
                    String preposition = "";
                    if(entry.getPreposition()!=null) preposition = entry.getPreposition().getCanonicalForm();
                    String input = entry.getCanonicalForm()+"\t"+preposition+"\t"+reference.getURI()+"\t";
                    if(hm_int.containsKey(input)){
                            int freq = hm_int.get(input);
                             hm_int.put(input, entry.getProvenance(sense).getFrequency()+freq);
                        }
                        else{
                            hm_int.put(input, entry.getProvenance(sense).getFrequency());
                        }
                }
                else if (ref instanceof de.citec.sc.matoll.core.Restriction){
                    Restriction reference = (Restriction) ref;
                    String input = entry.getCanonicalForm()+"\t"+reference.getValue()+"\t"+reference.getProperty()+"\t";
                    if(entry.getProvenance(sense).getConfidence()!=null){
                        if(hm_double.containsKey(input)){
                            double value = hm_double.get(input);
                             hm_double.put(input, entry.getProvenance(sense).getConfidence()+value);
                        }
                        else{
                            hm_double.put(input, entry.getProvenance(sense).getConfidence());
                        }
                    }
                    
                }
            }
        }
        
        PrintWriter writer;
        try {
                writer = new PrintWriter(path+"_restriction.tsv");
                for(String key:hm_double.keySet()){
                    writer.write(key+Double.toString(hm_double.get(key))+"\n");
                }
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        try {
                writer = new PrintWriter(path+"_simple.tsv");
                for(String key:hm_int.keySet()){
                    if(hm_int.get(key)>1)
                        writer.write(key+Integer.toString(hm_int.get(key))+"\n");
                }
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        
    }

}
