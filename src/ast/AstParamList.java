package ast;

import types.*;
import symboltable.*;

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

	public TypeList semantMe(boolean push) {

		if (head == null) {
			return null;
		}

		Type t = head.semantMe(push);

		TypeList tl = null;
		if (tail != null) {
            tl = tail.semantMe(push);
        }

		return new TypeList(t, tl);
	}
}
