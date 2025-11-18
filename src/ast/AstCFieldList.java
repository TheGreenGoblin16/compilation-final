package ast;

public class AstCFieldList extends AstNode
{
	public AstCField cfield;
    public AstCFieldList next;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCFieldList(AstCField cfield, AstCFieldList next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== cFieldList -> cField cFieldList\n");
		if (next == null) System.out.print("====================== cFieldList -> cField     \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.cfield = cfield;
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
		System.out.format("AST NODE CFIELD LIST\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CFIELD\nLIST\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cfield != null) AstGraphviz.getInstance().logEdge(serialNumber,cfield.serialNumber);
        if (next != null) AstGraphviz.getInstance().logEdge(serialNumber,next.serialNumber);
	}
}
