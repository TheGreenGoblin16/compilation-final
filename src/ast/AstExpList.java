package ast;

public class AstExpList extends AstNode
{
	public AstExp exp;
    public AstExpList next;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpList(AstExp exp, AstExpList next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== expList -> exp expList\n");
		if (next == null) System.out.print("====================== expList -> exp     \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
        this.next = next;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST EXP LIST */
		/*******************************/
		System.out.format("AST NODE EXP LIST\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("EXP LIST"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
        if (next != null) AstGraphviz.getInstance().logEdge(serialNumber,next.serialNumber);
	}
}
