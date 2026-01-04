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

public class IrCommandFieldAccess extends IrCommand
{
	public Temp dst;
	public Temp inst;
    public String fieldName;
	
	public IrCommandFieldAccess(Temp dst, Temp inst, String fieldName)
	{
        this.dst = dst;
		this.inst = inst;
		this.fieldName = fieldName;
	}

	public void printMe() {
		System.out.println("IrCommandFieldAccess");
		System.out.println("dst: " + dst);
		System.out.println("inst: " + inst);
		System.out.println("fieldName: " + fieldName);
	}
}
