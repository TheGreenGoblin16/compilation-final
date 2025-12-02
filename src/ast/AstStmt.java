package ast;

import types.*;
import symboltable.*;

public abstract class AstStmt extends AstNode
{
    public Type semantMe()
    {
        return null;
    }
}
