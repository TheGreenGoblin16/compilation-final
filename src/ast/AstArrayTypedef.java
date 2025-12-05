package ast;

import types.*;
import symboltable.*;

public class AstArrayTypedef extends AstDec
{
    String typeName;
    AstType elementType;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstArrayTypedef(String typeName, AstType elementType)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.typeName = typeName;
        this.elementType = elementType;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE ARRAY TYPEDEF\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (elementType != null) elementType.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"ARRAY\nTYPEDEF\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (elementType != null) AstGraphviz.getInstance().logEdge(serialNumber,elementType.serialNumber);
	}
	public Type semantMe()
	{
		Type t;
	
		/****************************/
		/* [1] Check if type exists */
		/****************************/
		t = SymbolTable.getInstance().find(elementType.name);
		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,elementType.name);
			System.exit(0);
		}

		/**************************************/
		/* [2] Check that typeName does NOT exist */
		/**************************************/
		if (SymbolTable.getInstance().find(typeName) != null)
		{
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,typeName);				
		}

		/************************************************/
		/* [3] Enter the identifier to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(typeName,t);

		/************************************************************/
		/* [4] Return value is irrelevant for declarations 			*/
		/************************************************************/
		return null;
	}
}
