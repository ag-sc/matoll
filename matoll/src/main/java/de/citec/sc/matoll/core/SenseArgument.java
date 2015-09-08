package de.citec.sc.matoll.core;

public class SenseArgument {

	String ArgumentType;
	String Value;
	
	/**
         * Sets the argument Type to an value (identifier)
         * @param argumentType
         * @param value Has to be a unique identifier
         */
	public SenseArgument(String argumentType, String value) {
		ArgumentType = argumentType;
		Value = value;
	}

	public String getArgumenType()
	{
		return ArgumentType;
	}
	
        /**
         * Gets the identifier for a given SenseArgument. If set correctly, identifier is unique in the world of one lexical entry.
         * @return identifier
         */
	public String getValue()
	{
		return Value;
	}

	@Override
	public String toString() {
		return "SenseArgument [ArgumentType=" + ArgumentType + ", Value="
				+ Value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
                /**
                 * TODO: Why prime * result ? The result(without the addition) is always 31; with the additional term the result is a multiple of 31
                 */
		result = prime * result
				+ ((ArgumentType == null) ? 0 : ArgumentType.hashCode());
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
		SenseArgument other = (SenseArgument) obj;
		if (ArgumentType == null) {
			if (other.ArgumentType != null)
				return false;
		} else if (!ArgumentType.equals(other.ArgumentType))
			return false;
		
		return true;
	}
	
	
	
	
	
}
