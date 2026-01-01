package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstVarDec extends AstDec
{
    public String typeName;
    public String name;
	public AstExp exp;
	public SymbolTableEntry entry;
	
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
		if (exp != null) {
			e = exp.semantMe();
			if (!Type.isMatchingTypeOf(e, t)) {
				System.out.format(">> ERROR [ %d ] type mismatch: cannot assign value of type %s to variable of type %s\n",lineNumber, e.name, t.name);
				abort();
			}
		}

		/************************************************/
		/* [4] Enter the identifier to the symbol table */
		/************************************************/
		entry = SymbolTable.getInstance().enter(name, t);

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

	public void irMe() {
        if (exp != null) {
            Temp src = exp.irMe();
            Ir.getInstance().AddIrCommand(new IrCommandWriteVar(entry, src));
        }
	}
}
