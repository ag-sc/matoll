import io.LexiconLoader;
import io.LexiconSerialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import core.FeatureVector;
import core.LexicalEntry;
import core.Lexicon;
import core.LexiconWithFeatures;
import core.Provenance;
import core.Restriction;
import core.Sense;
import core.SenseArgument;
import core.SyntacticArgument;
import core.SyntacticBehaviour;
import evaluation.LexiconEvaluation;

public class test6 {

	public static void main(String[] args) throws FileNotFoundException {
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry;
		
		entry = lexicon.createNewEntry("female");
		
		entry.setCanonicalForm("female");
		
		Sense sense = new Sense();
		
		sense.setReference(new Restriction("http://dbpedia.org/resource/Female","http://dbpedia.org/ontology/gender"));
		
		entry.setSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicativeFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeSubject","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.setSyntacticBehaviour(behaviour);
		
		Provenance provenance = new Provenance();
		
		provenance.setAgent("AdjectiveExtractor");
		provenance.setConfidence(0.8);
		
		entry.setProvenance(provenance);
		
		entry = lexicon.createNewEntry("female");
		
		entry.setSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");

		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectiveAttributiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#attributiveArg","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.setSyntacticBehaviour(behaviour);
		
		
		
		entry.setProvenance(provenance);
		
		lexicon.addEntry(entry);
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("female.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		
	}
	

}
