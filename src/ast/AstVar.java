package ast;
import types.*;

import ir.*;
import temp.*;

public abstract class AstVar extends AstNode
{
    public AstVar(int lineNumber)
    {
        super(lineNumber);
    }

    public abstract Type semantMe();

    public abstract Temp irMe();
}