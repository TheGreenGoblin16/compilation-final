package ast;

public class AstArgList extends AstNode
{
	public AstArg arg;
    public AstArgList next;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstArgList(AstArg arg, AstArgList next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== argList -> arg argList\n");
		if (next == null) System.out.print("====================== argList -> arg     \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.arg = arg;
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
		System.out.format("AST NODE ARG LIST\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARG\nLIST\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arg != null) AstGraphviz.getInstance().logEdge(serialNumber,arg.serialNumber);
        if (next != null) AstGraphviz.getInstance().logEdge(serialNumber,next.serialNumber);
	}
}
