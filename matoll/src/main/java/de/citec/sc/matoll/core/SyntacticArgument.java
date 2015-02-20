package de.citec.sc.matoll.core;

public class SyntacticArgument {

	
	@Override
	public String toString() {
		return "SyntacticArgument [ArgumentType=" + ArgumentType + ", Value="
				+ Value + ", Preposition=" + Preposition + "]";
	}

	String ArgumentType;
	
	String Value;
	
	String Preposition;
	
	public SyntacticArgument(String argumentType, String value, String preposition) {
		ArgumentType = argumentType;
		Value = value;
		Preposition = preposition;
		
	}
	
	public String getArgumentType()
	{
		return ArgumentType;
	}
	
	public String getValue()
	{
		return Value;
	}
	
	public String getPreposition()
	{
		return Preposition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ArgumentType == null) ? 0 : ArgumentType.hashCode());
		result = prime * result
				+ ((Preposition == null) ? 0 : Preposition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		// System.out.print("I am in equals (Syntactic Argument)\n");
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyntacticArgument other = (SyntacticArgument) obj;
		if (ArgumentType == null) {
			if (other.ArgumentType != null)
				return false;
		} else if (!ArgumentType.equals(other.ArgumentType))
			return false;
		if (Preposition == null) {
			if (other.Preposition != null)
				return false;
		} else if (!Preposition.equals(other.Preposition))
			return false;
		return true;
	}

}
