package core;

import java.util.HashSet;
import java.util.Set;

public class SyntacticBehaviour {

	String Frame;
	
	Set<SyntacticArgument> synArgs;

	public String getFrame() {
		return Frame;
	}

	public SyntacticBehaviour() {
		synArgs = new HashSet<SyntacticArgument>();
	}

	public void setFrame(String frame) {
		Frame = frame;
	}

	public Set<SyntacticArgument> getSynArgs() {
		return synArgs;
	}

	public void setSynArgs(Set<SyntacticArgument> synArgs) {
		this.synArgs = synArgs;
	}
	
	public void add(SyntacticArgument synArg)
	{
		synArgs.add(synArg);
	}
	
	public boolean equals(SyntacticBehaviour behaviour)
	{
		boolean found;
		
		if (Frame != null && behaviour.getFrame() != null)
		{
			if (!Frame.equals(behaviour.getFrame())) return false;
		}
		
		for (SyntacticArgument synArg: synArgs)
		{
			found = false;
			
			for (SyntacticArgument synGold: behaviour.getSynArgs())
			{
				if (synGold.getArgumentType().equals(synArg.getArgumentType()))
				{
					found = true;
					
					if (synGold.getPreposition() != null)
					{
						if (synArg.getPreposition() == null) found = false;
						else
						{
							if (!synGold.getPreposition().equals(synArg.getPreposition())) found = false;
						}
					}
				}
					
			}
			if (!found) return false;
			
		}
	
		return true;
	}

	@Override
	public String toString() {
		String string = "";
		
		string += "Frame:" +Frame+ "\n";
		
		for (SyntacticArgument arg: synArgs)
		{
			string += "\t Syntactic Argument: "+arg.getArgumentType() + "("+arg.getPreposition()+")\n";
		}
		
		return string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Frame == null) ? 0 : Frame.hashCode());
		result = prime * result + ((synArgs == null) ? 0 : synArgs.hashCode());
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
		SyntacticBehaviour other = (SyntacticBehaviour) obj;
		if (Frame == null) {
			if (other.Frame != null)
				return false;
		} else if (!Frame.equals(other.Frame))
			return false;
		if (synArgs == null) {
			if (other.synArgs != null)
				return false;
		} else if (!synArgs.equals(other.synArgs))
			return false;
		return true;
	}
	
	
}
