package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstStmtCall extends AstStmt
{
    public AstCallExp e;

    /*******************/
    /* CONSTRUCTOR(S) */
    /*******************/
    public AstStmtCall(AstCallExp e , int lineNumber)
    {
        super(lineNumber);
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
    public Type semantMe()
    {
        /********************************************/
        /* [1] Semant the Call Expression           */
        /* The logic to verify the function exists, */
        /* check arguments, and check signatures    */
        /* belongs in AstCallExp.semantMe().        */
        /********************************************/
        if (e != null)
        {
            e.semantMe();
        }

        /********************************************/
        /* [2] Return Null (Statements have no type)*/
        /* Note: It is legal to call a non-void     */
        /* function as a statement and ignore the   */
        /* return value (e.g. List.pop()).          */
        /********************************************/
        return null;
    }

    public Temp irMe() {
        AstVar var = e.var;
	    String functionName = e.name;
	    AstExpList args = e.args;

        TempList argTemps = null;

        if (args != null) {
            argTemps = args.irMe();
        }

		if (e.IsMethodCall && var != null) {
			Temp t = var.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandVirtualCallVoid(t, functionName, argTemps));
		}
        else if (e.IsMethodCall && var == null){
            Temp ths = TempFactory.getInstance().getFreshTemp();
            Ir.getInstance().AddIrCommand(new IrCommandGetThis(ths));
            Ir.getInstance().AddIrCommand(new IrCommandVirtualCallVoid(ths, functionName, argTemps));
        }
        else {
			Ir.getInstance().AddIrCommand(new IrCommandCallVoid(functionName, argTemps));
		}

        return null;
    }
}