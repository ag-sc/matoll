import de.citec.sc.matoll.core.Language;
import java.io.FileNotFoundException;
import java.util.List;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.io.LexiconLoader;

public class test2 {

	public static void main(String[] args) throws FileNotFoundException {
		LexiconLoader loader = new LexiconLoader();
		
		List<LexicalEntry> marry_entries;
		
		Lexicon lexicon = new Lexicon();
		
		String gold_standard_lexicon = "../lexica/dbpedia_en.rdf";
		
		System.out.print("Loading lexicon from: "+gold_standard_lexicon+"\n");
		
		Lexicon gold = loader.loadFromFile(gold_standard_lexicon);
		
		System.out.print("Lexicon loaded\n");
		
		marry_entries = gold.getEntriesWithCanonicalForm("marry@en");
		
		System.out.print("There are "+marry_entries.size()+" entries for marry\n");
		
		for (LexicalEntry entry: marry_entries)
		{
			if (!lexicon.contains(entry))
			{
				lexicon.addEntry(entry);
				System.out.print("Adding entry: "+entry+"\n");
				System.out.print("Hashcode: "+entry.hashCode()+"\n");
			}
		}
		
		System.out.print("Lexicon has "+lexicon.size()+" entries\n");
		
		LexicalEntry entry = new LexicalEntry(Language.EN);
		
		entry.setCanonicalForm("marry@en");
		
		Sense sense = new Sense();
		
		sense.setReference(new SimpleReference("http://dbpedia.org/ontology/spouse"));
		
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","2",null));
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","1",null));
		
		entry.addSyntacticBehaviour(behaviour,sense);
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#subjOfProp","1"));
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		System.out.print("Hashcode: "+entry.hashCode());
		
		
		if (lexicon.contains(entry)) 
		{
			System.out.print("Entry is already contained!\n");
		}
		else
		{
			System.out.print("Entry is not contained!\n");
		}
		
	}
	

}
