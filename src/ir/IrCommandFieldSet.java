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

public class IrCommandFieldSet extends IrCommand
{
	public Temp src;
	public Temp inst;
    public String fieldName;
	
	public IrCommandFieldSet(Temp src, Temp inst, String fieldName)
	{
        this.src = src;
		this.inst = inst;
		this.fieldName = fieldName;
	}

	public void printMe() {
		System.out.println("IrCommandFieldSet");
		System.out.println("src: " + src);
		System.out.println("inst: " + inst);
		System.out.println("fieldName: " + fieldName);
	}
}
