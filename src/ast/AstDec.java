package ast;
import types.*;

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
