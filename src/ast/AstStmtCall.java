package ast;

public class AstStmtCall extends AstStmt
{
    public AstCallExp e;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtCall(AstCallExp e)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> callExp SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.e = e;
    }

    /***************************************************/
    /* The printing message for a call statement AST node */
    /***************************************************/
    public void printMe()
    {
        /***************************************/
        /* AST NODE TYPE = AST CALL STATEMENT */
        /***************************************/
        System.out.print("AST NODE CALL STMT\n");

        /*************************************/
        /* RECURSIVELY PRINT CALL EXP ... */
        /*************************************/
        if (e != null) e.printMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "CALL STMT\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (e != null) AstGraphviz.getInstance().logEdge(serialNumber, e.serialNumber);
    }
}