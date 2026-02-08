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
	public int functionIndex = -1;
	public TypeFunction thisFunction;
	public TypeFunction overridingFunction = null;
	
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
		semantMe(-1);
		return null;
	}

	public int semantMe(int i)
	{
		Type returnType;
		TypeList paramsTypes = null;
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
			paramsTypes = params.semantMe();
		}

		thisFunction = new TypeFunction(returnType, name, paramsTypes);

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
							overridingFunction = (TypeFunction) it.head.type;
							if (!TypeFunction.signaturesEqual(thisFunction, overridingFunction)) {
                            	System.out.format(">> ERROR [%d] found a previous member with the same name and not the same signature in hierarchy\n", lineNumber);
								abort();
							}
							tc = null; // To break the outer loop
							break;
						} else {
                            System.out.format(">> ERROR [%d] found a previous member with the same name in hierarchy\n", lineNumber);
							abort();
						}
					}
				}
				if (tc != null) {
					tc = tc.parent;
					isProper = true; // Now we enter proper superclasses
				}
			}
		}
	
		/****************************/
		/* [2] Begin function scope */
		/****************************/
		// We want to already push the function so it can call itself
		SymbolTable.getInstance().enter(name, thisFunction);

		/******************************************************/
		/* [3] Add the identifier to currentClass.dataMembers */
		/******************************************************/

		if (currentClass != null) {
			TypedIdentifierList til = currentClass.dataMembers;
			currentClass.dataMembers = new TypedIdentifierList(
					new TypedIdentifier(thisFunction, name, null), til);
		}

		/*******************************************/
		/* [4] Begin scope and push auxiliary info */
		/*******************************************/

		SymbolTable.getInstance().beginScope();

		SymbolTable.getInstance().enter("$CURRENT-FUNCTION", thisFunction);

		SymbolTable.getInstance().enter("$RETURN-TYPE", returnType);

		/*******************/
		/* [5] Push params */
		/*******************/
		if (params != null) {
			params.semantMe(thisFunction);
		}

		/*******************/
		/* [6] Semant body */
		/*******************/
		body.semantMe();

		/*****************/
		/* [7] End scope */
		/*****************/
		SymbolTable.getInstance().endScope();
		
		/***********************************/
		/* [8] Store and return func index */
		/***********************************/
		functionIndex = (overridingFunction == null) ? i : overridingFunction.functionIndex;
		thisFunction.functionIndex = functionIndex;
		return (overridingFunction == null) ? i+1 : i;
	}

	public void irMe() {
		String functionLablelName = (functionIndex >= 0) ? ("Function_" + functionIndex + "_" + name) : ("Function_" + name);
		String labelFunction = IrCommand.getFreshLabel(functionLablelName);
		String labelEpilog = IrCommand.getFreshLabel("epilog_"+functionLablelName);
		
		Ir.getInstance().AddIrCommand(new IrCommandLabel(labelFunction));
		Ir.getInstance().AddIrCommand(new IrCommandProlog(thisFunction));
		body.irMe();
		Ir.getInstance().AddIrCommand(new IrCommandReturn(null));
		Ir.getInstance().AddIrCommand(new IrCommandLabel(labelEpilog));
		Ir.getInstance().AddIrCommand(new IrCommandEpilog(thisFunction));
		
	}
}
