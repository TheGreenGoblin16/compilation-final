package ast;
import types.*;

public abstract class AstStmt extends AstNode
{
    public AstStmt(int lineNumber)
    {
        super(lineNumber);
    }

    public Type semantMe()
    {
        return null;
    }

    public Temp irMe()
    {
        return null;
    }
}