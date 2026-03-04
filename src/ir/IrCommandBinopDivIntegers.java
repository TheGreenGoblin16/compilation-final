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
import mips.*;

public class IrCommandBinopDivIntegers extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;
	
	public IrCommandBinopDivIntegers(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void printMe() {
		System.out.println("IrCommandBinopDivIntegers");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("dst: " + dst);
	}

	public void mipsMe(){
		String label = IrCommand.getFreshLabel("end");
		MipsGenerator.getInstance().bnez(t2, label);
		MipsGenerator.getInstance().printString("string_illegal_div_by_0");
		MipsGenerator.getInstance().ExitAsm();
		MipsGenerator.getInstance().label(label);
		MipsGenerator.getInstance().div(dst,t1,t2);
	}
}
