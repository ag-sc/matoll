package core;

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

}
