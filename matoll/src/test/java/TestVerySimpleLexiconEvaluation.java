
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.evaluation.VerySimpleLexiconEvaluation;
import de.citec.sc.matoll.io.LexiconLoader;
import java.util.List;




/**
 *
 * @author swalter
 */
public class TestVerySimpleLexiconEvaluation {
 

    public static void main(String[] args){
                
        LexiconLoader loader = new LexiconLoader();
		
        Lexicon gold = loader.loadFromFile("../lexica/foaf.ttl");
        Lexicon gold1 = loader.loadFromFile("../dbpedia_en.rdf");
        Lexicon lexicon1 = loader.loadFromFile("../lexica/foaf_1.ttl");
        Lexicon lexicon2 = loader.loadFromFile("/Users/swalter/Documents/Test/dbpedia2014Full_new_beforeTraining.ttl");
        
        List<Double> result = VerySimpleLexiconEvaluation.evaluate(gold, gold,false);
        System.out.println("----------");
        System.out.println("Should be true:");
        System.out.println(result.get(0).equals(1.0)&&result.get(1).equals(1.0)&&result.get(2).equals(1.0));
        System.out.println("----------");
        
        result = VerySimpleLexiconEvaluation.evaluate(gold1, gold1,true);
        System.out.println("Should be true:");
        System.out.println(result.get(0).equals(1.0)&&result.get(1).equals(1.0)&&result.get(2).equals(1.0));
        System.out.println("----------");
        
        result = VerySimpleLexiconEvaluation.evaluate(gold, gold1,false);
        System.out.println("Should be false:");
        System.out.println(result.get(0).equals(1.0)&&result.get(1).equals(1.0)&&result.get(2).equals(1.0));
        System.out.println("----------");
        
        result = VerySimpleLexiconEvaluation.evaluate(gold1, gold,false);
        System.out.println("Should be false:");
        System.out.println(result.get(0).equals(1.0)&&result.get(1).equals(1.0)&&result.get(2).equals(1.0));
        System.out.println("----------");
        
        result = VerySimpleLexiconEvaluation.evaluate(lexicon1, gold,false);
        System.out.println("Should be true:");
        System.out.println(result.get(0).equals(1.0)&&result.get(1).equals(0.733));
//gold
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, friend@en, http://xmlns.com/foaf/0.1/know]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#adjective, acquainted@en, http://xmlns.com/foaf/0.1/friend], [http://www.lexinfo.net/ontology/2.0/lexinfo#noun, friend@en, http://xmlns.com/foaf/0.1/friend], [http://www.lexinfo.net/ontology/2.0/lexinfo#verb, know@en, http://xmlns.com/foaf/0.1/friend]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, project@en, http://xmlns.com/foaf/0.1/Project]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, member@en, http://xmlns.com/foaf/0.1/member]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, person@en, http://xmlns.com/foaf/0.1/Person]]
//
//
//
//automatic
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, friend@en, http://xmlns.com/foaf/0.1/friend], [http://www.lexinfo.net/ontology/2.0/lexinfo#verb, know@en, http://xmlns.com/foaf/0.1/friend]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, project@en, http://xmlns.com/foaf/0.1/Project]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, member@en, http://xmlns.com/foaf/0.1/member]]
//[[http://www.lexinfo.net/ontology/2.0/lexinfo#noun, person@en, http://xmlns.com/foaf/0.1/Person]]
        System.out.println("----------");
        
        result = VerySimpleLexiconEvaluation.evaluate(lexicon2, gold1,true);
        System.out.println(result.get(0));
        System.out.println(result.get(1));
        System.out.println(result.get(2));
        System.out.println("----------");
        
    }
}
