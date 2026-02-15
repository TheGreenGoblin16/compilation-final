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

public class IrCommandVirtualCall extends IrCommand
{
	public Temp dst;
	public Temp inst;
	public TypeFunction function;
    public TempList args;
	
	public IrCommandVirtualCall(Temp dst, Temp inst, TypeFunction function, TempList args)
	{
		this.dst = dst;
		this.inst = inst;
		this.function = function;
        this.args = args;
	}

	public void printMe() {
		System.out.println("IrCommandVirtualCall");
		System.out.println("dst: " + dst);
		System.out.println("inst: " + inst);
		System.out.println("function: " + function);
		System.out.println("args: " + args);
	}

	public void mipsMe(){
		// push args in reverse order
		int argCount = 0;
		for (TempList it = args; it != null; it = it.tail) { // there is a possibillity its the other way around, but we will see
			MipsGenerator.getInstance().push(it.head);
			argCount++;
		}

		// load function address from vtable
		MipsGenerator.getInstance().load("$t0", 0, inst.toString()); // load vtable pointer
		MipsGenerator.getInstance().load("$t1", function.functionIndex * MipsGenerator.WORD_SIZE, "$t0"); // load function address from vtable

		// call function
		MipsGenerator.getInstance().jalr("$t1");

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
