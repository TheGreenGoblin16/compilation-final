package ast;

import types.*;
import symboltable.*;
public class AstStmtReturn extends AstStmt
{
    public AstExp e;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtReturn(AstExp e)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (e != null)
        {
            System.out.print("====================== stmt -> RETURN exp SEMICOLON\n");
        }
        else
        {
            System.out.print("====================== stmt -> RETURN SEMICOLON\n");
        }

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.e = e;
    }

    /***************************************************/
    /* The printing message for a return statement AST node */
    /***************************************************/
    public void printMe()
    {
        /****************************************/
        /* AST NODE TYPE = AST RETURN STATEMENT */
        /****************************************/
        System.out.print("AST NODE RETURN STMT\n");

        /*************************************/
        /* RECURSIVELY PRINT EXP (if any) ... */
        /*************************************/
        if (e != null) e.printMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        String label = (e != null) ? "RETURN\nexp" : "RETURN\n(void)";
        AstGraphviz.getInstance().logNode(
                serialNumber,
                label);

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (e != null) AstGraphviz.getInstance().logEdge(serialNumber, e.serialNumber);
    }

    public Type semantMe()
    {
        Type actualType = null;
        Type expectedType = null;

        /******************************************************/
        /* [1] Get the Expected Return Type from Symbol Table */
        /* This was put here by AstFuncDec.semantMe()         */
        /******************************************************/
        expectedType = SymbolTable.getInstance().find("$RETURN_TYPE");

        // Sanity check: This should theoretically never happen if parser works
        if (expectedType == null)
        {
            // return statement outside of a function?
            System.out.format(">> ERROR [%d:%d] return statement outside of function scope\n", 0,0);
            System.exit(0);
        }

        /******************************************************/
        /* [2] Analyze the expression being returned (if any) */
        /******************************************************/
        if (e != null)
        {
            actualType = e.semantMe();
        }

        /******************************************************/
        /* [3] Check Rule: Void Functions                     */
        /* If function is void, return must be empty.         */
        /******************************************************/
        if (expectedType == TypeVoid.getInstance())
        {
            if (e != null)
            {
                System.out.format(">> ERROR [%d:%d] void function cannot return a value\n", 0,0);
                System.exit(0);
            }
            return null; // OK
        }

        /******************************************************/
        /* [4] Check Rule: Non-Void Functions                 */
        /* If function is NOT void, return must have value.   */
        /******************************************************/
        if (e == null)
        {
            System.out.format(">> ERROR [%d:%d] non-void function must return a value\n", 0,0);
            System.exit(0);
        }

        /******************************************************/
        /* [5] Check Rule: Exact Type Match                   */
        /******************************************************/
        if (actualType == expectedType)
        {
            return null; // OK
        }

        /******************************************************/
        /* [6] Check Rule: Nil Assignment                     */
        /* nil is compatible with Arrays and Classes          */
        /******************************************************/
        // Note: In your AST, AstExpNil returns TypeVoid.
        if (actualType == TypeVoid.getInstance())
        {
            if (expectedType.isClass() || expectedType.isArray())
            {
                return null; // OK
            }
            System.out.format(">> ERROR [%d:%d] cannot return nil for return type %s\n", 0,0, expectedType.name);
            System.exit(0);
        }

        /******************************************************/
        /* [7] Check Rule: Inheritance (Subclassing)          */
        /* Allowed to return Son if function expects Father   */
        /******************************************************/
        if (expectedType.isClass() && actualType.isClass())
        {
            TypeClass parentClass = (TypeClass) expectedType;
            TypeClass childClass  = (TypeClass) actualType;

            // Walk up the inheritance chain of the returned object
            TypeClass temp = childClass.parent;
            while (temp != null)
            {
                if (temp == parentClass)
                {
                    return null; // Match found
                }
                temp = temp.parent;
            }
        }

        /******************************************************/
        /* [8] Mismatch Error                                 */
        /******************************************************/
        System.out.format(">> ERROR [%d:%d] return type mismatch: expected %s, got %s\n", 0,0, expectedType.name, actualType.name);
        System.exit(0);

        return null;
    }
}
