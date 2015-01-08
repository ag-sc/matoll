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
	
}
