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
		MipsGenerator.getInstance().addi(dst,dst,-min-1);
		MipsGenerator.getInstance().addi(dst,dst,1);
		MipsGenerator.getInstance().bltz(dst,label);
		MipsGenerator.getInstance().li(dst,0);
		MipsGenerator.getInstance().label(label);
		MipsGenerator.getInstance().addi(dst,dst,min);
	}
}
