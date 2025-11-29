package ast;

public class AstType extends AstNode
{
    public String typeName;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstType(String typeName, int kind)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.typeName = typeName;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("TYPE: %s\n", typeName);
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("TYPE\n(%s)\n", typeName));
	}

	public Type semantMe()
	{
		return null;
	}
}
