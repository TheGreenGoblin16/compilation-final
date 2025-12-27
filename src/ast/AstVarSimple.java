package ast;

import types.*;
import symboltable.*;

import ir.*;
import temp.*;

public class AstVarSimple extends AstVar
{
    public String name;
    
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AstVarSimple(String name, int lineNumber)
    {
        super(lineNumber);
        serialNumber = AstNodeSerialNumber.getFresh();
        this.name = name;
    }

    /**************************************************/
    /* The printing message for a simple var AST node */
    /**************************************************/
    public void printMe()
    {
        System.out.format("AST NODE SIMPLE VAR( %s )\n",name);
        AstGraphviz.getInstance().logNode(
                serialNumber,
                String.format("SIMPLE\nVAR\n(%s)",name));
    }

    public Type semantMe()
    {
        // 1. Check for Local Variable (Innermost Scope)
        // usage of findLocal ensures we catch function params and locals
        Type tLocal = SymbolTable.getInstance().findLocal(name);
        if (tLocal != null) {
            return tLocal;
        }

        // 2. Check for Class Field (Class Hierarchy)
        // This takes precedence over Globals
        Type currentClassType = SymbolTable.getInstance().find("$CURRENT-CLASS");
        if ((currentClassType != null) && (currentClassType instanceof TypeClass))
        {
            TypeClass cls = (TypeClass) currentClassType;
            while (cls != null)
            {
                for (TypedIdentifierList it = cls.dataMembers; it != null; it = it.tail)
                {
                    if (it.head.name.equals(name))
                    {
                        if (it.head.type instanceof TypeFunction) {
                            // Method names cannot be used as variables
                            System.out.format(">> ERROR [%d] member %s is a method, not a field\n", lineNumber, name);
                            abort();
                        }
                        return it.head.type;
                    }
                }
                cls = cls.parent;
            }
        }

        // 3. Check for Global Variable
        // We use find() here, but since we already checked local, if this returns anything
        // it must be global (or we missed it in step 1, but findLocal covers that).
        Type tGlobal = SymbolTable.getInstance().find(name);
        if (tGlobal != null) {
            return tGlobal;
        }

        // 4. Not Found
        System.out.format(">> ERROR [%d] variable %s not found in scope\n", lineNumber, name);
        abort();
        return null;
    }

    
    public Temp irMe()
	{
		Temp t = TempFactory.getInstance().getFreshTemp();
		Ir.getInstance().AddIrCommand(new IrCommandWriteVar(name, t));
		return t;
	}
}