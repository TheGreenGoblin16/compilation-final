package ast;

public class AstNewExp extends AstExp
{
	public AstType t;
    public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExp(AstType t, AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (exp == null) System.out.println("====================== exp -> NEW type\n");
        else System.out.println("====================== NEW type LBRACK exp RBRACK\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.t = t;
		this.exp = exp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NEW EXPRESSION */
		/*******************************/
		System.out.format("AST NODE NEW EXPRESSION\n");

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
		if (t != null) AstGraphviz.getInstance().logEdge(serialNumber,t.serialNumber);  // have no idea if this is correct
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);  // have no idea if this is correct
	}
}
