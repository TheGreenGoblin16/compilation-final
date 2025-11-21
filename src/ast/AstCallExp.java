package ast;

public class AstCallExp extends AstNode
{
	public AstVar var;
	public String name;
	public AstArgList l;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCallExp(AstVar var, String name, AstArgList l)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (l != null && var != null) System.out.format("====================== Callexp -> var ( %s ).ID( %s ) [ expList ](%s) \n", var, name, l);
		if (l == null && var != null) System.out.format("====================== Callexp -> var ( %s ).ID( %s ) ( ) \n", var, name);
		if (l != null && var == null) System.out.format("====================== Callexp -> ID( %s ) [ expList ](%s) \n", name, l);
		if (l == null && var == null) System.out.format("====================== Callexp -> ID( %s ) ( ) \n", name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.l = l;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CALL EXP */
		/*******************************/
		System.out.format("AST NODE CALL EXP\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CALL EXP"));
	}
}
