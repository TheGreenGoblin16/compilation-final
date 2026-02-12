/***********/
/* PACKAGE */
/***********/
package ir;

import symboltable.SymbolTableEntry;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandFieldAccess extends IrCommand
{
	public Temp dst;
	public Temp inst;
    public SymbolTableEntry fieldEntry;
	
	public IrCommandFieldAccess(Temp dst, Temp inst, SymbolTableEntry fieldEntry)
	{
        this.dst = dst;
		this.inst = inst;
		this.fieldEntry = fieldEntry;
	}

	public void printMe() {
		System.out.println("IrCommandFieldAccess");
		System.out.println("dst: " + dst);
		System.out.println("inst: " + inst);
		System.out.println("fieldEntry: " + fieldEntry);
	}

	public void mipsMe(){}
}
