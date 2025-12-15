package ast;

import types.*;

public class AstExpBinop extends AstExp
{
	public int op;
	public AstExp left;
	public AstExp right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpBinop(AstExp left, AstExp right, int op, int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{
		String sop="";
		
		/*********************************/
		/* CONVERT op to a printable sop */
		/*********************************/
		if (op == 0) {sop = "+";}
		if (op == 1) {sop = "-";}
		if (op == 2) {sop = "*";}
		if (op == 3) {sop = "/";}
		if (op == 4) {sop = "<";}
		if (op == 5) {sop = ">";}
		if (op == 6) {sop = "=";}
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");
		System.out.format("BINOP EXP(%s)\n",sop);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.printMe();
		if (right != null) right.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("BINOP(%s)",sop));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AstGraphviz.getInstance().logEdge(serialNumber,left.serialNumber);
		if (right != null) AstGraphviz.getInstance().logEdge(serialNumber,right.serialNumber);
	}

	public Type semantMe()
	{
		Type t1 = left.semantMe();
		Type t2 = right.semantMe();

		// Check for operations on integers
		if (t1 == TypeInt.getInstance() && t2 == TypeInt.getInstance())
		{
			// Ops: +, -, *, /, <, >, =
			if (op == 3) { // Division
				if (right instanceof AstExpInt) {
					AstExpInt intExp = (AstExpInt) right;
					if (intExp.value == 0) {
						System.out.format(">> ERROR [ %d ] division by zero\n", lineNumber);
						abort();
					}
				}
			}
			return TypeInt.getInstance();
		}

		// Check for operations on strings
		if (t1 == TypeString.getInstance() && t2 == TypeString.getInstance())
		{
			// Ops: +, =
			if (op == 0) { // Concatenation
				return TypeString.getInstance();
			}
			if (op == 6) { // Equality
				return TypeInt.getInstance();
			}
		}

		// Check for equality operation on complex types
		if (op == 6) { // Equality
			if (t1.isClass() && t2.isClass()) {
				TypeClassInstance c1 = (TypeClassInstance) t1;
				TypeClassInstance c2 = (TypeClassInstance) t2;
				if (TypeClass.isSubTypeOf(c1.cls, c2.cls) || TypeClass.isSubTypeOf(c2.cls, c1.cls)) {
					return TypeInt.getInstance();
				}
			}
			if (t1.isClass() && t2 == TypeVoid.getInstance()) { // Comparing object with nil
				return TypeInt.getInstance();
			}
			if (t2.isClass() && t1 == TypeVoid.getInstance()) { // Comparing nil with object
				return TypeInt.getInstance();
			}
			if (t1.isArray() && (t2.isArray() || t2 == TypeVoid.getInstance())) { // Comparing two arrays
				return TypeInt.getInstance();
			}
			if (t2.isArray() && (t1.isArray() || t1 == TypeVoid.getInstance())) { // Comparing two arrays
				return TypeInt.getInstance();
			}
		}

		System.out.format(">> ERROR [ %d ] type mismatch for binary operation\n", lineNumber);
		abort();
		return null; // Should not be reached
	}
}
