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

public class IrCommandCallVoid extends IrCommand
{
	public String functionName;
    public TempList args;
	
	public IrCommandCallVoid(String functionName, TempList args)
	{
		this.functionName = functionName;
        this.args = args;
	}
}
