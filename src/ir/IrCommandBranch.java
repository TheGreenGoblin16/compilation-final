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

public class IrCommandBranch extends IrCommand
{
	String labelName;
	
	public IrCommandBranch(String labelName)
	{
		this.labelName = labelName;
	}
}
