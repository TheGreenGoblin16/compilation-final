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
import symboltable.*;

public class IrCommandReadVar extends IrCommand
{
	public Temp dst;
	public SymbolTableEntry varEntry;
	
	public IrCommandReadVar(Temp dst, SymbolTableEntry varEntry)
	{
		this.dst      = dst;
		this.varEntry = varEntry;
	}

	public void printMe() {
		System.out.println("IrCommandReadVar");
		System.out.println("dst: " + dst);
		System.out.println("varEntry: " + varEntry);
	}
}
