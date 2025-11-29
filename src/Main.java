import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import ast.*;


public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram ast;
		FileReader fileReader;
		PrintWriter fileWriter = null;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		boolean parsingSuccessful = false;
		
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
			ast = (AstProgram) p.parse().value;
			parsingSuccessful = true;

			fileWriter.print("OK");
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			ast.printMe();

			
			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			ast.semantMe();

			/*************************************/
			/* [8] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Throwable e)
		{
			if (e.getMessage() != null && e.getMessage().startsWith("SYNTAX_ERROR"))
			{
				// Extract "SYNTAX_ERROR(5)" -> "ERROR(5)"
				String msg = e.getMessage().replace("SYNTAX_", "");
				fileWriter.print(msg);
			}
			else if (parsingSuccessful == false)
			{
				fileWriter.print("ERROR");
			}
		}
		finally
		{
			/*************************/
			/* [8] Close output file */
			/*************************/
			if (fileWriter != null)
			{
				fileWriter.close();
			}
		}
	}
}


