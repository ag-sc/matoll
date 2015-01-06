package test;

import core.FeatureVector;
import core.LexicalEntry;
import core.LexiconWithFeatures;
import core.SenseArgument;
import core.SyntacticArgument;

public class test1 {

	public static void main(String[] args) {
		// This test class checks if the equals method works...
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		LexicalEntry entry = new LexicalEntry();
		
		entry.setCanonicalForm("marry");
		
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		lexicon.add(entry, new FeatureVector());
		
		entry = new LexicalEntry();
		
		entry.setCanonicalForm("marry");
		
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.print("Entry not included!!!");
		
		// Let's changed the lemma
		
		entry = new LexicalEntry();
		
		entry.setCanonicalForm("casar");
		
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.print("Entry not included!!!");
		
		// Let's change the frame type
		
		entry = new LexicalEntry();
		
		entry.setCanonicalForm("marry");
		
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.print("Entry not included!!!");
		
		// Let's change the mappings
		
		entry = new LexicalEntry();
		
		entry.setCanonicalForm("marry");
		
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));	
		
		// The following should say that entry is already contained:
		
		if (lexicon.contains(entry)) System.out.print("Entry already included!!!");
		else System.out.print("Entry not included!!!");
		
		
	}

}
