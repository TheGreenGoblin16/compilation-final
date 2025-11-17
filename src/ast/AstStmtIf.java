package ast;

public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		this.cond = cond;
		this.body = body;
	}

	/**************************************************/
	/* The printing message for an if statement AST node */
	/**************************************************/
	public void printMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST IF STATEMENT */
		/**************************************/
		System.out.print("AST NODE IF STMT\n");

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
				"IF\n(cond)\n{\n ... \n}\n");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
	}
}