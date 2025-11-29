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
	public TypeList dataMembers;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeClass(TypeClass parent, String name, TypeList dataMembers)
	{
		this.name = name;
		this.parent = parent;
		this.dataMembers = dataMembers;
	}
}