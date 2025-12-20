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

public class IrCommandWriteVar extends IrCommand
{
	String varName;
	Temp src;
	
	public IrCommandWriteVar(String varName, Temp src)
	{
		this.src      = src;
		this.varName = varName;
	}
}
