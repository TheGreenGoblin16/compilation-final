package types;

public class TypedIdentifierList
{
	public TypedIdentifier head;
	public TypedIdentifierList tail;
	
	public TypedIdentifierList( TypedIdentifier head, TypedIdentifierList tail)
	{
		this.head = head;
		this.tail = tail;
	}	
}