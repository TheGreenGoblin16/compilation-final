package ast;

import types.*;
import symboltable.*;
import ir.*;
import temp.*;

public class AstCallExp extends AstExp
{
	public AstVar var;
	public String name;
	public AstExpList args;
	public TypeFunction function;

	//true iff its call to class method
	public boolean isMethodCall;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCallExp(AstVar var, String name, AstExpList args, int lineNumber)
	{
		super(lineNumber);
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.args = args;
		this.isMethodCall = (var != null);
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CALL EXP */
		/*******************************/
		if (var != null) System.out.format("CALL var(%s).", var);
		else System.out.format("CALL ");
		System.out.format("ID(%s)\n WITH:\n", name);
		if (args != null) args.printMe();
		else System.out.format("NO ARGUMENTS\n");


		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
			serialNumber,
			String.format("CALL %s\n", name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (args != null) AstGraphviz.getInstance().logEdge(serialNumber,args.serialNumber);
	}
	public Type semantMe()
	{
		Type t = null;

		// Case 1: Method call on an object instance (e.g. obj.method())
		if (var != null) {
			Type t1 = var.semantMe();
			if (t1 == null) {
				System.out.format(">> ERROR [ %d ] variable not found in scope\n", lineNumber);
				abort();
			}
			if (!(t1 instanceof TypeClassInstance)) {
				System.out.format(">> ERROR [ %d ] variable is not a class instance\n", lineNumber);
				abort();
			}

			TypeClassInstance tci = (TypeClassInstance) t1;
			TypeClass tc = tci.cls;
			while (tc != null) {
				for (TypedIdentifierList it_tl = tc.dataMembers; it_tl != null; it_tl = it_tl.tail) {
					if (it_tl.head.name.equals(name)) {
						if (it_tl.head.type instanceof TypeFunction) {
                            t = it_tl.head.type;
                        } else {
                             System.out.format(">> ERROR [ %d ] member %s is not a method\n", lineNumber, name);
                             abort();
                        }
                        break;
					}
				}
				if (t != null) break;
				tc = tc.parent;
			}

			if (t == null) {
                System.out.format(">> ERROR [ %d ] method %s not found in class %s\n", lineNumber, name, tci.name);
                abort();
            }
		}
		// Case 2: Function call or method call within a class
		else {
			// Check for a method in the current class scope
			TypeClass currentClass = (TypeClass)SymbolTable.getInstance().find("$CURRENT-CLASS");
			if (currentClass != null) {
				TypeClass tc = currentClass;
				boolean found = false;
				while (tc != null && !found) {
					for (TypedIdentifierList it_tl = tc.dataMembers; it_tl != null; it_tl = it_tl.tail) {
						if (it_tl.head.name.equals(name)) {
							t = it_tl.head.type;
							found = true;
							this.isMethodCall = true;
							break;
						}
					}
					tc = tc.parent;
				}
			}

			// If not found in class, check global scope
			if (t == null) {
				t = SymbolTable.getInstance().find(name);
			}

			if (t == null) {
				System.out.format(">> ERROR [ %d ] name %s not found in scope\n", lineNumber, name);
				abort();
			}
		}

		if (!(t instanceof TypeFunction)) {
			System.out.format(">> ERROR [ %d ] name %s is not a function\n", lineNumber, name);
			abort();
		}

		function = (TypeFunction) t;
		TypeList expectedParams = function.paramsTypes;
		TypeList actualArgsTypes = (args != null) ? (TypeList) args.semantMe() : null;

		TypeList paramIter = expectedParams;
		TypeList argIter = actualArgsTypes;

		while (paramIter != null && argIter != null) {
			if (paramIter.head == null && argIter.head == null) {
				// Both are lists of empty expressions e.g. f() called with ()
				break;
			} else if (paramIter.head == null || argIter.head == null) {
				System.out.format(">> ERROR [ %d ] function %s argument type mismatch3\n", lineNumber, name);
				abort();
			} else if (!Type.isMatchingTypeOf(argIter.head, paramIter.head)) {
				System.out.format(">> ERROR [ %d ] function %s argument type mismatch0\n", lineNumber, name);
				abort();
			}
			paramIter = paramIter.tail;
			argIter = argIter.tail;
		}

		if (paramIter != null || argIter != null) {
			// Special case: single empty argument
			boolean singleEmptyArg = (paramIter != null && paramIter.head == null && paramIter.tail == null && argIter == null) ||
									 (argIter != null && argIter.head == null && argIter.tail == null && paramIter == null);
			if (!singleEmptyArg) {
				System.out.format(">> ERROR [ %d ] function %s argument count mismatch\n", lineNumber, name);
				abort();
			}
		}

		return function.returnType;
	}


	public Temp irMe(Temp newTemp)
	{
		// newTemp is only relevant for constructors, otherwise just ignore it
		Temp t = TempFactory.getInstance().getFreshTemp();
		TempList argTemps = null;

		if (args != null) {
			argTemps = args.irMe(newTemp);
		}
		
		// Case 1: Method call on an object instance (e.g. obj.method())
		if (isMethodCall && var != null) {
			Temp objTemp = var.irMe(newTemp);
			Ir.getInstance().AddIrCommand(new IrCommandVirtualCall(t, objTemp, function, argTemps));
		}
		// Case 2: Function is class method called inside the class, this.f(...)
		else if (isMethodCall && var == null) {
			Temp thisTemp = TempFactory.getInstance().getFreshTemp();
			Ir.getInstance().AddIrCommand(new IrCommandGetThis(thisTemp));
			Ir.getInstance().AddIrCommand(new IrCommandVirtualCall(t, thisTemp, function, argTemps));
		}
		// Case 3: Function call or method call within a class
		else { 
			Ir.getInstance().AddIrCommand(new IrCommandCall(t, function, argTemps));
		}

		return t;
	}
}
