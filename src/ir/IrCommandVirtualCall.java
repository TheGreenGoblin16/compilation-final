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
	Temp dst;
	Temp inst;
	String functionName;
    TempList args;
	
	public IrCommandVirtualCall(Temp dst, Temp inst, String functionName, TempList args)
	{
		this.dst = dst;
		this.inst = inst;
		this.functionName = functionName;
        this.args = args;
	}
}
