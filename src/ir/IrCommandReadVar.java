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
	SymbolTableEntry varEntry;
	
	public IrCommandReadVar(Temp dst, SymbolTableEntry varEntry)
	{
		this.dst      = dst;
		this.varEntry = varEntry;
	}
}
