package ast;

public class AstDecVar extends AstDec
{
    public AstVarDec vardec;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
    public AstDecVar(AstVarDec vardec) {
        /******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.vardec = vardec;
    }

    /***************************************************/
	/* The printing message for a while statement AST node */
	/***************************************************/
	public void printMe()
	{
		/****************************************/
		/* AST NODE TYPE = AST DECLARE VAR */
		/****************************************/
		System.out.print("AST NODE DECLARE VAR\n");

		/*************************************/
		/* RECURSIVELY PRINT VARDEC ... */
		/*************************************/
		if (vardec != null) vardec.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"DECLARE\nVAR\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (vardec != null) AstGraphviz.getInstance().logEdge(serialNumber, vardec.serialNumber);
	}
}
