package com.anritsu.intellij.plugin.dtl.parser;

import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;import com.intellij.lexer.FlexLexer;
import com.intellij.psi.JavaTokenType;import com.intellij.psi.TokenType;import com.intellij.psi.tree.IElementType;
import com.anritsu.intellij.plugin.dtl.parser.psi.DtlTypes;

/*-***
 *
 * This file defines a stand-alone lexical analyzer for a subset of the Pascal
 * programming language.  This is the same lexer that will later be integrated
 * with a CUP-based parser.  Here the lexer is driven by the simple Java test
 * program in ./PascalLexerTest.java, q.v.  See 330 Lecture Notes 2 and the
 * Assignment 2 writeup for further discussion.
 *
 */




%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%class DtlLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}


DTL_TOKEN_IMPORT = "import"
DTL_TOKEN_INCLUDE = "include"
DTL_TOKEN_NULLVALUE = "null"
DTL_TOKEN_TRUE = "true"
DTL_TOKEN_FALSE = "false"
DTL_TOKEN_VAR = "var"
DTL_TOKEN_FINAL = "final"
DTL_TOKEN_SET = "set"
DTL_TOKEN_LIST = "list"
DTL_TOKEN_BYTES = "bytes"
DTL_TOKEN_MAP = "map"
DTL_TOKEN_CLONE = "clone"
DTL_TOKEN_IF = "if"
DTL_TOKEN_ELSE = "else"
DTL_TOKEN_SWITCH = "switch"
DTL_TOKEN_CASE = "case"
DTL_TOKEN_DEFAULT = "default"
DTL_TOKEN_WHILE = "while"
DTL_TOKEN_FOR = "for"
DTL_TOKEN_BREAK = "break"
DTL_TOKEN_CONTINUE = "continue"
DTL_TOKEN_RETURN = "return"
DTL_TOKEN_ASSERT = "assert"
DTL_TOKEN_TRACE = "trace"
DTL_TOKEN_ISNULL = "isNull"
DTL_TOKEN_ISINTEGER = "isInteger"
DTL_TOKEN_ISSTRING = "isString"
DTL_TOKEN_ISDOUBLE = "isDouble"
DTL_TOKEN_ISBYTES = "isBytes"
DTL_TOKEN_ISLIST = "isList"
DTL_TOKEN_ISSET = "isSet"
DTL_TOKEN_ISMAP = "isMap"
DTL_TOKEN_TOINTEGER = "toInteger"
DTL_TOKEN_TOSTRING = "toString"
DTL_TOKEN_TODOUBLE = "toDouble"
DTL_TOKEN_TOBYTES = "toBytes"
DTL_TOKEN_TOLIST = "toList"
DTL_TOKEN_TOSET = "toSet"
DTL_TOKEN_TOMAP = "toMap"
DTL_TOKEN_CONTAINS = "contains"
DTL_TOKEN_FIND = "find"
DTL_TOKEN_PUT = "put"
DTL_TOKEN_SIZE = "size"
DTL_TOKEN_ERASE = "erase"
DTL_TOKEN_REMOVE = "remove"
DTL_TOKEN_KEYS = "keys"
DTL_TOKEN_VALUES = "values"
DTL_TOKEN_ENTRIES = "entries"
DTL_TOKEN_TOLOWER = "toLower"
DTL_TOKEN_TOUPPER = "toUpper"
DTL_TOKEN_SORT = "sort"
DTL_TOKEN_BEGINSWITH = "beginsWith"
DTL_TOKEN_ENDSWITH = "endsWith"
DTL_TOKEN_RFIND = "rfind"
DTL_TOKEN_SUBSTRING = "subString"
DTL_TOKEN_MATCH = "match"
DTL_TOKEN_SPLIT = "split"
DTL_TOKEN_FORMATINT = "formatInt"
DTL_TOKEN_PARSEINT = "parseInt"
DTL_TOKEN_INC = "++"
DTL_TOKEN_DEC = "--"
DTL_TOKEN_PLUS = "+"
DTL_TOKEN_MINUS = "-"
DTL_TOKEN_MOD = "%"
DTL_TOKEN_DIV = "\/"
DTL_TOKEN_MULT = "*"
DTL_TOKEN_BITWISE_SHIFTLEFT = "<<"
DTL_TOKEN_BITWISE_SHIFTRIGHT = ">>"
DTL_TOKEN_PLUSEQ = "+="
DTL_TOKEN_MINUSEQ = "-="
DTL_TOKEN_BITOREQ = "|="
DTL_TOKEN_BITANDEQ = "&="
DTL_TOKEN_BITXOREQ = "\^="
DTL_TOKEN_BITSHIFTLEFTEQ = "<<="
DTL_TOKEN_BITSHIFTRIGHTEQ = ">>="
DTL_TOKEN_NULLDEFAULTOP = "?:"
DTL_TOKEN_LOGICAL_AND = "&&"
DTL_TOKEN_LOGICAL_OR = "\|\|"
DTL_TOKEN_LOGICAL_NOT = "!"
DTL_TOKEN_COMPARE_EQUAL = "=="
DTL_TOKEN_COMPARE_NOTEQUAL = "!="
DTL_TOKEN_COMPARE_GREATER = ">"
DTL_TOKEN_COMPARE_LESS = "<"
DTL_TOKEN_COMPARE_GREATEREQUAL = ">="
DTL_TOKEN_COMPARE_LESSEQUAL = "<="
DTL_TOKEN_BITWISE_AND = "&"
DTL_TOKEN_BITWISE_OR = "|"
DTL_TOKEN_BITWISE_XOR = "\^"
DTL_TOKEN_BITWISE_NOT = "~"
DTL_TOKEN_OPAR = "("
DTL_TOKEN_CPAR  = ")"
DTL_TOKEN_OSQUARE = "["
DTL_TOKEN_CSQUARE = "]"
DTL_TOKEN_OBRA = "{"
DTL_TOKEN_CBRA = "}"
DTL_TOKEN_SEMICOL = ";"
DTL_TOKEN_COLON = ":"
DTL_TOKEN_COMA = ","
DTL_TOKEN_DOT = "."
DTL_TOKEN_ASSIGN = "="

HEXDIGIT =  [0-9A-Fa-f]
DIGIT = [0-9]
INTEGERNUMBER = {DIGIT}+
DOUBLENUMBER = {DIGIT}+ '.' {DIGIT}+

HEXNUMBER = "0" ("x"|"X") {HEXDIGIT}+
BYTEARR =  "\$" {HEXDIGIT}+
IDENTIFIER = [:jletter:] [:jletterdigit:]*
//IDENTIFIER = ('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*

ESCAPE_SEQUENCE = \\[^\r\n]
CHARACTER_LITERAL = "'" ([^\\\'\r\n] | {ESCAPE_SEQUENCE})* ("'"|\\)?
STRING = \" ([^\\\"\r\n] | {ESCAPE_SEQUENCE})* (\"|\\)?
SINGLE_QUOTED_STRING = \' ([^\\\'\r\n] | {ESCAPE_SEQUENCE})* (\'|\\)?

//STRING = '\"' ( ESCAPESEQUENCE | ~('\'|'\"') )* '\"'
ESCAPESEQUENCE = '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
	|	UNICODEESCAPE
	|	OCTALESCAPE

OCTALESCAPE =    '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')


UNICODEESCAPE = '\\' 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT

//WS =  (' '|'\r'|'\t'|'\u000C'|'\n')
WS =  [\ \n\r\t\f]

COMMENT = "\/*" (.)* "*\/"
LINE_COMMENT = "\/\/" ~("\n"|"\r")* "\r"? "\n"

C_STYLE_COMMENT=("/""*"+{COMMENT_TAIL})|"/*"
COMMENT_TAIL=([^"*"]*("*"+[^"*""/"])?)*("*"+"/")
END_OF_LINE_COMMENT="/""/"[^\r\n]*

%%

<YYINITIAL> {

       {WS}                                 { return DtlTypes.WS;}
       {C_STYLE_COMMENT}                    { return DtlTypes.C_STYLE_COMMENT; }
       {END_OF_LINE_COMMENT}                { return DtlTypes.END_OF_LINE_COMMENT; }

       {DTL_TOKEN_OPAR}                     { return DtlTypes.DTL_TOKEN_OPAR;}
       {DTL_TOKEN_CPAR}                     { return DtlTypes.DTL_TOKEN_CPAR;}
       {DTL_TOKEN_OSQUARE}                  { return DtlTypes.DTL_TOKEN_OSQUARE;}
       {DTL_TOKEN_CSQUARE}                  { return DtlTypes.DTL_TOKEN_CSQUARE;}
       {DTL_TOKEN_OBRA}                     { return DtlTypes.DTL_TOKEN_OBRA;}
       {DTL_TOKEN_CBRA}                     { return DtlTypes.DTL_TOKEN_CBRA;}
       {DTL_TOKEN_SEMICOL}                  { return DtlTypes.DTL_TOKEN_SEMICOL;}
       {DTL_TOKEN_COLON}                    { return DtlTypes.DTL_TOKEN_COLON;}
       {DTL_TOKEN_COMA}                     { return DtlTypes.DTL_TOKEN_COMA;}
       {DTL_TOKEN_DOT}                      { return DtlTypes.DTL_TOKEN_DOT;}
       {DTL_TOKEN_ASSIGN}                   { return DtlTypes.DTL_TOKEN_ASSIGN;}

       {DTL_TOKEN_IMPORT}                   { return DtlTypes.DTL_TOKEN_IMPORT;}
       {DTL_TOKEN_INCLUDE}                  { return DtlTypes.DTL_TOKEN_INCLUDE;}
       {DTL_TOKEN_NULLVALUE}                { return DtlTypes.DTL_TOKEN_NULLVALUE;}
       {DTL_TOKEN_TRUE}                     { return DtlTypes.DTL_TOKEN_TRUE;}
       {DTL_TOKEN_FALSE}                    { return DtlTypes.DTL_TOKEN_FALSE;}
       {DTL_TOKEN_VAR}                      { return DtlTypes.DTL_TOKEN_VAR;}
       {DTL_TOKEN_FINAL}                    { return DtlTypes.DTL_TOKEN_FINAL;}
       {DTL_TOKEN_SET}                      { return DtlTypes.DTL_TOKEN_SET;}
       {DTL_TOKEN_LIST}                     { return DtlTypes.DTL_TOKEN_LIST;}
       {DTL_TOKEN_BYTES}                    { return DtlTypes.DTL_TOKEN_BYTES;}
       {DTL_TOKEN_MAP}                      { return DtlTypes.DTL_TOKEN_MAP;}
       {DTL_TOKEN_CLONE}                    { return DtlTypes.DTL_TOKEN_CLONE;}
       {DTL_TOKEN_IF}                       { return DtlTypes.DTL_TOKEN_IF;}
       {DTL_TOKEN_ELSE}                     {return DtlTypes.DTL_TOKEN_ELSE;}
       {DTL_TOKEN_SWITCH}                   { return DtlTypes.DTL_TOKEN_SWITCH;}
       {DTL_TOKEN_CASE}                     { return DtlTypes.DTL_TOKEN_CASE;}
       {DTL_TOKEN_DEFAULT}                  { return DtlTypes.DTL_TOKEN_DEFAULT;}
       {DTL_TOKEN_WHILE}                    { return DtlTypes.DTL_TOKEN_WHILE;}
       {DTL_TOKEN_FOR}                      { return DtlTypes.DTL_TOKEN_FOR;}
       {DTL_TOKEN_BREAK}                    { return DtlTypes.DTL_TOKEN_BREAK;}
       {DTL_TOKEN_CONTINUE}                 { return DtlTypes.DTL_TOKEN_CONTINUE;}
       {DTL_TOKEN_RETURN}                   { return DtlTypes.DTL_TOKEN_RETURN;}
       {DTL_TOKEN_ASSERT}                   { return DtlTypes.DTL_TOKEN_ASSERT;}
       {DTL_TOKEN_TRACE}                    { return DtlTypes.DTL_TOKEN_TRACE;}
       {DTL_TOKEN_ISNULL}                   { return DtlTypes.DTL_TOKEN_ISNULL;}
       {DTL_TOKEN_ISINTEGER}                { return DtlTypes.DTL_TOKEN_ISINTEGER;}
       {DTL_TOKEN_ISSTRING}                 { return DtlTypes.DTL_TOKEN_ISSTRING;}
       {DTL_TOKEN_ISDOUBLE}                 { return DtlTypes.DTL_TOKEN_ISDOUBLE;}
       {DTL_TOKEN_ISBYTES}                  { return DtlTypes.DTL_TOKEN_ISBYTES;}
       {DTL_TOKEN_ISLIST}                   { return DtlTypes.DTL_TOKEN_ISLIST;}
       {DTL_TOKEN_ISSET}                    { return DtlTypes.DTL_TOKEN_ISSET;}
       {DTL_TOKEN_ISMAP}                    { return DtlTypes.DTL_TOKEN_ISMAP;}
       {DTL_TOKEN_TOINTEGER}                { return DtlTypes.DTL_TOKEN_TOINTEGER;}
       {DTL_TOKEN_TOSTRING}                 { return DtlTypes.DTL_TOKEN_TOSTRING;}
       {DTL_TOKEN_TODOUBLE}                 { return DtlTypes.DTL_TOKEN_TODOUBLE;}
       {DTL_TOKEN_TOBYTES}                  { return DtlTypes.DTL_TOKEN_TOBYTES;}
       {DTL_TOKEN_TOLIST}                   { return DtlTypes.DTL_TOKEN_TOLIST;}
       {DTL_TOKEN_TOSET}                    { return DtlTypes.DTL_TOKEN_TOSET;}
       {DTL_TOKEN_TOMAP}                    { return DtlTypes.DTL_TOKEN_TOMAP;}
       {DTL_TOKEN_CONTAINS}                 { return DtlTypes.DTL_TOKEN_CONTAINS;}
       {DTL_TOKEN_FIND}                     { return DtlTypes.DTL_TOKEN_FIND;}
       {DTL_TOKEN_PUT}                      { return DtlTypes.DTL_TOKEN_PUT;}
       {DTL_TOKEN_SIZE}                     { return DtlTypes.DTL_TOKEN_SIZE;}
       {DTL_TOKEN_ERASE}                    { return DtlTypes.DTL_TOKEN_ERASE;}
       {DTL_TOKEN_REMOVE}                   { return DtlTypes.DTL_TOKEN_REMOVE;}
       {DTL_TOKEN_KEYS}                     { return DtlTypes.DTL_TOKEN_KEYS;}
       {DTL_TOKEN_VALUES}                   { return DtlTypes.DTL_TOKEN_VALUES;}
       {DTL_TOKEN_ENTRIES}                  { return DtlTypes.DTL_TOKEN_ENTRIES;}
       {DTL_TOKEN_TOLOWER}                  { return DtlTypes.DTL_TOKEN_TOLOWER;}
       {DTL_TOKEN_TOUPPER}                  { return DtlTypes.DTL_TOKEN_TOUPPER;}
       {DTL_TOKEN_SORT}                     { return DtlTypes.DTL_TOKEN_SORT;}
       {DTL_TOKEN_BEGINSWITH}               { return DtlTypes.DTL_TOKEN_BEGINSWITH;}
       {DTL_TOKEN_ENDSWITH}                 { return DtlTypes.DTL_TOKEN_ENDSWITH;}
       {DTL_TOKEN_RFIND}                    { return DtlTypes.DTL_TOKEN_RFIND;}
       {DTL_TOKEN_SUBSTRING}                { return DtlTypes.DTL_TOKEN_SUBSTRING;}
       {DTL_TOKEN_MATCH}                    { return DtlTypes.DTL_TOKEN_MATCH;}
       {DTL_TOKEN_SPLIT}                    { return DtlTypes.DTL_TOKEN_SPLIT;}
       {DTL_TOKEN_FORMATINT}                { return DtlTypes.DTL_TOKEN_FORMATINT;}
       {DTL_TOKEN_PARSEINT}                 { return DtlTypes.DTL_TOKEN_PARSEINT;}
       {DTL_TOKEN_INC}                      { return DtlTypes.DTL_TOKEN_INC;}
       {DTL_TOKEN_DEC}                      { return DtlTypes.DTL_TOKEN_DEC;}
       {DTL_TOKEN_PLUS}                     { return DtlTypes.DTL_TOKEN_PLUS;}
       {DTL_TOKEN_MINUS}                    { return DtlTypes.DTL_TOKEN_MINUS;}
       {DTL_TOKEN_MOD}                      { return DtlTypes.DTL_TOKEN_MOD;}
       {DTL_TOKEN_DIV}                      { return DtlTypes.DTL_TOKEN_DIV;}
       {DTL_TOKEN_MULT}                     { return DtlTypes.DTL_TOKEN_MULT;}
       {DTL_TOKEN_BITWISE_SHIFTLEFT}        { return DtlTypes.DTL_TOKEN_BITWISE_SHIFTLEFT;}
       {DTL_TOKEN_BITWISE_SHIFTRIGHT}       { return DtlTypes.DTL_TOKEN_BITWISE_SHIFTRIGHT;}
       {DTL_TOKEN_PLUSEQ}                   { return DtlTypes.DTL_TOKEN_PLUSEQ;}
       {DTL_TOKEN_MINUSEQ}                  { return DtlTypes.DTL_TOKEN_MINUSEQ;}
       {DTL_TOKEN_BITOREQ}                  { return DtlTypes.DTL_TOKEN_BITOREQ;}
       {DTL_TOKEN_BITANDEQ}                 { return DtlTypes.DTL_TOKEN_BITANDEQ;}
       {DTL_TOKEN_BITXOREQ}                 { return DtlTypes.DTL_TOKEN_BITXOREQ;}
       {DTL_TOKEN_BITSHIFTLEFTEQ}           { return DtlTypes.DTL_TOKEN_BITSHIFTLEFTEQ;}
       {DTL_TOKEN_BITSHIFTRIGHTEQ}          { return DtlTypes.DTL_TOKEN_BITSHIFTRIGHTEQ;}
       {DTL_TOKEN_NULLDEFAULTOP}            { return DtlTypes.DTL_TOKEN_NULLDEFAULTOP;}
       {DTL_TOKEN_LOGICAL_AND}              { return DtlTypes.DTL_TOKEN_LOGICAL_AND;}
       {DTL_TOKEN_LOGICAL_OR}               { return DtlTypes.DTL_TOKEN_LOGICAL_OR;}
       {DTL_TOKEN_LOGICAL_NOT}              { return DtlTypes.DTL_TOKEN_LOGICAL_NOT;}
       {DTL_TOKEN_COMPARE_EQUAL}            { return DtlTypes.DTL_TOKEN_COMPARE_EQUAL;}
       {DTL_TOKEN_COMPARE_NOTEQUAL}         { return DtlTypes.DTL_TOKEN_COMPARE_NOTEQUAL;}
       {DTL_TOKEN_COMPARE_GREATER}          { return DtlTypes.DTL_TOKEN_COMPARE_GREATER;}
       {DTL_TOKEN_COMPARE_LESS}             { return DtlTypes.DTL_TOKEN_COMPARE_LESS;}
       {DTL_TOKEN_COMPARE_GREATEREQUAL}     { return DtlTypes.DTL_TOKEN_COMPARE_GREATEREQUAL;}
       {DTL_TOKEN_COMPARE_LESSEQUAL}        { return DtlTypes.DTL_TOKEN_COMPARE_LESSEQUAL;}
       {DTL_TOKEN_BITWISE_AND}              { return DtlTypes.DTL_TOKEN_BITWISE_AND;}
       {DTL_TOKEN_BITWISE_OR}               { return DtlTypes.DTL_TOKEN_BITWISE_OR;}
       {DTL_TOKEN_BITWISE_XOR}              { return DtlTypes.DTL_TOKEN_BITWISE_XOR;}
       {DTL_TOKEN_BITWISE_NOT}              { return DtlTypes.DTL_TOKEN_BITWISE_XOR;}

       {STRING}                             { return DtlTypes.STRING;}
       {SINGLE_QUOTED_STRING}               { return DtlTypes.SINGLE_QUOTED_STRING;}
       {INTEGERNUMBER}                      { return DtlTypes.INTEGERNUMBER;}
       {DOUBLENUMBER}                       { return DtlTypes.DOUBLENUMBER;}
       {HEXNUMBER}                          { return DtlTypes.HEXNUMBER;}
       {BYTEARR}                            { return DtlTypes.BYTEARR;}
       {IDENTIFIER}                         { return DtlTypes.IDENTIFIER;}
       {ESCAPESEQUENCE}                     {}
       {OCTALESCAPE}                        {}
       {UNICODEESCAPE}                      {}
}


[^]  { return TokenType.BAD_CHARACTER;}





