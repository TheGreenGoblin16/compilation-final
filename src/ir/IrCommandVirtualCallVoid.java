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
	public Temp inst;
	public String functionName;
    public TempList args;
	
	public IrCommandVirtualCallVoid(Temp inst, String functionName, TempList args)
	{
		this.inst = inst;
		this.functionName = functionName;
        this.args = args;
	}
}
