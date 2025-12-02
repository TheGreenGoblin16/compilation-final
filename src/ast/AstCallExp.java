package ast;

import types.*;
import symboltable.*;

public class AstCallExp extends AstExp
{
	public AstVar var;
	public String name;
	public AstExpList args;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCallExp(AstVar var, String name, AstExpList args)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.args = args;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CALL EXP */
		/*******************************/
		if (var != null) System.out.format("CALL var(%s).", var);
		else System.out.format("CALL ");
		System.out.format("ID(%s)\n WITH:\n", name);
		if (args != null) args.printMe();
		else System.out.format("NO ARGUMENTS\n");


		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("CALL %s\n", name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (args != null) AstGraphviz.getInstance().logEdge(serialNumber,args.serialNumber);
	}
}
