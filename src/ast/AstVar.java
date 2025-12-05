package ast;
import types.*;

public abstract class AstVar extends AstNode
{
    public AstVar(int lineNumber)
    {
        super(lineNumber);
    }

    public abstract Type semantMe();
}