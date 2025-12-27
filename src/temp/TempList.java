/***********/
/* PACKAGE */
/***********/
package temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TempList
{
	public Temp head;
	public TempList tail;
	
	public TempList(Temp head, TempList tail)
	{
		this.head = head;
		this.tail = tail;
	}
}
