package types;

public class TypeArray extends Type
{
    public Type type; // The element type (e.g., int or string)

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public TypeArray(Type type, String name)
    {
        this.type = type;
        this.name = name;
    }

    @Override
    public boolean isArray() { return true; }
}