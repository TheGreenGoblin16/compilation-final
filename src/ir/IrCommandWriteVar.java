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
import mips.MipsGenerator;
import temp.*;
import symboltable.*;
import types.VariableKind;

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

	public void mipsMe(){

		VariableKind kind = varEntry.kind;

		if (kind == VariableKind.GLOBAL){
			String labelOfGlobal = varEntry.label;
			MipsGenerator.getInstance().sw(src , labelOfGlobal);
		} else if (kind == VariableKind.PARAMETER) {
			int parameterPosition = varEntry.position;
			int stackIndexFromFp = (2+parameterPosition)*4;
			MipsGenerator.getInstance().sw(src.toString() , stackIndexFromFp , "$fp");
		} else if (kind == VariableKind.LOCAL) {
			int stackIndexFromFp = ((-11) - (varEntry.position))*4;
			MipsGenerator.getInstance().sw(src.toString() , stackIndexFromFp , "$fp");
		}


	}
}
