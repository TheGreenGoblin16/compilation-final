package ast;

public class AstType extends AstNode
{
    String typeName;
    int kind; // A synonym of type...?

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstType(String typeName, int kind)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (kind == 0) System.out.print("====================== type -> TYPE_INT\n");
		if (kind == 1) System.out.print("====================== type -> TYPE_STRING\n");
		if (kind == 2) System.out.print("====================== type -> TYPE_VOID\n");
		if (kind == 3) System.out.print("====================== type -> ID\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.typeName = typeName;
		this.kind = kind;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE TYPE\n");
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (kind == 0)  AstGraphviz.getInstance().logNode(
				            serialNumber,
			                "TYPE\n(int)\n");
		if (kind == 1)  AstGraphviz.getInstance().logNode(
				            serialNumber,
			                "TYPE\n(string)\n");
		if (kind == 2)  AstGraphviz.getInstance().logNode(
				            serialNumber,
			                "TYPE\n(void)\n");
		if (kind == 3)  AstGraphviz.getInstance().logNode(
				            serialNumber,
			            String.format("TYPE\n(%s)\n", typeName));
	}
}
