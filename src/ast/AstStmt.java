package ast;
import types.*;

import ir.*;
import temp.*;

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