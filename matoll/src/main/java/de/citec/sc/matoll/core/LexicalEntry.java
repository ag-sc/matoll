package de.citec.sc.matoll.core;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LexicalEntry {

	String URI;
	
	String CanonicalForm;
	
	String POS;
        
        Preposition preposition;

        
        /*
        Set default to English
        */
        Language language = Language.EN;

        
	

//        HashSet<Sense> hashsetSense = new HashSet<Sense>();

	HashMap<Sense,HashSet<SyntacticBehaviour>> hashsetSenseBehaviour = new HashMap<Sense,HashSet<SyntacticBehaviour>>();
        
	
	//Provenance Provenance;
        HashMap<Sense,Provenance> mappingReferenceProvenance = new HashMap<Sense,Provenance>();
	
	
	
	public LexicalEntry(Language language)
	{
            this.language = language;
	}
	
	
	public LexicalEntry(String uri, Language language) {
            URI = uri;
            this.language = language;
	}
	


	public void setCanonicalForm(String canonicalForm)
	{
		CanonicalForm = canonicalForm;
	}
	
	public String getCanonicalForm()
	{
		return CanonicalForm;
	}

	
	public Language getLanguage() {
            return language;
        }
	
	public String toString()
	{
		String string = "";
		
		string += "Lexical Entry: "+this.CanonicalForm +" (" + URI+")\n";
				
		
		string += "POS: "+this.POS+"\n";
		
                
		for(Sense sense :hashsetSenseBehaviour.keySet()) {
                    for(SyntacticBehaviour Behaviour : hashsetSenseBehaviour.get(sense)) string += Behaviour.toString();
                    string += sense.toString();
                    if(mappingReferenceProvenance.containsKey(sense)){
                        Provenance provenance = mappingReferenceProvenance.get(sense);
                        if(provenance.getPatternset()!=null)for(String pattern:provenance.getPatternset())string+="Pattern: "+pattern+"\n";
                        if(provenance.getSentences()!=null)for(Sentence sentence:provenance.getShortestSentences(5))string+="Sentence: "+sentence.getSentence()+"\n";
                    }
                }
		
		/*for (String synArg: argumentMap.keySet())
		{
			string += synArg + " => "+argumentMap.get(synArg)+"\n";
		}*/
		
		
//		for (String sentence: Sentences)
//		{
//			string += "Sentence: "+sentence+"\n";
//		}
//                
                
		
		
		return string;
	}


	
//	public HashSet<Sense> getSenses()
//	{
//		return hashsetSense;
//	}
	
	
//	public void addSense(Sense sense) {
//		hashsetSense.add(sense);
//		
//	}
        /**
         * Mapping of argument types
         * @param sense
         * @return 
         */
//	private HashMap<String,String> computeMappings(Sense sense) {
//		
//		HashMap<String,String> map = new HashMap<String,String>();
//		
//                for(SyntacticBehaviour Behaviour : hashsetSenseBehaviour.get(sense)){
//                    for (SyntacticArgument synArg: Behaviour.getSynArgs())
//                    {
//			// System.out.print("Checking: "+synArg.getArgumentType()+"\n");
//						
//				for (SenseArgument senseArg: sense.getSenseArgs())
//				{
//					// System.out.print("Checking: "+senseArg.getArgumenType()+"\n");
//				
//					if (synArg.getValue().equals(senseArg.getValue()))
//					{
//						map.put(synArg.getArgumentType(), senseArg.getArgumenType());
//					
//						//System.out.print("Adding mapping: "+synArg.getArgumentType() + " -> "+senseArg.getArgumenType()+"\n");
//					}	
//				}
//                    }
//                }
//			
//		return map;
//	}

	


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
		
                LexicalEntry other = (LexicalEntry) obj;
            //now URIs are even so if the URI is not equal, return false and do not check other options
                if(this.getURI()==null || this.getReferences().isEmpty()||other.getURI()==null || other.getReferences() == null) {
                    return false;
                }
                //else return this.getURI().equals(other.getURI()) && this.getReferences().equals(other.getReferences());
                else return this.getURI().equals(other.getURI())&&other.getLanguage().equals(this.getLanguage());
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		// System.out.print("Checking class equivalence!!!\n");
//		if (getClass() != obj.getClass())
//		{
//			System.out.print(getClass() +" vs. "+obj.getClass()+"\n");
//			return false;
//		}
//		
//		if (hashsetSenseBehaviour.isEmpty()) {
//			// System.out.print("Behaviour is null!\n");
//			if (!other.hashsetSenseBehaviour.isEmpty())
//				return false;
//		} /*else if (!Behaviour.equals(other.Behaviour))
//			return false;*/
//                else if (!hashsetSenseBehaviour.equals(other.hashsetSenseBehaviour))
//			return false;
//				
//		if (CanonicalForm == null) {
//			if (other.CanonicalForm != null)
//				return false;
//		} else if (!CanonicalForm.equals(other.CanonicalForm))
//			return false;
//		if (POS == null) {
//			if (other.POS != null)
//				return false;
//		} else if (!POS.equals(other.POS))
//			return false;
//		if (hashsetSense.isEmpty()) {
//			if (!other.hashsetSense.isEmpty())
//				return false;
//		} /*else if (!Sense.equals(other.Sense))
//			return false;*/
//                else if (!hashsetSense.equals(other.hashsetSense))
//			return false;
//		
//		/*if (argumentMap == null) {
//			if (other.argumentMap != null)
//				return false;
//		} */
//                /*else 
//		{
//			for (String synArg: this.getArgumentMap().keySet())	
//			{
//				if (!other.getArgumentMap().containsKey(synArg)) return false;
//				else
//				{
//					if (!other.getArgumentMap().get(synArg).equals(this.getArgumentMap().get(synArg))) return false;
//				}
//					
//			}
//		}*/
//		return true;
	}


	public HashMap<Sense, HashSet<SyntacticBehaviour>> getSenseBehaviours() {
		return hashsetSenseBehaviour;
	}
	
	

	public void addSyntacticBehaviour(SyntacticBehaviour behaviour, Sense sense)
	{
            if(hashsetSenseBehaviour.containsKey(sense)){
                HashSet<SyntacticBehaviour> list = hashsetSenseBehaviour.get(sense);
                list.add(behaviour);
                hashsetSenseBehaviour.put(sense, list);
            }
            else{
                HashSet<SyntacticBehaviour> list = new HashSet<SyntacticBehaviour>();
                list.add(behaviour);
                hashsetSenseBehaviour.put(sense, list);
            }
            
        }
        
        public void addAllSyntacticBehaviour(HashSet<SyntacticBehaviour> behaviours, Sense sense)
	{
            if(hashsetSenseBehaviour.containsKey(sense)){
                HashSet<SyntacticBehaviour> list = hashsetSenseBehaviour.get(sense);
                list.addAll(behaviours);
                hashsetSenseBehaviour.put(sense, list);
            }
            else{
                HashSet<SyntacticBehaviour> list = new HashSet<SyntacticBehaviour>();
                list.addAll(behaviours);
                hashsetSenseBehaviour.put(sense, list);
            }
            
        }
	

        public int getOverallFrequency(){
            int tmp_freq = 0;
            tmp_freq = mappingReferenceProvenance.keySet().stream().map((sense) -> mappingReferenceProvenance.get(sense)).map((tmp_prov) -> tmp_prov.getFrequency()).reduce(tmp_freq, Integer::sum);
            return tmp_freq;
        }

	public String getPOS() {
		return POS;
	}
        
        public Preposition getPreposition() {
            if(preposition==null) return null;
            if(preposition.getCanonicalForm().equals("")) return null;
            return preposition;
        }

        public void setPreposition(Preposition preposition) {
            this.preposition = preposition;
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


	public void addProvenance(Provenance provenance, Sense sense) {
//            this.hashsetSense.add(sense);
                if(mappingReferenceProvenance.containsKey(sense)){
                    //System.out.println("For given sense there is already a provenance");
                    Provenance tmp_provenance = mappingReferenceProvenance.get(sense);
                    tmp_provenance.increaseFrequency(provenance.getFrequency());
                    tmp_provenance.addAllPattern(provenance.getPatternset());
                    tmp_provenance.addSentences(provenance.getSentences());
                    mappingReferenceProvenance.remove(sense);
                    mappingReferenceProvenance.put(sense, tmp_provenance);
                }
                else{
                    mappingReferenceProvenance.put(sense, provenance);
                }
		//Provenance = provenance;
		
	}
	
	public Provenance getProvenance(Sense sense)
	{
		return mappingReferenceProvenance.get(sense);
	}



        public Set<Reference> getReferences() {
		
            Set<Reference> references = new HashSet<Reference>();
            if(hashsetSenseBehaviour!=null){
                for(Sense sense :hashsetSenseBehaviour.keySet()){
                    references.add(sense.getReference());
                }
            }
//		if (hashsetSense!=null){
//                    for(Sense sense: hashsetSense){
//                        references.add(sense.getReference());
//                    }
//                    
//                }
		
            return references;
		
	}


       
	
	
}


