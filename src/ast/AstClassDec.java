package ast;

import types.*;
import symboltable.*;

public class AstClassDec extends AstDec
{
    String name;
    String parent;
	AstDecList body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstClassDec(String name, String parent, AstDecList body, int lineNumber)
	{
		super(lineNumber);

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.parent = parent;
		this.body = body;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("CLASS DEC = %s\n",name);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (body != null) body.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (name != null) AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("CLASS\n%s",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}
	public Type semantMe()
    {   
        TypeClass parentType = null;

        /*******************************************************/
        /* [1] Resolve Parent Class (Inheritance)              */
        /* Rule: A class can extend only previously defined    */
        /* classes.                                  */
        /*******************************************************/
        if (parent != null) {
            Type t = SymbolTable.getInstance().find(parent);
            
            // Check 1: Does the parent exist?
            if (t == null) {
                System.out.format(">> ERROR [%d] parent class %s not found\n", lineNumber, parent);
                abort();
            }
            
            // Check 2: Is the parent actually a class?
            if (!(t instanceof TypeClass)) {
                System.out.format(">> ERROR [%d] parent %s is not a class\n", lineNumber, parent);
                abort();
            }

            parentType = (TypeClass) t;
        }

        /*******************************************************/
        /* [2] Create the TypeClass Object                     */
        /* We pass 'null' for members because the children     */
        /* (body) will add themselves to this object later.    */
        /*******************************************************/
        TypeClass myClass = new TypeClass(parentType, name, null);

        /*******************************************************/
        /* [3] Enter Class into Global Scope                   */
        /* Must be done BEFORE body so methods can refer to    */
        /* the class type (e.g., return type).                 */
        /*******************************************************/
        if (SymbolTable.getInstance().find(name) != null) {
            System.out.format(">> ERROR [%d] class %s already exists\n", lineNumber, name);
            abort();
        }
        SymbolTable.getInstance().enter(name, myClass);

        /*******************************************************/
        /* [4] Begin Class Scope                               */
        /*******************************************************/
        SymbolTable.getInstance().beginScope();

        // Register the current class so children can find it and add themselves
        // Note: Using "$CURRENT-CLASS" as a consistent key
        SymbolTable.getInstance().enter("$CURRENT-CLASS", myClass);

        /*******************************************************/
        /* [5] Process Body (Children add themselves)          */
        /*******************************************************/
        if (body != null) {
            body.semantMe();
        }

        /*******************************************************/
        /* [6] End Scope                                       */
        /*******************************************************/
        SymbolTable.getInstance().endScope();

        return null;        
    }
}
