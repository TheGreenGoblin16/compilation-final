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
		TypeFunction t;

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
		// We want to already push the function itself for recursion purposes
		t = new TypeFunction(returnType, name, null);
		SymbolTable.getInstance().enter(name, t);
		SymbolTable.getInstance().beginScope();

		// MR KOREN please keep this line in the semantMe() right after opening new scope!
		SymbolTable.getInstance().enter("$RETURN-TYPE", returnType);

		/************************************/
		/* [2] Semant and push input params */
		/************************************/
		t.paramsTypes = params.semantMe();

		/*************************/
		/* [3] Handle overriding */
		/*************************/
		

		/*******************/
		/* [4] Semant body */
		/*******************/
		body.semantMe();

		/*****************/
		/* [5] End scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/************************************************************/
		/* [6] Return value is irrelevant for function declarations */
		/************************************************************/
		return null;		
	}
}
