package types;

public class TypeClassFieldList
{
	public TypeClassField head;
	public TypeClassFieldList tail;
	
	public TypeClassFieldList(TypeClassField head, TypeClassFieldList tail)
	{
		this.head = head;
		this.tail = tail;
	}	
}