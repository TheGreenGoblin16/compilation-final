package ast;

import types.*;
import symboltable.*;

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
        Type t = SymbolTable.getInstance().find(name);

        /******************************************************/
        /* [1] If found in Symbol Table, return it            */
        /* (Covers Locals, Params, Current Fields, Globals)   */
        /******************************************************/
        if (t != null)
        {
            return t;
        }

        /******************************************************/
        /* [2] Check Class Hierarchy (Inheritance)            */
        /* If not found in stack, check Parent classes        */
        /******************************************************/
        // 1. Get the current class context we are in
        Type currentClassType = SymbolTable.getInstance().find("$CURRENT_CLASS");
        
        // 2. If we are inside a class, walk up the hierarchy
        if (currentClassType != null && currentClassType instanceof TypeClass)
        {
            TypeClass cls = (TypeClass) currentClassType;
            
            // Start checking from the current class (or parent)
            // Since find(name) failed, it wasn't in the current class scope 
            // defined in the symbol table, so we check the hierarchy object directly.
            while (cls != null)
            {
                // Iterate over data members (fields/methods)
                // NOTE: dataMembers is a TypeList based on your TypeClass.java
                for (TypedIdentifierList it = cls.dataMembers; it != null; it = it.tail)
                {
                    if (it.head.name.equals(name))
                    {
                        // [CRITICAL CHECK] Ensure it is a FIELD, not a METHOD
                        if (it.head.type instanceof TypeFunction)
                        {
                            System.out.format(">> ERROR [ %d ] member %s is a method, not a field\n", lineNumber, name);
                            abort();
                        }
                        return it.head.type;
                    }
                }
                // Move to parent
                cls = cls.parent;
            }
        }

        /******************************************************/
        /* [3] Not found anywhere                             */
        /******************************************************/
        System.out.format(">> ERROR [ %d ] variable %s not found in scope\n", lineNumber, name);
        abort();
        return null;
    }
}