package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstStmtIfElse extends AstStmt
{
    public AstExp cond;
    public AstStmtList body;
    public AstStmtList elsebody;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtIfElse(AstExp cond, AstStmtList body, AstStmtList elsebody , int lineNumber)
    {
        super(lineNumber);
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
    public Type semantMe()
    {
        /********************************************/
        /* [1] Check Condition Type                 */
        /* Rule: The type of the condition inside   */
        /* if/while statements is primitive int.    */
        /********************************************/
        if (cond != null)
        {
            Type tCond = cond.semantMe();
            if (tCond != TypeInt.getInstance())
            {
                System.out.format(">> ERROR [ %d ] condition inside if statement must be of type int\n",lineNumber);
                abort();
            }
        }

        /********************************************/
        /* [2] Begin If-Block Scope                 */
        /********************************************/
        SymbolTable.getInstance().beginScope();

        /********************************************/
        /* [3] Semant If-Body                       */
        /********************************************/
        if (body != null) body.semantMe();

        /********************************************/
        /* [4] End If-Block Scope                   */
        /********************************************/
        SymbolTable.getInstance().endScope();

        /********************************************/
        /* [5] Begin Else-Block Scope               */
        /********************************************/
        SymbolTable.getInstance().beginScope();

        /********************************************/
        /* [6] Semant Else-Body                     */
        /********************************************/
        if (elsebody != null) elsebody.semantMe();

        /********************************************/
        /* [7] End Else-Block Scope                 */
        /********************************************/
        SymbolTable.getInstance().endScope();

        return null;
    }

    public Temp irMe()
	{
		String labelEnd   = IrCommand.getFreshLabel("end");
		String labelFalse = IrCommand.getFreshLabel("false");

		Temp condTemp = cond.irMe();

		Ir.getInstance().AddIrCommand(new IrCommandBranchIfZero(condTemp,labelFalse));

		body.irMe();

		Ir.getInstance().AddIrCommand(new IrCommandBranch(labelEnd));

        Ir.getInstance().AddIrCommand(new IrCommandLabel(labelFalse));

        elsebody.irMe();

		Ir.getInstance().AddIrCommand(new IrCommandLabel(labelEnd));

		return null;
	}
}