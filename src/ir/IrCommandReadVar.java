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

public class IrCommandReadVar extends IrCommand
{
	Temp dst;
	String varName;
	
	public IrCommandReadVar(Temp dst, String varName)
	{
		this.dst      = dst;
		this.varName = varName;
	}
}
