/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.process;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sentence;
import de.citec.sc.matoll.io.Config;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.utils.Stopwords;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.jena.riot.RDFFormat;
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


        TObjectIntHashMap<String> freq = new TObjectIntHashMap<String>();


        for(File file:list_files){
            Model model = RDFDataMgr.loadModel(file.toString());
            sentences.clear();
            sentences = getSentences(model);
            for(Model sentence: sentences){
                Sentence sentenceObject = returnSentence(sentence);
                subj = sentenceObject.getSubjOfProp();
                obj = sentenceObject.getObjOfProp();
                if (!stopwords.isStopword(obj, language) 
                        && !stopwords.isStopword(subj, language) 
                        && !subj.equals(obj) 
                        && !subj.contains(obj) 
                        && !obj.contains(subj)) {
                    reference = getReference(sentence);
                    
//                    String str = "ZZZZL <%= dsn %> AFFF <%= AFG %>";
                    String str = sentenceObject.getSentence();
                    Pattern pattern = Pattern.compile(subj.toLowerCase()+"(.*?)"+obj.toLowerCase());
                    Matcher matcher = pattern.matcher(str.toLowerCase());
                    while (matcher.find()) {
                        String tmp = matcher.group(1);
                        tmp = tmp.replace(",","");
                        tmp = tmp.replace(".","");
                        tmp = tmp.replace(";","");
                        tmp = tmp.replace("?","");
                        tmp = tmp.replace("!","");
                        tmp = tmp.replace("'", "");
                        tmp = tmp.replace("-lrb-", "");
                        tmp = tmp.replace("-rbr-", "");
                        tmp = tmp.trim();
                        
                        if(tmp.length()>2 && tmp.split(" ").length <4 && !stopwords.isStopword(tmp, language)){
                            if(tmp.contains(" ")){
                                String tmp2 = "";
                                for(String z: tmp.split(" ")){
                                    if(z.length()>1) tmp2+=" "+z;
                                }
                                tmp = tmp2.trim();
                            }
                            System.out.println("subj:"+subj);
                            System.out.println("obj:"+obj);
                            System.out.println("Sentence:"+str);
                            System.out.println("pattern:"+tmp);
                            System.out.println();
                            System.out.println();
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
            model.close();
        }

        System.out.println("Extracted entries");


        LexiconSerialization serializer = new LexiconSerialization(config.removeStopwords());

        Model model = ModelFactory.createDefaultModel();

        serializer.serialize(automatic_lexicon, model);

        FileOutputStream out = new FileOutputStream(new File(output_lexicon.replace(".lex","_beforeTraining.ttl")));

        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;

        out.close();



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
    
    
    
    
    
    
    
}
