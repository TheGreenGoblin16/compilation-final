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

public class IrCommandFieldAccess extends IrCommand
{
	Temp dst;
	Temp inst;
    String fieldName;
	
	public IrCommandFieldAccess(Temp dst, Temp inst, String fieldName)
	{
        this.dst = dst;
		this.inst = inst;
		this.fieldName = fieldName;
	}
}
