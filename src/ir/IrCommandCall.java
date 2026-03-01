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
import types.*;
import mips.*;

public class IrCommandCall extends IrCommand
{
	public Temp dst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandCall(Temp dst, TypeFunction function, TempList args)
	{
		this.dst = dst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandCall");
		System.out.println("dst: " + dst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}
	
	public void mipsMe(){
		// push args in reverse order
		int argCount = 0;
		TempList reversed_args = null;
		for (TempList it = args; it != null; it = it.tail) {
			reversed_args = new TempList(it.head, reversed_args); // it is the right order?
			argCount++;
		}
		for (TempList it = reversed_args; it != null; it = it.tail) {
			MipsGenerator.getInstance().push(it.head);
		}

		// call function
		MipsGenerator.getInstance().jal(function.labelProlog);

		// pop args
		if (argCount > 0) {
			MipsGenerator.getInstance().addi("$sp", "$sp", argCount * MipsGenerator.WORD_SIZE);
		}

		// move return value to dst
		if (dst != null) {
			MipsGenerator.getInstance().move(dst.toString(), "$v0");
		}
	}
}
