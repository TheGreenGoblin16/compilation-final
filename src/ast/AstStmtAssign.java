package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstStmtAssign extends AstStmt
{
	/***************/
	/*  var := exp */
	/***************/
	public AstVar var;
	public AstExp exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtAssign(AstVar var, AstExp exp , int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void printMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.printMe();
		if (exp != null) exp.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}

	public Type semantMe()
	{
		Type t1 = null;
		Type t2 = null;

		/********************************************/
		/* [1] Semant the Variable (LHS)            */
		/********************************************/
		if (var != null) t1 = var.semantMe();

		/********************************************/
		/* [2] Semant the Expression (RHS)          */
		/********************************************/
		if (exp != null) t2 = exp.semantMe();

		/********************************************/
		/* [3] Check for valid inputs               */
		/********************************************/
		if (t1 == null || t2 == null)
		{
			// Error would have been printed in the child nodes
			abort();
		}

		if (Type.isMatchingTypeOf(t2, t1)) {
			return null;
		}

		/***************************************************/
		/* [4] Type Mismatch Error                         */
		/***************************************************/
		System.out.format(">> ERROR [ %d ] type mismatch: cannot assign value of type %s to variable of type %s\n",lineNumber, t2.name, t1.name);
		abort();

		return null;
	}

	public Temp irMe()
	{
		Temp src = exp.irMe();

		if (var instanceof AstVarSimple){
			Ir.getInstance().AddIrCommand(new IrCommandWriteVar(((AstVarSimple) var).entry, src));
		}
		else if (var instanceof AstVarSubscript){
			Temp arr = ((AstVarSubscript) var).var.irMe();
			Temp index = ((AstVarSubscript) var).subscript.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandArraySet(src, arr, index));
		}
		else if (var instanceof AstVarField){
			Temp arr = ((AstVarField) var).var.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandFieldSet(src, arr , ((AstVarField)var).fieldName ));
		}

		return null;
	}

}
