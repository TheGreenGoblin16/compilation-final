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

public class IrCommandArraySet extends IrCommand
{
	public Temp src;
	public Temp arr;
	public Temp index;
	
	public IrCommandArraySet(Temp src, Temp arr, Temp index)
	{
		this.src = src;
		this.arr = arr;
		this.index = index;
	}

	public void printMe() {
		System.out.println("IrCommandArraySet");
		System.out.println("src: " + src);
		System.out.println("arr: " + arr);
		System.out.println("index: " + index);
	}

	public void mipsMe(){
		String index_string = index.toString();
		String arr_string = arr.toString();
		String src_string = src.toString();
		String s0 =  "$s0";

		String abort = IrCommand.getFreshLabel("abort!_invalid_array_index");
		String next = IrCommand.getFreshLabel("next_instruction");

		MipsGenerator.getInstance().bltz(index , abort);
		MipsGenerator.getInstance().load(s0 , 0 , arr_string);
		MipsGenerator.getInstance().bge(index_string , s0 , abort);

		MipsGenerator.getInstance().move(s0 , index_string);
		MipsGenerator.getInstance().addi(s0 , s0 , 1);
		MipsGenerator.getInstance().muli(s0 , s0 , 4);
		MipsGenerator.getInstance().addu(s0 , arr_string , s0 );
		MipsGenerator.getInstance().sw(src_string , 0 , s0);
		MipsGenerator.getInstance().jump(next);

		MipsGenerator.getInstance().label(abort);
		MipsGenerator.getInstance().printString("string_invalid_ptr_dref");
		MipsGenerator.getInstance().ExitAsm();
		MipsGenerator.getInstance().label(next);

	}
}
