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
import types.*;

public class IrCommandCall extends IrCommand
{
	public Temp dst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandCall(Temp dst, TypeFunction function, TempList args)
	{
		this.dst = dst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandCall");
		System.out.println("dst: " + dst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}
}
