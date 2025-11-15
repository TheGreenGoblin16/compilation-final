package ast;

public class AstNewExpression extends AstExp
{
	public String name;
    public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExpression(String name, AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (exp == null) System.out.format("====================== exp -> NEW %s ( )\n", name);
        else System.out.format("====================== exp -> NEW %s ( %s )\n", name, exp);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
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
			String.format("NEW %s", name));

        
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);  // have no idea if this is correct
	}
}
