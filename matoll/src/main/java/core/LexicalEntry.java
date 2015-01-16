package core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Resource;

public class LexicalEntry {

	String URI;
	
	String CanonicalForm;
	
	String POS;
	
	HashMap<String,String> argumentMap;
	
	Sense Sense;
	
	SyntacticBehaviour Behaviour;
	
	Provenance Provenance;
	
	List<String> Sentences;
	
	public LexicalEntry()
	{
		argumentMap = new HashMap<String,String>();
		Sentences  = new ArrayList<String>();
			
	}
	
	
	public LexicalEntry(String uri) {
		URI = uri;
		argumentMap = new HashMap<String,String>();
		Sentences  = new ArrayList<String>();
	}
	

	public String getMapping(String synArg)
	{
		if (argumentMap.containsKey(synArg))
			return argumentMap.get(synArg);
		else return null;
	}

	public void setCanonicalForm(String canonicalForm)
	{
		CanonicalForm = canonicalForm;
	}
	
	public String getCanonicalForm()
	{
		return CanonicalForm;
	}

	
	
	
	public String toString()
	{
		String string = "";
		
		string += "Lexical Entry: "+this.CanonicalForm +"(" + URI+")\n";
				
		
		string += "POS: "+this.POS+"\n";
		
		string += Behaviour.toString();
		
		string += Sense.toString();
		
		for (String sentence: Sentences)
		{
			string += "Sentence: "+sentence+"\n";
		}
		
		for (String synArg: argumentMap.keySet())
		{
			string += synArg + " => "+argumentMap.get(synArg)+"\n";
		}
		
		return string;
	}


	
	public Sense getSense()
	{
		return Sense;
	}
	
	
	public void setSense(Sense sense) {
		Sense = sense;
		
	}


	public HashMap<String,String> computeMappings(Sense sense) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		
		for (SyntacticArgument synArg: Behaviour.getSynArgs())
		{
			// System.out.print("Checking: "+synArg.getArgumentType()+"\n");
						
				for (SenseArgument senseArg: sense.getSenseArgs())
				{
					// System.out.print("Checking: "+senseArg.getArgumenType()+"\n");
				
					if (synArg.getValue().equals(senseArg.getValue()))
					{
						map.put(synArg.getArgumentType(), senseArg.getArgumenType());
					
						// System.out.print(synArg.getArgumentType() + " -> "+senseArg.getArgumenType()+"\n");
					}	
				}
			}	
		return map;
	}
	
	public HashMap<String,String> getArgumentMap()
	{
		return this.argumentMap;
	}
	
	@Override
	public boolean equals(Object entry)
	{
		
		if (!CanonicalForm.equals(((LexicalEntry) entry).getCanonicalForm())) return false;

		// check that senses are compatible
		
		if (Behaviour != null)
		
			if (!Behaviour.getFrame().equals(((LexicalEntry) entry).getBehaviour().getFrame())) return false;
		
		if (POS != null)
		
			if (!POS.equals(((LexicalEntry) entry).getPOS())) return false;
		
		if (!argumentMap.equals(((LexicalEntry) entry).getArgumentMap())) return false;		
		
		return true;
	}


	





	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((CanonicalForm == null) ? 0 : CanonicalForm.hashCode());
		result = prime * result + ((POS == null) ? 0 : POS.hashCode());
		return result;
	}


	

	public SyntacticBehaviour getBehaviour() {
		return Behaviour;
	}
	
	

	public void setSyntacticBehaviour(SyntacticBehaviour behaviour)
	{
		Behaviour = behaviour;
	}
	


	public String getPOS() {
		return POS;
	}


	public void setPOS(String pos) {
		POS = pos;
		
	}


	public void setURI(String uri) {
		URI = uri;
		
	}
	
	public String getURI()
	{
		return URI;
	}


	public void setProvenance(Provenance provenance) {
		Provenance = provenance;
		
	}
	
	public Provenance getProvenance()
	{
		return Provenance;
	}


	public void addSentence(String sentence) {
		Sentences.add(sentence);
		
	}


	public String getReference() {
		
		if (Sense != null) return Sense.getReference();
		else return null;
		
	}


	public void setMappings(HashMap<String, String> map) {
		argumentMap = map;
		
	}


	public List<String> getSentences() {
		return Sentences;
	}


	public void addSentences(List<String> sentences) {
		Sentences.addAll(sentences);
		
	}
	
	
}


