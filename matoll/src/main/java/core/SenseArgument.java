package core;

public class SenseArgument {

	String ArgumentType;
	String Value;
	
	
	public SenseArgument(String argumentType, String value) {
		ArgumentType = argumentType;
		Value = value;
	}

	public String getArgumenType()
	{
		return ArgumentType;
	}
	
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
		result = prime * result
				+ ((ArgumentType == null) ? 0 : ArgumentType.hashCode());
		result = prime * result + ((Value == null) ? 0 : Value.hashCode());
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
