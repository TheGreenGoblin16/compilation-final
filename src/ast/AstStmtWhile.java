package ast;

public class AstStmtWhile extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtWhile(AstExp cond, AstStmtList body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE ( exp ) { stmtList }\n");


		this.cond = cond;
		this.body = body;
	}

	/***************************************************/
	/* The printing message for a while statement AST node */
	/***************************************************/
	public void printMe()
	{
		/****************************************/
		/* AST NODE TYPE = AST WHILE STATEMENT */
		/****************************************/
		System.out.print("AST NODE WHILE STMT\n");

		/*************************************/
		/* RECURSIVELY PRINT COND + BODY ... */
		/*************************************/
		if (cond != null) cond.printMe();
		if (body != null) body.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
				"WHILE\n(cond)\n{\n ... \n}\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
	}
}