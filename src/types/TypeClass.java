package types;

public class TypeClass extends Type
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TypeClass parent;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TypedIdentifierList dataMembers;

	private TypeClassInstance instance;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeClass(TypeClass parent, String name, TypedIdentifierList dataMembers)
	{
		this.name = name;
		this.parent = parent;
		this.dataMembers = dataMembers;
	}


	public TypeClassInstance getInstance()
	{
		if (instance == null)
		{
			instance = new TypeClassInstance(this);
		}
		return instance;
	}
}