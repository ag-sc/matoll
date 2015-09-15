package de.citec.sc.matoll.core;
import de.citec.sc.matoll.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;


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
	
        public void addLexicon(Lexicon lex){
            lex.getEntries().stream().forEach((e) -> {
                this.addEntry(e);
            });
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
        
        public List<Preposition> getPrepositions(){
            List<Preposition> prepositions = new ArrayList<>();
            
            prepositions = this.entries.stream()
                    .filter(e->e.getPreposition()!=null)
                    .map((LexicalEntry e)->{return e.getPreposition();})
                    .collect(Collectors.toList());
            return prepositions;
        }
        
        /**
         * Returns all top k (e.g. 1000) senses, created by a given pattern from the lexicon. Additionaly writes each selected entry into a septerated file.
         * @param pattern_name Name of the pattern to look for
         * @param topK Number of best (according to frequency) entries (senses) with are returned
         * @param path Path to folder, where each entry is saved
         * @return 
         */
        public List<String> getTopKEntriesForPattern(String pattern_name, int topK, String path){
            System.out.println("Start with: "+pattern_name);
            List<String> results = new ArrayList<>();
            Map<Sense,Integer> hm = new HashMap<>();
            Map<String,List<Provenance>> hm2 = new HashMap<>();
            
            LexiconSerialization serial = new LexiconSerialization(false);
            
            class EntryClass{
                LexicalEntry entry;
                int frequency = 0;

                public LexicalEntry getEntry() {
                    return entry;
                }

                public int getFrequency() {
                    return frequency;
                }
                public EntryClass(LexicalEntry entry, int frequency){
                    this.entry = entry;
                    this.frequency=frequency;
                }
            }
            
            List<EntryClass> test = new ArrayList<>();
            
            entries.stream().forEach((entry) -> {
                entry.getSenseBehaviours().keySet().stream().forEach((sense) -> {
                    Provenance provenance = entry.getProvenance(sense);
                    if (provenance.getPatternset().contains(pattern_name)) {
                        LexicalEntry newEntry = new LexicalEntry(entry.getLanguage());
                        newEntry.setPOS(entry.getPOS());
                        newEntry.setURI(entry.getURI());
                        newEntry.addAllSyntacticBehaviour(entry.getSenseBehaviours().get(sense), sense);
                        newEntry.addProvenance(provenance, sense);
                        if(entry.getPreposition()!=null){
                            newEntry.setPreposition(entry.getPreposition());
                        }
                        newEntry.setCanonicalForm(entry.getCanonicalForm());
                        EntryClass tmp = new EntryClass(newEntry,provenance.getFrequency());
                        test.add(tmp);
                    }
                });
            });
         
            
            Collections.sort(test, (EntryClass ec1, EntryClass ec2) ->{
                    return Integer.valueOf(ec2.getFrequency()).compareTo(ec1.getFrequency());
            });
            int counter = 0;
            for(EntryClass ec : test){
                if(counter<topK){
                    LexicalEntry entry = ec.getEntry();
                    String name = pattern_name+"_"+Integer.toString(counter);
                    String output = name+" "+entry.getCanonicalForm()+" "+ec.getFrequency()+" "+entry.getPOS();
                    output = entry.getReferences().stream().map((r) -> " "+r.getURI()).reduce(output, String::concat);
                    if(entry.getPreposition()!=null) output+=" "+entry.getPreposition().getCanonicalForm();
                    for(Sense sense : entry.getSenseBehaviours().keySet()){
                        Provenance provenance = entry.getProvenance(sense);
                        for(Sentence sentence : provenance.getShortestSentences(5)) output+=" "+sentence.getSentence();
                    }
                    results.add(output);
                    System.out.println(output);
                    /*
                    Write each entry in seperate File
                    */
                    Lexicon output_lexicon = new Lexicon();
                    output_lexicon.setBaseURI(baseURI);
                    output_lexicon.addEntry(entry);
                    Model model = ModelFactory.createDefaultModel();
                    serial.serialize(output_lexicon, model);		
                    try {
                        FileOutputStream out = new FileOutputStream(new File(path+name+".ttl"));
                        RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
                        out.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Lexicon.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }
                counter+=1;
                
            }
            return results;
        }
}
