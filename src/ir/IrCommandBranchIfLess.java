/***********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandBranchIfLess extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public String labelName;
	
	public IrCommandBranchIfLess(Temp t1, Temp t2, String labelName)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.labelName = labelName;
	}

	public void printMe() {
		System.out.println("IrCommandBranchIfLess");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("labelName: " + labelName);
	}

	public void mipsMe() {
		MipsGenerator.getInstance().blt(t1, t2, labelName);
	}
}
