package ast;

import types.*;
import symboltable.*;
import ir.*;
import temp.*;

public class AstExpList extends AstNode
{
	public AstExp exp;
    public AstExpList next;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpList(AstExp exp, AstExpList next, int lineNumber)
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
	public TypeList semantMe()
	{
		if (exp == null) {
			return null;
		}
        Type headType = exp.semantMe();
        TypeList tailTypes = null;
        if (next != null) {
            tailTypes = next.semantMe();
        }
		return new TypeList(headType, tailTypes);
	}

	public TempList irMe()
	{
		if (exp == null) {
			return null;
		}
		Temp headTemp = exp.irMe();
		TempList tailTemps = null;
		if (next != null) {
			tailTemps = next.irMe();
		}
		return new TempList(headTemp, tailTemps);
	}
}
