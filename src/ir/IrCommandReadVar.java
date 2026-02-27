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

	public void mipsMe(){

		VariableKind kind = varEntry.kind;

		if (kind == VariableKind.GLOBAL){
			String labelOfGlobal = varEntry.label;
			MipsGenerator.getInstance().load(dst , labelOfGlobal);
		} else if (kind == VariableKind.PARAMETER) {
			int parameterPosition = varEntry.position;
			int stackIndexFromFp = (2+parameterPosition)*4;
			MipsGenerator.getInstance().load(dst.toString() , stackIndexFromFp , "$fp");
		} else if (kind == VariableKind.LOCAL) {
			int stackIndexFromFp = ((-11) - (varEntry.position))*4;
			MipsGenerator.getInstance().load(dst.toString() , stackIndexFromFp , "$fp");
		}


	}
}
