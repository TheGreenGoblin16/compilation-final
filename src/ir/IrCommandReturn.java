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

public class IrCommandReturn extends IrCommand
{
	public Temp src;
	
	public IrCommandReturn(Temp src)
	{
		this.src = src;
	}

	public void printMe() {
		System.out.println("IrCommandReturn");
		System.out.println("src: " + src);
	}

	public void mipsMe(){}
}
