package de.citec.sc.matoll.LabelApproach;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.citec.sc.lemon.core.Language;
import static de.citec.sc.lemon.core.Language.EN;

import de.citec.sc.lemon.core.LexicalEntry;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Provenance;
import de.citec.sc.lemon.core.Reference;
import de.citec.sc.lemon.core.Restriction;
import de.citec.sc.lemon.core.Sense;
import de.citec.sc.lemon.core.SenseArgument;
import de.citec.sc.lemon.core.SimpleReference;
import de.citec.sc.lemon.core.SyntacticArgument;
import de.citec.sc.lemon.core.SyntacticBehaviour;
import de.citec.sc.lemon.io.LexiconSerialization;
import de.citec.sc.matoll.utils.Levenshtein;
import de.citec.sc.matoll.utils.OntologyImporter;
import de.citec.sc.matoll.utils.StanfordLemmatizer;
import de.citec.sc.matoll.utils.Wordnet;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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



/*
 * Good description for using Weka with Java:
 * http://stackoverflow.com/questions/21674522/get-prediction-percentage-in-weka-using-own-java-code-and-a-model/21678307#21678307
 */
public class ProcessWordnet {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String path_to_wordnet = "/Users/swalter/Backup/Software/WordNet-3.0";
		/*
		 * TODO: Automatically import via maven
		 */
		
		//String path_to_tagger_model ="resources/english-left3words/english-caseless-left3words-distsim.tagger";
                String path_to_tagger_model ="resources/english-caseless-left3words-distsim.tagger";
                
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
                
                Wordnet wordnet = new Wordnet(path_to_wordnet);
		

		
		
		/*
		 * Lexicon
		 */
		Lexicon lexicon = new Lexicon();
                lexicon.setBaseURI("http://localhost:8080/");
		
		


                runWornetPropertyApproach(properties,lexicon,wordnet,sl);
                
		runWornetClassApproach(classes,lexicon,wordnet,"/Users/swalter/Downloads/EnglishIndexReduced");
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		serializer.serialize(lexicon, model);
		
                String path = "new_adjectives.ttl";
		FileOutputStream out = new FileOutputStream(new File(path));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
                Set<String> entries = new HashSet<>();
                for(LexicalEntry entry: lexicon.getEntries()){
                    try{
                         for(Sense sense: entry.getSenseBehaviours().keySet()){
                             String tmp_uri = sense.getReference().getURI().replace("http://dbpedia.org/ontology/", "");
                             if(!Character.isUpperCase(tmp_uri.charAt(0))){
                                entries.add(entry.getCanonicalForm().toLowerCase()+" "+sense.getReference().getURI());
                             }
                         }
                    }
                    catch(Exception e){};
                }
                
                PrintWriter writer = new PrintWriter("baseline_PropClassWordNet.txt");
                for(String r:entries) writer.write(r+"\n");
                writer.close();

		
		
		
		
		
		 
		 
		 
		
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







    private static void runWornetClassApproach(Set<String> classes, Lexicon lexicon, Wordnet wordnet, String pathToIndex) throws FileNotFoundException, IOException, ParseException {
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
    
    
    
//    private static void extportTSV(Lexicon lexicon, String path){
//        Map<String,Double> hm_double = new HashMap<>();
//        Map<String,Integer> hm_int = new HashMap<>();
//        for(LexicalEntry entry : lexicon.getEntries()){
//            for(Sense sense:entry.getSenseBehaviours().keySet()){
//                Reference ref = sense.getReference();
//                if (ref instanceof de.citec.sc.lemon.core.SimpleReference){
//                    SimpleReference reference = (SimpleReference) ref;
//                    String input = entry.getCanonicalForm()+"\t"+reference.getURI()+"\t";
//                    if(hm_int.containsKey(input)){
//                            int freq = hm_int.get(input);
//                             hm_int.put(input, entry.getProvenance(sense).getFrequency()+freq);
//                        }
//                        else{
//                            hm_int.put(input, entry.getProvenance(sense).getFrequency());
//                        }
//                }
//                else if (ref instanceof de.citec.sc.lemon.core.Restriction){
//                    Restriction reference = (Restriction) ref;
//                    String input = entry.getCanonicalForm()+"\t"+reference.getValue()+"\t"+reference.getProperty()+"\t";
//                    if(entry.getProvenance(sense).getConfidence()!=null){
//                        if(hm_double.containsKey(input)){
//                            double value = hm_double.get(input);
//                             hm_double.put(input, entry.getProvenance(sense).getConfidence()+value);
//                        }
//                        else{
//                            hm_double.put(input, entry.getProvenance(sense).getConfidence());
//                        }
//                    }
//                    
//                }
//            }
//        }
//        
//        PrintWriter writer;
//        try {
//                writer = new PrintWriter(path+"_restriction.tsv");
//                for(String key:hm_double.keySet()){
//                    writer.write(key+Double.toString(hm_double.get(key))+"\n");
//                }
//                writer.close();
//        } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//        }
//
//        try {
//                writer = new PrintWriter(path+"_simple.tsv");
//                for(String key:hm_int.keySet()){
//                    writer.write(key+Integer.toString(hm_int.get(key))+"\n");
//                }
//                writer.close();
//        } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//        }
//        
//    }
    
    
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