/**********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;
/*******************/
/* GENERAL IMPORTS */
/*******************/
import types.TypeFunction;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandProlog extends IrCommand
{
	public TypeFunction function;
	
	public IrCommandProlog(TypeFunction function)
	{
		this.function = function;
	}

	public void printMe() {
		if (function != null) {
			System.out.format("IrCommandProlog for function %s with %d locals and %d params\n",
				function.name,
				function.localVarCounter,
				function.paramCounter
			);
		} else {
			System.out.println("IrCommandProlog with null function type");
		}
	}

	public void mipsMe() {
		// Push return address and previous frame pointer
		MipsGenerator.getInstance().push("$ra"); 
		MipsGenerator.getInstance().push("$fp");
		MipsGenerator.getInstance().move("$fp", "$sp");

		// Push register backup
		for (int i = 0; i < 10; i++) {
			MipsGenerator.getInstance().push("$t" + i);
		}

		// Reserve space for locals
		MipsGenerator.getInstance().addi("%sp", "%sp", -function.localVarCounter * MipsGenerator.WORD_SIZE);
	}
}