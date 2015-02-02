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
		
		String string = "";
		
		string += "Reference: "+Reference+"\n";
		
		for (SenseArgument arg: senseArgs)
		{
			string += "\t SenseArg: "+arg.getArgumenType();
		}
		
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((Reference == null) ? 0 : Reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sense other = (Sense) obj;
		if (Reference == null) {
			if (other.Reference != null)
				return false;
		} else if (!Reference.equals(other.Reference))
			return false;
		if (senseArgs == null) {
			if (other.senseArgs != null)
				return false;
		} else if (!senseArgs.equals(other.senseArgs))
			return false;
		return true;
	}

	public String getReference() {
		return Reference;
	}
	public void setReference(String reference) {
		Reference = reference;
	}
	
}
