package ast;

public class AstParam extends AstNode
{
    String typeName;
    String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstParam(String typeName, String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = typeName;
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
		System.out.print("AST PARAM\n");
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("PARAM\n(%s)\n", name));
	}

	public Type semantMe() {
		// Validate that typeName exists
		Type t = validateTypeName(typeName);

		// Validate that name isn't a previous parameter
		if (SymbolTable.getInstance().findLocal(name) != null) {
			abort();
		}

		// Add parameter as a local variable
		SymbolTable.getInstance().enter(name, t);
		return t;
	}
}
