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
import types.*;

public class IrCommandReturn extends IrCommand
{
	public TypeFunction function;
	public Temp src;
	
	public IrCommandReturn(TypeFunction function, Temp src)
	{
		this.function = function;
		this.src = src;
	}

	public void printMe() {
		System.out.println("IrCommandReturn");
		System.out.println("function: " + function);
		System.out.println("src:      " + src);
	}

	public void mipsMe() {
		if (src != null) {
			MipsGenerator.getInstance().move("$v0", src);
		}
		MipsGenerator.getInstance().jump(function.labelEpilog);
	}
}
