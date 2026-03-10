/***********/
/* IMPORTS */
/***********/
import java.io.*;

import java_cup.runtime.Symbol;
import ast.*;

import ir.*;
import mips.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstDecList ast;
		FileReader fileReader;
		PrintWriter fileWriter = null;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		boolean finished = false;

		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFileName);

			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);

			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			// This will throw "SYNTAX_ERROR(line)" if parsing fails
			ast = (AstDecList) p.parse().value;

			/**********************************************************/
			/* [6] Print the AST to console (Optional Debugging Step) */
			/**********************************************************/
			
			/***********************************************************/
			/* [7] Semant the AST                                      */
			/* This is the critical step. It recursively checks the    */
			/* tree and will throw "SEMANT_ERROR(line)" if it fails.   */
			/***********************************************************/
			ast.semantMe();
			finished = true;

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			
			ast.irMe();
			System.out.println("IR code generated.");

			// NEW: Run Register Allocation
			// This calculates liveness, builds the graph, colors it,
			// and updates the 'regIndex' inside every Temp object.
			System.out.println("Running Register Allocation...");
			ir.RegisterAllocator allocator = new ir.RegisterAllocator();
			allocator.allocate(Ir.getInstance());
			System.out.println("Register Allocation Complete.");
			System.out.println("______________________________________________________________________");

			Ir.getInstance().mipsMe();

			// NEW: Run Dataflow Analysis
			//controlFlow.controlFlow(Ir.getInstance().head, fileWriter); // Pass head and output path

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();

			/**********************************************************/
			/* [10] Success!                                          */
			/* If we reached here, no exceptions were thrown.         */
			/* Per PDF: "When the input program is semantically       */
			/* correct: OK"                                           */
			/**********************************************************/

			MipsGenerator.getInstance().finalizeFile();
		}
		catch (Throwable e)
		{
			// We use Throwable to catch everything, including RuntimeExceptions and Errors
			if (!finished){
				if (e.getMessage() != null)
				{
					// Handle Syntax Errors (from CUP / Parser.java)
					// Format: SYNTAX_ERROR(line) -> ERROR(line)
					if (e.getMessage().startsWith("SYNTAX_ERROR"))
					{
						String msg = e.getMessage().replace("SYNTAX_", "");
						fileWriter.print(msg);
					}
					// Handle Semantic Errors (from AstNode.abort)
					// Format: SEMANT_ERROR(line) -> ERROR(line)
					else if (e.getMessage().startsWith("SEMANT_ERROR"))
					{
						String msg = e.getMessage().replace("SEMANT_", "");
						fileWriter.print(msg);
					}
					// Handle Lexical Errors or other crashes
					// Per PDF: "When there is a lexical error: ERROR"
					else
					{
						fileWriter.print("ERROR");
					}
				}
				else
				{
					// Fallback for null messages
					fileWriter.print("ERROR");
				}
			}

		}
		finally
		{
			/***********************************************/
			/* [11] Close output file safely               */
			/* This ensures the text is actually written.  */
			/***********************************************/
			if (fileWriter != null)
			{
				fileWriter.close();
			}
		}
	}
}