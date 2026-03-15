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
import ir.*;
public class IrCommandArrayAccess extends IrCommand
{
	public Temp dst;
	public Temp arr;
	public Temp index;
	
	public IrCommandArrayAccess(Temp dst, Temp arr, Temp index)
	{
		this.dst = dst;
		this.arr = arr;
		this.index = index;
	}

	public void printMe() {
		System.out.println("IrCommandArrayAccess");
		System.out.println("dst: " + dst);
		System.out.println("arr: " + arr);
		System.out.println("index: " + index);
	}

public void mipsMe(){
        String index_string = index.toString();
        String arr_string = arr.toString();
        String dst_string = dst.toString();
        String s0 = "$s0";

        String abort_bounds = IrCommand.getFreshLabel("abort_invalid_array_index");
        String abort_null = IrCommand.getFreshLabel("abort_null_ptr"); // NEW: Null pointer label
        String next = IrCommand.getFreshLabel("next_instruction");

        // 1. NULL POINTER CHECK (Must happen first!)
        MipsGenerator.getInstance().beqz(arr_string, abort_null);

        // 2. BOUNDS CHECKS
        MipsGenerator.getInstance().bltz(index, abort_bounds);
        MipsGenerator.getInstance().load(s0 , 0 , arr_string); // Now safe to load size
        MipsGenerator.getInstance().bge(index_string , s0 , abort_bounds);

        // 3. ACTUAL ARRAY ACCESS
        MipsGenerator.getInstance().move(s0 , index_string);
        MipsGenerator.getInstance().addi(s0 , s0 , 1);
        MipsGenerator.getInstance().muli(s0 , s0 , 4);
        MipsGenerator.getInstance().addu(s0 , arr_string , s0 );
        MipsGenerator.getInstance().load(dst_string , 0 , s0);
        MipsGenerator.getInstance().jump(next);

        // 4. GRACEFUL EXIT: OUT OF BOUNDS
        MipsGenerator.getInstance().label(abort_bounds);
        MipsGenerator.getInstance().printString("string_access_violation");
        MipsGenerator.getInstance().ExitAsm();

        // 5. GRACEFUL EXIT: NULL POINTER (NEW)
        MipsGenerator.getInstance().label(abort_null);
        MipsGenerator.getInstance().printString("string_invalid_ptr_dref");
        MipsGenerator.getInstance().ExitAsm();

        MipsGenerator.getInstance().label(next);
    }
}
