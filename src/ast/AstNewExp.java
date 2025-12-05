package ast;

import types.*;
import symboltable.*;

public class AstNewExp extends AstExp
{
	public String type;
    public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExp(String type, AstExp exp, int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NEW EXPRESSION */
		/*******************************/
		else System.out.format("AST NODE NEW EXPRESSION (%s)\n", type);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"NEW\nEXPRESSION\n"
		);
        
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}
	public Type semantMe()
	{
		if (exp != null) exp.semantMe();

		symboltable.Type t = SymbolTable.getInstance().find(this.type);
		if (t == null)
		{
			System.out.format(">> ERROR [ %d ] type %s not found in scope\n", lineNumber, this.type);
			abort();
		}

		return null;
	}
}
