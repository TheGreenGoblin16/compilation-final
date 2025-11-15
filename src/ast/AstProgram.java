package ast;

public class AstProgram extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstDec dec;
	public AstProgram next;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstProgram(AstDec dec, AstProgram next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== program -> dec program \n"); // dec {dec}
		if (next == null) System.out.print("====================== program -> dec     \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.dec = dec;
		this.next = next;
	}

	/******************************************************/
	/* The printing message for a program list AST node */
	/******************************************************/
	public void printMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST PROGRAM LIST */
		/**************************************/
		System.out.print("AST NODE PROGRAM\n");

		/*************************************/
		/* RECURSIVELY PRINT DEC + NEXT ... */
		/*************************************/
		dec.printMe();
		if (next != null) next.printMe();  // Note: we print the whole list of decs - not sure if needed

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"PROGRAM\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,dec.serialNumber);
		if (next != null) AstGraphviz.getInstance().logEdge(serialNumber,next.serialNumber);
    }
}