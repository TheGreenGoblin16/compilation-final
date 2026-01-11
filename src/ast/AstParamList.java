package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstParamList extends AstNode
{
	public AstParam head;
    public AstParamList tail;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstParamList(AstParam head, AstParamList tail, int lineNumber)
	{
		super(lineNumber);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
        this.tail = tail;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST EXP LIST */
		/*******************************/
		System.out.format("AST NODE PARAM LIST\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("PARAM\nLIST\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}

	public TypeList semantMe() { // For just getting the types

		if (head == null) {
			return null;
		}

		Type t = head.semantMe();

		TypeList tl = null;
		if (tail != null) {
            tl = tail.semantMe();
        }

		return new TypeList(t, tl);
	}

	public TypeList semantMe(TypeFunction thisFunc) { // For also adding them to the symbol table
		if (head == null) {
			return null;
		}

		Type t = head.semantMe(thisFunc);

		TypeList tl = null;
		if (tail != null) {
            tl = tail.semantMe(thisFunc);
        }

		return new TypeList(t, tl);
	}
}
