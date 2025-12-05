package types;

public class TypedIdentifierList
{
	public TypedIdentifier head;
	public TypedIdentifierList tail;
	
	public (TypedIdentifierListdIdentifierField head, TypedIdentifierList tail)
	{
		this.head = head;
		this.tail = tail;
	}	
}