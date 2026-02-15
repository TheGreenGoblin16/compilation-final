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

public class IrCommandBinopAddStrings extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;
	
	public IrCommandBinopAddStrings(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void printMe() {
		System.out.println("IrCommandBinopAddStrings");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("dst: " + dst);
	}

	public void mipsMe(){
		String label1 = IrCommand.getFreshLabel("str_count");
		String label_end1 = IrCommand.getFreshLabel("str_count_end");
		String label2 = IrCommand.getFreshLabel("str_count");
		String label_end2 = IrCommand.getFreshLabel("str_count_end");
		String label_copy = IrCommand.getFreshLabel("str_copy");
		String label_copy_end = IrCommand.getFreshLabel("str_copy_end");
		String label_copy2 = IrCommand.getFreshLabel("str_copy");
		String label_copy_end2 = IrCommand.getFreshLabel("str_copy_end");

		MipsGenerator.getInstance().li("$s2",1);

		MipsGenerator.getInstance().move("$s0",t1);
		MipsGenerator.getInstance().label(label1);
		MipsGenerator.getInstance().lb("$s1",0,"$s0");
		MipsGenerator.getInstance().beqz("$s1",label_end1);
		MipsGenerator.getInstance().addi("$s0","$s0",1);
		MipsGenerator.getInstance().addi("$s2","$s2",1);
		MipsGenerator.getInstance().jump(label1);
		MipsGenerator.getInstance().label(label_end1);

		MipsGenerator.getInstance().move("$s0",t2);
		MipsGenerator.getInstance().label(label2);
		MipsGenerator.getInstance().lb("$s1",0,"$s0");
		MipsGenerator.getInstance().beqz("$s1",label_end2);
		MipsGenerator.getInstance().addi("$s0","$s0",1);
		MipsGenerator.getInstance().addi("$s2","$s2",1);
		MipsGenerator.getInstance().jump(label2);
		MipsGenerator.getInstance().label(label_end2);

		MipsGenerator.getInstance().allocate(dst,"$s2");
		MipsGenerator.getInstance().move("$s3",dst);
		MipsGenerator.getInstance().la("$s0",t1);
		MipsGenerator.getInstance().label(label_copy);
		MipsGenerator.getInstance().lb("$s1",0,"$s0");
		MipsGenerator.getInstance().beqz("$s1",label_copy_end);
		MipsGenerator.getInstance().sb("$s1",0,"$s3");
		MipsGenerator.getInstance().addi("$s0","$s0",1);
		MipsGenerator.getInstance().addi("$s3","$s3",1);
		MipsGenerator.getInstance().jump(label_copy);
		MipsGenerator.getInstance().label(label_copy_end);

		MipsGenerator.getInstance().la("$s0",t2);
		MipsGenerator.getInstance().label(label_copy2);
		MipsGenerator.getInstance().lb("$s1",0,"$s0");
		MipsGenerator.getInstance().beqz("$s1",label_copy_end2);
		MipsGenerator.getInstance().sb("$s1",0,"$s3");
		MipsGenerator.getInstance().addi("$s0","$s0",1);
		MipsGenerator.getInstance().addi("$s3","$s3",1);
		MipsGenerator.getInstance().jump(label_copy2);
		MipsGenerator.getInstance().label(label_copy_end2);

		MipsGenerator.getInstance().lb("$s1",0,"$s0");
		MipsGenerator.getInstance().sb("$s1",0,"$s3");
	}
}
