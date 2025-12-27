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
	String varName; // x := t1
	Temp src;
	
	public IrCommandWriteVar(String varName, Temp src)
	{
		this.src     = src;
		this.varName = varName;
	}
}
