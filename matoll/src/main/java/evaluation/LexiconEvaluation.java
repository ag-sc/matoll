package evaluation;
import io.LexiconLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import core.LexicalEntry;
import core.Lexicon;

public class LexiconEvaluation {

	static Set<String> references;
	
	static boolean onlyGoldReferences = false;
	static boolean filterReferences = false;
	
	static HashMap<String,Double> fmeasures;
	static HashMap<String,Double> correct;
	static HashMap<String,Double> precision;
	static HashMap<String,Double> recall;
	static HashMap<String,Double> entries;
	static HashMap<String,Double> gold_entries;
	static HashMap<String,Double> gold_correct;
	
	public static void main(String[] args) throws IOException {
		
		
		if (args.length < 2)
		{
			System.out.print("Usage: LexiconEvaluation <Lexicon.rdf> <GoldLexicon.rdf> (--onlyGoldReferences|<FileWithReferences>)?\n");
			return;
		}
		
		String referenceFile = null;
		
		if (args.length >2)
		{
			if (args[2].equals("--onlyGoldReferences"))
			{
				onlyGoldReferences = true;
			}
			else
			{
				referenceFile = args[2];
				filterReferences = true;
				references = readReferenceFile(referenceFile);
			}
		}
		
		
		
		String lexiconFile = args[0];
		String goldFile = args[1];
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon lexicon = loader.loadFromFile(lexiconFile);
		Lexicon gold = loader.loadFromFile(goldFile);
		
		evaluate(lexicon,gold);
		
	}

	private static Set<String> readReferenceFile(String referenceFile) throws IOException {
		
		Set<String> references = new HashSet<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(referenceFile));
        String line;
        while((line = br.readLine()) != null) {
             references.add(line);
        }
        return references;
	}

	public static void evaluate(Lexicon lexicon, Lexicon gold) {
		
			
		fmeasures = new HashMap<String,Double>();
		precision = new HashMap<String,Double>();
		recall = new HashMap<String,Double>();
		correct = new HashMap<String,Double>();
		gold_correct = new HashMap<String,Double>();
		entries = new HashMap<String,Double>();
		gold_entries = new HashMap<String,Double>();
		
		Boolean foundLemma;
		
		Boolean foundSyntax;
		
		Boolean foundMapping;
		
		System.out.print("Computing Precision...\n");
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
			foundLemma = false;
			foundSyntax = false;
			foundMapping = false;
			
			if (entry.getReference() != null) 
			{
				System.out.print("Reference: "+entry.getReference()+"\n");
				if ((!filterReferences & !onlyGoldReferences) || (filterReferences && references.contains(entry.getReference())) || (onlyGoldReferences && gold.getReferences().contains(entry.getReference())))
				{

					update(entries,"lemma",1.0);
					update(entries,"syntactic",1.0);
					update(entries,"mapping",1.0);


					if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) 
					{
						update(entries,"lemma_verb",1.0);
						update(entries,"syntactic_verb",1.0);
						update(entries,"mapping_verb",1.0);
					}
					
					if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun")))
					{
						update(entries,"lemma_noun",1.0);
						update(entries,"syntactic_noun",1.0);
						update(entries,"mapping_noun",1.0);
					}
					
					// System.out.print("Checking entry "+lex_entries+"("+entry.getCanonicalForm()+")\nCandidates:");
					
					// System.out.print(gold.getEntriesWithCanonicalForm(entry.getCanonicalForm())+"\n");
					
					if (gold.getEntriesWithCanonicalForm(entry.getCanonicalForm()) != null)
					{
						for (LexicalEntry gold_entry: gold.getEntriesWithCanonicalForm(entry.getCanonicalForm()))
						{
							if (checkLemmaReference(entry,gold_entry)) 
							{
								foundLemma = true;
								
								if (checkSyntax(entry,gold_entry))
								{
									foundSyntax = true;
									if (checkMappings(entry,gold_entry))
									{
										foundMapping = true;
									}
								}
							}
							else
							{
								// System.out.print("Lemma and reference not found!\n");
							}
								
						}
						if (foundLemma) 
						{
							update(correct,"lemma",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) update(correct,"lemma_verb",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(correct,"lemma_noun",1.0);
						}
					
						if (foundSyntax)
						{
							update(correct,"syntactic",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) update(correct,"syntactic_verb",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(correct,"syntactic_noun",1.0);
						}
						else
						{
							// System.out.print("Syntax wrong for: "+entry+"\n");
						}
						
						if (foundMapping)
						{
							update(correct,"mapping",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  update(correct,"mapping_verb",1.0);
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(correct,"mapping_noun",1.0);
						}
					}
				}
			}						
		}
		
		for (String entry: entries.keySet())
		{
			System.out.print(entry+": "+entries.get(entry)+"\n");
		}
		
		for (String key: correct.keySet())
		{
			if (entries.get(key) != null)
			
				update(precision, key, (double) correct.get(key).doubleValue() / entries.get(key) );
			else 
				update(precision, key, 1.0);
		}
		
		System.out.print("Computing Recall...\n");
		
		for (LexicalEntry gold_entry: gold.getEntries())
		{
			foundLemma = false;
			foundSyntax = false;
			foundMapping = false;
			
			if (gold_entry.getReference() != null) 
			{
				if (!filterReferences || (filterReferences && references.contains(gold_entry.getReference())))
				{
				
					update(gold_entries,"lemma",1.0);
					update(gold_entries,"syntactic",1.0);
					update(gold_entries,"mapping",1.0);
					
					// System.out.print("Checking entry "+lex_entries+"("+gold_entry.getCanonicalForm()+")");
					
					if (lexicon.getEntriesWithCanonicalForm(gold_entry.getCanonicalForm()) != null)
					{
						for (LexicalEntry entry: lexicon.getEntriesWithCanonicalForm(gold_entry.getCanonicalForm()))
						{
							if (checkLemmaReference(gold_entry,entry)) 
							{
								foundLemma = true;
								
								if (checkSyntax(gold_entry,entry))
								{
									foundSyntax = true;
									if (checkMappings(gold_entry,entry))
									{
										foundMapping = true;
									}
								}
							}
	
						}
						if (foundLemma) 
						{
							update(gold_correct,"lemma",1.0);
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  update(gold_correct,"lemma_verb",1.0);
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(gold_correct,"lemma_noun",1.0);
						}
						}
					
						if (foundSyntax)
						{
							update(gold_correct,"syntactic",1.0);
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  update(gold_correct,"syntactic_verb",1.0);
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(gold_correct,"syntactic_noun",1.0);
						}
						else
						{
							// System.out.print("Syntax wrong for: "+gold_entry+"\n");
						}
						if (foundMapping)
						{
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  update(gold_correct,"mapping_verb",1.0);
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) update(gold_correct,"mapping_verb",1.0);
							update(gold_correct,"mapping",1.0);
						}
					}						
				}
			}
		
		for (String key: gold_correct.keySet())
		{
			if (gold_entries.get(key) != null)
			
				update(recall, key, (double) gold_correct.get(key).doubleValue() / gold_entries.get(key) );
			
			else 
				update(recall, key,1.0);
				
		}
		
		for (String key: correct.keySet())
		{
			if (precision.containsKey(key) && recall.containsKey(key))
			
			update(fmeasures,key, 2 * (precision.get(key) * recall.get(key)) / (precision.get(key) + recall.get(key)));
			
			else update(fmeasures,key,0.0);
		}
		
	}


	private static void update(HashMap<String, Double> map, String key, double value) {
		
		if (map.containsKey(key))
		{
			map.put(key, map.get(key) + value);
		}
		else
		{
			map.put(key, value);
		}
	}

	private static boolean checkMappings(LexicalEntry entry, LexicalEntry gold_entry) {
		
		HashMap<String,String> entry_map = entry.getArgumentMap();
		HashMap<String,String> gold_entry_map = gold_entry.getArgumentMap();
				
		for (String synArg: entry_map.keySet())
		{
			
			if (!gold_entry_map.containsKey(synArg)) 
			{
				return false;
			}
			else
			{
				if (!entry_map.get(synArg).equals(gold_entry_map.get(synArg))) return false;
			}
		}
		
		return true;
		
	}

	private static boolean checkSyntax(LexicalEntry entry, LexicalEntry gold_entry) {
		
		return entry.getBehaviour().equals(gold_entry.getBehaviour());
		
	
	}

	private static boolean checkLemmaReference(LexicalEntry entry, LexicalEntry gold_entry) {
	
		boolean check = true;
	
		// check if one of the references is ok
		
//		if (entry.getReference() != null && gold_entry.getReference() != null)
//			if (!entry.getReference().equals(gold_entry.getReference())) check=false;
		
		if (entry.getPOS() != null && gold_entry.getPOS() != null)
			if (!entry.getPOS().equals(gold_entry.getPOS())) check=false;
		
		return check && entry.getCanonicalForm().equals(gold_entry.getCanonicalForm());
	}

	public void addReference(String string) {
		
		if (references == null)
		{
			references = new HashSet<String>();
		}
		
		references.add(string);
		
		
	}

	public void setReferences(Set<String> references2) {
		this.references = references;
		
	}

	public double getFMeasure(String key) {
		if (fmeasures.containsKey(key)) return fmeasures.get(key);
		else return 0.0;
	}
	
	public double getPrecision(String key) {
		if (precision.containsKey(key)) return precision.get(key);
		else return 1.0;
	}
	
	public double getRecall(String key) {
		if (recall.containsKey(key)) return recall.get(key);
		else return 1.0;
	}

	
}
