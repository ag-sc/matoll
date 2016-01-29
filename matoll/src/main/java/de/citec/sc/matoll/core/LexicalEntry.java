package de.citec.sc.matoll.core;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LexicalEntry {

	String URI;
	
	String CanonicalForm;
        Set<String> AlternativeForms = new HashSet<String>();

	
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
	
        public Set<String> getAlternativeForms() {
            return AlternativeForms;
        }

        public void setAlternativeForms(Set AlternativeForms) {
            this.AlternativeForms = AlternativeForms;
        }
        
        public void addAlternativeForms(String alternativeForm) {
            this.AlternativeForms.add(alternativeForm);
        }
        public void addAlternativeFormsAll(Set alternativeForms) {
            this.AlternativeForms.addAll(alternativeForms);
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
	
        @Override
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
                
		
		
		return string;
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
                LexicalEntry other = (LexicalEntry) obj;
            //now URIs are even so if the URI is not equal, return false and do not check other options
                if(this.getURI()==null || this.getReferences().isEmpty()||other.getURI()==null || other.getReferences() == null) {
                    return false;
                }
                //else return this.getURI().equals(other.getURI()) && this.getReferences().equals(other.getReferences());
                else return this.getURI().equals(other.getURI())&&other.getLanguage().equals(this.getLanguage());
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
            return references;
		
	}


       
	
	
}


