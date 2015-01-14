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
	
	
	
}
