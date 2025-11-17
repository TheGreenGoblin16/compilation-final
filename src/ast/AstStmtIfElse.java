package ast;

public class AstStmtIfElse extends AstStmt
{
    public AstExp cond;
    public AstStmtList body;
    public AstStmtList elsebody;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtIfElse(AstExp cond, AstStmtList body, AstStmtList elsebody)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> IF ( exp ) { stmtList } ELSE { stmtList }\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.cond = cond;
        this.body = body;
        this.elsebody = elsebody;
    }

    /**************************************************/
    /* The printing message for an if-else statement AST node */
    /**************************************************/
    public void printMe()
    {
        /******************************************/
        /* AST NODE TYPE = AST IF-ELSE STATEMENT */
        /******************************************/
        System.out.print("AST NODE IF-ELSE STMT\n");

        /**********************************************/
        /* RECURSIVELY PRINT COND, BODY, ELSEBODY ... */
        /**********************************************/
        if (cond != null) cond.printMe();
        if (body != null) body.printMe();
        if (elsebody != null) elsebody.printMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "IF-ELSE\n(cond)\n{...}\nELSE\n{...}\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber, cond.serialNumber);
        if (body != null) AstGraphviz.getInstance().logEdge(serialNumber, body.serialNumber);
        if (elsebody != null) AstGraphviz.getInstance().logEdge(serialNumber, elsebody.serialNumber);
    }
}