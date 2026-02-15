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

public class IrCommandBinopMulIntegers extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;
	public int max = (1<<15)-1;
	public int min = -(1<<15);
	
	public IrCommandBinopMulIntegers(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void printMe() {
		System.out.println("IrCommandBinopMulIntegers");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("dst: " + dst);
	}

	public void mipsMe()
	{
		String label1 = IrCommand.getFreshLabel("end");
		String label2 = IrCommand.getFreshLabel("end");
		MipsGenerator.getInstance().mul(dst,t1,t2);
		MipsGenerator.getInstance().li("$s0",min);
		MipsGenerator.getInstance().bge(dst,"$s0",label1);
		MipsGenerator.getInstance().li(dst,min);
		MipsGenerator.getInstance().jump(label2);
		MipsGenerator.getInstance().label(label1);
		MipsGenerator.getInstance().li("$s0",max);
		MipsGenerator.getInstance().ble(dst,"$s0",label2);
		MipsGenerator.getInstance().li(dst,max);
		MipsGenerator.getInstance().label(label2);
	}
}
