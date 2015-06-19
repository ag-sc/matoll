package de.citec.sc.matoll.core;

import java.util.HashSet;
import java.util.Set;

public class Sense {

	Set<SenseArgument> senseArgs;
	Reference Reference;
	
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
			string += "\t SenseArg: "+arg.getArgumenType()+"\n";
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
		
            
                if(obj== null) return false;
                else{
                    Sense other = (Sense) obj;
                    if(other.getReference()==null||Reference == null){
                        return false;
                    }
                    else{
                        if(other.getReference().getURI().equals(Reference.getURI())){
                            if(senseArgs == null||other.senseArgs==null){
                                return false;
                            }
                            else{
                                if(senseArgs.equals(other.senseArgs)){
                                    return true;
                                }
                                else{
                                    return false;
                                }
                            }
                        }
                    }
                }
                return false;
//		 System.out.print("I am in equals (Sense)\n");
//		
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		Sense other = (Sense) obj;
//		if (Reference == null) {
//			if (other.Reference != null)
//				return false;
//		} else if (!Reference.equals(other.Reference))
//			{
//				 System.out.print("Reference is different\n");
//				return false;
//			}
//		if (senseArgs == null) {
//			if (other.senseArgs != null)
//				return false;
//		} 
//		else if (!senseArgs.equals(other.senseArgs))
//			{
//				 System.out.print("Sense args are different\n");
//				 System.out.println(senseArgs);
//				 System.out.println(other.senseArgs);
//				return false;
//			}
//		return true;
	}

	public Reference getReference() {
		return Reference;
	}
	public void setReference(Reference reference) {
		Reference = reference;
	}
	
}
