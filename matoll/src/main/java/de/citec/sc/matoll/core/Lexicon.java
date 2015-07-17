package de.citec.sc.matoll.core;
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
		                        
//                        
//			List<String> sentences = new ArrayList<String>();
//                        sentences.addAll(entry.getSentences());
//                        sentences.addAll(containedEntry.getSentences());
//                        
//			
//			
//			containedEntry.setSentences(sentences);
                        
                        HashMap<Sense, HashSet<SyntacticBehaviour>> senseBehaviours = entry.getSenseBehaviours();
                        senseBehaviours.keySet().stream().map((sense) -> {
                            HashSet<SyntacticBehaviour> behaviours = senseBehaviours.get(sense);
                            containedEntry.addAllSyntacticBehaviour(behaviours, sense);
                        return sense;
                        }).forEach((sense) -> {
                            /*
                            Update Provenance
                            */
                            Provenance provenance = entry.getProvenance(sense);
                            containedEntry.addProvenance(provenance, sense);
                        });

                        
                        
                        
			
		}
	
		if (entry.getSenseBehaviours() != null)
                    entry.getReferences().stream().forEach((reference) -> {
                        references.add(reference);
                });
			
	}

        public String getBaseURI() 
        {
               return baseURI;
        }
        
        public void setBaseURI(String baseURI){
            this.baseURI = baseURI;
        }
        
	public List<LexicalEntry> getEntries()
	{
		return entries;
	}
        
        public List<LexicalEntry> getEntries(Language language)
	{
		List<LexicalEntry> entries_tmp = new ArrayList<LexicalEntry>();
                for(LexicalEntry e : getEntries()){
                    try{
                        if(e.getLanguage().equals(language)) entries_tmp.add(e);
                    }
                    catch(Exception exp){
                        System.err.println("Entry: "+e.toString()+"does NOT contain any language");
                    }
                }
                return entries_tmp;
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
                    for (Sense sense : entry.getSenseBehaviours().keySet())
                            references.add(sense.getReference());
		}
		
		return references;
	}
	
	public String getStatistics()
	{
		return "";
	}

	private LexicalEntry createNewEntry(String canonicalForm, Language language) {
		
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
