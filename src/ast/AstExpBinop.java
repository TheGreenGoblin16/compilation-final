package ast;

import types.*;
import ir.*;
import temp.*;

public class AstExpBinop extends AstExp
{
	public int op;
	public AstExp left;
	public AstExp right;
	public Type leftType;
	public Type rightType;
	
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
		this.leftType = left.semantMe();
		this.rightType = right.semantMe();

		// Check for operations on integers
		if (this.leftType == TypeInt.getInstance() && this.rightType == TypeInt.getInstance())
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
		if (this.leftType == TypeString.getInstance() && this.rightType == TypeString.getInstance())
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
			if (this.leftType.isClass() && this.rightType.isClass()) {
				TypeClassInstance c1 = (TypeClassInstance) this.leftType;
				TypeClassInstance c2 = (TypeClassInstance) this.rightType;
				if (TypeClass.isSubTypeOf(c1.cls, c2.cls) || TypeClass.isSubTypeOf(c2.cls, c1.cls)) {
					return TypeInt.getInstance();
				}
			}
			if (this.leftType.isClass() && this.rightType == TypeVoid.getInstance()) { // Comparing object with nil
				return TypeInt.getInstance();
			}
			if (this.rightType.isClass() && this.leftType == TypeVoid.getInstance()) { // Comparing nil with object
				return TypeInt.getInstance();
			}
			if (this.leftType.isArray() || this.rightType.isArray()) {
				if (this.leftType == this.rightType){
					return TypeInt.getInstance();
				}
				else {
					System.out.format(">> ERROR [ %d ] type mismatch for binary operation\n", lineNumber);
					abort();
				}
			}
		}

		System.out.format(">> ERROR [ %d ] type mismatch for binary operation\n", lineNumber);
		abort();
		return null; // Should not be reached
	}

	public Temp irMe()
	{
		Temp t1 = null;
		Temp t2 = null;
		Temp dst = TempFactory.getInstance().getFreshTemp();

		if (left  != null) t1 = left.irMe();
		if (right != null) t2 = right.irMe();

		if (this.leftType == TypeString.getInstance() && this.rightType == TypeString.getInstance()){
			if (op == 0) {
				Ir.getInstance().AddIrCommand(new IrCommandBinopAddStrings(dst,t1,t2));
			} else if (op == 6) {
				String label = IrCommand.getFreshLabel("end");
				Ir.getInstance().AddIrCommand(new IrCommandConstInt(dst, 1));
				Ir.getInstance().AddIrCommand(new IrCommandBranchIfEqualsStrings(t1,t2,label));
				Ir.getInstance().AddIrCommand(new IrCommandConstInt(dst, 0));
				Ir.getInstance().AddIrCommand(new IrCommandLabel(label));
			}
			return dst;
		}
		
		if (op == 0)
		{
			Ir.getInstance().AddIrCommand(new IrCommandBinopAddIntegers(dst,t1,t2));
		} else if (op == 1)
		{
			Ir.getInstance().AddIrCommand(new IrCommandBinopSubIntegers(dst,t1,t2));
		} else if (op == 2)
		{
			Ir.getInstance().AddIrCommand(new IrCommandBinopMulIntegers(dst,t1,t2));
		} else if (op == 3)
		{
			Ir.getInstance().AddIrCommand(new IrCommandBinopDivIntegers(dst,t1,t2));
		} else {
			String label = IrCommand.getFreshLabel("end");
			Ir.getInstance().AddIrCommand(new IrCommandConstInt(dst, 1));
			if (op == 4)
			{
				Ir.getInstance().AddIrCommand(new IrCommandBranchIfLess(t1,t2, label));
			} else if (op == 5)
			{
				Ir.getInstance().AddIrCommand(new IrCommandBranchIfLess(t2,t1, label));
			} else if (op == 6)
			{
				Ir.getInstance().AddIrCommand(new IrCommandBranchIfEquals(t1,t2, label));
			}
			Ir.getInstance().AddIrCommand(new IrCommandConstInt(dst, 0));
			Ir.getInstance().AddIrCommand(new IrCommandLabel(label));
		}
		return dst;
	}
}
