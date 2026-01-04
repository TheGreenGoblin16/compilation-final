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
	public Temp src;
	public Temp arr;
	public Temp index;
	
	public IrCommandArraySet(Temp src, Temp arr, Temp index)
	{
		this.src = src;
		this.arr = arr;
		this.index = index;
	}

	public void printMe() {
		System.out.println("IrCommandArraySet");
		System.out.println("src: " + src);
		System.out.println("arr: " + arr);
		System.out.println("index: " + index);
	}
}
