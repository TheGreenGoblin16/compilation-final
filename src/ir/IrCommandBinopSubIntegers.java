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
import mips.*;
import temp.*;

public class IrCommandBinopSubIntegers extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;
	public int min = -(1<<15);
	
	public IrCommandBinopSubIntegers(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void printMe() {
		System.out.println("IrCommandBinopSubIntegers");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("dst: " + dst);
	}

	public void mipsMe()
	{
		String label = IrCommand.getFreshLabel("end");
		MipsGenerator.getInstance().sub(dst,t1,t2);
		MipsGenerator.getInstance().li("$s0",min);
		MipsGenerator.getInstance().bge(dst,"$s0",label);
		MipsGenerator.getInstance().li(dst,min);
		MipsGenerator.getInstance().label(label);
	}
}
