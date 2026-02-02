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

public class IrCommandVirtualCallVoid extends IrCommand
{
	public Temp inst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandVirtualCallVoid(Temp inst, TypeFunction function, TempList args)
	{
		this.inst = inst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandVirtualCallVoid");
		System.out.println("inst: " + inst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}
}
