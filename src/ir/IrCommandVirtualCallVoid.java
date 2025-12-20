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

public class IrCommandVirtualCallVoid extends IrCommand
{
	Temp inst;
	String functionName;
    TempList args;
	
	public IrCommandVirtualCallVoid(Temp inst, String functionName, TempList args)
	{
		this.inst = inst;
		this.functionName = functionName;
        this.args = args;
	}
}
