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
import types.TypeFunction;
import mips.*;

public class IrCommandCallVoid extends IrCommand
{
	public TypeFunction function;
    public TempList args;
	
	public IrCommandCallVoid(TypeFunction function, TempList args)
	{
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandCallVoid");
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
		MipsGenerator.getInstance().call(function.name);

		// pop args
		if (argCount > 0) {
			MipsGenerator.getInstance().addi("$sp", "$sp", argCount * MipsGenerator.WORD_SIZE);
		}
		// no return value to move since this is a void function
	}
}
