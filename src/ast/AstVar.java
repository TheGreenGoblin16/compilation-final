package ast;

import types.*;

public abstract class AstVar extends AstNode
{
    /*********************************************************/
    /* The semantic analysis for variables returns their Type */
    /* (e.g. TypeInt, TypeString, TypeClass, TypeArray)      */
    /*********************************************************/
    public abstract Type semantMe();
}