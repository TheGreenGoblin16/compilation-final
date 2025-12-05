package ast;

import types.*;
import symboltable.*;

public class AstExpString extends AstExp
{
	public String str;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpString(String str, int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STRING( %s )\n", str);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.str = str;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STRING EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",str);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("STRING(%s)",str.replace('"','\'')));
	}

	public Type semantMe()
	{
		return TypeString.getInstance();
	}
}
