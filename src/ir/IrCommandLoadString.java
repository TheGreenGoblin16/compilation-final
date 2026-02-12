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

public class IrCommandLoadString extends IrCommand
{
	public Temp t;
	public String label;
	
	public IrCommandLoadString(Temp t, String label)
	{
		this.t = t;
		this.label = label;
	}

	public void printMe() {
		System.out.println("IrCommandConstInt");
		System.out.println("t: " + t);
		System.out.println("labelName: " + label);
	}

	public void mipsMe(){
		MipsGenerator.getInstance().loadString(t, label);
	}
}
