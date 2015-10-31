
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.evaluation.LemmaBasedEvaluation;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io._LexiconLoaderGreaterK_;
import java.util.ArrayList;
import java.util.List;




/**
 *
 * @author swalter
 */
public class TestVerySimpleLexiconEvaluation {
 

    public static void main(String[] args){
                

        _LexiconLoaderGreaterK_ loader = new _LexiconLoaderGreaterK_(1);
        LexiconLoader normalloader = new LexiconLoader();
        Lexicon gold = normalloader.loadFromFile("../dbpedia_en.rdf");
        Lexicon lex = loader.loadFromFile("/Users/swalter/Downloads/ResultsEndOktober/dbpedia2014Full_new_beforeTraining.ttl");
        
        
        List<String> uris = new ArrayList<>();
        uris.add("http://dbpedia.org/ontology/spouse");
        uris.add("http://dbpedia.org/ontology/elevation");
        uris.add("http://dbpedia.org/ontology/deathCause");
        uris.add("http://dbpedia.org/ontology/deathPlace");
        uris.add("http://dbpedia.org/ontology/village");
        uris.add("http://dbpedia.org/ontology/writer");
        uris.add("http://dbpedia.org/ontology/successor");
        uris.add("http://dbpedia.org/ontology/foundedBy");
        uris.add("http://dbpedia.org/ontology/developer");
        uris.add("http://dbpedia.org/ontology/creator");
        uris.add("http://dbpedia.org/ontology/populationPlace");
//        for(Reference ref : lex.getReferences()) uris.add(ref.getURI());
        
        List<Double> result = LemmaBasedEvaluation.evaluate(lex, gold,true,uris,true);
        System.out.println("----------");
        System.out.println(result.get(0)+","+result.get(1)+","+result.get(2));
        System.out.println("----------");
        
        result = LemmaBasedEvaluation.evaluate(lex, gold,true,uris,false);
        System.out.println("----------");
        System.out.println(result.get(0)+","+result.get(1)+","+result.get(2));
        System.out.println("----------");
        

    }
}
