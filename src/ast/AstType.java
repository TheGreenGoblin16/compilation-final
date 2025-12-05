package ast;

public class AstType extends AstNode
{
    public String name;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstType(String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("TYPE: %s\n", name);
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("TYPE\n(%s)\n", name));
	}

	// No semantMe() method! AstType is only a container for typeName. 
}
