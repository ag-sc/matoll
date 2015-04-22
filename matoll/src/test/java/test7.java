

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;

public class test7 {

	public static void main(String[] args) {
		// This test class checks if the equals method works...
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry1 = new LexicalEntry();
		
		entry1.setCanonicalForm("marry");
		
		Sense sense1 = new Sense();
		
		sense1.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		entry1.addSense(sense1);
		
		entry1.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour1 = new SyntacticBehaviour();
		
		behaviour1.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour1.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense1.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		lexicon.addEntry(entry1);
		
		LexicalEntry entry2 = new LexicalEntry();
		
		entry2.setCanonicalForm("marry");
		
		Sense sense2 = new Sense();
		
		sense2.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));

		entry2.addSense(sense2);
		
		entry2.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour2 = new SyntacticBehaviour();
		
		behaviour2.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour2.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense2.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		entry2.addSyntacticBehaviour(behaviour2);
		
		SyntacticBehaviour behaviour3 = new SyntacticBehaviour();
		
		behaviour3.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePP");
				
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour3.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2","with"));
		
		entry2.addSyntacticBehaviour(behaviour3);
		
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry2)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
                
                
                
                LexicalEntry entry3 = new LexicalEntry();
		
		entry3.setCanonicalForm("marry");
		
		Sense sense3 = new Sense();
		
		sense3.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));

		entry3.addSense(sense3);
		
		entry3.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour4 = new SyntacticBehaviour();
		
		behaviour4.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour4.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour4.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense3.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		entry3.addSyntacticBehaviour(behaviour4);
		
		SyntacticBehaviour behaviour5 = new SyntacticBehaviour();
		
		behaviour5.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#IntransitivePP");
				
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour5.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#prepositionalObject","2","with"));
		
		entry3.addSyntacticBehaviour(behaviour5);
                
                
                // The following should say that entry is already contained:
		
		if (lexicon.contains(entry3)) System.out.println("Entry already included!!!");
		else System.out.println("Entry not included!!!");
		
		
	}

}
