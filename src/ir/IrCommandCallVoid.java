/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;
import types.TypeFunction;

public class IrCommandCallVoid extends IrCommand
{
	public TypeFunction function;
    public TempList args;
	
	public IrCommandCallVoid(TypeFunction function, TempList args)
	{
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandCallVoid");
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}
}
