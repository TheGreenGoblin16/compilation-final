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

public class IrCommandNewClass extends IrCommand
{
	public Temp dst;
	public String typeName;
	
	public IrCommandNewClass(Temp dst, String typeName)
	{
		this.dst = dst;
		this.typeName = typeName;
	}
}
