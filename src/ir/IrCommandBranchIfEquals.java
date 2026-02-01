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

public class IrCommandBranchIfEquals extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public String labelName;
	
	public IrCommandBranchIfEquals(Temp t1, Temp t2, String labelName)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.labelName = labelName;
	}

	public void printMe() {
		System.out.println("IrCommandBranchIfEquals");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("labelName: " + labelName);
	}
}
