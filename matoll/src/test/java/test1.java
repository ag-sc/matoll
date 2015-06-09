

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;

public class test1 {

	public static void main(String[] args) {
		// This test class checks if the equals method works...
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		LexicalEntry entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("marry");
		
		Sense sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		lexicon.add(entry, new FeatureVector());
		
		entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("marry");
		
		sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		/*
                TODO: addSense
                */
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
		
		// Let's changed the lemma
		
		entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("casar");
		
		sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.println("Entry not included!!!");
		
		// Let's change the frame type
		
		entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("marry");
		
		sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.println("Entry not included!!!");
		
		// Let's change the mappings
		
		entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("marry");
		
		// entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
		
		// entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		// entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
		
		
	}

}
