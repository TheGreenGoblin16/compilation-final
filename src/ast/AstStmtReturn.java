package ast;

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
}
