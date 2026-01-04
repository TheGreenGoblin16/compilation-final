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

public class IrCommandCall extends IrCommand
{
	public Temp dst;
	public String functionName;
    public TempList args;
	
	public IrCommandCall(Temp dst, String functionName, TempList args)
	{
		this.dst = dst;
		this.functionName = functionName;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandCall");
		System.out.println("dst: " + dst);
		System.out.println("functionName: " + functionName);
		System.out.println("args: " + args);
	}
}
