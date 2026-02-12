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

public class IrCommandList
{
	public IrCommand head;
	public IrCommandList tail;

	IrCommandList(IrCommand head, IrCommandList tail)
	{
		this.head = head;
		this.tail = tail;
	}

	public void printMe() {
		System.out.println("IrCommandList");
		if (head != null) {
			head.printMe();
		}
		if (tail != null) {
			tail.printMe();
		}
	}
	public void mipsMe() {
		if (head != null) {
			head.mipsMe();
		}
		if (tail != null) {
			tail.mipsMe();
		}
	}
}
