package ast;

public class AstExpParentheses extends AstExp
{
	public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpParentheses(AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> ( exp )\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST PARENTHESES EXP */
		/*******************************/
		System.out.format("AST NODE PARENTHESES\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("PARENTHESES"));
        
        
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);  // have no idea if this is correct
	}
}
