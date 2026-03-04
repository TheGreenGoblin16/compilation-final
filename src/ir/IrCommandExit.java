/***********/
/* PACKAGE */
/***********/
package ir;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;
import types.*;

public class IrCommandExit extends IrCommand
{
	
	public IrCommandExit()
	{
        // Why r u gay?
    }

	public void printMe() {
		System.out.println("IrCommandExitAsm");
	}

	public void mipsMe() {
		MipsGenerator.getInstance().ExitAsm();
	}
}
