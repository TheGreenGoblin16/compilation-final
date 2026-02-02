package types;

import symboltable.*;

public class TypedIdentifier
{
	public Type type;
	public String name;
	public SymbolTableEntry entry; // Only relevant for class fields
	
	public TypedIdentifier(Type type, String name, SymbolTableEntry entry)
	{
		this.type = type;
		this.name = name;
		this.entry = entry;
	}
}