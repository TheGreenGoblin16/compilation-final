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

public class IrCommandFieldSet extends IrCommand
{
	public Temp src;
	public Temp inst;
    public SymbolTableEntry fieldEntry;
	
	public IrCommandFieldSet(Temp src, Temp inst, SymbolTableEntry fieldEntry)
	{
        this.src = src;
		this.inst = inst;
		this.fieldEntry = fieldEntry;
	}

	public void printMe() {
		System.out.println("IrCommandFieldSet");
		System.out.println("src: " + src);
		System.out.println("inst: " + inst);
		System.out.println("fieldEntry: " + fieldEntry);
	}

	public void mipsMe(){}
}
