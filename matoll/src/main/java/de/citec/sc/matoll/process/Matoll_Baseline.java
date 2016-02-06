/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.process;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.io.Config;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.utils.RelationshipEdge;
import de.citec.sc.matoll.utils.Stopwords;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.jgrapht.DirectedGraph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.xml.sax.SAXException;

/**
 *
 * @author swalter
 */
public class Matoll_Baseline {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {



        String directory;
        String gold_standard_lexicon;
        String output_lexicon;
        String configFile;
        Language language;
        String output;

        Stopwords stopwords=new Stopwords();

        HashMap<String,Double> maxima; 
        maxima = new HashMap<String,Double>();


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

        gold_standard_lexicon = config.getGoldStandardLexicon();

        String model_file = config.getModel();

        output_lexicon = config.getOutputLexicon();
        output = config.getOutput();


        language = config.getLanguage();

        LexiconLoader loader = new LexiconLoader();
	
        Lexicon gold = loader.loadFromFile(gold_standard_lexicon);



        Set<String> gold_entries = new HashSet<>();
        Set<String> uris = new HashSet<>();
        
        //consider only properties
        for(LexicalEntry entry: gold.getEntries()){
            try{
                 for(Sense sense: entry.getSenseBehaviours().keySet()){
                     String tmp_uri = sense.getReference().getURI().replace("http://dbpedia.org/ontology/", "");
                     if(!Character.isUpperCase(tmp_uri.charAt(0))){
                        gold_entries.add(entry.getCanonicalForm()+" "+sense.getReference().getURI());
                        uris.add(sense.getReference().getURI());
                     }
                 }
            }
            catch(Exception e){};
        }
//        for(String x: gold_entries) System.out.println(x);
        Lexicon automatic_lexicon = new Lexicon();
        automatic_lexicon.setBaseURI(config.getBaseUri());


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


        Set<String> results = new HashSet<>();
        Map<String,Integer> shortestpatterns = new HashMap<>();
        Set<String> fragments = new HashSet<>();
        for(File file:list_files){
            Model model = RDFDataMgr.loadModel(file.toString());
            sentences.clear();
            sentences = getSentences(model);
            for(Model sentence: sentences){
                Sentence sentenceObject = returnSentence(sentence);
                subj = sentenceObject.getSubjOfProp();
                obj = sentenceObject.getObjOfProp();
                try{
                     if (!stopwords.isStopword(obj, language) 
                        && !stopwords.isStopword(subj, language) 
                        && !subj.equals(obj) 
                        && !subj.contains(obj) 
                        && !obj.contains(subj)) {
                    reference = getReference(sentence);
                    
//                    String str = "ZZZZL <%= dsn %> AFFF <%= AFG %>";
                    String str = sentenceObject.getSentence();
                    doShortestPathExtraction(sentence,subj,obj,shortestpatterns,fragments,reference);
                    Pattern pattern = Pattern.compile(subj.toLowerCase()+"(.*?)"+obj.toLowerCase());
                    Matcher matcher = pattern.matcher(str.toLowerCase());
                    while (matcher.find()) {
                        String tmp = matcher.group(1);
                        tmp = firstClean(tmp);
                        
                        if(tmp.length()>2 && tmp.split(" ").length <3 && !stopwords.isStopword(tmp, language)){
                            tmp = secondClean(tmp);
                            results.add(tmp+" "+reference);
                            /*
                            TODO:
                            + Identify posTags from each term in pattern (use information of depedency graph)
                            + Only accept those terms which are noun, verbs etc (similar to the patterns)
                            + Create entry based on pos tag
                            */
                            }
                        }
                    }        
                }       
                
                catch(Exception e){
                    
                
                }
            }
            model.close();
        }

        System.out.println("Extracted entries");
//        for(String x: results)System.out.println(x);
        
        /*
        Calculate recall
        */
        int overall_entries = 0;
        int correct_entries = 0;
        for(String uri:uris){
            for(String g: gold_entries){
                if(g.contains(uri)){
                    overall_entries+=1;
                    for(String r:results){
                        if(g.equals(r)){
                            correct_entries+=1;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(overall_entries);
        System.out.println(correct_entries);
        System.out.println("Baseline1: "+(correct_entries+0.0)/overall_entries);
        
        
        overall_entries = 0;
        correct_entries = 0;
        for(String g: gold_entries){
            overall_entries+=1;
            for(String r:results){
                if(g.equals(r)){
                    correct_entries+=1;
                    break;
                }
            }            
        }
        System.out.println(overall_entries);
        System.out.println(correct_entries);
        System.out.println("Baseline1b: "+(correct_entries+0.0)/overall_entries);
        
        
        
        overall_entries = 0;
        correct_entries = 0;
        for(String uri:uris){
            for(String g: gold_entries){
                if(g.contains(uri)){
                    overall_entries+=1;
                    for(String r:fragments){
                        if(g.equals(r)){
                            correct_entries+=1;
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(overall_entries);
        System.out.println(correct_entries);
        System.out.println("Baseline2: "+(correct_entries+0.0)/overall_entries);
        
        
        overall_entries = 0;
        correct_entries = 0;
        for(String g: gold_entries){
            overall_entries+=1;
            for(String r:fragments){
                if(g.equals(r)){
                    correct_entries+=1;
                    break;
                }
            }            
        }
        System.out.println(overall_entries);
        System.out.println(correct_entries);
        System.out.println("Baseline2b: "+(correct_entries+0.0)/overall_entries);
        
        
        
        
        
        
        
        
        
        
        shortestpatterns.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) 
        .limit(15) 
        .forEach(System.out::println); // or any other terminal method

//        LexiconSerialization serializer = new LexiconSerialization(config.removeStopwords());
//
//        Model model = ModelFactory.createDefaultModel();
//
//        serializer.serialize(automatic_lexicon, model);
//
//        FileOutputStream out = new FileOutputStream(new File(output_lexicon.replace(".lex","_beforeTraining.ttl")));
//
//        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
//
//        out.close();



    }
    
    private static Sentence returnSentence(Model model) {
        /* In the model is always only one plain_sentence*/
		
        String plain_sentence = "";
        String subjOfProp = "";
        String objOfProp = "";

        StmtIterator iter = model.listStatements(null,model.createProperty("conll:sentence"), (RDFNode) null);
        Statement stmt;
        stmt = iter.next();
        plain_sentence = stmt.getObject().toString();

        iter = model.listStatements(null,model.createProperty("own:subj"), (RDFNode) null);
        stmt = iter.next();
        subjOfProp = stmt.getObject().toString();

        iter = model.listStatements(null,model.createProperty("own:obj"), (RDFNode) null);
        stmt = iter.next();
        objOfProp = stmt.getObject().toString();

        Sentence sentence = new Sentence(plain_sentence,subjOfProp,objOfProp);

        try{
            iter = model.listStatements(null,model.createProperty("own:subjuri"), (RDFNode) null);
            stmt = iter.next();
            sentence.setSubjOfProp_uri(stmt.getObject().toString());
        }catch (Exception e){}


        try{
            iter = model.listStatements(null,model.createProperty("own:objuri"), (RDFNode) null);
            stmt = iter.next();
            sentence.setObjOfProp_uri(stmt.getObject().toString());
        }catch (Exception e){}

        return sentence;

	}
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
    
    private static String getReference(Model model) {
		StmtIterator iter = model.listStatements(null,model.getProperty("conll:reference"), (RDFNode) null);
		Statement stmt;
		while (iter.hasNext()) {
			stmt = iter.next();
                   return stmt.getObject().toString();
                }
		
		return null;
	}

    private static void doShortestPathExtraction(Model sentence, String subj, String obj, Map<String,Integer> patterns, Set<String> fragments, String reference) throws IOException, InterruptedException {
        
//        DirectedGraph<Integer, RelationshipEdge> g = new DefaultDirectedGraph<Integer, RelationshipEdge>(RelationshipEdge.class);
        UndirectedGraph<Integer, RelationshipEdge> g = new SimpleGraph<Integer, RelationshipEdge>(RelationshipEdge.class);


        Statement stmt;
        int id_subject = 0;
        int id_object = 0;
        Set<Integer> hm = new HashSet<>();
        Map<String,String> relations = new HashMap<>();
        Map<String,String> forms = new HashMap<>();
        
        StmtIterator iter = sentence.listStatements(null, sentence.createProperty("conll:form"), (RDFNode) null);
         while (iter.hasNext()) {
            stmt = iter.next();
            String subject = stmt.getSubject().toString();
            subject = subject.replace("token:token","").replace("_","");
            Integer subject_value = Integer.valueOf(subject);
            String object = stmt.getObject().toString();
            if(subj.contains(object)){
                id_subject = subject_value;
            }
            if(obj.contains(object)){
                id_object = subject_value;
            }
            forms.put(subject, object);
         }
         
         iter = sentence.listStatements(null, sentence.createProperty("conll:deprel"), (RDFNode) null);
         while (iter.hasNext()) {
            stmt = iter.next();
            String subject = stmt.getSubject().toString();
            subject = subject.replace("token:token","").replace("_","");
            String object = stmt.getObject().toString();
            relations.put(subject, object);
         }
         
        iter = sentence.listStatements(null, sentence.createProperty("conll:head"), (RDFNode) null);

         while (iter.hasNext()) {
            stmt = iter.next();
            String subject = stmt.getSubject().toString();
            String object = stmt.getObject().toString();
            int object_value = 0;
            int subject_value = 0;
            subject = subject.replace("token:token","").replace("_","");
            object = object.replace("token:token","").replace("_","");
            object_value = Integer.valueOf(object);
            subject_value = Integer.valueOf(subject);
            if(!hm.contains(subject_value)){
                g.addVertex(subject_value);
                hm.add(subject_value);
            }
            
            if(!hm.contains(object_value)){
                g.addVertex(object_value);
                hm.add(object_value);
            }
            
            g.addEdge(subject_value, object_value,new RelationshipEdge<String>(subject, object, relations.get(subject), forms.get(subject)));
         }
         

         
         
         List path;
         try{
             
                if(id_subject>0 && id_object>0 && id_subject!=id_object){
                    KShortestPaths ksp = new KShortestPaths(g, id_subject, 2); 
                    List<GraphPath> paths = ksp.getPaths(id_object);
                    if (paths != null){
                        for (GraphPath p : paths) {
                            if(p.getEdgeList().size()>2){
//                                System.out.println(p.getEdgeList());
                                String tmp = "";
                                String tmp2 = "";
                                for(Object x:p.getEdgeList()){
                                    tmp+=" "+x.toString().split("-->")[1];
                                    tmp2 += x.toString().split("-->")[2];
                                }
                                tmp = tmp.trim();
                                tmp2 = tmp2.trim();
                                tmp2 = firstClean(tmp2);
                                tmp2 = secondClean(tmp2);
                                fragments.add(tmp2+" "+reference);
                                if(patterns.containsKey(tmp)){
                                    patterns.put(tmp, patterns.get(tmp)+1);
                                }
                                else patterns.put(tmp,1);
//                                System.out.println(p.getStartVertex());
//                                System.out.println(p.getEndVertex());
//                                System.out.println();
                            }
                            
                        }
                    }
                }
                
 
         }
         catch(Exception e){
             e.printStackTrace();
         }
        


    
    }

    private static String firstClean(String tmp) {
        tmp = tmp.replace(",","");
        tmp = tmp.replace(".","");
        tmp = tmp.replace(";","");
        tmp = tmp.replace("?","");
        tmp = tmp.replace("!","");
        tmp = tmp.replace("'", "");
        tmp = tmp.replace("-lrb-", "");
        tmp = tmp.replace("-rbr-", "");
        tmp = tmp.trim();
        return tmp;
    }

    private static String secondClean(String tmp) {
        tmp = tmp.replace("with","");
        tmp = tmp.replace("to","");
        tmp = tmp.replace("from","");
        tmp = tmp.replace("by","");
        tmp = tmp.replace("after","");
        tmp = tmp.replace("of","");
        tmp = tmp.replace("and","");
        tmp = tmp.trim();
        if(tmp.contains(" ")){
            String tmp2 = "";
            for(String z: tmp.split(" ")){
                if(z.length()>1) tmp2+=" "+z;
            }
            tmp = tmp2.trim();
        }
        return tmp;
    }
    
    
    
    
    
    
    
}


