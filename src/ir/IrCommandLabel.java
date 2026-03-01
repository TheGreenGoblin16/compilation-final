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

public class IrCommandLabel extends IrCommand
{
	public String labelName;
	
	public IrCommandLabel(String labelName)
	{
		this.labelName = labelName;
	}

	public void printMe() {
		System.out.println("IrCommandLabel");
		System.out.println("labelName: " + labelName);
	}

	public void mipsMe() {
		MipsGenerator.getInstance().label(labelName);
	}
}
