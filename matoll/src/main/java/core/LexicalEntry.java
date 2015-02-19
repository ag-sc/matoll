package core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		
		string += "Lexical Entry: "+this.CanonicalForm +" (" + URI+")\n";
				
		
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
					
						// System.out.print("Adding mapping: "+synArg.getArgumentType() + " -> "+senseArg.getArgumenType()+"\n");
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((CanonicalForm == null) ? 0 : CanonicalForm.hashCode());
		result = prime * result + ((POS == null) ? 0 : POS.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		
		// System.out.print("I am in equals (LexicalEntry)\n");
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		// System.out.print("Checking class equivalence!!!\n");
		if (getClass() != obj.getClass())
		{
			System.out.print(getClass() +" vs. "+obj.getClass()+"\n");
			return false;
		}
		LexicalEntry other = (LexicalEntry) obj;
		if (Behaviour == null) {
			// System.out.print("Behaviour is null!\n");
			if (other.Behaviour != null)
				return false;
		} else { 
			// System.out.print("Checking Syntactic behaviour...\n"); 
			if (!Behaviour.equals(other.Behaviour))
				return false;
				}
		if (CanonicalForm == null) {
			if (other.CanonicalForm != null)
				return false;
		} else if (!CanonicalForm.equals(other.CanonicalForm))
			return false;
		if (POS == null) {
			if (other.POS != null)
				return false;
		} else if (!POS.equals(other.POS))
			return false;
		if (Sense == null) {
			if (other.Sense != null)
				return false;
		} else if (!Sense.equals(other.Sense))
			return false;
		
		if (argumentMap == null) {
			if (other.argumentMap != null)
				return false;
		} else 
		{
			for (String synArg: this.getArgumentMap().keySet())	
			{
				if (!other.getArgumentMap().containsKey(synArg)) return false;
				else
				{
					if (!other.getArgumentMap().get(synArg).equals(this.getArgumentMap().get(synArg))) return false;
				}
					
			}
		}
		return true;
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


	public Reference getReference() {
		
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


	public void setSentences(List<String> sentences) {
		Sentences = sentences;
		
	}
	
	
}


