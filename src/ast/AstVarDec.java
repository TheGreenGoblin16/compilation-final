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
		Type t;
		Type e;
		TypeClass currentClass = (TypeClass) SymbolTable.getInstance().find("$CURRENT-CLASS");
		boolean isStmt = (SymbolTable.getInstance().find("$RETURN-TYPE") != null);

		/********************************/
		/* [1] Check If typeName exists */
		/********************************/
		t = validateTypeName(typeName);

		/**************************************/
		/* [2] Check for previous appearances */
		/**************************************/
		if (currentClass == null || isStmt) { // Is not a class field
			if (SymbolTable.getInstance().findLocal(name) != null) {
				// We can't allow a variable with the same name in local scope
				System.out.format(">> ERROR [%d] found a previous member with the same name in local scope\n", lineNumber);
				abort();
			}
		} else { // Is a class field
			TypeClass tc = currentClass;
			while (tc != null) { // Move upwards the hierarchy
				for (TypedIdentifierList it = tc.dataMembers; it != null; it = it.tail) { // Traverse data members
					if (it.head.name.equals(name)) {
						System.out.format(">> ERROR [%d] found a previous member with the same name in class hierarchy\n", lineNumber);
						abort();
					}
				}
				tc = tc.parent;
			}
		}

		/********************************************/
		/* [3] Semant the initial value (if any)    */
		/********************************************/
		if (exp != null)
		{
			e = exp.semantMe();

			// Validation Logic (Same as AstStmtAssign)
			boolean valid = false;

			// A. Exact match
			if (t == e) { valid = true; }

			// B. Nil (allowed for Class/Array)
			if (!valid && e == TypeVoid.getInstance())
			{
				if (t.isClass() || t.isArray()) { valid = true; }
			}

			// C. Inheritance (Subclassing)
			if (!valid && t.isClass() && e.isClass())
			{
				TypeClassInstance parent = (TypeClassInstance) t;
				TypeClassInstance child = (TypeClassInstance) e;
				if (TypeClass.isSubTypeOf(child.cls , parent.cls)) { valid = true; }
			}

			// D. New array 
			if (!valid && t.isArray() && e.isArray()) {
				TypeArrayInstance parent = (TypeArrayInstance) t;
				TypeArrayInstance child = (TypeArrayInstance) e;
				if (child.arr.name.equals("$NEW") && child.arr.type == parent.arr.type) { valid = true; }
			}

			if (!valid)
			{
				System.out.format(">> ERROR [%d] expression type does not match\n", lineNumber);
				abort();
			}
		}

		/************************************************/
		/* [4] Enter the identifier to the symbol table */
		/************************************************/
		SymbolTable.getInstance().enter(name, t);

		/******************************************************/
		/* [5] Add the identifier to currentClass.dataMembers */
		/******************************************************/
		if (currentClass != null && !isStmt) { // Is a class field
			TypedIdentifierList til = currentClass.dataMembers;
			currentClass.dataMembers = new TypedIdentifierList(
				new TypedIdentifier(t, name), til);
		}

		return null;
	}
}
