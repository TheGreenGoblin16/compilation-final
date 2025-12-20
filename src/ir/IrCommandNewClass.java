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
	Temp dst;
	String typeName;
	
	public IrCommandNewClass(Temp dst, String typeName)
	{
		this.dst = dst;
		this.typeName = typeName;
	}
}
