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
	Temp dst;
	String functionName;
    TempList args;
	
	public IrCommandCall(Temp dst, String functionName, TempList args)
	{
		this.dst = dst;
		this.functionName = functionName;
        this.args = args;
	}
}
