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
	
	String Reference;
	
	String FrameType;
	
	String POS;
	
	HashMap<String,String> argumentMap;
	
	Set<SyntacticArgument> synArgs;
	
	Set<SenseArgument> senseArgs;
		
	Provenance Provenance;
	
	List<String> sentences;
	
	public LexicalEntry()
	{
		argumentMap = new HashMap<String,String>();
		synArgs = new HashSet<SyntacticArgument>();
		senseArgs = new HashSet<SenseArgument>();
		sentences  = new ArrayList<String>();
			
	}
	
	
	public LexicalEntry(String uri) {
		URI = uri;
		argumentMap = new HashMap<String,String>();
		synArgs = new HashSet<SyntacticArgument>();
		senseArgs = new HashSet<SenseArgument>();
		sentences  = new ArrayList<String>();
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


	public String getFrameType()
	{
		return FrameType;
	}

	
	public void setFrame(String frame) {
		FrameType = frame;
		
	}


	public void setReference(String reference) {
		Reference = reference;
		
	}
	
	public String getReference() {
		
		return Reference;
	}
	
	
	public String toString()
	{
		String string = "";
		
		string += "Lexical Entry: "+this.CanonicalForm +"(" + URI+")\n";
		
		string += "Reference: "+this.Reference+"\n";
		
		string += "Frame Type: "+this.FrameType+"\n";
		
		string += "POS: "+this.POS+"\n";
		
		for (String synArg: argumentMap.keySet())
		{
			string += synArg + " => " + argumentMap.get(synArg) +"\n" ;
		}
		
		for (String sentence: sentences)
		{
			string += "Sentence: "+sentence+"\n";
		}
		
		return string;
	}



	public void setSyntacticArguments(Set<SyntacticArgument> syntacticArguments) {
		
		synArgs = syntacticArguments;
	}
	
	public Set<SyntacticArgument> getSyntacticArguments()
	{
		return synArgs;
	}
	
	public Set<SenseArgument> getSenseArguments()
	{
		return senseArgs;
	}
	
	
	public void setSenseArguments(Set<SenseArgument> senseArguments) {
		senseArgs = senseArguments;
		
	}


	public void computeMappings() {
		
		for (SyntacticArgument synArg: synArgs)
		{
			// System.out.print("Checking: "+synArg.getArgumentType()+"\n");
			
			for (SenseArgument senseArg: senseArgs)
			{
				// System.out.print("Checking: "+senseArg.getArgumenType()+"\n");
				
				if (synArg.getValue().equals(senseArg.getValue()))
				{
					argumentMap.put(synArg.getArgumentType(), senseArg.getArgumenType());
					
					// System.out.print(synArg.getArgumentType() + " -> "+senseArg.getArgumenType()+"\n");
					
				}
			}		
		}
	}
	
	public HashMap<String,String> getArgumentMap()
	{
		return this.argumentMap;
	}
	
	@Override
	public boolean equals(Object entry)
	{
		this.computeMappings();
		((LexicalEntry) entry).computeMappings();
		
		if (!CanonicalForm.equals(((LexicalEntry) entry).getCanonicalForm())) return false;
		
		if (!Reference.equals(((LexicalEntry) entry).getReference())) return false;
		
		if (!FrameType.equals(((LexicalEntry) entry).getFrameType())) return false;
		
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
		result = prime * result
				+ ((FrameType == null) ? 0 : FrameType.hashCode());
		result = prime * result + ((POS == null) ? 0 : POS.hashCode());
		result = prime * result
				+ ((Reference == null) ? 0 : Reference.hashCode());
		result = prime * result
				+ ((argumentMap == null) ? 0 : argumentMap.hashCode());
		return result;
	}


//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		LexicalEntry other = (LexicalEntry) obj;
//		if (CanonicalForm == null) {
//			if (other.CanonicalForm != null)
//				return false;
//		} else if (!CanonicalForm.equals(other.CanonicalForm))
//			return false;
//		if (FrameType == null) {
//			if (other.FrameType != null)
//				return false;
//		} else if (!FrameType.equals(other.FrameType))
//			return false;
//		if (POS == null) {
//			if (other.POS != null)
//				return false;
//		} else if (!POS.equals(other.POS))
//			return false;
//		if (Reference == null) {
//			if (other.Reference != null)
//				return false;
//		} else if (!Reference.equals(other.Reference))
//			return false;
//		if (argumentMap == null) {
//			if (other.argumentMap != null)
//				return false;
//		} else if (!argumentMap.equals(other.argumentMap))
//			return false;
//		return true;
//	}


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


	public void addSyntacticArgument(SyntacticArgument synArg) {
		synArgs.add(synArg);
		
	}
	

	public void addSenseArgument(SenseArgument semArg) {
		senseArgs.add(semArg);
		
	}


	public void setProvenance(Provenance provenance) {
		Provenance = provenance;
		
	}
	
	public Provenance getProvenance()
	{
		return Provenance;
	}


	public void addSentence(String sentence) {
		// TODO Auto-generated method stub
		
	}
	
	
}


