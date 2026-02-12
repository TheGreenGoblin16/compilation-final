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
	public Temp dst;
	public Temp arr;
	public Temp index;
	
	public IrCommandArrayAccess(Temp dst, Temp arr, Temp index)
	{
		this.dst = dst;
		this.arr = arr;
		this.index = index;
	}

	public void printMe() {
		System.out.println("IrCommandArrayAccess");
		System.out.println("dst: " + dst);
		System.out.println("arr: " + arr);
		System.out.println("index: " + index);
	}

	public void mipsMe(){}
}
