package ast;

public class AstExpCall extends AstExp
{
	public AstCallExpression callexp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpCall(AstCallExpression callexp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> callexp \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.callexp = callexp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST EXP CALL */
		/*******************************/
		System.out.format("AST NODE EXP CALL\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("EXP CALL"));
        
        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (callexp != null) AstGraphviz.getInstance().logEdge(serialNumber,callexp.serialNumber);
	}
}
