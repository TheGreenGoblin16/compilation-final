package ast;

import types.*;
import symboltable.*;
import ir.*;
import temp.*;

public class AstArrayTypedef extends AstDec
{
    String typeName;
    String elementTypeName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstArrayTypedef(String typeName, String elementTypeName, int lineNumber)
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
        this.elementTypeName = elementTypeName;
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
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"ARRAY\nTYPEDEF\n");
	}
	public Type semantMe()
	{
		Type t;
	
		/****************************/
		/* [1] Check if type exists */
		/****************************/
		t = validateTypeName(elementTypeName);

		/**********************************/
		/* [2] Check that typeName exists */
		/**********************************/
		if (SymbolTable.getInstance().find(typeName) != null) {
			abort();
		}

		/************************************************/
		/* [3] Enter the identifier to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(typeName, new TypeArray(t, typeName));

		/************************************************************/
		/* [4] Return value is irrelevant for declarations 			*/
		/************************************************************/
		return null;
	}
}
