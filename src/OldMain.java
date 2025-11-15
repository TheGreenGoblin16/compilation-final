import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;

public class OldMain
{
	public static final String[] TOKEN_NAMES_MAP = {
		"EOF", "LPAREN", "RPAREN", "LBRACK", "RBRACK", "LBRACE", "RBRACE",
    	"PLUS", "MINUS", "TIMES", "DIVIDE", "COMMA", "DOT", "SEMICOLON",
    	"TYPE_INT", "TYPE_STRING", "TYPE_VOID", "ASSIGN", "EQ", "LT", "GT",
    	"ARRAY", "CLASS", "RETURN", "WHILE", "IF", "ELSE", "NEW", "EXTENDS",
    	"NIL", "INT", "STRING", "ID",
	};

	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];
		String coordinates;
		
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

			/***********************/
			/* [4] Read next token */
			/***********************/

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			coordinates = "";
			do
			{
				try {
					/***********************/
					/* [6] Read next token */
					/***********************/
					s = l.next_token();
				}
				catch(Throwable t) {
					// Clear the output file and write "ERROR"
					fileWriter.close();  // Close current writer
					fileWriter = new PrintWriter(outputFileName); // Reopen file for overwrite
					fileWriter.print("ERROR");
					
					break; // Stop processing tokens after error
				}

				// Stop processing tokens after EOF
				if (s.sym == TokenNames.EOF) break;
				if (l.getLine() != 1 || l.getTokenStartPosition() != 1) {
					fileWriter.print("\n");
				}

				/************************/
				/* [7] Print to console */
				/************************/
				coordinates = "[" + l.getLine() + "," + l.getTokenStartPosition() + "]";
				System.out.print(coordinates);
				System.out.print(":");
				System.out.print(s.value);
				System.out.print("\n");
				
				/*********************/
				/* [8] Print to file */
				/*********************/
				fileWriter.print(TOKEN_NAMES_MAP[s.sym]);
				
				if (s.sym == TokenNames.ID || s.sym == TokenNames.INT){
					fileWriter.print("(");
					fileWriter.print(s.value);
					fileWriter.print(")");
				}
				else if (s.sym == TokenNames.STRING){
					fileWriter.print("(\"");
					fileWriter.print(s.value);
					fileWriter.print("\")");
				}
				fileWriter.print(coordinates);

			} while (s.sym != TokenNames.EOF);
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			fileWriter.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


