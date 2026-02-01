package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstParam extends AstNode
{
    String typeName;
    String name;
	SymbolTableEntry entry;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstParam(String typeName, String name, int lineNumber)
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
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST PARAM\n");
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("PARAM\n(%s)\n", name));
	}

	public Type semantMe() { // For just getting the types
		// Validate that typeName exists
		Type t = validateTypeName(typeName);


		return t;
	}

	public Type semantMe(TypeFunction thisFunc) { // For also adding them to the symbol table
		// Validate that typeName exists
		Type t = validateTypeName(typeName);

		// Validate that name isn't a previous parameter
		if (SymbolTable.getInstance().findLocal(name) != null) {
			System.out.format(">> ERROR [%d] parameter %s was already used\n", lineNumber, name);
			abort();
		}

		// Add parameter to the symbol table
		entry = SymbolTable.getInstance().enter(name, t);

		entry.kind = VariableKind.PARAMETER;
		entry.position = thisFunc.paramCounter;
		thisFunc.paramCounter++;

		return t;
	}
}
