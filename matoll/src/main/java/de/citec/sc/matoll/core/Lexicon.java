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

	//HashMap<String,List<LexicalEntry>> map;
	
	List<LexicalEntry> entries;
	
	Set<Reference> references;
	
	String baseURI = "";
	
	public Lexicon()
	{
		entries = new ArrayList<LexicalEntry>();
		
//		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<Reference>();
                
                this.baseURI = "http://dblexipedia.org/lexicon/";
	}
	
	public Lexicon(String baseURI)
	{
		entries = new ArrayList<LexicalEntry>();
		
//		map = new HashMap<String,List<LexicalEntry>>();
		
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
		
		}
		else
		{   
                        /*
                    This is the entry from the list entries, which is updated
                    */
                        //System.out.println("Update entry");
                        LexicalEntry containedEntry;
	
			containedEntry = getLexicalEntry(entry);
                        //if entry of URI is the same, but different forms appear, add alternative forms
                        if(!containedEntry.getCanonicalForm().equals(entry.getCanonicalForm())){
                            containedEntry.addAlternativeForms(entry.getCanonicalForm());
                        }
                        if(!entry.getAlternativeForms().isEmpty()) containedEntry.addAlternativeFormsAll(entry.getAlternativeForms());
                        
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
            List<LexicalEntry> results = new ArrayList<>();
            for(LexicalEntry entry: entries){
                if(entry.getCanonicalForm().equals(canonicalForm)) results.add(entry);
            }
            
            return results;

                

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
	
        public LexicalEntry getLexicalEntry(LexicalEntry entry){
            String uri = entry.getURI();
            for(LexicalEntry containedEntry : entries){
                /// if URI are euqal, then we assume the whole entry is the same (but maybe contains different senses)
                if(containedEntry.getURI().equals(uri)) return containedEntry;
            }
            return null;
        }
                
//	public LexicalEntry getLexicalEntry(LexicalEntry entry)
//	{
//		if (entries.contains(entry))
//		{
//                        //check here fore all entries with the same URI....
//			for (LexicalEntry containedEntry: this.getEntriesWithCanonicalForm(entry.getCanonicalForm()))
//			{
//				if (entry.equals(containedEntry)) return containedEntry;
//			}
//		}
//		
//		return null;
//	}
	
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
     * @param topK
         * @param min
         * @param max
         * @param path Path to folder, where each entry is saved
         * @return 
         */
        public Set<String> getTopKEntriesForPattern(String pattern_name, int topK, int min, int max, String path){
            System.out.println("Start with: "+pattern_name);
            Set<String> results = new HashSet<>();
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
                        if(provenance.getFrequency()>=min && provenance.getFrequency()<=max){
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
                        
                    }
                });
            });
         
            
            Collections.sort(test, (EntryClass ec1, EntryClass ec2) ->{
                    return Integer.valueOf(ec2.getFrequency()).compareTo(ec1.getFrequency());
            });
            int counter = 0;
            for(EntryClass ec : test){
                if(counter<topK){
                    Set<String> frameset = new HashSet<>();
                    LexicalEntry entry = ec.getEntry();
                    String name = pattern_name+"_"+Integer.toString(counter)+"_"+Integer.toString(min)+"_"+Integer.toString(max);
                    String output = "\t\t"+entry.getCanonicalForm();
                    if(entry.getPreposition()!=null) output+="\t"+entry.getPreposition().getCanonicalForm();
                    else{output+="\t";}
                    
                    output = entry.getReferences().stream().map((r) -> "\t"+r.getURI().replace("http://dbpedia.org/ontology/","dbo:")).reduce(output, String::concat);
                    
                    for(Sense sense : entry.getSenseBehaviours().keySet()){
                        Provenance provenance = entry.getProvenance(sense);
                        int sentence_counter =  0;
                        for(Sentence sentence : provenance.getShortestSentences(5)) {
                            output+="\t"+sentence.getSentence()+" sop:"+sentence.getSubjOfProp()+" oop:"+sentence.getObjOfProp();
                            sentence_counter+=1;
                        }
                        while (sentence_counter<5){
                            output+="\t";
                            sentence_counter+=1;
                        }
                    }
                    for(Sense sense : entry.getSenseBehaviours().keySet()){
                         for( SyntacticBehaviour s : entry.getSenseBehaviours().get(sense)){
                             String tmp_frame = s.getFrame().replace("http://www.lexinfo.net/ontology/2.0/lexinfo#","lexinfo:");
                             if(!frameset.contains(tmp_frame)){
                                 output+="\t"+tmp_frame;
                                 frameset.add(tmp_frame);
                             }
                             
                         }
                     }
                    output+="\t"+name;
                    results.add(output);
                    //System.out.println(output);
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

        public Set<String> getTopKEntriesForURI(String givenURI, int topK, Language givenLanguage,String path){
            System.out.println("Start with: "+givenURI);
            Set<String> results = new HashSet<>();
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
                    if(entry.getLanguage().equals(givenLanguage)){
                        
                        Provenance provenance = entry.getProvenance(sense);
                        if (sense.getReference().getURI().contains(givenURI)) {
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
                    String name = givenLanguage.toString()+"_"+givenURI.replaceAll("http:\\/\\/","").replaceAll("\\/","_").replaceAll("\\.","_")+"_"+Integer.toString(counter);
                    String output = name+"\t"+entry.getCanonicalForm()+"\t"+ec.getFrequency()+"\t"+entry.getPOS();
                    output = entry.getReferences().stream().map((r) -> "\t"+r.getURI()).reduce(output, String::concat);
                    if(entry.getPreposition()!=null) output+="\t"+entry.getPreposition().getCanonicalForm();
                    else{output+="\t\t";}
                    for(Sense sense : entry.getSenseBehaviours().keySet()){
                        Provenance provenance = entry.getProvenance(sense);
                        for(Sentence sentence : provenance.getShortestSentences(5)) output+="\t"+sentence.getSentence()+" sop:"+sentence.getSubjOfProp()+" oop:"+sentence.getObjOfProp();
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

