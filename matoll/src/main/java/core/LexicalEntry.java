package core;
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
			
	}
	
	
	public LexicalEntry(String uri) {
		URI = uri;
		argumentMap = new HashMap<String,String>();
		synArgs = new HashSet<SyntacticArgument>();
		senseArgs = new HashSet<SenseArgument>();
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
		
		string += "Lexical Entry: "+this.CanonicalForm +"(" +  URI+")\n";
		
		string += "Reference: "+this.Reference+"\n";
		
		string += "Frame Type: "+this.FrameType+"\n";
		
		string += "POS: "+this.POS+"\n";
		
		for (String synArg: argumentMap.keySet())
		{
			string += synArg + " => " + argumentMap.get(synArg) +"\n" ;
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
	
	public boolean equals(LexicalEntry entry)
	{
		if (!CanonicalForm.equals(entry.getCanonicalForm())) return false;
		
		if (!Reference.equals(entry.getReference())) return false;
		
		if (!FrameType.equals(entry.getFrameType())) return false;
		
		if (!POS.equals(entry.getPOS())) return false;
		
		for (SyntacticArgument synArg: synArgs)
		{
			if (entry.getMapping(synArg.getArgumentType()) == null) return false;
			else
			{
				return (this.getMapping(synArg.getArgumentType()) == entry.getMapping(synArg.getArgumentType()));
			}
		}
		
		
		return true;
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
	
	
}


