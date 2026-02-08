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
	public TypeFunction functionType;
	
	public IrCommandEpilog(TypeFunction functionType)
	{
		this.functionType = functionType;
	}

	public void printMe() {
		if (functionType != null) {
			System.out.format("IrCommandEpilog for function %s with %d locals and %d params\n",
				functionType.name,
				functionType.localVarCounter,
				functionType.paramCounter
			);
		} else {
			System.out.println("IrCommandEpilog with null function type");
		}
	}
}
