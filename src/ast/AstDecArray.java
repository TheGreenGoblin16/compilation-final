package ast;

public class AstDecArray extends AstDec
{
    public AstArrayTypedef arraydef;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
    public AstDecArray(AstArrayTypedef arraydef) {
        /******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> arrayTypedef\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.arraydef = arraydef;
    }

    /***************************************************/
	/* The printing message for a while statement AST node */
	/***************************************************/
	public void printMe()
	{
		/****************************************/
		/* AST NODE TYPE = AST DECLARE VAR */
		/****************************************/
		System.out.print("AST NODE DECLARE ARRAY\n");

		/*************************************/
		/* RECURSIVELY PRINT VARDEC ... */
		/*************************************/
		if (arraydef != null) arraydef.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"DECLARE\nARRAY\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arraydef != null) AstGraphviz.getInstance().logEdge(serialNumber, arraydef.serialNumber);
	}
}
