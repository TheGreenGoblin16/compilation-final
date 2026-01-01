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
		this.t          = t;
		this.labelName = labelName;
	}
}
