package ast;

import types.*;
import symboltable.*;
import ir.*;
import temp.*;

public class AstExpNil extends AstExp
{
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpNil(int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> NIL\n");
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NIL EXP */
		/*******************************/
		System.out.format("AST NODE NIL\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("NIL"));
	}
	public Type semantMe()
	{
		// Type t = value.semantMe();
		// if (t != TypeVoid.getInstance())
		// {
		// 	System.out.format(">> ERROR [ %d ] type %s is not void\n", lineNumber, this.type);
		// 	abort();
		// }
		return TypeVoid.getInstance();
	}

	public Temp irMe()
	{
		Temp t = TempFactory.getInstance().getFreshTemp();
		return t;
	}
}
