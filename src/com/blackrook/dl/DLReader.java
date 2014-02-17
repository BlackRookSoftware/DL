/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl;

import java.io.*;

import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.linkedlist.Stack;
import com.blackrook.lang.CommonLexer;
import com.blackrook.lang.CommonLexerKernel;
import com.blackrook.lang.Lexer;
import com.blackrook.lang.Parser;

/**
 * Reads an ASCII file or stream and creates a DLStructTable out of it.
 * @author Matthew Tropiano
 */
public class DLReader
{
	/** Creates a new reader. */
	public DLReader()
	{
		
	}
	
	/**
	 * Reads in a database from a file (presumably ASCII or text-based)
	 * Note: Calls apply() with a new table.
	 * @param f	the file to read from.
	 * @return A new DLStruct that represents the new database.
	 * @throws IOException				if the stream can't be read.
	 * @throws NullPointerException		if f is null. 
	 */
	public DLStruct read(File f) throws IOException
	{
		return read(f.getPath(),new FileInputStream(f));
	}
	
	/**
	 * Reads in a database from a InputStream.
	 * Note: Calls apply() with a new table.
	 * @param streamName the name of the stream.
	 * @param in the stream to read from.
	 * @return A new DLStruct that represents the new database.
	 * @throws IOException if the stream can't be read.
	 * @throws NullPointerException if in is null. 
	 */
	public DLStruct read(String streamName, InputStream in) throws IOException
	{
		DLStruct out = new DLStruct();
		apply(streamName, in, out);
		return out;
	}
	
	/**
	 * Applies the information read to an already existing table.
	 * @param f	the file to read from.
	 * @param dlst	the table to apply the info to.
	 * @throws NullPointerException	if either object is null. 
	 */
	public void apply(File f, DLStruct dlst) throws IOException
	{
		apply(f.getPath(), new FileInputStream(f), dlst);
	}
	
	/**
	 * Applies the information read to an already existing table.
	 * @param streamName the name of the stream.
	 * @param in the stream to read from.
	 * @param dlst	the table to apply the info to.
	 * @throws NullPointerException	if either object is null. 
	 */
	public void apply(String streamName, InputStream in, DLStruct dlst)
	{
		DLLexer lexer = new DLLexer(streamName, new InputStreamReader(in));
		DLParser parser = new DLParser(dlst, lexer);
		parser.read();
	}
	
	/**
	 * Returns a stream to a resource using a string path.
	 * May return null, if the path refers to a resource that doesn't exist.
	 * @deprecated As of version 2.1.2. Override {@link #getIncludeResource(String, String)} instead.
	 * This will still be called, but you won't know the original stream path that is at the top of the stack. 
	 */
	public InputStream getIncludeResource(String path) throws IOException
	{
		return new FileInputStream(new File(path));
	}
	
	/**
	 * Returns a stream to a resource using a string path.
	 * May return null, if the path refers to a resource that doesn't exist.
	 */
	public InputStream getIncludeResource(String currentSreamName, String path) throws IOException
	{
		return getIncludeResource(path);
	}
	
	/**
	 * Kernel for DLLexer.
	 */
	private static class DLLexerKernel extends CommonLexerKernel
	{
		public static final int TYPE_LBRACE = 0;
		public static final int TYPE_RBRACE = 1;
		public static final int TYPE_LPAREN = 2;
		public static final int TYPE_RPAREN = 3;
		public static final int TYPE_LBRACK = 4;
		public static final int TYPE_RBRACK = 5;
		public static final int TYPE_SEMICOLON = 6;
		public static final int TYPE_COLON = 7;
		public static final int TYPE_COMMA = 8;
		public static final int TYPE_PERIOD = 9;
		public static final int TYPE_MINUS = 10;

		public static final int TYPE_COMMENT = 11;

		public DLLexerKernel()
		{
			addStringDelimiter('"', '"');
			
			addDelimiter("{", TYPE_LBRACE);
			addDelimiter("}", TYPE_RBRACE);
			addDelimiter("(", TYPE_LPAREN);
			addDelimiter(")", TYPE_RPAREN);
			addDelimiter("[", TYPE_LBRACK);
			addDelimiter("]", TYPE_RBRACK);
			addDelimiter(";", TYPE_SEMICOLON);
			addDelimiter(":", TYPE_COLON);
			addDelimiter(",", TYPE_COMMA);
			addDelimiter(".", TYPE_PERIOD);
			addDelimiter("-", TYPE_MINUS);
			
			addCommentStartDelimiter("/*", TYPE_COMMENT);
			addCommentLineDelimiter("//", TYPE_COMMENT);
			addCommentEndDelimiter("*/", TYPE_COMMENT);
		}
	}
	
	private static final DLLexerKernel KERNEL = new DLLexerKernel();

	/**
	 * Lexer class for the reader. 
	 */
	private class DLLexer extends CommonLexer
	{
		protected DLLexer(String name, Reader in)
		{
			super(KERNEL, name, in);
		}
		
		@Override
		public InputStream getResource(String path) throws IOException
		{
			return getIncludeResource(getCurrentStreamName(), path);
		}
		
	}

	/**
	 * Parser class for the reader. 
	 */
	private class DLParser extends Parser
	{
		private Stack<DLStruct> structStack;
		private DLStruct stackTop;
		private Stack<String> currentName;
		private Stack<DLValue> currentValue;
		private HashMap<String,String[]> archetypeTable;
		
		protected DLParser(DLStruct dls, Lexer lexer)
		{
			super(lexer);
			structStack = new Stack<DLStruct>();
			currentName = new Stack<String>();
			currentValue = new Stack<DLValue>();
			archetypeTable = new HashMap<String, String[]>();
			pushStruct(dls);
		}
		
		public void read()
		{
			nextToken();
			
			boolean strOk = false;
			while (currentToken() != null && (strOk = StructList()));
			if (!strOk)
				addErrorMessage("Expected valid structure.");
			
			String[] errors = getErrorMessages();
			if (errors.length > 0)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < errors.length; i++)
				{
					sb.append(errors[i]);
					if (i < errors.length-1)
						sb.append('\n');
				}
				throw new DLParseException(sb.toString());
			}
		}
		
		/* 
		 * <StructList> :=	<EOLEX> | 
		 *					"." <ID> <ArchetypeDecl> ";" <StructList> |
		 *					<StructDecl> <StructList>
		 */
		private boolean StructList()
		{
			if (currentToken() == null)
				return true;
			
			else if (matchType(DLLexerKernel.TYPE_PERIOD))
			{
				if (!currentType(DLLexer.TYPE_IDENTIFIER))
				{
					addErrorMessage("Expected identifier.");
					return false;
				}

				currentName.push(currentToken().getLexeme());
				nextToken();

				if (!ArchetypeDecl())
					return false;
				
				if (!matchTypeStrict(DLLexerKernel.TYPE_SEMICOLON))
					return false; 
				
				if (!StructList())
					return false;
				
				return true;
			}
			
			return StructDecl();
		}

		/* 
		 * <ArchetypeDecl> :=	"(" <IDList> ")"
		 */
		private boolean ArchetypeDecl()
		{
			if (!matchTypeStrict(DLLexerKernel.TYPE_LPAREN))
				return false;
			
			if (!IDList())
				return false;

			if (!matchTypeStrict(DLLexerKernel.TYPE_RPAREN))
				return false;
			
			// Store Archetype IDs
			String[] archids = new String[currentName.size()-1];
			int i = archids.length - 1;
			while (currentName.size()-1 > 0)
				archids[i--] = currentName.pop();
			
			archetypeTable.put(currentName.pop(), archids);

			return true;
		}
		
		/* 
		 * <StructDecl> := <ID> <StructTail>
		 */
		private boolean StructDecl()
		{
			if (currentType(DLLexer.TYPE_IDENTIFIER))
			{
				currentName.push(currentToken().getLexeme());
				nextToken();
				
				if (!StructTail())
					return false;
		
				return true;
			}
			
			return false;
		}

		/* 
		 * <InnerStructList> :=	<ID> <StructTail> <InnerStructList> |
		 * 						[e]
		 */
		private boolean InnerStructList()
		{
			if (currentType(DLLexer.TYPE_IDENTIFIER))
			{
				currentName.push(currentToken().getLexeme());
				nextToken();
				
				if (!StructTail())
					return false;
		
				return InnerStructList();
			}
		
			return true;
		}

		/*
		 * <StructTail> :=	"{" <InnerStructList> "}" |
		 *					<InheritClause> <StatementEndOrStruct> |
		 *					<ArchetypeClause> <StatementEndOrStruct> |
		 *					<Value> <StatementEndOrStruct>
		 */
		private boolean StructTail()
		{
			if (matchType(DLLexerKernel.TYPE_LBRACE))
			{
				pushStruct(stackTop.putStruct(currentName.peek()));

				if (!InnerStructList())
					return false;
				
				if (!matchTypeStrict(DLLexerKernel.TYPE_RBRACE))
					return false;
				
				popStruct();
				currentName.pop();
				return true;
			}
			
			// lookahead for <InheritClause>
			else if (currentType(DLLexerKernel.TYPE_COLON))
			{
				pushStruct(stackTop.putStruct(currentName.peek()));

				if (!InheritClause())
					return false;
				
				return StatementEndOrStruct();
			}
			
			// lookahead for <ArchetypeClause>
			else if (currentType(DLLexerKernel.TYPE_LPAREN))
			{
				pushStruct(stackTop.putStruct(currentName.peek()));

				if (!ArchetypeClause())
					return false;
				
				return StatementEndOrStruct();
			}
			
			else
			{

				if (!Value())
					return false;

				if (currentType(DLLexerKernel.TYPE_SEMICOLON))
				{
					nextToken();
					stackTop.put(currentName.peek(),currentValue.peek());
					currentName.pop();
					currentValue.pop();
					return true;
				}

				pushStruct(stackTop.putStruct(currentName.peek()));
				stackTop.set(currentValue.pop());
				
				return StatementEndOrStruct();
			}
		}
		
		/*
		 * <StatementEndOrStruct> := ";" |
		 *							 "{" <InnerStructList> "}" |
		 *							 <InheritClause> <StatementEndOrStruct> 
		 *							 <ArchetypeClause> <StatementEndOrStruct>
		 */
		private boolean StatementEndOrStruct()
		{
			if (matchType(DLLexerKernel.TYPE_SEMICOLON))
			{
				popStruct();
				currentName.pop();
				return true;
			}
			
			else if (matchType(DLLexerKernel.TYPE_LBRACE))
			{
				if (!InnerStructList())
					return false;
				
				if (!matchTypeStrict(DLLexerKernel.TYPE_RBRACE))
					return false;
				
				popStruct();
				currentName.pop();
				return true;
			}
			
			// lookahead for <InheritClause>
			else if (currentType(DLLexerKernel.TYPE_COLON))
			{
				if (!InheritClause())
					return false;
				
				return StatementEndOrStruct();
			}
			
			// lookahead for <ArchetypeClause>
			else if (currentType(DLLexerKernel.TYPE_LPAREN))
			{
				if (!ArchetypeClause())
					return false;
				
				return StatementEndOrStruct();
			}
			
			addTypeError(DLLexerKernel.TYPE_SEMICOLON, DLLexerKernel.TYPE_LPAREN, DLLexerKernel.TYPE_LBRACE, DLLexerKernel.TYPE_COLON);
			return false;
		}
		
		/*
		 * <InheritClause> := ":" <ID> <Value>
		 */
		private boolean InheritClause()
		{
			if (!matchTypeStrict(DLLexerKernel.TYPE_COLON))
				return false;
			
			if (!currentType(DLLexer.TYPE_IDENTIFIER))
			{
				addErrorMessage("Expected identifier.");
				return false;
			}
			
			currentName.push(currentToken().getLexeme());
			nextToken();
		
			if (!Value())
				return false;
		
			String structName = currentName.peek();
			DLValue dlv = currentValue.pop();
			DLStruct found = null;
			
			DLStruct last = popStruct();
			
			Queue<DLStruct> structList = stackTop.get(structName);
			if (structList != null) for (DLStruct dls : structList)
				if (dls.getValue() != null && dls.getValue().equals(dlv))
					found = dls;
			
			currentName.pop();
			
			pushStruct(last);
			
			if (found != null)
			{
				DLValue v = stackTop.getValue();
				found.copyInto(stackTop);
				stackTop.set(v);
			}
			else
			{
				addErrorMessage("The struct that this struct is supposed to inherit data from, "+(structName)+" "+dlv.toString()+", can't be found.");
				return false;
			}
		
			return true;
		}

		/*
		 * <ArchetypeClause> :=	"(" <ValueList> ")"
		 */
		private boolean ArchetypeClause()
		{
			if (!matchTypeStrict(DLLexerKernel.TYPE_LPAREN))
				return false;
		
			int startSize = currentValue.size();
			
			if (!ValueList())
				return false;
			
			if (!matchTypeStrict(DLLexerKernel.TYPE_RPAREN))
				return false;
			
			String archName = currentName.peek();
			String[] params = archetypeTable.get(archName);
			
			if (params == null)
			{
				addErrorMessage("Archetype '"+archName+"' was not declared.");
				return false;
			}
		
			if (currentValue.size() - startSize > params.length)
			{
				addErrorMessage("Archetype '"+archName+"' requires "+params.length+" values (or less) to complete statement. Too many values provided.");
				return false;
			}
			
			int i = currentValue.size() - startSize - 1; //params.length-1;
			while (i >= 0)
			{
				stackTop.put(params[i],currentValue.pop());
				i--;
			}
			
			return true;
		}

		// <IDList> :=	<ID> <IDList'>
		private boolean IDList()
		{
			if (currentType(DLLexer.TYPE_IDENTIFIER))
			{
				currentName.push(currentToken().getLexeme());
				nextToken();
				return IDListPrime();
			}
		
			addErrorMessage("Expected archetype field declaration.");
			return false;
		}

		// <IDList'> :=	"," <IDList> |
		//				[E]
		private boolean IDListPrime()
		{
			if (matchType(DLLexerKernel.TYPE_COMMA))
				return IDList();
					
			return true;
		}

		// <Value> :=	"[" <ArrayValues> "]" |
		//				<STRING> |
		//				<Number>
		// Pushes a value onto the value stack.
		private boolean Value()
		{
			if (matchType(DLLexerKernel.TYPE_LBRACK))
			{
				if (!ArrayValues())
					return false;
				
				if (!matchTypeStrict(DLLexerKernel.TYPE_RBRACK))
					return false;
				
				return true;
			}
			
			else if (currentType(DLLexer.TYPE_STRING))
			{
				currentValue.push(new DLValue(currentToken().getLexeme()));
				nextToken();
				return true;
			}
			
			else if (Number())
			{
				return true;
			}

			addErrorMessage("Expected value in value list.");
			return false;
		}
		
		// <ValueList> :=	<Value> <ValueListPrime>
		private boolean ValueList()
		{
			if (!Value())
				return false;
			return ValueListPrime();
		}
		
		// <ValueListPrime> :=	"," <Value> <ValueListPrime> |
		//						[E]
		private boolean ValueListPrime()
		{
			if (matchType(DLLexerKernel.TYPE_COMMA))
			{
				if (!Value())
				{
					addErrorMessage("Expected value in value list.");
					return false;
				}
				
				return ValueListPrime();
			}
			
			return true;
		}
		
		// <ArrayValues> :=	<STRING> <StringArray> |
		//					<Number> <NumberArray>
		private boolean ArrayValues()
		{
			if (currentType(DLLexer.TYPE_STRING))
			{
				currentValue.push(new DLValue(currentToken().getLexeme()));
				nextToken();
				return StringArray();
			}
			
			if (!Number())
				return false;
			
			return NumberArray();
		}

		// <StringArray> :=	"," <String> <StringArray> |
		//					[E]
		private boolean StringArray()
		{
			if (matchType(DLLexerKernel.TYPE_COMMA))
			{
				if (!currentType(DLLexer.TYPE_STRING))
				{
					addErrorMessage("Expected string in string array.");
					return false;
				}
				
				currentValue.peek().append(currentToken().getLexeme());
				nextToken();
				return StringArray();
			}
			
			return true;
		}
		
		// <NumberArray> :=	"," <Number> <NumberArray> |
		//					[E]
		private boolean NumberArray()
		{
			if (matchType(DLLexerKernel.TYPE_COMMA))
			{
				if (!Number())
					return false;
				
				DLValue val = currentValue.pop();
				currentValue.peek().append(val.getDouble());
				
				return NumberArray();
			}
			
			return true;
		}
		
		// <Number> :=	"-" [NumberToken] |
		//				[NumberToken]
		private boolean Number()
		{
			if (matchType(DLLexerKernel.TYPE_MINUS))
				return NumberToken(true);
			
			return NumberToken(false);
		}
		
		// <NumberToken> :=	[INTEGER] | [FLOAT]
		private boolean NumberToken(boolean negate)
		{
			int n = negate ? -1 : 1;
			
			if (currentType(DLLexer.TYPE_NUMBER) || currentType(DLLexer.TYPE_FLOAT))
			{
				currentValue.push(new DLValue(n*Double.parseDouble(currentToken().getLexeme())));
				nextToken();
				return true;
			}
			
			addErrorMessage("Expected numerical value.");
			return false;
		}
		
		private void pushStruct(DLStruct dls)
		{
			structStack.push(dls);
			stackTop = structStack.peek();
		}
		
		private DLStruct popStruct()
		{
			DLStruct returned = structStack.pop();
			stackTop = structStack.peek();
			return returned;
		}

		@Override
		protected String getTypeErrorText(int tokenType)
		{
			switch (tokenType)
			{
				case DLLexerKernel.TYPE_LPAREN:
					return "'('";
				case DLLexerKernel.TYPE_RPAREN:
					return "')'";
				case DLLexerKernel.TYPE_LBRACE:
					return "'{'";
				case DLLexerKernel.TYPE_RBRACE:
					return "'}'";
				case DLLexerKernel.TYPE_LBRACK:
					return "'['";
				case DLLexerKernel.TYPE_RBRACK:
					return "']'";
				case DLLexerKernel.TYPE_SEMICOLON:
					return "';'";
				case DLLexerKernel.TYPE_COLON:
					return "':'";
				case DLLexerKernel.TYPE_COMMA:
					return "','";
			}
			return "";
		}
	}
	
}
