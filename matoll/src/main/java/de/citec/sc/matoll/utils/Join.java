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
import de.citec.sc.matoll.io._LexiconLoaderGreaterK_;
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
public class Join {
    public static void main(String[] args) throws FileNotFoundException, IOException{

        String path_automatic_english = "/Users/swalter/Downloads/new_results/dbpedia2014Full_new_beforeTraining.ttl";
        String path_automatic_german = "/Users/swalter/Downloads/new_results/dbpedia2014_DE_Full_beforeTraining.ttl";
        String path_automatic_spanish = "/Users/swalter/Downloads/new_results/dbpedia2014_ES_Full_beforeTraining.ttl";
        String path_automatic_adjectives = "/Users/swalter/Downloads/new_results/new_adjectives.ttl";
        
        _LexiconLoaderGreaterK_ loader = new _LexiconLoaderGreaterK_();
        
        Lexicon automatic_english = loader.loadFromFile(path_automatic_english,2);
        Lexicon automatic_german = loader.loadFromFile(path_automatic_german,1);
        Lexicon automatic_spanish = loader.loadFromFile(path_automatic_spanish,1);
        
        Lexicon automatic_label_based_approach = loader.loadFromFile(path_automatic_adjectives,2);
        
        Lexicon joined = new Lexicon();
        joined.setBaseURI("http://localhost:8080/");
        for(LexicalEntry entry:automatic_english.getEntries())joined.addEntry(entry);
        for(LexicalEntry entry:automatic_label_based_approach.getEntries())joined.addEntry(entry);
        for(LexicalEntry entry:automatic_german.getEntries())joined.addEntry(entry);
        for(LexicalEntry entry:automatic_spanish.getEntries())joined.addEntry(entry);
        
        
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
        
    
        
    }
}
