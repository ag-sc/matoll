

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

public class test7 {

	public static void main(String[] args) throws FileNotFoundException {
		// This test class checks if the equals method works...
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry1 = new LexicalEntry(Language.EN);
		
		entry1.setCanonicalForm("marry");
		
		Sense sense1 = new Sense();
		Reference ref1 = new SimpleReference("http://dbpedia.org/ontology/spouse");
		sense1.setReference(ref1);
		
                entry1.setURI("http://localhost:8080/TransitiveFrame_marry");
		
		entry1.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour1 = new SyntacticBehaviour();
		
		behaviour1.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
                Provenance provenance1 = new Provenance();
                provenance1.setFrequency(1);
                entry1.addProvenance(provenance1,sense1);
		
		lexicon.addEntry(entry1);
		
		LexicalEntry entry2 = new LexicalEntry(Language.EN);
		
		entry2.setCanonicalForm("marry");
                //entry2.setURI("dblexipedia.org/lexica/TransitiveFrame_marry");
                entry2.setURI("http://localhost:8080/TransitiveFrame_marry");
		
		Sense sense2 = new Sense();
		Reference ref2 = new SimpleReference("http://dbpedia.org/ontology/spouse");
		sense2.setReference(ref2);

		
		entry2.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour2 = new SyntacticBehaviour();
		
		behaviour2.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		entry2.addSyntacticBehaviour(behaviour2,sense2);
		
		SyntacticBehaviour behaviour3 = new SyntacticBehaviour();
		
		behaviour3.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePP");
				
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2","with"));
		
		entry2.addSyntacticBehaviour(behaviour3,sense2);
                
                Provenance provenance2 = new Provenance();
                provenance2.setFrequency(1);
                entry2.addProvenance(provenance2,sense2);
		
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry2)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
                lexicon.addEntry(entry2);
                
                
                LexicalEntry entry3 = new LexicalEntry(Language.EN);
		
		entry3.setCanonicalForm("marry");
		
		Sense sense3 = new Sense();
		Reference ref3 = new SimpleReference("http://dbpedia.org/ontology/spouse");
		sense3.setReference(ref3);

                //entry3.setURI("dblexipedia.org/lexica/TransitiveFrame_marry");
                entry3.setURI("http://localhost:8080/TransitiveFrame_marry");
		
		entry3.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour4 = new SyntacticBehaviour();
		
		behaviour4.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour4.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour4.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		entry3.addSyntacticBehaviour(behaviour4,sense3);
		
		SyntacticBehaviour behaviour5 = new SyntacticBehaviour();
		
		behaviour5.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePP");
				
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2","with"));
		
		entry3.addSyntacticBehaviour(behaviour5,sense3);
                
                Provenance provenance3 = new Provenance();
                provenance3.setFrequency(1);
                entry3.addProvenance(provenance3,sense3);
                
                // The following should say that entry is already contained:
		
		if (lexicon.contains(entry3)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
                lexicon.addEntry(entry3);
                
                Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization(false);
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("example7.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		
	}

}
