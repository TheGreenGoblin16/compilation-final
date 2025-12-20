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
	String functionName;
    TempList args;
	
	public IrCommandCallVoid(String functionName, TempList args)
	{
		this.functionName = functionName;
        this.args = args;
	}
}
