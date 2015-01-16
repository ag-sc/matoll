package core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Lexicon {

	HashMap<String,List<LexicalEntry>> map;
	
	List<LexicalEntry> entries;
	
	Set<String> references;
	
	String baseURI = "";
	
	public Lexicon()
	{
		entries = new ArrayList<LexicalEntry>();
		
		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<String>();
	}
	
	public Lexicon(String baseURI)
	{
		entries = new ArrayList<LexicalEntry>();
		
		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<String>();
		
		this.baseURI = baseURI;
	}
	
	public void addEntry(LexicalEntry entry)
	{
		
		LexicalEntry containedEntry;
		
		if(!entries.contains(entry))
		{
			entries.add(entry);
		
			ArrayList<LexicalEntry> list;
		
			if (map.containsKey(entry.getCanonicalForm()))
			{
				list = (ArrayList<LexicalEntry>) map.get(entry.getCanonicalForm());
				list.add(entry);
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
			containedEntry = getLexicalEntry(entry);
			for (String sentence: entry.getSentences())
			{
				containedEntry.addSentence(sentence);
			}
		}
	
		if (entry.getSense() != null)
		
			references.add(entry.getSense().getReference());
			
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
	
	public Set<String> getReferences()
	{
		Set<String> references = new HashSet<String>();
		
		for (LexicalEntry entry: entries)
		{
			references.add(entry.getReference());
		}
		
		return references;
	}
	
	public String getStatistics()
	{
		return "";
	}

	public LexicalEntry createNewEntry(String canonicalForm) {
		
		LexicalEntry entry = new LexicalEntry();
		
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
		if (entries.contains(entry)) return entry;
		else return null;
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
}
