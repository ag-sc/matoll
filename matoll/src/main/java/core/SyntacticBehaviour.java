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
		return "SyntacticBehaviour [Frame=" + Frame + ", synArgs=" + synArgs
				+ "]";
	}
	
	
}
