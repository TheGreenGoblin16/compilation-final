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

	public void mipsMe(){

		String dst_string = dst.toString();
		String inst_string = inst.toString();

		String abort = IrCommand.getFreshLabel("abort!_null_pointer");
		String next = IrCommand.getFreshLabel("next_instruction");


		MipsGenerator.getInstance().beqz(inst , abort);

		MipsGenerator.getInstance().load(dst_string , 4*(fieldEntry.position+1) , inst_string );
		MipsGenerator.getInstance().jump(next);

		MipsGenerator.getInstance().label(abort);
		MipsGenerator.getInstance().printString("string_invalid_ptr_dref");
		MipsGenerator.getInstance().Exit();
		MipsGenerator.getInstance().label(next);

	}
}
