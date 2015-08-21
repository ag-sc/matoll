


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

public class test9 {

	public static void main(String[] args) throws FileNotFoundException {
		// This test class checks if the equals method works...
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry1 = new LexicalEntry(Language.EN);
		
		entry1.setCanonicalForm("marry");
		
		Sense sense1 = new Sense();
		Reference ref1 = new SimpleReference("http://dbpedia.org/ontology/spouse");
		sense1.setReference(ref1);
		
		//entry1.addSense(sense1);
                entry1.setURI("http://localhost:8080/TransitiveFrame_marry");
		
		entry1.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour1 = new SyntacticBehaviour();
		
		behaviour1.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","subject",null));
		
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));
                Provenance provenance1 = new Provenance();
                provenance1.setFrequency(1);
                entry1.addProvenance(provenance1,sense1);
                entry1.addSyntacticBehaviour(behaviour1,sense1);
                
                
                Sense sense2 = new Sense();
		Reference ref2 = new SimpleReference("http://dbpedia.org/ontology/deathCause");
		sense2.setReference(ref2);
		
		entry1.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour2 = new SyntacticBehaviour();
		
		behaviour2.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
		
                
                SyntacticBehaviour behaviour5 = new SyntacticBehaviour();
		
		behaviour5.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#SuperFrame");
				
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","subject",null));
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","object",null));
		
                
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","subject"));
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","object"));	
		entry1.addSyntacticBehaviour(behaviour2,sense2);
                entry1.addSyntacticBehaviour(behaviour5,sense2);
                
                Provenance provenance2 = new Provenance();
                provenance2.setFrequency(20);
                entry1.addProvenance(provenance2,sense2);
                
		
		lexicon.addEntry(entry1);
                
                
                
                LexicalEntry entry2 = new LexicalEntry(Language.DE);
		
		entry2.setCanonicalForm("marry");
		
		Sense sense3 = new Sense();
		Reference ref3 = new SimpleReference("http://dbpedia.org/ontology/spouse");
		sense3.setReference(ref3);
		
		//entry2.addSense(sense3);
                entry2.setURI("http://localhost:8080/__TransitiveFrame_marry");
		
		entry2.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour3 = new SyntacticBehaviour();
		
		behaviour3.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","object",null));
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","subject",null));
		
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","object"));
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","subject"));
                Provenance provenance3 = new Provenance();
                provenance3.setFrequency(2);
                entry2.addProvenance(provenance3,sense3);
                entry2.addSyntacticBehaviour(behaviour3,sense3);
		
                lexicon.addEntry(entry2);
                System.out.println(lexicon.getEntries(Language.EN).size());
                
                System.out.println("#entries:"+lexicon.size());
                
                Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("example9.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		
	}

}
