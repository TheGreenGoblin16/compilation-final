/**********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import types.TypeFunction;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandProlog extends IrCommand
{
	public TypeFunction function;
	
	public IrCommandProlog(TypeFunction function)
	{
		this.function = function;
	}

	public void printMe() {
		if (function != null) {
			System.out.format("IrCommandProlog for function %s with %d locals and %d params\n",
				function.name,
				function.localVarCounter,
				function.paramCounter
			);
		} else {
			System.out.println("IrCommandProlog with null function type");
		}
	}

	public void mipsMe(){}
}