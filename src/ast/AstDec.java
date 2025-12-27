package ast;
import types.*;

import ir.*;
import temp.*;

public abstract class AstDec extends AstNode
{
	public AstDec(int lineNumber) {
		super(lineNumber);
	}

    /***********************************************/
	/* The default semantic action for an AST node */
	/***********************************************/
	public Type semantMe()
	{
		return null;
	}
}
