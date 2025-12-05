package ast;

import types.*;
import symboltable.*;

public class AstVarSimple extends AstVar
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSimple(String name , int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void printMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public Type semantMe()
	{
		/******************************************************/
		/* [1] Look up the name in the Symbol Table           */
		/* The SymbolTable handles scope resolution:      */
		/* Locals -> Params -> Class Fields -> Globals    */
		/******************************************************/
		Type t = SymbolTable.getInstance().find(name);

		/******************************************************/
		/* [2] If t is null, the variable is not defined      */
		/******************************************************/
		if (t == null)
		{
			// In a real implementation, we would pass line number to AstNode
			// to print "ERROR(line)". For now, we print a fatal error.
			System.out.format(">> ERROR [%d:%d] variable %s not found in scope\n",0,0,name);

			// We exit to ensure the Output file writes "ERROR" (handled by Main/Makefile wrapper usually)
			// or simply stop execution.
			abort();
		}

		/******************************************************/
		/* [3] Return the found type                          */
		/******************************************************/
		return t;
	}
}
