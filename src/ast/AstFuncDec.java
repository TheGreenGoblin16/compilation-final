package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstFuncDec extends AstDec
{
    public String typeName;
    public String name;
	public AstParamList params;
	public AstStmtList body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncDec(String typeName, String name, AstParamList params, AstStmtList body, int lineNumber)
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
		this.params = params;
		this.body = body;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("FUNC(%s):%s\n",name,typeName);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (params != null) params.printMe();
		if (body != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("FUNC(%s):%s\n",name,typeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AstGraphviz.getInstance().logEdge(serialNumber,params.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}


	public Type semantMe()
	{
		Type returnType;
		TypeList paramsTypes = null;
		TypeFunction thisFunc;
		TypeClass currentClass = (TypeClass) SymbolTable.getInstance().find("$CURRENT-CLASS");

		/***********************************/
		/* [0] Get return and params types */
		/***********************************/
		if (typeName.equals("void")) {
			returnType = TypeVoid.getInstance();
		} else {
			returnType = validateTypeName(typeName);
		}

		if (params != null) {
			paramsTypes = params.semantMe(false);
		}

		thisFunc = new TypeFunction(returnType, name, paramsTypes);

		/**************************************/
		/* [1] Check for previous appearances */
		/**************************************/
		if (currentClass == null) { // Is in global scope
			if (SymbolTable.getInstance().find(name) != null) {
				// We can't allow a function with the same name in global scope
                System.out.format(">> ERROR [%d] found a previous member with the same name in global scope\n", lineNumber);
				abort();
			}
		} else { // Is in class scope
			TypeClass tc = currentClass;
			boolean isProper = false;
			while (tc != null) { // Move upwards the hierarchy
				for (TypedIdentifierList it = tc.dataMembers; it != null; it = it.tail) { // Traverse data members
					if (it.head.name.equals(name)) {
						if (it.head.type instanceof TypeFunction && isProper) { // Is a function in a proper superlcass
							TypeFunction otherFunc = (TypeFunction) it.head.type;
							if (!TypeFunction.signaturesEqual(thisFunc, otherFunc)) {
                            	System.out.format(">> ERROR [%d] found a previous member with the same name and not the same signature in hierarchy\n", lineNumber);
								abort();
							}
						} else {
                            System.out.format(">> ERROR [%d] found a previous member with the same name in hierarchy\n", lineNumber);
							abort();
						}
					}
				}
				tc = tc.parent;
				isProper = true; // Now we enter proper superclasses
			}
		}
	
		/****************************/
		/* [2] Begin function scope */
		/****************************/
		// We want to already push the function so it can call itself
		SymbolTable.getInstance().enter(name, thisFunc);

		SymbolTable.getInstance().beginScope();

		SymbolTable.getInstance().enter("$RETURN-TYPE", returnType);

		/*******************/
		/* [3] Push params */
		/*******************/
		if (params != null) {
			params.semantMe(true);
		}

		/*******************/
		/* [4] Semant body */
		/*******************/
		body.semantMe();

		/*****************/
		/* [5] End scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/******************************************************/
		/* [6] Add the identifier to currentClass.dataMembers */
		/******************************************************/
		if (currentClass != null) {
			TypedIdentifierList til = currentClass.dataMembers;
			currentClass.dataMembers = new TypedIdentifierList(
				new TypedIdentifier(thisFunc, name), til);
		}

		/************************************************************/
		/* [7] Return value is irrelevant for function declarations */
		/************************************************************/
		return null;		
	}
}
