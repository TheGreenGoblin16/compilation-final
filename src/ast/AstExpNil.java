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
		return TypeVoid.getInstance();
	}

	public Temp irMe(Temp newTemp)
	{
		// ignore newTemp
		Temp t = TempFactory.getInstance().getFreshTemp();
		Ir.getInstance().AddIrCommand(new IrCommandConstInt(t, 0));
		return t;
	}
}
