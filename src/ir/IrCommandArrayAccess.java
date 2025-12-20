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

public class IrCommandArrayAccess extends IrCommand
{
	Temp dst;
	Temp arr;
	Temp index;
	
	public IrCommandArrayAccess(Temp dst, Temp arr, Temp index)
	{
		this.dst = dst;
		this.arr = arr;
		this.index = index;
	}
}
