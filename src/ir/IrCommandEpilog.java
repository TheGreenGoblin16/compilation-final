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
import types.TypeFunction;

public class IrCommandEpilog extends IrCommand
{
	public TypeFunction function;
	
	public IrCommandEpilog(TypeFunction function)
	{
		this.function = function;
	}

	public void printMe() {
		if (function != null) {
			System.out.format("IrCommandEpilog for function %s with %d locals and %d params\n",
				function.name,
				function.localVarCounter,
				function.paramCounter
			);
		} else {
			System.out.println("IrCommandEpilog with null function type");
		}
	}

	public void mipsMe(){}
}
