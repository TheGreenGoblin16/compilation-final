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

    public Temp irMe(Temp newTemp) {
        return null;
    }

    public Temp irMe() {
        return irMe(null);
    }
}