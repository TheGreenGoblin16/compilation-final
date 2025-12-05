package types;

public class TypeClassInstance extends Type {
    public TypeClass cls;

    public TypeClassInstance(TypeClass cls) {
        this.cls = cls;
        this.name = cls.name;
    }
    
	@Override
	public boolean isClass() { return true; }

    public boolean isSubTypeOf(TypeClassInstance other) {
        TypeClass current = this.cls;
        while (current != null) {
            if (current.name.equals(other.cls.name)) {
                return true;
            }
            current = current.parent;
        }
        return false;
    }
}
