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

public class IrCommandArraySet extends IrCommand
{
	Temp src;
	Temp arr;
	Temp index;
	
	public IrCommandArraySet(Temp src, Temp arr, Temp index)
	{
		this.src = src;
		this.arr = arr;
		this.index = index;
	}
}
