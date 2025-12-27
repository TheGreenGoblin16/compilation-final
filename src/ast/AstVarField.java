package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstVarField extends AstVar
{
	public AstVar var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarField(AstVar var, String fieldName , int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void printMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.printMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
	}

	public Type semantMe()
	{
		Type t = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.semantMe();

		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		TypeClass tc = null;
        
        if (t instanceof TypeClass) {
            tc = (TypeClass) t;
        } else if (t instanceof TypeClassInstance) {
            tc = ((TypeClassInstance)t).cls;
        } else {
            System.out.format(">> ERROR [%d] access field of a non-class variable\n", lineNumber);
            abort();
        }

		/**************************************************************/
		/* [3] Look for fieldName inside tc, or its superclasses      */
		/**************************************************************/
		while (tc != null)
		{
			for (TypedIdentifierList it = tc.dataMembers; it != null; it = it.tail)
			{
				if (it.head.name.equals(fieldName))
				{
					// FIX: Ensure the member is NOT a method
					if (it.head.type instanceof TypeFunction)
					{
						System.out.format(">> ERROR [ %d ] member %s is a method, not a field\n",lineNumber,fieldName);
						abort();
					}
					return it.head.type;
				}
			}
			tc = tc.parent;
		}

		/*********************************************/
		/* [4] fieldName does not exist in hierarchy */
		/*********************************************/
		System.out.format(">> ERROR [%d] field %s does not exist in class %s\n",lineNumber,fieldName, t.name);
		abort();
		return null;
	}

	public Temp irMe(){
		Temp dst = TempFactory.getInstance().getFreshTemp();
		Temp src = var.irMe();
		Ir.getInstance().AddIrCommand(new IrCommandFieldAccess(dst,src,fieldName));
		return dst;
	}
}
