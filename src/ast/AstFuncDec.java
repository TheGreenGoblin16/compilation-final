package ast;

import types.*;
import symboltable.*;

public class AstFuncDec extends AstDec
{
    public String typeName;
    public String name;
	public AstParamList params;
	public AstStmtList body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncDec(String typeName, String name, AstParamList params, AstStmtList body, int lineNumber)
	{
		super(lineNumber);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
        this.typeName = typeName;
		this.name = name;
		this.params = params;
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
		System.out.format("FUNC(%s):%s\n",name,typeName);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (params != null) params.printMe();
		if (body != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("FUNC(%s):%s\n",name,typeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AstGraphviz.getInstance().logEdge(serialNumber,params.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}


	public Type semantMe()
	{
		Type returnType;
		TypeList paramsTypes;
		Type t;

		/*******************/
		/* [0] Get return type */
		/*******************/
		returnType = SymbolTable.getInstance().find(typeName);
		if (returnType == null)
		{
			abort();			
		}
	
		/****************************/
		/* [1] Begin function scope */
		/****************************/
		SymbolTable.getInstance().beginScope();

		// MR KOREN please keep this line in the semantMe() right after opening new scope!
		SymbolTable.getInstance().enter("$RETURN-TYPE", returnType);

		/************************************/
		/* [2] Semant and push input params */
		/************************************/
		paramsTypes = params.semantMe();

		/*************************/
		/* [3] Handle overriding */
		/*************************/
		t = new TypeFunction(returnType, name, paramsTypes)
		// WIP

		/*******************/
		/* [4] Semant body */
		/*******************/
		// We want to already insert the function itself for recursion
		SymbolTable.getInstance().enter(name, t);
		body.semantMe();

		/*****************/
		/* [5] End scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/***************************************************/
		/* [6] Enter the function type to the symbol table */
		/***************************************************/
		SymbolTable.getInstance().enter(name, t);

		/************************************************************/
		/* [7] Return value is irrelevant for function declarations */
		/************************************************************/
		return null;		
	}
}
