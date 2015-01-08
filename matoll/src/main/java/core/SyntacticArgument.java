package core;

public class SyntacticArgument {

	
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

}
