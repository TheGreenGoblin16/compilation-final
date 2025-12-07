package ast;

import types.*;
import symboltable.*;

public class AstNewExp extends AstExp
{
	public String typeName;
    public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExp(String typeName, AstExp exp, int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
		this.typeName = typeName;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NEW EXPRESSION */
		/*******************************/
		System.out.format("AST NODE NEW EXPRESSION (%s)\n", typeName);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"NEW\nEXPRESSION\n"
		);
        
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}
	public Type semantMe()
	{
		// 1. Resolve the Type from the Symbol Table
		Type t = validateTypeName(typeName);

		// 2. Handle Array Allocation: new int[size]
		if (exp != null)
		{
			// Check array size is an integer
			Type sizeType = exp.semantMe();
			if (sizeType != TypeInt.getInstance())
			{
				System.out.format(">> ERROR [%d] array size must be an integer\n", lineNumber);
				abort();
			}

			// Check for negative constant size (e.g., new int[-5])
			if (exp instanceof AstExpInt) {
				if (((AstExpInt)exp).value <= 0) {
					System.out.format(">> ERROR [%d] array size must be greater than zero\n", lineNumber);
					abort();
				}
			}

			// Return a new Array Instance of this type
			// Note: If t is "int", we return "int[]"
			return new TypeArray(t, "$NEW").getInstance();
		}

		// 3. Handle Class Allocation: new MyClass
		if (!t.isClass())
		{
			System.out.format(">> ERROR [ %d ] cannot instantiate non-class type %s\n", lineNumber, t.name);
			abort();
		}

		// Return the class instance
		return ((TypeClassInstance)t);
	}
}
