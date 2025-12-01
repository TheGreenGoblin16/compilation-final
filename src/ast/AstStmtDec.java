package ast;
import types.*;
import symboltable.*;
public class AstStmtDec extends AstStmt
{
    public AstVarDec d;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtDec(AstVarDec d)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        serialNumber = AstNodeSerialNumber.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== stmt -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.d = d;
    }

    /***************************************************/
    /* The printing message for a var dec statement AST node */
    /***************************************************/
    public void printMe()
    {
        /********************************************/
        /* AST NODE TYPE = AST VAR DEC STATEMENT */
        /********************************************/
        System.out.print("AST NODE VAR DEC STMT\n");

        /*************************************/
        /* RECURSIVELY PRINT VAR DEC ... */
        /*************************************/
        if (d != null) d.printMe();

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AstGraphviz.getInstance().logNode(
                serialNumber,
                "VAR DEC\nSTMT\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (d != null) AstGraphviz.getInstance().logEdge(serialNumber, d.serialNumber);
    }
    public Type semantMe()
    {
        /********************************************/
        /* [1] Semant the Variable Declaration      */
        /* logic for checking types, uniqueness,    */
        /* and void-checks resides in AstVarDec     */
        /********************************************/
        if (d != null)
        {
            d.semantMe();
        }

        return null;
    }
}