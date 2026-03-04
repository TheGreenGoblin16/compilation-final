package ast;

import types.*;
import symboltable.*;
import ir.*;
import temp.*;

public class AstDecList extends AstNode
{
	public AstDec head;
    public AstDecList tail;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecList(AstDec head, AstDecList tail, int lineNumber)
	{
		super(lineNumber);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
        this.tail = tail;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST EXP LIST */
		/*******************************/
		System.out.format("AST NODE CFIELD LIST\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("DEC\nLIST\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
        if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}

	public Type semantMe() {
		for (AstDecList it = this; it != null; it = it.tail) {
			it.head.semantMe();
		}
		return null;
	}

	public void irMe() {
		Ir.getInstance().AddIrCommand(new IrCommandLabel("main"));
		for (AstDecList it = this; it != null; it = it.tail) {
			if (it.head instanceof AstVarDec) {
				AstVarDec dec = (AstVarDec) it.head;
				dec.irMe();
			}
		}
		
		TypeFunction mainFunction = null;
		for (AstDecList it = this; it != null; it = it.tail) {
			if (it.head instanceof AstFuncDec && ((AstFuncDec)it.head).name.equals("main")) {
				mainFunction = ((AstFuncDec)it.head).thisFunction;
			}
		}
		Ir.getInstance().AddIrCommand(new IrCommandCallVoid(mainFunction, null));
		Ir.getInstance().AddIrCommand(new IrCommandExitAsm());

		for (AstDecList it = this; it != null; it = it.tail) {
			if (it.head instanceof AstFuncDec) {
				AstFuncDec func = (AstFuncDec) it.head;
				func.irMe();
			} else if (it.head instanceof AstClassDec) {
				AstClassDec cls = (AstClassDec) it.head;
				cls.irMe();
			}
		}
	}
}
