package ast;

import types.*;
import symboltable.*;

public class AstVarDec extends AstDec
{
    public AstType type;
    public String name;
	public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarDec(AstType type, String name, AstExp exp)
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
		this.exp = exp;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		if (exp != null) System.out.format("VAR-DEC(%s):%s := initialValue\n",name,type.toString());
		if (exp == null) System.out.format("VAR-DEC(%s):%s                \n",name,type.toString());


		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (type != null) type.printMe();
		if (exp != null) exp.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,type.typeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}
	public Type semantMe()
	{
		Type t = SymbolTable.getInstance().find(type.typeName);
		Type e = null;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type.typeName);
			System.exit(0);
		}

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SymbolTable.getInstance().find(name) != null)
		{
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,name);
			System.exit(0);
		}

		/********************************************/
		/* [3] Semant the Initial Value (if any)    */
		/********************************************/
		if (exp != null)
		{
			e = exp.semantMe();

			// Validation Logic (Same as AstStmtAssign)
			boolean valid = false;

			// A. Exact Match
			if (t == e) valid = true;

			// B. Nil (allowed for Class/Array)
			if (!valid && e == TypeVoid.getInstance())
			{
				if (t.isClass() || t.isArray()) valid = true;
			}

			// C. Inheritance (Subclassing)
			if (!valid && t.isClass() && e.isClass())
			{
				TypeClass parent = (TypeClass)t;
				TypeClass child = (TypeClass)e;
				TypeClass temp = child.parent;
				while (temp != null)
				{
					if (temp == parent) { valid = true; break; }
					temp = temp.parent;
				}
			}

			if (!valid)
			{
				System.out.format(">> ERROR [%d:%d] type mismatch: cannot assign %s to %s\n",2,2,e.name,t.name);
				System.exit(0);
			}
		}

		/************************************************/
		/* [4] Enter the Identifier to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(name,t);

		return null;
	}
}
