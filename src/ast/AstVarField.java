package ast;

import types.*;
import symboltable.*;

public class AstVarField extends AstVar
{
	public AstVar var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarField(AstVar var, String fieldName)
	{
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
		if (t == null || t.isClass() == false)
		{
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",0,0,fieldName);
			System.exit(0);
		}

		// Cast to TypeClass to access fields and hierarchy
		TypeClass tc = (TypeClass) t;

		/**************************************************************/
		/* [3] Look for fieldName inside tc, or its superclasses      */
		/**************************************************************/
		while (tc != null)
		{
			// Iterate over the data members of the current class in the hierarchy
			for (TypeList it = tc.dataMembers; it != null; it = it.tail)
			{
				// Check if the member name matches the requested fieldName
				if (it.head.name.equals(fieldName))
				{
					return it.head;
				}
			}

			// If not found in this class, move up to the father class
			tc = tc.parent;
		}

		/*********************************************/
		/* [4] fieldName does not exist in hierarchy */
		/*********************************************/
		System.out.format(">> ERROR [%d:%d] field %s does not exist in class %s or its superclasses\n",0,0,fieldName, t.name);
		System.exit(0);
		return null;
	}
}
