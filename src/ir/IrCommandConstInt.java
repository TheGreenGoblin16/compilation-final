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

public class IrCommandConstInt extends IrCommand
{
	public Temp t;
	public int value;
	
	public IrCommandConstInt(Temp t, int value)
	{
		this.t = t;
		this.value = value;
	}

	public void printMe() {
		System.out.println("IrCommandConstInt");
		System.out.println("t: " + t);
		System.out.println("value: " + value);
	}

	public void mipsMe(){}
}
