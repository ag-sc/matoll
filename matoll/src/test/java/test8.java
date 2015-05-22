import java.io.FileNotFoundException;
import java.util.List;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.evaluation.LexiconEvaluation;
import de.citec.sc.matoll.evaluation.LexiconEvaluationSimple;
import de.citec.sc.matoll.io.LexiconLoader;

public class test8 {

	public static void main(String[] args) throws FileNotFoundException {
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon gold = loader.loadFromFile("../lexica/foaf.ttl");
		
                
                Lexicon lexicon7 = loader.loadFromFile("../lexica/foaf_7.ttl");
                
                Lexicon lexicon8 = loader.loadFromFile("../lexica/foaf_8.ttl");
	
		LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
	
		System.out.print("T=P=1.0");
		
		eval.evaluate(gold, gold);
		
                
		eval.evaluate(lexicon7, gold);
		

		

		eval.evaluate(lexicon8, gold);
		

		

	}
	

}
