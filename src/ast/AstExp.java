package ast;

import types.*;

public abstract class AstExp extends AstNode
{
	public AstExp(int lineNumber)
	{
		super(lineNumber);
	}

    public Type semantMe()
	{
		return null;
	}
}