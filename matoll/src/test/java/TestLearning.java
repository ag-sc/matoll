
import de.citec.sc.matoll.classifiers.WEKAclassifier;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.evaluation.LexiconEvaluationSimple;
import de.citec.sc.matoll.evaluation.VerySimpleLexiconEvaluation;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.utils.Learning;
import java.util.HashSet;
import java.util.List;
import java.util.Set;




/**
 *
 * @author swalter
 */
public class TestLearning {
    
     public static void main(String[] args) throws Exception{
        LexiconLoader loader = new LexiconLoader();
	Lexicon gold = loader.loadFromFile("/Users/swalter/Git/matoll/lexica/dbpedia_en.rdf");
        Lexicon automatic = loader.loadFromFile("/Users/swalter/Documents/resultsFirstTraining/resultsFirstTraining/dbpedia2014Full_new_beforeTraining.ttl");
        
        WEKAclassifier classifier = new WEKAclassifier(Language.EN);
        Learning.doTraining(automatic, gold, null, Language.EN, classifier,4);        
        Learning.doPrediction(automatic, gold, classifier, null, Language.EN);
        
        Lexicon learned = loader.loadFromFile("learned_lexiconEN.ttl");
        System.out.println(learned.size());
        LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
        
        HashSet<String> properties_gold = new HashSet<String>();
        HashSet<String> properties_automatic = new HashSet<String>();
        
        for(LexicalEntry entry : gold.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    properties_gold.add(uri);
                }
                else{
                    Restriction reference = (Restriction) ref;
                    //properties_gold.add(reference.getProperty());
                }
            }
         }
        
        for(LexicalEntry entry : automatic.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    properties_automatic.add(uri);
                }
                else{
                    Restriction reference = (Restriction) ref;
                    properties_automatic.add(reference.getProperty());
                }
            }
         }
        
        
        System.out.println("properties_gold.size():"+properties_gold.size());
        System.out.println("properties_automatic.size():"+properties_automatic.size());
        properties_gold.retainAll(properties_automatic);
        System.out.println("#common properties:"+properties_gold.size());
        
        Lexicon new_gold = new Lexicon();
        Lexicon new_automatic = new Lexicon();
        
        for(LexicalEntry entry : gold.getEntries()){
            Set<Reference> references = entry.getReferences();
            for(Reference ref:references){
                if (ref instanceof de.citec.sc.matoll.core.SimpleReference){
                    String uri = ref.getURI();
                    try{
                        if(properties_gold.contains(uri)){
                            new_gold.addEntry(entry);
                            break;
                        }
                    }
                    catch (Exception e){
                        /*
                        There is not always a ref in gold....
                        */
                    }
                    
                }
            }
         }
//        System.out.println();
//        System.out.println("LexiconEvaluationSimple:");
//        System.out.println("With gold:");
//        eval.evaluate(automatic,gold);
//        System.out.println("Automatic => P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")+"\tF:"+eval.getFMeasure("lemma"));
//        
//        eval.evaluate(learned,gold);
//        System.out.println("Learned => P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")+"\tF:"+eval.getFMeasure("lemma"));
//        
//        System.out.println();
//        System.out.println("With reduced gold:");
//        eval.evaluate(automatic,new_gold);
//        System.out.println("Automatic => P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")+"\tF:"+eval.getFMeasure("lemma"));
//        
//        eval.evaluate(learned,new_gold);
//        System.out.println("Learned => P:"+eval.getPrecision("lemma")+"\tR:"+eval.getRecall("lemma")+"\tF:"+eval.getFMeasure("lemma"));
        
        
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("VerySimpleLexiconEvaluation:");
        System.out.println("With gold:");
        List<Double> result = VerySimpleLexiconEvaluation.evaluate(automatic,gold);
        System.out.println("Automatic => P:"+result.get(0)+"\tR:"+result.get(1)+"\tF:"+result.get(2));
        
        result = VerySimpleLexiconEvaluation.evaluate(learned,gold);
        System.out.println("Learned => P:"+result.get(0)+"\tR:"+result.get(1)+"\tF:"+result.get(2));
        
        System.out.println();
        System.out.println("With reduced gold:");
        result = VerySimpleLexiconEvaluation.evaluate(automatic,new_gold);
        System.out.println("Automatic => P:"+result.get(0)+"\tR:"+result.get(1)+"\tF:"+result.get(2));
        
        result = VerySimpleLexiconEvaluation.evaluate(learned,new_gold);
        System.out.println("Learned => P:"+result.get(0)+"\tR:"+result.get(1)+"\tF:"+result.get(2));
        


    }
    
}
