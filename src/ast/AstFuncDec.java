package ast;

import types.*;
import symboltable.*;

public class AstFuncDec extends AstDec
{
    public AstType type;
    public String name;
	public AstParamList paramlist;
	public AstStmtList body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncDec(AstType type, String name, AstParamList paramlist, AstStmtList body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
        this.type = type;
		this.name = name;
		this.paramlist = paramlist;
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
		System.out.format("FUNC(%s):%s\n",name,type.toString());

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (type != null) type.printMe();
		if (paramlist != null) paramlist.printMe();
		if (body != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("FUNC(%s):%s\n",name,type.typeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (paramlist != null) AstGraphviz.getInstance().logEdge(serialNumber,paramlist.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}


	public Type semantMe()
	{
		Type t;
		Type returnType = null;
		TypeList type_list = null;

		/*******************/
		/* [0] return type */
		/*******************/
		returnType = SymbolTable.getInstance().find(type.typeName);
		if (returnType == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,type.typeName);				
		}
	
		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SymbolTable.getInstance().beginScope();

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AstTypeNameList it = paramlist; it  != null; it = it.tail)
		{
			t = SymbolTable.getInstance().find(it.head.type);
			if (t == null)
			{
				System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,it.head.type.typeName);				
			}
			else
			{
				type_list = new TypeList(t,type_list);
				SymbolTable.getInstance().enter(it.head.name,t);
			}
		}

		/*******************/
		/* [3] Semant Body */
		/*******************/
		body.semantMe();

		/*****************/
		/* [4] End Scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		SymbolTable.getInstance().enter(name,new TypeFunction(returnType,name,type_list));

		/************************************************************/
		/* [6] Return value is irrelevant for function declarations */
		/************************************************************/
		return null;		
	}
}
