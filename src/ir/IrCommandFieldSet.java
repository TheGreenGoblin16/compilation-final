/***********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;
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

	public void mipsMe(){

		String src_string = src.toString();
		String inst_string = inst.toString();

		String abort = IrCommand.getFreshLabel("abort_null_pointer");
		String next = IrCommand.getFreshLabel("next_instruction");


		MipsGenerator.getInstance().beqz(inst, abort);

		MipsGenerator.getInstance().sw(src_string, 4*(fieldEntry.position+1) , inst_string );
		MipsGenerator.getInstance().jump(next);

		MipsGenerator.getInstance().label(abort);
		MipsGenerator.getInstance().printString("string_access_violation");
		MipsGenerator.getInstance().ExitAsm();
		MipsGenerator.getInstance().label(next);

	}
}
