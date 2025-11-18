package ast;

public class AstDecClass extends AstDec
{
    public AstClassDec classdec;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
    public AstDecClass(AstClassDec classdec) {
        /******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> classDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.classdec = classdec;
    }

    /***************************************************/
	/* The printing message for a while statement AST node */
	/***************************************************/
	public void printMe()
	{
		/****************************************/
		/* AST NODE TYPE = AST DECLARE VAR */
		/****************************************/
		System.out.print("AST NODE DECLARE CLASS\n");

		/*************************************/
		/* RECURSIVELY PRINT VARDEC ... */
		/*************************************/
		if (classdec != null) classdec.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
				"DECLARE\nCLASS\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (classdec != null) AstGraphviz.getInstance().logEdge(serialNumber, classdec.serialNumber);
	}
}
