package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript , int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (subscript != null) subscript.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber,subscript.serialNumber);
	}

	public Type semantMe()
	{
		Type tVar = null;
		Type tSub = null;

		/******************************************/
		/* [1] Recursively semant the array var   */
		/******************************************/
		if (var != null) tVar = var.semantMe();

		/************************************************/
		/* [2] Ensure the variable is of type Array     */
		/************************************************/
		TypeArray arrType = null;

        if (tVar instanceof TypeArray) {
            arrType = (TypeArray) tVar;
        } else if (tVar instanceof TypeArrayInstance) {
            arrType = ((TypeArrayInstance)tVar).arr;
        } else {
            System.out.format(">> ERROR [%d] subscript applied to non-array type\n", lineNumber);
            abort();
        }

		/************************************************/
		/* [3] Recursively semant the subscript index   */
		/************************************************/
		if (subscript != null) tSub = subscript.semantMe();

		/************************************************/
		/* [4] Ensure the subscript is an Integer       */
		/************************************************/
		if (tSub != TypeInt.getInstance())
		{
			System.out.format(">> ERROR [%d] array subscript must be an integer\n",lineNumber);
			abort();
		}

		// 4.5 CHECK FOR NEGATIVE CONSTANT INDEX (e.g., arr[-1])
		if (subscript instanceof AstExpInt) {
			int val = ((AstExpInt)subscript).value;
			if (val < 0) {
				System.out.format(">> ERROR [%d] array subscript must be non-negative\n", lineNumber);
				abort();
			}
		}

		/************************************************/
		/* [5] Return the element type of the Array     */
		/* FIX: Cast to TypeArray                       */
		/************************************************/
		if (tVar instanceof TypeArrayInstance)
		{
			return ((TypeArrayInstance)tVar).arr.type;
		}

		return null;
	}


	public Temp irMe(){
		Temp dst = TempFactory.getInstance().getFreshTemp();
		Temp arr = var.irMe();
		Temp index = subscript.irMe();
		Ir.getInstance().AddIrCommand(new IrCommandArrayAccess(dst,arr,index));
		return dst;
	}
}
