/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.matoll.utils;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.evaluation.LemmaBasedEvaluation;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.patterns.PatternLibrary;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_DatatypeNoun;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_DatatypeNoun_2;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Intransitive_PP;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_appos;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_copulative;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_player;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Noun_PP_possessive;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Predicative_Participle_copulative;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Predicative_Participle_passive;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Transitive_Passive;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_Transitive_Verb;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Intransitive_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_PP_appos;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive_appos;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Noun_Possessive_b;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Predicative_Adjective;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Refelexive_Transitive_PP;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Transitive;
import de.citec.sc.matoll.patterns.german.SparqlPattern_DE_Transitive_Passive;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Intransitive_PP;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_appos;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_appos_b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative_b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Noun_PP_copulative_withHop;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Predicative_Participle_Copulative;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Predicative_Participle_Passive;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Reflexive_Transitive_PP;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive_Reciprocal;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_Transitive_passive;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/**
 *
 * @author swalter
 */
public class DoEvaluations {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        String path_gold_english = "../lexica/dbpedia_en.rdf";
        String path_gold_german = "../lexica/dbpedia_de.rdf";
        String path_gold_spanish = "../lexica/dbpedia_es.rdf";
        String path_automatic_english = "dbpedia2014Full_new_beforeTraining.ttl";
        String path_automatic_german = "dbpedia2014_DE_Full_beforeTraining.ttl";
        String path_automatic_spanish = "dbpedia2014_ES_Full_beforeTraining.ttl";
        String path_automatic_adjectives = "new_adjectives.ttl";
        
        LexiconLoader loader = new LexiconLoader();
        Lexicon gold_english = loader.loadFromFile(path_gold_english);
        Lexicon gold_german = loader.loadFromFile(path_gold_german);
        Lexicon gold_spanish = loader.loadFromFile(path_gold_spanish);
        
        Lexicon automatic_english = loader.loadFromFile(path_automatic_english);
        Lexicon automatic_german = loader.loadFromFile(path_automatic_german);
        Lexicon automatic_spanish = loader.loadFromFile(path_automatic_spanish);
        
        Lexicon automatic_label_based_approach = loader.loadFromFile(path_automatic_adjectives);
        
        Lexicon joined = new Lexicon();
        joined.setBaseURI("http://localhost:8080/");
        for(LexicalEntry entry:automatic_english.getEntries())joined.addEntry(entry);
        for(LexicalEntry entry:automatic_label_based_approach.getEntries())joined.addEntry(entry);
        
        
        List<SparqlPattern> patterns = new ArrayList<>();
					
        patterns.add(new SparqlPattern_EN_Intransitive_PP());
        patterns.add(new SparqlPattern_EN_Noun_PP_appos());
        patterns.add(new SparqlPattern_EN_Noun_PP_copulative());
        patterns.add(new SparqlPattern_EN_Predicative_Participle_passive());
        patterns.add(new SparqlPattern_EN_Transitive_Verb());
        patterns.add(new SparqlPattern_EN_Predicative_Participle_copulative());
        patterns.add(new SparqlPattern_EN_Transitive_Passive());
        patterns.add(new SparqlPattern_EN_Noun_PP_possessive());
        patterns.add(new SparqlPattern_EN_DatatypeNoun());
        patterns.add(new SparqlPattern_EN_DatatypeNoun_2());
        patterns.add((new SparqlPattern_EN_Noun_PP_player()));
        patterns.add(new SparqlPattern_DE_Predicative_Adjective());
        patterns.add(new SparqlPattern_DE_Noun_PP());
        patterns.add(new SparqlPattern_DE_Noun_Possessive());
        patterns.add(new SparqlPattern_DE_Noun_Possessive_b());
        patterns.add(new SparqlPattern_DE_Transitive());
        patterns.add(new SparqlPattern_DE_Transitive_Passive());
        //Patterns.add(new SparqlPattern_DE_Transitive_Passive_optional());
        patterns.add(new SparqlPattern_DE_Intransitive_PP());
        patterns.add(new SparqlPattern_DE_Refelexive_Transitive_PP());
        patterns.add(new SparqlPattern_DE_Noun_PP_appos());
        patterns.add(new SparqlPattern_DE_Noun_Possessive_appos());					
        patterns.add(new SparqlPattern_ES_Transitive());
        patterns.add(new SparqlPattern_ES_Noun_PP_copulative_b());
        patterns.add(new SparqlPattern_ES_Noun_PP_copulative_withHop());
        patterns.add(new SparqlPattern_ES_Noun_PP_copulative());
        patterns.add(new SparqlPattern_ES_Noun_PP_appos_b());
        patterns.add(new SparqlPattern_ES_Noun_PP_appos());
        patterns.add(new SparqlPattern_ES_Predicative_Participle_Copulative());
        patterns.add(new SparqlPattern_ES_Predicative_Participle_Passive());
        patterns.add(new SparqlPattern_ES_Intransitive_PP());
        patterns.add(new SparqlPattern_ES_Transitive_Reciprocal());
        patterns.add(new SparqlPattern_ES_Reflexive_Transitive_PP());
        patterns.add(new SparqlPattern_ES_Transitive_passive());
                                        
        PatternLibrary library = new PatternLibrary();
        library.setPatterns(patterns);
        PrintWriter writer = new PrintWriter("results_evaluation.txt");
        
        LexiconSerialization serial = new LexiconSerialization(library.getPatternSparqlMapping(),true);
        Model model = ModelFactory.createDefaultModel();
        serial.serialize(joined, model);		
        FileOutputStream out = new FileOutputStream(new File("joined.ttl"));
        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
        out.close();
        
        
        Set<String> set_gold = new HashSet<>();
        Set<String> set_automatic = new HashSet<>();
        for(LexicalEntry entry:gold_english.getEntries()){
            for(Sense sense:entry.getSenseBehaviours().keySet()){
                set_gold.add(sense.getReference().getURI());
            }
        }
        
        for(LexicalEntry entry:automatic_english.getEntries()){
            for(Sense sense:entry.getSenseBehaviours().keySet()){
                set_automatic.add(sense.getReference().getURI());
            }
        }
        set_automatic.retainAll(set_gold);
        List<String> uris = new ArrayList<>();
        for(String x: set_automatic) uris.add(x);

                                
        /*
        English only properties
        */
        writer.println("English: Only properties");
        List<Double> result = LemmaBasedEvaluation.evaluate(automatic_label_based_approach, gold_english,true,true);
        writer.println("(Label Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,true,false);
        writer.println("(Label Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,true,true);
        writer.println("(Dependency Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,true,false);
        writer.println("(Dependency Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,true,true);
        writer.println("(All): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,true,false);
        writer.println("(All): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,true,uris,true);
        writer.println("(Only automatic properties): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,true,uris,false);
        writer.println("(Only automatic porperties): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        writer.println("####################");
        writer.println("####################");
        
        
        writer.println("English: Properties and Classes");
        result = LemmaBasedEvaluation.evaluate(automatic_label_based_approach, gold_english,false,true);
        writer.println("(Label Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,false,false);
        writer.println("(Label Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,false,true);
        writer.println("(Dependency Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_english, gold_english,false,false);
        writer.println("(Dependency Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,false,true);
        writer.println("(All): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(joined, gold_english,false,false);
        writer.println("(All): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        writer.println("####################");
        writer.println("####################");
        writer.println();
        writer.println();
        
        /*
        German 
        */
        writer.println("German");
        
        result = LemmaBasedEvaluation.evaluate(automatic_german, gold_german,true,true);
        writer.println("(Dependency Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_german, gold_german,true,false);
        writer.println("(Dependency Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        writer.println("####################");
        writer.println("####################");
        writer.println();
        writer.println();
        /*
        German 
        */
        writer.println("Spanish");
        
        result = LemmaBasedEvaluation.evaluate(automatic_spanish, gold_spanish,true,true);
        writer.println("(Dependency Based Approach): Macro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        result = LemmaBasedEvaluation.evaluate(automatic_spanish, gold_spanish,true,false);
        writer.println("(Dependency Based Approach): Micro  P:"+result.get(0)+", R:"+result.get(1)+", F:"+result.get(2));
        
        
        
        
        

        writer.close();
        
    }
}
