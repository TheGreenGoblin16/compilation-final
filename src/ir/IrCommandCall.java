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
}
