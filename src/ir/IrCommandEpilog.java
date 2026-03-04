/***********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import types.TypeFunction;

public class IrCommandEpilog extends IrCommand
{
	public TypeFunction function;
	
	public IrCommandEpilog(TypeFunction function)
	{
		this.function = function;
	}

	public void printMe() {
		if (function != null) {
			System.out.format("IrCommandEpilog for function %s with %d locals and %d params\n",
				function.name,
				function.localVarCounter,
				function.paramCounter
			);
		} else {
			System.out.println("IrCommandEpilog with null function type");
		}
	}

	public void mipsMe() {
		// Fold up locals
		MipsGenerator.getInstance().addi("$sp", "$sp", +function.localVarCounter * MipsGenerator.WORD_SIZE);

		// Pop register backup and restore
		for (int i = 9; i >= 0; i--) {
			MipsGenerator.getInstance().pop("$t" + i);
		}

		// Pop return address and previous frame pointer
		MipsGenerator.getInstance().pop("$fp");
		MipsGenerator.getInstance().pop("$ra"); 
		MipsGenerator.getInstance().jr("$ra");
	}
}
