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
import mips.MipsGenerator;
import temp.*;
import types.*;

public class IrCommandNewClass extends IrCommand
{
	public Temp dst;
	public TypeClass cls;
	
	public IrCommandNewClass(Temp dst, TypeClass cls)
	{
		this.dst = dst;
		this.cls = cls;
	}

	public void printMe() {
		System.out.println("IrCommandNewClass");
		System.out.println("dst: " + dst);
		System.out.println("cls: " + cls);
	}

	public void mipsMe(){
		int instanceSize = (cls.fieldCounter+1)*4;
		MipsGenerator.getInstance().li("$s0" , instanceSize);
		MipsGenerator.getInstance().allocate(dst , "$s0");
		MipsGenerator.getInstance().la("$s0" , cls.labelVirtualTable);
		MipsGenerator.getInstance().sw("$s0", 0 , dst.toString());
	}
}
