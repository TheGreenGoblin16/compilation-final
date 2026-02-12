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

public class IrCommandWriteVar extends IrCommand
{
	public SymbolTableEntry varEntry; // x := t1
	public Temp src;
	
	public IrCommandWriteVar(SymbolTableEntry varEntry, Temp src)
	{
		this.src      = src;
		this.varEntry = varEntry;
	}

	public void printMe() {
		System.out.println("IrCommandWriteVar");
		System.out.println("varEntry: " + varEntry);
		System.out.println("src: " + src);
	}

	public void mipsMe(){}
}
