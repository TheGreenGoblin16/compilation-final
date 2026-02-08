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
	public TypeFunction functionType;
	
	public IrCommandProlog(TypeFunction functionType)
	{
		this.functionType = functionType;
	}

	public void printMe() {
		if (functionType != null) {
			System.out.format("IrCommandProlog for function %s with %d locals and %d params\n",
				functionType.name,
				functionType.localVarCounter,
				functionType.paramCounter
			);
		} else {
			System.out.println("IrCommandProlog with null function type");
		}
	}
}