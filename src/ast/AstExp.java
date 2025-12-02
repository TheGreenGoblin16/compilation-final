package ast;

import types.*;
import symboltable.*;

public abstract class AstExp extends AstNode
{
    public Type semantMe()
	{
		return null;
	}
}