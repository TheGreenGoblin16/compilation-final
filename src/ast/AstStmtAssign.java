package ast;

import types.*;
import symboltable.*;

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

		/***************************************************/
		/* [4] Check for Exact Type Match                  */
		/* Covers: int, string, exact array, exact class   */
		/***************************************************/
		if (t1 == t2)
		{
			return null;
		}

		/***************************************************/
		/* [5] Check for Nil Assignment                    */
		/* Rule: nil is allowed for Arrays and Classes     */
		/* Rule: nil is ILLEGAL for int and string         */
		/***************************************************/
		if (t2 == TypeVoid.getInstance())
		{
			if (t1.isClass() || t1.isArray())
			{
				return null;
			}
			System.out.format(">> ERROR [%d:%d] cannot assign nil to variable of type %s\n",0,0, t1.name);
			abort();
		}

		/***************************************************/
		/* [6] Check for Class Inheritance (Polymorphism)  */
		/* Rule: RHS can be a subclass of LHS              */
		/***************************************************/
		if (t1.isClass() && t2.isClass())
		{
			TypeClassInstance t1Inst = (TypeClassInstance) t1;
			TypeClassInstance t2Inst = (TypeClassInstance) t2;
			if (t2Inst.isSubTypeOf(t1Inst)) return null;
		}


		// 7.5 Array Name Equivalence Match
		// Arrays are name-equivalent. If t1 is "IntArray" and t2 is "IntArray", they are compatible.
		if (t1.isArray() && t2.isArray())
		{
			if (t1.name.equals(t2.name)) return null;
		}
		/***************************************************/
		/* [7] Type Mismatch Error                         */
		/***************************************************/
		System.out.format(">> ERROR [%d:%d] type mismatch: cannot assign value of type %s to variable of type %s\n",0,0, t2.name, t1.name);
		abort();

		return null;
	}
}
