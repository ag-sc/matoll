
import de.citec.sc.matoll.classifiers.WEKAclassifier;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.utils.Learning;




/**
 *
 * @author swalter
 */
public class TestLearning {
    
     public static void main(String[] args) throws Exception{
        LexiconLoader loader = new LexiconLoader();
	Lexicon gold = loader.loadFromFile("/Users/swalter/Git/matoll/lexica/dbpedia_en.rdf");
        Lexicon automatic = loader.loadFromFile("spouse.ttl");
        
        WEKAclassifier classifier = new WEKAclassifier(Language.EN);
        Learning.doTraining(automatic, gold, null, Language.EN, classifier,2);        
        Learning.doPrediction(automatic, gold, classifier, null, Language.EN);

    }
    
}
