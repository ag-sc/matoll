import java.io.FileNotFoundException;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.evaluation.LexiconEvaluationSimple;
import de.citec.sc.matoll.io.LexiconLoader;

public class test5 {

	public static void main(String[] args) throws FileNotFoundException {
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon gold = loader.loadFromFile("../lexica/foaf.ttl");
		
		Lexicon lexicon1 = loader.loadFromFile("../lexica/foaf_1.ttl");
		
		Lexicon lexicon2 = loader.loadFromFile("../lexica/foaf_2.ttl");
		
		Lexicon lexicon3 = loader.loadFromFile("../lexica/foaf_3.ttl");
		
		Lexicon lexicon4 = loader.loadFromFile("../lexica/foaf_4.ttl");
		
		Lexicon lexicon5 = loader.loadFromFile("../lexica/foaf_5.ttl");
		
		Lexicon lexicon6 = loader.loadFromFile("../lexica/foaf_6.ttl");
                
                Lexicon lexicon7 = loader.loadFromFile("../lexica/foaf_7.ttl");
                
                Lexicon lexicon8 = loader.loadFromFile("../lexica/foaf_8.ttl");
	
		//LexiconEvaluation eval = new LexiconEvaluation();
                LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
	
		System.out.print("Evaluating foaf.ttl against foaf.ttl All measures should be 1.0...\n");
		
		eval.evaluate(gold, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\n Precision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
			
		System.out.print("Evaluating foaf_1.ttl against foaf.ttl. Recall should be 0.75 \n");
		
		System.out.print("foaf_1.ttl has the following: "+lexicon1.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon1.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon1, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
		System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_2.ttl against foaf.ttl. Precision should be 0.8 \n");
		
		System.out.print("foaf_2.ttl has the following: "+lexicon2.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon2.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon2, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
		System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_3.ttl against foaf.ttl. Precision should be 0.75 \n");
		
		System.out.print("foaf_3.ttl has the following: "+lexicon3.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon3.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon3, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
		System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_4.ttl against foaf.ttl. Syntactic precision and recall should be 0.75 \n");
		
		System.out.print("foaf_4.ttl has the following: "+lexicon4.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon4.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon4, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
		System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_5.ttl against foaf.ttl. Mapping precision and recall should be 0.75 \n");
		
		System.out.print("foaf_5.ttl has the following: "+lexicon5.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon5.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon5, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
		System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_6.ttl against foaf.ttl. Syntactic precision and recall should be 0.75 \n");
		
		System.out.print("foaf_6.ttl has the following: "+lexicon6.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon6.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon6, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
                
                System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_7.ttl against foaf.ttl. Everything should be 1.0 \n");
		
		System.out.print("foaf_7.ttl has the following: "+lexicon7.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon7.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon7, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
                
                System.out.print("\n======================\n\n");
		
		System.out.print("Evaluating foaf_8.ttl against foaf.ttl. Everything shoul be 0.0 \n");
		
		System.out.print("foaf_8.ttl has the following: "+lexicon8.size()+" entries...\n");
		
		for (LexicalEntry entry: lexicon8.getEntries())
		{
			System.out.print(entry.getCanonicalForm()+"("+entry.getPOS()+")\n");
		}
		
		eval.evaluate(lexicon8, gold);
		
		System.out.print("Precision (lemma): "+eval.getPrecision("lemma")+"\nRecall(lemma): "+eval.getRecall("lemma")+"\nF-Measure(lemma): "+eval.getFMeasure("lemma")+"\nPrecision(syntactic): "+eval.getPrecision("syntactic")+"\nRecall(syntactic): "+eval.getRecall("syntactic")+"\nF-Measure(syntactic): "+eval.getFMeasure("syntactic")+"\nPrecision(mapping): "+eval.getPrecision("mapping")+"\nRecall(mapping): "+eval.getRecall("mapping")+"\nF-Measure(mapping): "+eval.getFMeasure("mapping")+"\n");
		
	}
	

}
