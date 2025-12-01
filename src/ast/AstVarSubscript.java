package ast;
import types.*;
import symboltable.*;
public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (subscript != null) subscript.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber,subscript.serialNumber);
	}

	public Type semantMe()
	{
		Type tVar = null;
		Type tSub = null;

		/******************************************/
		/* [1] Recursively semant the array var   */
		/******************************************/
		if (var != null) tVar = var.semantMe();

		/************************************************/
		/* [2] Ensure the variable is of type Array     */
		/************************************************/
		// Note: tVar might be null if the variable wasn't declared,
		// but semantMe() usually exits on error before returning null.
		if (tVar == null || tVar.isArray() == false)
		{
			System.out.format(">> ERROR [%d:%d] subscript applied to non-array type\n",0,0);
			System.exit(0);
		}

		/************************************************/
		/* [3] Recursively semant the subscript index   */
		/************************************************/
		if (subscript != null) tSub = subscript.semantMe();

		/************************************************/
		/* [4] Ensure the subscript is an Integer       */
		/************************************************/
		if (tSub != TypeInt.getInstance())
		{
			System.out.format(">> ERROR [%d:%d] array subscript must be an integer\n",0,0);
			System.exit(0);
		}

		/**********************************************************/
		/* [5] Check for negative constant index (Sanity check)   */
		/**********************************************************/
		if (subscript instanceof AstExpInt)
		{
			int val = ((AstExpInt)subscript).value;
			if (val < 0)
			{
				System.out.format(">> ERROR [%d:%d] array subscript must be non-negative\n",0,0);
				System.exit(0);
			}
		}

		/************************************************/
		/* [6] Return the element type of the Array     */
		/************************************************/
		// We cast to TypeArray to access the inner element type.
		// Assumes TypeArray has a field 'type' holding the element type.
		return ((TypeList)tVar).type;
	}
}
