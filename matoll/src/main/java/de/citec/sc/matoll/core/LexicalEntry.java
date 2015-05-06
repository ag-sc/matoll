package de.citec.sc.matoll.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LexicalEntry {

	String URI;
	
	String CanonicalForm;
	
	String POS;
	

        HashSet<Sense> hashsetSense = new HashSet<Sense>();

	HashSet<SyntacticBehaviour> hashsetBehaviour = new HashSet<SyntacticBehaviour>();
        
	
	Provenance Provenance;
	
	List<String> Sentences;
	
	public LexicalEntry()
	{
		//argumentMap = new HashMap<String,String>();
		Sentences  = new ArrayList<String>();
			
	}
	
	
	public LexicalEntry(String uri) {
		URI = uri;
		//argumentMap = new HashMap<String,String>();
		Sentences  = new ArrayList<String>();
	}
	

	/*public String getMapping(String synArg)
	{
		if (argumentMap.containsKey(synArg))
			return argumentMap.get(synArg);
		else return null;
	}*/

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
		
                for(SyntacticBehaviour Behaviour : hashsetBehaviour) string += Behaviour.toString();
		for(Sense sense :hashsetSense) string += sense.toString();
		
		/*for (String synArg: argumentMap.keySet())
		{
			string += synArg + " => "+argumentMap.get(synArg)+"\n";
		}*/
		
		
		for (String sentence: Sentences)
		{
			string += "Sentence: "+sentence+"\n";
		}
		
		
		return string;
	}


	
	public HashSet<Sense> getSense()
	{
		return hashsetSense;
	}
	
	
	public void addSense(Sense sense) {
		hashsetSense.add(sense);
		
	}
        /**
         * Mapping of argument types
         * @param sense
         * @return 
         */
	public HashMap<String,String> computeMappings(Sense sense) {
		
		HashMap<String,String> map = new HashMap<String,String>();
		
                for(SyntacticBehaviour Behaviour : hashsetBehaviour){
                    for (SyntacticArgument synArg: Behaviour.getSynArgs())
                    {
			// System.out.print("Checking: "+synArg.getArgumentType()+"\n");
						
				for (SenseArgument senseArg: sense.getSenseArgs())
				{
					// System.out.print("Checking: "+senseArg.getArgumenType()+"\n");
				
					if (synArg.getValue().equals(senseArg.getValue()))
					{
						map.put(synArg.getArgumentType(), senseArg.getArgumenType());
					
						//System.out.print("Adding mapping: "+synArg.getArgumentType() + " -> "+senseArg.getArgumenType()+"\n");
					}	
				}
                    }
                }
			
		return map;
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
		if (hashsetBehaviour.isEmpty()) {
			// System.out.print("Behaviour is null!\n");
			if (!other.hashsetBehaviour.isEmpty())
				return false;
		} /*else if (!Behaviour.equals(other.Behaviour))
			return false;*/
                else if (!hashsetBehaviour.equals(other.hashsetBehaviour))
			return false;
				
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
		if (hashsetSense.isEmpty()) {
			if (!other.hashsetSense.isEmpty())
				return false;
		} /*else if (!Sense.equals(other.Sense))
			return false;*/
                else if (!hashsetSense.equals(other.hashsetSense))
			return false;
		
		/*if (argumentMap == null) {
			if (other.argumentMap != null)
				return false;
		} */
                /*else 
		{
			for (String synArg: this.getArgumentMap().keySet())	
			{
				if (!other.getArgumentMap().containsKey(synArg)) return false;
				else
				{
					if (!other.getArgumentMap().get(synArg).equals(this.getArgumentMap().get(synArg))) return false;
				}
					
			}
		}*/
		return true;
	}


	public HashSet<SyntacticBehaviour> getBehaviours() {
		return hashsetBehaviour;
	}
	
	

	public void addSyntacticBehaviour(SyntacticBehaviour behaviour)
	{
            hashsetBehaviour.add(behaviour);
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
		
		if (hashsetSense!=null){
                    if(hashsetSense.size()>1) System.err.println("Returns reference of the first sense!");
                    /*
                    TODO: return List of of references
                    */
                    for(Sense sense: hashsetSense){
                        return sense.getReference();
                    }
                    
                }
		else return null;
            return null;
		
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


