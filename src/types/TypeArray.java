package types;

public class TypeArray extends Type
{
    public Type type; // The element type (e.g., int or string)

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public TypeArray(Type type)
    {
        this.name = type.name + "[]";
        this.type = type;
    }

    @Override
    public boolean isArray() { return true; }
}