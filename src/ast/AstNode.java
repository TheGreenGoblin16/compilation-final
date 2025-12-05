package ast;

import types.*;

public abstract class AstNode
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int serialNumber;
	public int lineNumber;

	public AstNode(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}
	
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void printMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public void abort()
	{
		// Throw exception to be caught by Main.java
		// Note: Using 0 as line number since AST doesn't have line info yet.
		// The output format matches the spec: ERROR(location)
		throw new RuntimeException("SEMANT_ERROR(" + lineNumber + ")");
	}
}
