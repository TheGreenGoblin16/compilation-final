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

public class IrCommandBranchIfZero extends IrCommand
{
	public Temp t;
	public String labelName;
	
	public IrCommandBranchIfZero(Temp t, String labelName)
	{
		this.t = t;
		this.labelName = labelName;
	}

	public void printMe() {
		System.out.println("IrCommandBranchIfZero");
		System.out.println("t: " + t);
		System.out.println("labelName: " + labelName);
	}
}
