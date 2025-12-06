package ast;

import types.*;
import symboltable.*;

public class AstVarDec extends AstDec
{
    public String typeName;
    public String name;
	public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarDec(String typeName, String name, AstExp exp, int lineNumber)
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
		if (exp != null) System.out.format("VAR-DEC(%s):%s := initialValue\n",name,typeName);
		if (exp == null) System.out.format("VAR-DEC(%s):%s                \n",name,typeName);


		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (exp != null) exp.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,typeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}
	public Type semantMe()
	{
		Type t = SymbolTable.getInstance().find(typeName);
		Type e = null;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		if (t == null)
		{
			System.out.format(">> ERROR [%d] type %s not found\n", lineNumber, typeName);
			abort();
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
