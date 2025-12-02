package ast;

import types.*;
import symboltable.*;

public class AstExpList extends AstNode
{
	public AstExp exp;
    public AstExpList next;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpList(AstExp exp, AstExpList next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
        this.next = next;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST EXP LIST */
		/*******************************/
		System.out.format("AST NODE EXP LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (exp != null) exp.printMe();
		if (next != null) next.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("EXP LIST\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
        if (next != null) AstGraphviz.getInstance().logEdge(serialNumber,next.serialNumber);
	}
	public Type semantMe()
	{
		if (exp != null) exp.semantMe();
		if (next != null) next.semantMe();

		return TypeList.getInstance();
	}
}
