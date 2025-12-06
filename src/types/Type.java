package types;

public abstract class Type
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	static public boolean isMatchingTypeOf(Type e, Type t) {
		// Assuming e, t != null

		/***************************************************/
		/* [1] Check for Exact Type Match                  */
		/* Covers: int, string, exact array, exact class   */
		/***************************************************/
		if (t == e) { return true; }

		/***************************************************/
		/* [2] Check for Nil Assignment                    */
		/* Rule: nil is allowed for Arrays and Classes     */
		/* Rule: nil is ILLEGAL for int and string         */
		/***************************************************/
		if (e == TypeVoid.getInstance())
		{
			if (t.isClass() || t.isArray()) { return true; }
		}

		/***************************************************/
		/* [3] Check for Class Inheritance (Polymorphism)  */
		/* Rule: RHS can be a subclass of LHS              */
		/***************************************************/
		if (t.isClass() && e.isClass())
		{
			TypeClassInstance parent = (TypeClassInstance) t;
			TypeClassInstance child = (TypeClassInstance) e;
			if (TypeClass.isSubTypeOf(child.cls , parent.cls)) { return true; }
		}

		/***************************************************/
		/* [4] Check for New Array						   */
		/***************************************************/
		if (t.isArray() && e.isArray()) {
			TypeArrayInstance parent = (TypeArrayInstance) t;
			TypeArrayInstance child = (TypeArrayInstance) e;
			if (child.arr.name.equals("$NEW") && child.arr.type == parent.arr.type) { return true; }
		}

		return false;
	}
}