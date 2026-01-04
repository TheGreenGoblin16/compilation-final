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

public class IrCommandVirtualCall extends IrCommand
{
	public Temp dst;
	public Temp inst;
	public String functionName;
    public TempList args;
	
	public IrCommandVirtualCall(Temp dst, Temp inst, String functionName, TempList args)
	{
		this.dst = dst;
		this.inst = inst;
		this.functionName = functionName;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandVirtualCall");
		System.out.println("dst: " + dst);
		System.out.println("inst: " + inst);
		System.out.println("functionName: " + functionName);
		System.out.println("args: " + args);
	}
}
