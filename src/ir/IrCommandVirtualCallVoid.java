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

public class IrCommandVirtualCallVoid extends IrCommand
{
	public Temp inst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandVirtualCallVoid(Temp inst, TypeFunction function, TempList args)
	{
		this.inst = inst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandVirtualCallVoid");
		System.out.println("inst: " + inst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}

	public void mipsMe(){
		// push args in reverse order
		int argCount = 1;
		TempList reversed_args = null;
		for (TempList it = args; it != null; it = it.tail) {
			reversed_args = new TempList(it.head, reversed_args); // it is the right order?
			argCount++;
		}
		for (TempList it = reversed_args; it != null; it = it.tail) {
			MipsGenerator.getInstance().push(it.head);
		}
		MipsGenerator.getInstance().push(inst); // push the instance as the first argument

		// load function address from vtable
		MipsGenerator.getInstance().load("$s0", 0, inst.toString()); // load vtable pointer
		MipsGenerator.getInstance().load("$s1", function.functionIndex * MipsGenerator.WORD_SIZE, "$s0"); // load function address from vtable

		// call function
		MipsGenerator.getInstance().jalr("$s1");

		// pop args
		if (argCount > 0) {
			MipsGenerator.getInstance().addi("$sp", "$sp", argCount * MipsGenerator.WORD_SIZE);
		}
		// no return value to move since this is a void function
	}
}
