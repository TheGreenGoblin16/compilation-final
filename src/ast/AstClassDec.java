package ast;

import types.*;
import symboltable.*;

public class AstClassDec extends AstDec
{
    String name;
    String parent;
	AstDecList body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstClassDec(String name, String parent, AstDecList body, int lineNumber)
	{
		super(lineNumber);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.parent = parent;
		this.body = body;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("CLASS DEC = %s\n",name);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (body != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("CLASS\n%s",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}
	public Type semantMe()
	{	
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		TypeClass t = new TypeClass(parent, name, null);
		SymbolTable.getInstance().enter(name,t);

		SymbolTable.getInstance().beginScope();

		SymbolTable.getInstance().enter("$CURRENT-CLASS", t);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.semantMe();

		//MR KOREN please keep this line in the semantMe() right after opening new scope!
		SymbolTable.getInstance().enter("$CURRENT-CLASS", t);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(name,t);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
}
