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

public class IrCommandNewArray extends IrCommand
{
	public Temp dst;
	public Temp size;
	
	public IrCommandNewArray(Temp dst, Temp size)
	{
		this.dst = dst;
		this.size = size;
	}

	public void printMe() {
		System.out.println("IrCommandNewArray");
		System.out.println("dst: " + dst);
		System.out.println("size: " + size);
	}

	public void mipsMe(){
		MipsGenerator.getInstance().move("$s0" , size.toString());
		MipsGenerator.getInstance().addi("$s0", "$s0" , 1);
		MipsGenerator.getInstance().muli("$s0" , "$s0" , 4);
		MipsGenerator.getInstance().allocate(dst , "$s0");
		MipsGenerator.getInstance().sw(size, 0 , dst);
	}
}
