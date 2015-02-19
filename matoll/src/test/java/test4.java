import io.LexiconLoader;

import java.io.FileNotFoundException;
import java.util.List;

import core.FeatureVector;
import core.LexicalEntry;
import core.Lexicon;
import core.LexiconWithFeatures;
import core.Sense;
import core.SenseArgument;
import core.SimpleReference;
import core.SyntacticArgument;
import core.SyntacticBehaviour;

public class test4 {

	public static void main(String[] args) throws FileNotFoundException {
		LexiconLoader loader = new LexiconLoader();
		
		List<LexicalEntry> marry_entries;
		
		LexiconWithFeatures lexicon = new LexiconWithFeatures();
		
		String gold_standard_lexicon = "/Users/cimiano/Projects/matoll/dbpedia_en.rdf";
		
		System.out.print("Loading lexicon from: "+gold_standard_lexicon+"\n");
		
		Lexicon gold = loader.loadFromFile(gold_standard_lexicon);
		
		System.out.print("Lexicon loaded\n");
		
		marry_entries = gold.getEntriesWithCanonicalForm("marry@en");
		
		System.out.print("There are "+marry_entries.size()+" entries for marry\n");
		
		FeatureVector vector = new FeatureVector();
		
		vector.add("freq", 1.0);
		vector.add("id1",5.0);
		
		for (LexicalEntry entry: marry_entries)
		{
			if (!lexicon.contains(entry))
			{
				entry.addSentence("This is the first sentence");
				lexicon.add(entry, vector);
				System.out.print("Adding entry: "+entry+"\n");
				System.out.print("Hashcode: "+entry.hashCode()+"\n");
			}
		}
		
		System.out.print("Lexicon has "+lexicon.size()+" entries\n");
		
		LexicalEntry entry = new LexicalEntry();
		
		entry.setCanonicalForm("marry@en");
		
		Sense sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		entry.setSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.addSentence("This is the second sentence");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.setSyntacticBehaviour(behaviour);
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		System.out.print("Hashcode: "+entry.hashCode());
		
		System.out.print(entry);
		
		lexicon.add(entry,vector);
		
		for (LexicalEntry myEntry: lexicon.getEntries())
		{
			System.out.print(myEntry);
			System.out.print("Vector: "+lexicon.getFeatureVector(myEntry));
		}
		
	}
	

}
