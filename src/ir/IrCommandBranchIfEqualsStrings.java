/***********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandBranchIfEqualsStrings extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public String labelName;
	
	public IrCommandBranchIfEqualsStrings(Temp t1, Temp t2, String labelName)
	{
		this.t1 = t1;
		this.t2 = t2;
		this.labelName = labelName;
	}

	public void printMe() {
		System.out.println("IrCommandBranchIfEqualsStrings");
		System.out.println("t1: " + t1);
		System.out.println("t2: " + t2);
		System.out.println("labelName: " + labelName);
	}

	public void mipsMe() {
		String labelLoop = IrCommand.getFreshLabel("str_eq_loop");
		String labelReject = IrCommand.getFreshLabel("str_neq");
		String labelEnd = IrCommand.getFreshLabel("str_eq_end");

		MipsGenerator.getInstance().li("$s4", 1);
		MipsGenerator.getInstance().move("$s0", t1);
		MipsGenerator.getInstance().move("$s1", t2);

		MipsGenerator.getInstance().label(labelLoop);
		MipsGenerator.getInstance().lb("$s2", 0, "$s0");
		MipsGenerator.getInstance().lb("$s3", 0, "$s1");
		MipsGenerator.getInstance().bne("$s2", "$s3", labelReject);
		MipsGenerator.getInstance().beqz("$s2", labelEnd);
		MipsGenerator.getInstance().addi("$s0", "$s0", 1);
		MipsGenerator.getInstance().addi("$s1", "$s1", 1);
		MipsGenerator.getInstance().jump(labelEnd);

		MipsGenerator.getInstance().label(labelReject);
		MipsGenerator.getInstance().li("$s4", 0);

		MipsGenerator.getInstance().label(labelEnd);
		MipsGenerator.getInstance().bnez("$s4", labelName);
	}
}
