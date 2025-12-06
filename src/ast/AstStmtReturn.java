package ast;

import types.*;
import symboltable.*;
public class AstStmtReturn extends AstStmt
{
    public AstExp e;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtReturn(AstExp e , int lineNumber)
    {
        super(lineNumber);
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
        expectedType = SymbolTable.getInstance().find("$RETURN-TYPE");

        // Sanity check: This should theoretically never happen if parser works
        if (expectedType == null)
        {
            // return statement outside of a function?
            System.out.format(">> ERROR [%d:%d] return statement outside of function scope\n", 0,0);
            abort();
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
                abort();
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
            abort();
        }

        /******************************************************/
        /* [5] Check Rule: Exact Type Match                   */
        /******************************************************/
        Type t1 = expectedType;
        Type t2 = actualType;

        if (t1 instanceof TypeClassInstance) t1 = ((TypeClassInstance)t1).cls;
        if (t2 instanceof TypeClassInstance) t2 = ((TypeClassInstance)t2).cls;
        
        if (t1 instanceof TypeArrayInstance) t1 = ((TypeArrayInstance)t1).arr;
        if (t2 instanceof TypeArrayInstance) t2 = ((TypeArrayInstance)t2).arr;

        // 1. Exact Match
        if (t1 == t2) return null;

        /******************************************************/
        /* [6] Check Rule: Nil Assignment                     */
        /* nil is compatible with Arrays and Classes          */
        /******************************************************/
        // Note: In your AST, AstExpNil returns TypeVoid.
        if (t2 == TypeVoid.getInstance()) {
            if (t1 instanceof TypeClass || t1 instanceof TypeArray) return null;
            System.out.format(">> ERROR [%d] cannot return nil for return type %s\n", lineNumber, t1.name);
            abort();
        }

        /******************************************************/
        /* [7] Check Rule: Inheritance (Subclassing)          */
        /* Allowed to return Son if function expects Father   */
        /******************************************************/
        if (t1 instanceof TypeClass && t2 instanceof TypeClass) {
            TypeClass parent = (TypeClass) t1;
            TypeClass child = (TypeClass) t2;
            TypeClass temp = child.parent;
            while (temp != null) {
                if (temp == parent) return null;
                temp = temp.parent;
            }
        }

        //arrey name match
        if (t1 instanceof TypeArray && t2 instanceof TypeArray) {
             if (t1.name.equals(t2.name)) return null;
        }

        /******************************************************/
        /* [8] Mismatch Error                                 */
        /******************************************************/
        System.out.format(">> ERROR [%d:%d] return type mismatch: expected %s, got %s\n", 0,0, expectedType.name, actualType.name);
        abort();

        return null;
    }
}
