package types;

public class TypedIdentifier
{
	public Type type;
	public String name;
	
	public TypedIdentifier(Type type, String name)
	{
		this.type = type;
		this.name = name;
	}
}