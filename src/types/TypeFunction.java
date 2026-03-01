package types;

public class TypeFunction extends Type
{
	/***********************************/
	/* the return type of the function */
	/***********************************/
	public Type returnType;

	/*************************/
	/* the types of the input params */
	/*************************/
	public TypeList paramsTypes;

	/**********************************************/
	/* index of the function in the virtual table */
	/**********************************************/
	public int functionIndex = -1;

	/************************/
	/* declaration counters */
	/************************/
	public int paramCounter = 0;
	public int localVarCounter = 0;

	/********************************************************/
	/* the label which we will need to jump to when calling */
	/********************************************************/
	public String labelProlog;
	public String labelEpilog;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeFunction(Type returnType, String name, TypeList paramsTypes)
	{
		this.name = name;
		this.returnType = returnType;
		this.paramsTypes = paramsTypes;
	}

	public static boolean signaturesEqual(TypeFunction tf1, TypeFunction tf2)
	{
		if (tf1 == null || tf2 == null) return false;

		if (tf1.returnType != tf2.returnType) return false;

		TypeList tl1 = tf1.paramsTypes;
		TypeList tl2 = tf2.paramsTypes;
		while (tl1 != null && tl2 != null) {
			if (tl1.head != tl2.head) return false;
			tl1 = tl1.tail;
			tl2 = tl2.tail;
		}
		return tl1 == null && tl2 == null;
	}
}