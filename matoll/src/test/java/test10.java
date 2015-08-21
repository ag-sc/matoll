

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

public class test10 {

	public static void main(String[] args) throws FileNotFoundException {
		LexiconLoader loader = new LexiconLoader();
						
		String gold_standard_lexicon = "example9.ttl";
		
		System.out.print("Loading lexicon from: "+gold_standard_lexicon+"\n");
		
		Lexicon lexicon = loader.loadFromFile(gold_standard_lexicon);
                
		
//                Lexicon gold = loader.loadFromFile("../lexica/dbpedia_en.rdf");
                
		
//		System.out.print("Lexicon loaded\n");
//                System.out.println(gold.size());
                
                Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("example10.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
                
//                
//                LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
//                eval.evaluate(lexicon,gold);
//                System.out.println(eval.getRecall("lemma"));
	}

}
