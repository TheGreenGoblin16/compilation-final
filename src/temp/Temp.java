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

public class Temp
{
	private int serial=0;

	// NEW: The physical register index (0-9) assigned by the RegisterAllocator
	// -1 indicates it hasn't been allocated yet.
	private int regIndex = -1;

	public Temp(int serial)
	{
		this.serial = serial;
	}

	public int getSerialNumber()
	{
		return serial;
	}

	public int getRegIndex() {
		return regIndex;
	}

	public void setRegIndex(int regIndex) {
		this.regIndex = regIndex;
	}

	@Override
	public String toString() {
		// Debug helper: shows T(serial) mapped to $t(regIndex)
		if (regIndex != -1) return "Temp_" + serial + "($t" + regIndex + ")";
		return "Temp_" + serial;
	}
}