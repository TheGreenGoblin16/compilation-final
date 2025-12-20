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

public class IrCommandNewArray extends IrCommand
{
	Temp dst;
	Temp size;
	
	public IrCommandNewArray(Temp dst, Temp size)
	{
		this.dst = dst;
		this.size = size;
	}
}
