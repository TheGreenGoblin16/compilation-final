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
	SymbolTableEntry varEntry; // x := t1
	Temp src;
	
	public IrCommandWriteVar(SymbolTableEntry varEntry, Temp src)
	{
		this.src      = src;
		this.varEntry = varEntry;
	}
}
