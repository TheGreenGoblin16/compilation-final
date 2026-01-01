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

public class IrCommandFieldSet extends IrCommand
{
	public Temp src;
	public Temp inst;
    public String fieldName;
	
	public IrCommandFieldSet(Temp src, Temp inst, String fieldName)
	{
        this.src = src;
		this.inst = inst;
		this.fieldName = fieldName;
	}
}
