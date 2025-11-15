/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/

import java_cup.runtime.*;

/******************************/
/* DOLLAR DOLLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

%state COMMENT_TYPE2

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECLARATIONS */
/***********************/
Letter			= [a-zA-Z]
Digit			= [0-9]
LineTerminator	= \r|\n|\r\n

WhiteSpace		= {LineTerminator} | [ \t\f]
WhiteSpaceNoLineTerminator = [ \t\f]
CommentType1Allowed = ({WhiteSpaceNoLineTerminator}|{Letter}|{Digit}|\(|\)|\[|\]|\{|\}|\?|\!|\+|-|\*|\/|\.|;)
CommentType2Allowed = ({CommentType1Allowed}|{LineTerminator})

INTEGER			= 0 | [1-9]{Digit}*
ID				= {Letter}({Letter}|{Digit})*
STRING			= \"{Letter}*\"
COMMENT_TYPE1	= \/\/{CommentType1Allowed}*{LineTerminator}


%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"					{ return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
","					{ return symbol(TokenNames.COMMA);}
"."					{ return symbol(TokenNames.DOT);}
";"					{ return symbol(TokenNames.SEMICOLON);}
"int"				{ return symbol(TokenNames.TYPE_INT);}
"string"			{ return symbol(TokenNames.TYPE_STRING);}
"void"				{ return symbol(TokenNames.TYPE_VOID);}
":="				{ return symbol(TokenNames.ASSIGN);}
"="					{ return symbol(TokenNames.EQ);}
"<"					{ return symbol(TokenNames.LT);}
">"					{ return symbol(TokenNames.GT);}
"array"				{ return symbol(TokenNames.ARRAY);}
"class"				{ return symbol(TokenNames.CLASS);}
"return"			{ return symbol(TokenNames.RETURN);}
"while"				{ return symbol(TokenNames.WHILE);}
"if"				{ return symbol(TokenNames.IF);}
"else"				{ return symbol(TokenNames.ELSE);}
"new"				{ return symbol(TokenNames.NEW);}
"extends"			{ return symbol(TokenNames.EXTENDS);}
"nil"				{ return symbol(TokenNames.NIL);}
{INTEGER}			{ if (Integer.valueOf(yytext()) > 32767) throw new Error(); else return symbol(TokenNames.INT, Integer.valueOf(yytext()));}
{STRING}			{ return symbol(TokenNames.STRING, yytext().substring(1, yylength() - 1));}
{ID}				{ return symbol(TokenNames.ID,     yytext());}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{COMMENT_TYPE1}		{ /* just skip what was found, do nothing */ }

"/*"                { yybegin(COMMENT_TYPE2);}

<<EOF>>				{ return symbol(TokenNames.EOF);}
}

<COMMENT_TYPE2> {

"*/"                {yybegin(YYINITIAL);}
{CommentType2Allowed}   { /* just skip what was found, do nothing */ }

<<EOF>>             {throw new Error();}

}