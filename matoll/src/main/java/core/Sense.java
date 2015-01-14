package core;

import java.util.HashSet;
import java.util.Set;

public class Sense {

	Set<SenseArgument> senseArgs;
	String Reference;
	
	public Sense()
	{
		senseArgs = new HashSet<SenseArgument>();
	}
	
	public Set<SenseArgument> getSenseArgs() {
		return senseArgs;
	}
	public void setSenseArgs(Set<SenseArgument> senseArgs) {
		this.senseArgs = senseArgs;
	}
	
	public void addSenseArg(SenseArgument senseArg)
	{
		senseArgs.add(senseArg);
	}
	
	@Override
	public String toString() {
		return "Sense [senseArgs=" + senseArgs + ", Reference=" + Reference
				+ "]\n";
	}

	public String getReference() {
		return Reference;
	}
	public void setReference(String reference) {
		Reference = reference;
	}
	
}
