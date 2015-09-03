
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import de.citec.sc.matoll.patterns.PatternLibrary;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_1;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_2;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_3;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_4;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_5;
import de.citec.sc.matoll.patterns.english.SparqlPattern_EN_6;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_1;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_2c;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_3;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_4;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_5;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_6;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_7;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_7b;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_8;
import de.citec.sc.matoll.patterns.spanish.SparqlPattern_ES_9;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author swalter
 */
public class Test {
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        LexiconLoader loader = new LexiconLoader();
        
//        Lexicon eins = loader.loadFromFile("/Users/swalter/Documents/Test/dbpedia2014_DE_Full_beforeTraining.ttl");
        Lexicon zwei = loader.loadFromFile("/Users/swalter/Documents/Test/dbpedia2014Full_new_beforeTraining.ttl");
        zwei.setBaseURI("http://localhost:8080/");
//        Lexicon drei = loader.loadFromFile("/Users/swalter/Documents/Test/dbpedia2014_ES_Full_beforeTraining.ttl");        //System.out.println(eins.getEntries().size());
        System.out.println(zwei.getEntries().size());
        //System.out.println(drei.getEntries().size());
//        
////        zwei.addLexicon(eins);
////        zwei.addLexicon(drei);
//        for(LexicalEntry lex : eins.getEntries())zwei.addEntry(lex);
//        for(LexicalEntry lex : drei.getEntries())zwei.addEntry(lex);
        System.out.println(zwei.getEntries().size());
//        
        List<SparqlPattern> patterns = new ArrayList<>();
					
        patterns.add(new SparqlPattern_EN_1());
        patterns.add(new SparqlPattern_EN_2());
        patterns.add(new SparqlPattern_EN_3());
        patterns.add(new SparqlPattern_EN_4());
        patterns.add(new SparqlPattern_EN_5());
        patterns.add(new SparqlPattern_EN_6());
        patterns.add(new SparqlPattern_ES_1());
        patterns.add(new SparqlPattern_ES_2());
        patterns.add(new SparqlPattern_ES_2b());
        patterns.add(new SparqlPattern_ES_2c());
        patterns.add(new SparqlPattern_ES_3());
        patterns.add(new SparqlPattern_ES_4());
        patterns.add(new SparqlPattern_ES_5());
        patterns.add(new SparqlPattern_ES_6());
        patterns.add(new SparqlPattern_ES_7());
        patterns.add(new SparqlPattern_ES_7b());
        patterns.add(new SparqlPattern_ES_8());
        patterns.add(new SparqlPattern_ES_9());
        PatternLibrary library = new PatternLibrary();
        library.setPatterns(patterns);
//        
        LexiconSerialization serial = new LexiconSerialization(library.getPatternSparqlMapping(),true);
        Model model = ModelFactory.createDefaultModel();
        serial.serialize(zwei, model);		
        FileOutputStream out = new FileOutputStream(new File("joined.ttl"));
        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
        out.close();
        
    }
}
