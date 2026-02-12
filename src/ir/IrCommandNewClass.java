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

	public void mipsMe(){}
}
