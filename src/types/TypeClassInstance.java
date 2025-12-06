package types;

public class TypeClassInstance extends Type {
    public TypeClass cls;

    public TypeClassInstance(TypeClass cls) {
        this.cls = cls;
        this.name = cls.name;
    }
    
	@Override
	public boolean isClass() { return true; }
}
