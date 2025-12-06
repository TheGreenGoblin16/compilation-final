package ast;

import types.*;
import symboltable.*;

public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body , int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		this.cond = cond;
		this.body = body;
	}

	/**************************************************/
	/* The printing message for an if statement AST node */
	/**************************************************/
	public void printMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST IF STATEMENT */
		/**************************************/
		System.out.print("AST NODE IF STMT\n");

		/*************************************/
		/* RECURSIVELY PRINT COND + BODY ... */
		/*************************************/
		if (cond != null) cond.printMe();
		if (body != null) body.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
				"IF\n(cond)\n{\n ... \n}\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
	}

	public Type semantMe()
	{
		/********************************************/
		/* [1] Check Condition Type                 */
		/* Rule: The type of the condition inside   */
		/* if/while statements is primitive int.    */
		/********************************************/
		if (cond != null)
		{
			Type tCond = cond.semantMe();
			if (tCond != TypeInt.getInstance())
			{
				System.out.format(">> ERROR [ %d ] condition inside if statement must be of type int\n",lineNumber);
				abort();
			}
		}

		/********************************************/
		/* [2] Begin Block Scope                    */
		/* Rule: if, else and while create scopes   */
		/********************************************/
		SymbolTable.getInstance().beginScope();

		/********************************************/
		/* [3] Semant Body                          */
		/********************************************/
		if (body != null) body.semantMe();

		/********************************************/
		/* [4] End Block Scope                      */
		/********************************************/
		SymbolTable.getInstance().endScope();

		return null;
	}
}