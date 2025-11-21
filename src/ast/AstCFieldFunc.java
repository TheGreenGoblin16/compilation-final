package ast;

public class AstCFieldFunc extends AstCField
{
    public AstFuncDec funcdec;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
    public AstCFieldFunc(AstFuncDec funcdec) {
        /******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cField -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.funcdec = funcdec;
    }

    /***************************************************/
	/* The printing message for a while statement AST node */
	/***************************************************/
	public void printMe()
	{
		/****************************************/
		/* AST NODE TYPE = AST DECLARE VAR */
		/****************************************/
		System.out.print("AST NODE CFIELD FUNC\n");

		/*************************************/
		/* RECURSIVELY PRINT VARDEC ... */
		/*************************************/
		if (funcdec != null) funcdec.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			"CFIELD\nFUNC\n");

		/****************************************/ 
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/ 
		if (funcdec != null) AstGraphviz.getInstance().logEdge(serialNumber, funcdec.serialNumber);
	}
}