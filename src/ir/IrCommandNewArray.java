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
	public Temp dst;
	public Temp size;
	
	public IrCommandNewArray(Temp dst, Temp size)
	{
		this.dst = dst;
		this.size = size;
	}

	public void printMe() {
		System.out.println("IrCommandNewArray");
		System.out.println("dst: " + dst);
		System.out.println("size: " + size);
	}

	public void mipsMe(){}
}
