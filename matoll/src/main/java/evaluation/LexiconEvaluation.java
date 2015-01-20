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
import core.SyntacticArgument;
import core.SenseArgument;

public class LexiconEvaluation {

	static Set<String> references;
	
	static boolean onlyGoldReferences = false;
	static boolean filterReferences = false;
	
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
		
		
		System.out.print(lexicon.getStatistics());
		
		System.out.print(gold.getStatistics());
		
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
		
		int lemma_correctness = 0;
		
		int syntactic_correctness = 0;
		
		int mapping_correctness = 0;
		
		int lemma_correctness_verb = 0;
		
		int syntactic_correctness_verb = 0;
		
		int mapping_correctness_verb = 0;
		
		int lemma_correctness_noun = 0;
		
		int syntactic_correctness_noun = 0;
		
		int mapping_correctness_noun = 0;
		
		int lex_entries = 0;
		
		int lex_entries_verb = 0;
		
		int lex_entries_noun = 0;
		
		double lemma_precision;
		
		double lemma_recall;
		
		double lemma_recall_noun;
		
		double lemma_precision_verb;
		
		double lemma_recall_verb;
		
		double lemma_precision_noun;
		
		double mapping_recall;
		
		double syntactic_precision;
		
		double syntactic_recall;
		
		double mapping_precision;
		
		double syntactic_precision_verb;
		
		double syntactic_recall_verb;
		
		double mapping_precision_verb;

		double syntactic_precision_noun;
		
		double syntactic_recall_noun;
		
		double mapping_precision_noun;
		
		double mapping_recall_noun;
		
		double mapping_recall_verb;
		
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
				
				if ((!filterReferences & !onlyGoldReferences) || (filterReferences && references.contains(entry.getReference())) || (onlyGoldReferences && gold.getReferences().contains(entry.getReference())))
				{
					lex_entries++;

					if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) lex_entries_verb++;
					if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) lex_entries_noun++;
					
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
							lemma_correctness ++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) lemma_correctness_verb++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) lemma_correctness_noun++;
						}
					
						if (foundSyntax)
						{
							syntactic_correctness ++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb"))) syntactic_correctness_verb++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) syntactic_correctness_noun++;
						}
						else
						{
							// System.out.print("Syntax wrong for: "+entry+"\n");
						}
						
						if (foundMapping)
						{
							mapping_correctness ++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  mapping_correctness_verb++;
							if (entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) mapping_correctness_noun++;
						}
					}
				}
			}						
		}
		
		if (lex_entries > 0)
		{
			lemma_precision = (double) lemma_correctness / lex_entries;
			syntactic_precision = (double) syntactic_correctness / lex_entries;
			mapping_precision = (double) mapping_correctness / lex_entries;
		}
		else
		{
			lemma_precision = 1;
			syntactic_precision = 1;
			mapping_precision = 1;
		}
		
		if (lex_entries_verb > 0)
		{
			lemma_precision_verb = (double) lemma_correctness_verb / lex_entries_verb;
			syntactic_precision_verb = (double) syntactic_correctness_verb / lex_entries_verb;
			mapping_precision_verb = (double) mapping_correctness_verb / lex_entries_verb;
		}
		else
		{
			lemma_precision_verb = 1;
			syntactic_precision_verb = 1;
			mapping_precision_verb = 1;
		}
		
		
		if (lex_entries_noun > 0)
		{
			lemma_precision_noun = (double) lemma_correctness_noun / lex_entries_noun;
			syntactic_precision_noun = (double) syntactic_correctness_noun / lex_entries_noun;
			mapping_precision_noun = (double) mapping_correctness_noun / lex_entries_noun;
		}
		else
		{
			lemma_precision_noun = 1;
			syntactic_precision_noun = 1;
			mapping_precision_noun = 1;
		}
		
		System.out.print("Precision at lemma level: "+ lemma_precision+"\n");
		System.out.print("Precision at syntactic level: "+ syntactic_precision+"\n");
		System.out.print("Precision at mapping level: "+ mapping_precision+"\n");
		
		System.out.print("Precision at lemma level (verb): "+ lemma_precision_verb+"\n");
		System.out.print("Precision at syntactic level (verb): "+ syntactic_precision_verb+"\n");
		System.out.print("Precision at mapping level (verb): "+ mapping_precision_verb+"\n");
		
		System.out.print("Precision at lemma level (noun): "+ lemma_precision_noun+"\n");
		System.out.print("Precision at syntactic level (noun): "+ syntactic_precision_noun+"\n");
		System.out.print("Precision at mapping level (noun): "+ mapping_precision_noun+"\n");
		
		System.out.print("Computing Recall...\n");
		

		lemma_correctness = 0;
		syntactic_correctness = 0;
		mapping_correctness = 0;
		lex_entries = 0;
		
		lemma_correctness_verb = 0;
		syntactic_correctness_verb = 0;
		mapping_correctness_verb = 0;
		lex_entries_verb = 0;
		
		lemma_correctness_noun = 0;
		syntactic_correctness_noun = 0;
		mapping_correctness_noun = 0;
		lex_entries_noun = 0;

		
		for (LexicalEntry gold_entry: gold.getEntries())
		{
			foundLemma = false;
			foundSyntax = false;
			foundMapping = false;
			
			if (gold_entry.getReference() != null) 
			{
				if (!filterReferences || (filterReferences && references.contains(gold_entry.getReference())))
				{
				
					lex_entries++;
					
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
							lemma_correctness ++;
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  lemma_correctness_verb++;
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) lemma_correctness_noun++;
						}
						}
					
						if (foundSyntax)
						{
							syntactic_correctness ++;
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  syntactic_correctness_verb++;
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) syntactic_correctness_noun++;
						}
						else
						{
							// System.out.print("Syntax wrong for: "+gold_entry+"\n");
						}
						if (foundMapping)
						{
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#verb")))  mapping_correctness_verb++;
							if (gold_entry.getPOS().equals(("http://www.lexinfo.net/ontology/2.0/lexinfo#commonNoun"))) mapping_correctness_noun++;
							mapping_correctness ++;
						}
					}						
				}
			}
		
		
		if (lex_entries > 0)
		{
			lemma_recall = (double) lemma_correctness / lex_entries;
			syntactic_recall = (double) syntactic_correctness / lex_entries;
			mapping_recall = (double) mapping_correctness / lex_entries;
		}
		else
		{
			lemma_recall = 1;
			syntactic_recall = 1;
			mapping_recall = 1;
		}
		
		if (lex_entries_verb > 0)
		{
			lemma_recall_verb = (double) lemma_correctness_verb / lex_entries_verb;
			syntactic_recall_verb = (double) syntactic_correctness_verb / lex_entries_verb;
			mapping_recall_verb = (double) mapping_correctness_verb / lex_entries_verb;
		}
		else
		{
			lemma_recall_verb = 1;
			syntactic_recall_verb = 1;
			mapping_recall_verb = 1;
		}
		
		if (lex_entries_noun > 0)
		{
		
			lemma_recall_noun = (double) lemma_correctness_noun / lex_entries_noun;
			syntactic_recall_noun = (double) syntactic_correctness_noun / lex_entries_noun;
			mapping_recall_noun = (double) mapping_correctness_noun / lex_entries_noun;
		}
		else
		{
			lemma_recall_noun = 1;
			syntactic_recall_noun = 1;
			mapping_recall_noun = 1;
		}
		
		System.out.print("Recall at lemma level: "+ lemma_recall+"\n");
		System.out.print("Recall at syntactic level: "+ syntactic_recall+"\n");
		System.out.print("Recall at mapping level: "+ mapping_recall+"\n");
		
		System.out.print("Recall at lemma level (verb): "+ lemma_recall_verb+"\n");
		System.out.print("Recall at syntactic level (verb): "+ syntactic_recall_verb+"\n");
		System.out.print("Recall at mapping level (verb): "+ mapping_recall_verb+"\n");
		
		System.out.print("Recall at lemma level (noun): "+ lemma_recall_verb+"\n");
		System.out.print("Recall at syntactic level (noun): "+ syntactic_recall_verb+"\n");
		System.out.print("Recall at mapping level (noun): "+ mapping_recall_verb+"\n");
		
		double lemma_fmeasure = (2 * lemma_precision * lemma_recall) / (lemma_precision + lemma_recall);
	
		System.out.print("F-Measure at lemma level: "+ lemma_fmeasure+"\n");
		
		double syntactic_fmeasure = (2 * syntactic_precision * syntactic_recall) / (syntactic_precision + syntactic_recall);
		
		System.out.print("F-Measure at syntactic level: "+ syntactic_fmeasure+"\n");
		
		double mapping_fmeasure = (2 * mapping_precision * mapping_recall) / (mapping_precision + mapping_recall);
		
		System.out.print("F-Measure at mapping level: "+ mapping_fmeasure+"\n");
		
		double lemma_fmeasure_verb = (2 * lemma_precision_verb * lemma_recall_verb) / (lemma_precision_verb + lemma_recall_verb);
		
		System.out.print("F-Measure at lemma level (verb): "+ lemma_fmeasure_verb+"\n");
		
		double syntactic_fmeasure_verb = (2 * syntactic_precision_verb * syntactic_recall_verb) / (syntactic_precision_verb + syntactic_recall_verb);
		
		System.out.print("F-Measure at syntactic level (verb): "+ syntactic_fmeasure_verb+"\n");
		
		double mapping_fmeasure_verb = (2 * mapping_precision_verb * mapping_recall_verb) / (mapping_precision_verb + mapping_recall_verb);
		
		System.out.print("F-Measure at mapping level (verb): "+ mapping_fmeasure_verb+"\n");
		
		double lemma_fmeasure_noun = (2 * lemma_precision_noun * lemma_recall_noun) / (lemma_precision_noun + lemma_recall_noun);
		
		System.out.print("F-Measure at lemma level (noun): "+ lemma_fmeasure_noun+"\n");
		
		double syntactic_fmeasure_noun = (2 * syntactic_precision_noun * syntactic_recall_noun) / (syntactic_precision_noun + syntactic_recall_noun);
		
		System.out.print("F-Measure at syntactic level (noun): "+ syntactic_fmeasure_noun+"\n");
		
		double mapping_fmeasure_noun = (2 * mapping_precision_noun * mapping_recall_noun) / (mapping_precision_noun + mapping_recall_noun);
		
		System.out.print("F-Measure at mapping level (noun): "+ mapping_fmeasure_noun+"\n");
		
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

	
}
