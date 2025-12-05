package types;

public class TypeArray extends Type
{
    public Type type; // The element type (e.g., int or string)

    private TypeArrayInstance instance;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public TypeArray(Type type, String name)
    {
        this.type = type;
        this.name = name;
    }


    public static TypeArrayInstance getInstance()
	{
		if (instance == null)
		{
			instance = new TypeArrayInstance(this);
		}
		return instance;
	}
}