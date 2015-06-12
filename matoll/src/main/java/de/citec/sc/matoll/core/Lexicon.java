package de.citec.sc.matoll.core;
import static de.citec.sc.matoll.vocabularies.LEMON.sense;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Lexicon {

	HashMap<String,List<LexicalEntry>> map;
	
	List<LexicalEntry> entries;
	
	Set<Reference> references;
	
	String baseURI = "";
	
	public Lexicon()
	{
		entries = new ArrayList<LexicalEntry>();
		
		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<Reference>();
                
                this.baseURI = "http://dblexipedia.org/lexicon/";
	}
	
	public Lexicon(String baseURI)
	{
		entries = new ArrayList<LexicalEntry>();
		
		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<Reference>();
		
		this.baseURI = baseURI;
	}
	
	public void addEntry(LexicalEntry entry)
	{
                
		
		if(!entries.contains(entry))
		{
			entries.add(entry);
		
			ArrayList<LexicalEntry> list;
		
			if (map.containsKey(entry.getCanonicalForm()))
			{
				list = (ArrayList<LexicalEntry>) map.get(entry.getCanonicalForm());
				list.add(entry);
                                /*
                                TODO:Do I have to add the line map.put(entry..)?
                                */
                                /*
                                TODO: Maybe we can later remove this map, as it increases the cost, as bigger the lexicon gets.
                                */

			}
			else
			{
				list = new ArrayList<LexicalEntry>();
				list.add(entry);
				map.put(entry.getCanonicalForm(), list);
			}
		}
		else
		{   
                        /*
                    This is the entry from the list entries, which is updated
                    */
                        //System.out.println("Update entry");
                        LexicalEntry containedEntry;
	
			containedEntry = getLexicalEntry(entry);
			
			List<String> sentences = new ArrayList<String>();
                        
                        /*
                        Increase the frequency
                        */
                        containedEntry.getProvenance().increaseFrequency(entry.getProvenance().getFrequency());
			sentences.addAll(entry.getSentences());
			sentences.addAll(containedEntry.getSentences());
			
			containedEntry.setSentences(sentences);
                        
                        for(Sense sense : entry.getSenses()) containedEntry.addSense(sense);
			for( SyntacticBehaviour behaviours : entry.getBehaviours())  containedEntry.addSyntacticBehaviour(behaviours);
                        
                        //System.out.println(containedEntry.toString());
                        
                        
                        
			
		}
	
		if (entry.getSenses() != null)
                    for( Reference reference : entry.getReferences()) references.add(reference);
			
	}

        public String getBaseURI() 
        {
               return baseURI;
        }
        
	public List<LexicalEntry> getEntries()
	{
		return entries;
	}
	
	public List<LexicalEntry> getEntriesWithCanonicalForm(String canonicalForm)
	{
		return map.get(canonicalForm);
	}
	
	public int size() {

		return entries.size();
	}
	
	public Set<Reference> getReferences()
	{
		Set<Reference> references = new HashSet<Reference>();
		
		for (LexicalEntry entry: entries)
		{   
                    for (Sense sense : entry.getSenses())
                            references.add(sense.getReference());
		}
		
		return references;
	}
	
	public String getStatistics()
	{
		return "";
	}

	public LexicalEntry createNewEntry(String canonicalForm, Language language) {
		
		LexicalEntry entry = new LexicalEntry(language);
		
		entry.setCanonicalForm(canonicalForm);
		
		addEntry(entry);
		
		entry.setURI(baseURI+"LexicalEntry_"+entries.size()+"_"+canonicalForm);
		
		return entry;
	
	}

	public boolean contains(LexicalEntry entry)
	{
		return entries.contains(entry);
	}
	
	public LexicalEntry getLexicalEntry(LexicalEntry entry)
	{
		if (entries.contains(entry))
		{
			for (LexicalEntry containedEntry: this.getEntriesWithCanonicalForm(entry.getCanonicalForm()))
			{
				if (entry.equals(containedEntry)) return containedEntry;
			}
		}
		
		return null;
	}
	
	public Iterator<LexicalEntry> iterator()
	{
		return entries.iterator();
	}
	
	public String toString()
	{
		String string = "";
		
		for (LexicalEntry entry: entries)
		{
			string+= entry.toString() + "\n\n";
		}
		
		return string;
	}

	public List<LexicalEntry> getEntriesForReference(String input_ref) {
		
		List<LexicalEntry> entries = new ArrayList<LexicalEntry>();
		
		for (LexicalEntry entry: this.entries)
		{
                    for(Reference ref: entry.getReferences()){
                        if (ref.toString().equals(input_ref)) {
                            entries.add(entry);
                            break;
                        }
                    }
			
		}
		
		return entries;
	}
}
