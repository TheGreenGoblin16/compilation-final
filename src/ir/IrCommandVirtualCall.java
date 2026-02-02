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

public class IrCommandVirtualCall extends IrCommand
{
	public Temp dst;
	public Temp inst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandVirtualCall(Temp dst, Temp inst, TypeFunction function, TempList args)
	{
		this.dst = dst;
		this.inst = inst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandVirtualCall");
		System.out.println("dst: " + dst);
		System.out.println("inst: " + inst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}
}
