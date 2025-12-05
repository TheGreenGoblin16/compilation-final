package types;

public class TypeArrayInstance extends Type {
    public TypeArray arr;

    public TypeArrayInstance(TypeArray arr) {
        this.arr = arr;
        this.name = arr.name;
    }

    @Override
    public boolean isArray() { return true; }
}
