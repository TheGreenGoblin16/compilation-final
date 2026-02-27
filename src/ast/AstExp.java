package ast;

import types.*;
import ir.*;
import temp.*;

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

	public Temp irMe(Temp newTemp) {
		return null;
	}

	public Temp irMe() {
		return irMe(null);
	}
}