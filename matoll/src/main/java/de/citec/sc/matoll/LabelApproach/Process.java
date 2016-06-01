package de.citec.sc.matoll.LabelApproach;



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
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.citec.sc.matoll.core.Language;
import static de.citec.sc.matoll.core.Language.EN;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.utils.Levenshtein;
import de.citec.sc.matoll.utils.OntologyImporter;
import de.citec.sc.matoll.utils.StanfordLemmatizer;
import de.citec.sc.matoll.utils.Wordnet;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;


import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
//import weka.classifiers.functions.SMO;
//import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
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

		String path_annotatedFiles = "resources/annotatedAdjectives/";
		String path_raw_files = "resources/plainAdjectives/";
		String path_to_write_arff = "adjective.arff";
		String path_weka_model = path_to_write_arff.replace(".arff", ".model");
		String path_to_wordnet = "/Users/swalter/Backup/Software/WordNet-3.0";
		String path_to_objects = "/Users/swalter/Downloads/tmp_extractPropertiesWithData/results/ontology/";
		
                Set<String> feature_list = new HashSet<String>();
                feature_list.add("NAF");
                feature_list.add("Trigrams");
                feature_list.add("Bigrams");
                feature_list.add("PR");
                feature_list.add("POSPR");
                feature_list.add("AR");
                feature_list.add("PAP");

//                feature_list.add("NAF");
//                feature_list.add("Trigrams");
//                feature_list.add("Bigrams");
//                feature_list.add("PR");
//                feature_list.add("POSPR");
//                feature_list.add("AR");
//                feature_list.add("PAP");
//                feature_list.add("NOF");
//                feature_list.add("PP");
//                feature_list.add("NLD");
//                feature_list.add("AP");
//                feature_list.add("AFP");
//                feature_list.add("ALP");
                        

                LabelFeature label_feature = new LabelFeature();
                label_feature.setFeature(feature_list);
                
                
                String path_to_tagger_model ="resources/english-caseless-left3words-distsim.tagger";
		RandomForest smo = new RandomForest();
//                J48 smo = new J48();
                //smo.setOptions(weka.core.Utils.splitOptions("-M"));
                Classifier cls = smo; 
                
                final StanfordLemmatizer sl = new StanfordLemmatizer(EN);
	
//                String path_to_input_file = "../dbpedia_2014.owl";
                String path_to_input_file = "test.txt";
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
                lexicon.setBaseURI("http://localhost:8080/");
		
		
		/*
		 * Generate ARFF File (Training)
		 */
		System.out.println("Generate ARFF File (Training)");
		try {
			GenerateArff.run(path_annotatedFiles, path_raw_files, path_to_write_arff,label_3,label_2,pos,posAdj,tagger,label_feature);
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
		
		System.out.println("Done preprosessing");
		

               

                //runWornetPropertyApproach(properties,lexicon,wordnet,sl);
		runAdjectiveApproach(properties,adjectiveExtractor,posAdj,pos,label_3,label_2, prediction,tagger, lexicon, mp,path_to_objects,label_feature);
                
//		runWornetClassApproach(classes,lexicon,wordnet,"/Users/swalter/Downloads/EnglishIndexReduced");
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
                String path = "new_adjectives.ttl";
		FileOutputStream out = new FileOutputStream(new File(path));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		extportTSV(lexicon,path);

		
		
		
		
		
		 
		 
		 
		
	}

	private static void createRestrictionClassEntry(Lexicon lexicon,String adjective, String object_uri, String uri, int frequency, double distribution) {
            if(isAlpha(adjective) && isAlpha(frag(object_uri))){
                LexicalEntry entry = new LexicalEntry(Language.EN);
		entry.setCanonicalForm(adjective);
                
		
		Sense sense = new Sense();
                Reference ref = new Restriction(lexicon.getBaseURI()+"RestrictionClass_"+frag(uri)+"_"+frag(object_uri),object_uri,uri);
		//Reference ref = new Restriction(lexicon.getBaseURI()+"RestrictionClass_"+frag(uri)+"_"+frag(adjective),object_uri,uri);
                //Reference ref = new Restriction(lexicon.getBaseURI()+"RestrictionClass",object_uri,uri);

                sense.setReference(ref);
                
                entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+adjective.replace(" ","_")+"_as_AdjectiveRestriction");
				
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicativeFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeSubject","subject",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
		
		entry.addSyntacticBehaviour(behaviour,sense);

				
		SyntacticBehaviour behaviour2 = new SyntacticBehaviour();
		
		behaviour2.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectiveAttributiveFrame");
				
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#attributiveArg","object",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));
		
		entry.addSyntacticBehaviour(behaviour2,sense);
                
                
                Provenance provenance = new Provenance();
		
		provenance.setConfidence(distribution);
		
                provenance.setFrequency(frequency);
		
		entry.addProvenance(provenance,sense);		
                lexicon.addEntry(entry);
            }
                
                
		
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

        public static BufferedReader readDataFile(String filename) {
            BufferedReader inputReader = null;

            try {
                inputReader = new BufferedReader(new FileReader(filename));
            } catch (FileNotFoundException ex) {
                System.err.println("File not found: " + filename);
            }

            return inputReader;
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

    private static void runAdjectiveApproach(Set<String> properties,ExtractData adjectiveExtractor,
            HashSet<String> posAdj, HashSet<String> pos, HashSet<String> label_3, HashSet<String> label_2, 
            Prediction prediction,MaxentTagger tagger, Lexicon lexicon, Morphology mp, String path_to_objects,LabelFeature label_feature) {
        int counter = 0;
        int uri_counter = 0;
        int uri_used = 0;
        for(String uri:properties){
            uri_counter+=1;
            System.out.println("URI:"+uri);
            System.out.println(uri_counter+"/"+properties.size());
            try{
                List<AdjectiveObject> object_list = adjectiveExtractor.start(path_to_objects, uri, tagger, mp);
                System.out.println("Got "+object_list.size()+" objects");
                if(object_list.size()>0)uri_used+=1;
                for(AdjectiveObject adjectiveObject : object_list){
                    /*
                     * ignore "adjectives", which start with a digit
                     */
                    if(adjectiveObject.isAdjective() && !Character.isDigit(adjectiveObject.getAdjectiveTerm().charAt(0))){
                        String tmp = "/tmp/tmp.arff";
//                        writeSingleArffFile(tmp,arff_prefix,adjectiveObject,label_2, label_3, pos, posAdj);
                        List<String> lines = new ArrayList<String>();
                        List<AdjectiveObject> small_object_list = new ArrayList<>();
                        /*
                        we want to pretict that it is a correct entry
                        */
                        adjectiveObject.setAnnotation("1");
                        small_object_list.add(adjectiveObject);
                        GenerateArff.getCsvLine(lines,small_object_list,label_2,label_3,pos,posAdj,tagger,label_feature);
                        GenerateArff.writeArff(lines,tmp,label_2,label_3,pos,posAdj,label_feature);
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
                             HashMap<Integer, Double> result = prediction.predict(current);
                             for(int key : result.keySet()){
                                if(key==1 && result.get(key)>0.70){
                                     counter+=1;
                                     try{
                                         createRestrictionClassEntry(lexicon,adjectiveObject.getAdjectiveTerm(),adjectiveObject.getObjectURI(),uri, adjectiveObject.getFrequency(),result.get(key));
                                     }
                                     catch(Exception e){
                                            e.printStackTrace();
                                     }
                                }
                                else{
//                                    System.out.println("Predicted 0 for "+adjectiveObject.getAdjectiveTerm());
//                                    System.out.println();
                                }

                            }


                        }
                    }
                    
                }
            }
            catch(Exception e){
                    e.printStackTrace();
            }
        }
     
        System.out.println("Properties:"+Integer.toString(properties.size()));
        System.out.println("Created entries:"+Integer.toString(counter));
        System.out.println("Average Entries per Property:"+Double.toString((double) counter/properties.size()));
        System.out.println("Properties with Data:"+Integer.toString(uri_used));
        System.out.println("Average Entries per Property with data:"+Double.toString((double) counter/uri_used));
                
    }

    private static void runWornetClassApproach(Set<String> classes, Lexicon lexicon, Wordnet wordnet, String pathToIndex) throws FileNotFoundException, IOException, ParseException {
        System.out.println("in runWornetClassApproach");
        System.out.println(classes.size());
        List<String> stopwords = new ArrayList<>();
        String everything = "";
        FileInputStream inputStream = new FileInputStream("resources/englishST.txt");
        try {
        everything = IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }

        for(String stopword : everything.split("\n")){
            stopwords.add(stopword.toLowerCase());
        }
        stopwords.add("-LRB-");
        stopwords.add("-RRB-");
        
        DirectoryReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(pathToIndex)));
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new EnglishAnalyzer();
        int uri_counter = 0;
        for(String uri : classes){
            uri_counter+=1;
            System.out.println("URI:"+uri);
            System.out.println(uri_counter+"/"+classes.size()+" classes");
            String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?label WHERE{<"+uri+"> rdfs:label ?label. FILTER (lang(?label) = 'en') }";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            Set<String> labels = new HashSet<>();
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();
                String label = (qs.get("?label").toString());
                label = label.replace("@en","");
                label = label.replace("\"", "");
                Set<String> canonicalForms = new HashSet<>();
                canonicalForms.add(label);
                canonicalForms.addAll(wordnetDisambiguation(uri,label,wordnet,stopwords,searcher,analyzer));
    //            canonicalForms.addAll(wordnet.getAllSynonyms(label));
                for(String c : canonicalForms){
                    c = c.replace("_"," ");
                    createWordnetClassEntry(c,lexicon,uri);
                }
            }
            
            
            
        }
    }

    private static void createWordnetClassEntry(String label, Lexicon lexicon, String uri) {
        LexicalEntry entry = new LexicalEntry(Language.EN);
        entry.setCanonicalForm(label);


        Sense sense = new Sense();
        Reference ref = new SimpleReference(uri);
        sense.setReference(ref);

        Provenance provenance = new Provenance();
        provenance.setFrequency(1);

        sense.setReference(ref);

        //System.out.println(adjective);
        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+label.replace(" ","_")+"_as_WordnetClassEntry");

        entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");

        SyntacticBehaviour behaviour = new SyntacticBehaviour();
        behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");

        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));

        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

        entry.addSyntacticBehaviour(behaviour,sense);

        entry.addProvenance(provenance,sense);

        lexicon.addEntry(entry);
        
    }
    
    private static void createWordnetNounEntry(String label, Lexicon lexicon, String uri) {
        LexicalEntry entry = new LexicalEntry(Language.EN);
        entry.setCanonicalForm(label);


        Sense sense = new Sense();
        Reference ref = new SimpleReference(uri);
        sense.setReference(ref);

        Provenance provenance = new Provenance();
        provenance.setFrequency(1);

        sense.setReference(ref);

        //System.out.println(adjective);
        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+label.replace(" ","_")+"_as_WordnetNounEntry");

        entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun");

        SyntacticBehaviour behaviour = new SyntacticBehaviour();
        behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");

        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));

        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

        entry.addSyntacticBehaviour(behaviour,sense);

        entry.addProvenance(provenance,sense);

        lexicon.addEntry(entry);
        
    }
    
    private static void createWordnetVerbEntry(String label, Lexicon lexicon, String uri) {
        LexicalEntry entry = new LexicalEntry(Language.EN);
        entry.setCanonicalForm(label);


        Sense sense = new Sense();
        Reference ref = new SimpleReference(uri);
        sense.setReference(ref);

        Provenance provenance = new Provenance();
        provenance.setFrequency(1);

        sense.setReference(ref);

        //System.out.println(adjective);
        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+label.replace(" ","_")+"_as_WordnetVerbEntry");

        entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");

        SyntacticBehaviour behaviour = new SyntacticBehaviour();
        behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");

        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));

        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

        entry.addSyntacticBehaviour(behaviour,sense);

        entry.addProvenance(provenance,sense);

        lexicon.addEntry(entry);
        
    }
    
    private static void createWordnetAdjectiveEntry(String label, Lexicon lexicon, String uri) {
        LexicalEntry entry = new LexicalEntry(Language.EN);
        entry.setCanonicalForm(label);


        Sense sense = new Sense();
        Reference ref = new SimpleReference(uri);
        sense.setReference(ref);

        Provenance provenance = new Provenance();
        provenance.setFrequency(1);

        sense.setReference(ref);

        //System.out.println(adjective);
        entry.setURI(lexicon.getBaseURI()+"LexicalEntry_"+label.replace(" ","_")+"_as_WordnetAdjectiveEntry");

        entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");

        SyntacticBehaviour behaviour = new SyntacticBehaviour();
        behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicateFrame");

        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","object",null));
        behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));

        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
        sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));

        entry.addSyntacticBehaviour(behaviour,sense);

        entry.addProvenance(provenance,sense);

        lexicon.addEntry(entry);
        
    }

    private static void runWornetPropertyApproach(Set<String> properties, Lexicon lexicon, Wordnet wordnet, StanfordLemmatizer sl) {
        for(String uri : properties){
            String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?label WHERE{<"+uri+"> rdfs:label ?label. FILTER (lang(?label) = 'en') }";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            Set<String> labels = new HashSet<>();
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();
                String label = (qs.get("?label").toString());
                label = label.replace("@en","");
                label = label.replace("\"", "");
                String lemma = sl.getLemma(label);
                boolean b_nouns = false;
                boolean b_adverbs = false;
                boolean b_verbs = false;
                boolean b_adjectives = false;
            
                Set<String> canonicalForms = new HashSet<>();
                canonicalForms.addAll(wordnet.getNounSynonyms(lemma));
                if(!canonicalForms.isEmpty()){
                    b_nouns = true;
                    canonicalForms.add(lemma);
                    for(String c : canonicalForms){
                        c = c.replace("_"," ");
                        createWordnetNounEntry(c,lexicon,uri);
                    }
                }

                canonicalForms.clear();
                canonicalForms.addAll(wordnet.getAdjectiveSynonyms(label));
                if(!canonicalForms.isEmpty()){
                    b_adjectives = true;
                    canonicalForms.add(label);
                    for(String c : canonicalForms){
                        c = c.replace("_"," ");
                        createWordnetAdjectiveEntry(c,lexicon,uri);
                    }
                }

                canonicalForms.clear();
                canonicalForms.addAll(wordnet.getAdverbSynonyms(lemma));
                if(!canonicalForms.isEmpty()){
                    b_adverbs = true;
                    canonicalForms.add(lemma);
                    for(String c : canonicalForms){
                        c = c.replace("_"," ");
                        createWordnetVerbEntry(c,lexicon,uri);
                    }
                }

                canonicalForms.clear();
                canonicalForms.addAll(wordnet.getVerbSynonyms(lemma));
                if(!canonicalForms.isEmpty()){
                    b_verbs = true;
                    canonicalForms.add(lemma);
                    for(String c : canonicalForms){
                        c = c.replace("_"," ");
                        createWordnetVerbEntry(c,lexicon,uri);
                    }
                }
                
                /*
                if notheing else is generated before....
                */
                if(!b_nouns && !b_adverbs && !b_verbs && !b_adjectives){
                     createWordnetVerbEntry(lemma,lexicon,uri);
                     createWordnetAdjectiveEntry(label,lexicon,uri);
                     createWordnetNounEntry(lemma,lexicon,uri);
                }
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
                    String input = entry.getCanonicalForm()+"\t"+reference.getURI()+"\t";
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
                    writer.write(key+Integer.toString(hm_int.get(key))+"\n");
                }
                writer.close();
        } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        
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

    private static Set<String> wordnetDisambiguation(String uri, String inputLabel, Wordnet wordnet,List<String> stopwords, IndexSearcher searcher,Analyzer analyzer) throws ParseException, IOException {
        
        Set<String> wordnetLabels = wordnet.getAllSynonyms(inputLabel);
        System.out.println("Input:");
        for(String l : wordnetLabels){
            System.out.println(l);
        }
        if(wordnetLabels.size()<=3){
            return wordnetLabels;
        }
        
        else{
            String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?label WHERE{?res <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <"+uri+">. ?res rdfs:label ?label. FILTER (lang(?label) = 'en') } LIMIT 100";
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            ResultSet results = qexec.execSelect();
            Set<String> labels = new HashSet<>();
            while ( results.hasNext() ) {
                 QuerySolution qs = results.next();
                 labels.add(qs.get("?label").toString());
            }

            Map<String,Integer> hm = new HashMap<>();
            for(String label : labels){
                label = label.replace("@en","");
                label = label.replace("\"", "");
                Map<String,Integer> bagOfWords = new HashMap<>();
                try{
                    bagOfWords = getBagOfWords(label,stopwords,searcher,analyzer);
                }
                catch(Exception e){}
                for(String x : bagOfWords.keySet()){
                    if(hm.containsKey(x)){
                        int value = hm.get(x);
                        hm.put(x, value+bagOfWords.get(x));
                    }
                    else{
                        hm.put(x, bagOfWords.get(x));
                    }
                }
            }
            Set<String> terms = new HashSet<>();
            Map<String,Double> tmp_terms = new HashMap();
            for(String x: wordnetLabels){
                List<Double> list_nld = new ArrayList<>();
                for(String key: hm.keySet()){
                    list_nld.add(Levenshtein.normalizedLevenshteinDistance(x, key)*hm.get(key));
                }
                double value = 0;
                for(Double d:list_nld) value+=d;
                //normalization
                if(value!=0)
                    tmp_terms.put(x, value/list_nld.size());
            }

            for(String x:tmp_terms.keySet()){
                if(tmp_terms.get(x)>0.6)terms.add(x);
            }
            System.out.println("Output:");
            for(String l : terms){
                System.out.println(l);
            }
        
            return terms;
        }
        

    }

    private static Map<String, Integer> getBagOfWords(String label, List<String> stopwords, IndexSearcher searcher,Analyzer analyzer) throws ParseException, IOException {
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(new QueryParser("plain", analyzer).parse("\""+label.toLowerCase()+"\""), BooleanClause.Occur.MUST);
        
        Map<String, Integer> bag = new HashMap<String,Integer>();
	
        TopScoreDocCollector collector = TopScoreDocCollector.create(99);
        searcher.search(booleanQuery, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          ArrayList<String> result = new ArrayList<>();
          String sentence = d.get("parsed");
          String[] tmp = sentence.split("\t\t");
          sentence = "";
          for(String x : tmp){
              sentence+=" "+x.split("\t")[1];
          }
          for(String x : sentence.split(" ")){
              if(!stopwords.contains(x)&&isAlpha(x)){
                  if(bag.containsKey(x)){
                      int value = bag.get(x);
                      bag.put(x, value+1);
                  }
                  else{
                      bag.put(x, 1);
                  }
              }
          }
        }
        
        return bag;
    }
}