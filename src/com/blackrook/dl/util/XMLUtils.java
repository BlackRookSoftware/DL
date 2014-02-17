/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;


import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.blackrook.dl.DLStruct;

/**
 * This class contains a series of methods used for converting the text-heavy
 * XML and other compatible markup languages (like HTML 4.1) to a DLStruct, for the purposes of conversion or export.
 * <p>
 * Each XML tag grouping becomes a DLStruct.
 * </p>
 * @author Matthew Tropiano
 */
public final class XMLUtils
{
	/** The DL field name for inner text (text enclosed in XML tags). */
	public static final String XML_INNERTEXT_FIELD_NAME = "innerText";
	
	private XMLUtils() {}
	
	/**
	 * Starts the parsing of an XML file from a file in the file system.
	 * @param xmlFile				the file to parse.
	 * @throws IOException			if a read error occurs.
	 */
	public static final DLStruct read(File xmlFile) throws IOException
	{
		DLStruct out = new DLStruct();
		readInto(out, xmlFile);
		return out;
	}
	
	/**
	 * Parses an XML file and puts the resulting contents into an existing DLStruct.
	 * @param dls					the DLStruct to put the resulting contents into.
	 * @param xmlFile				the file to parse.
	 * @throws IOException			if a read error occurs.
	 */
	public static final void readInto(DLStruct dls, File xmlFile) throws IOException
	{
		readInto(dls, xmlFile.getPath(), new FileInputStream(xmlFile));
	}
	
	/**
	 * Starts the parsing of an XML stream.
	 * @param streamName			the name of the input stream.
	 * @param in					the input stream to read from.
	 * @throws IOException			if a read error occurs.
	 */
	public static final DLStruct read(String streamName, InputStream in) throws IOException
	{
		DLStruct out = new DLStruct();
		readInto(out, streamName, in);
		return out;
	}
	
	/**
	 * Parses an XML stream and puts the resulting contents into an existing DLStruct.
	 * @param dls					the DLStruct to put the resulting contents into.
	 * @param streamName			the name of the input stream.
	 * @param in					the input stream to read from.
	 * @throws IOException			if a read error occurs.
	 */
	public static final void readInto(DLStruct dls, String streamName, InputStream in) throws IOException
	{
		try {
			Reader reader = new Reader();
			reader.read(dls, in);
		} catch (SAXException e) {
			IOException ex = new IOException("("+streamName+"): "+e.getMessage());
			ex.initCause(e);
			throw ex;
		}
	}
	
	private static class Reader
	{
		XMLReader xmlReader;
		DLStruct dls;
		Stack<DLStruct> structStack;
		
		public Reader() throws SAXException
		{
			xmlReader = XMLReaderFactory.createXMLReader();
			Handler handler = new Handler();
			xmlReader.setContentHandler(handler);
			xmlReader.setErrorHandler(handler);
			structStack = new Stack<DLStruct>();
		}
		
		public void read(DLStruct dls, InputStream in) throws IOException, SAXException
		{
			InputSource is = new InputSource(in);
			this.dls = dls;
			xmlReader.parse(is);
		}
		
		private class Handler extends DefaultHandler
		{
			public Handler()
			{
				super();
			}
			
			@Override
			public void startDocument() throws SAXException
			{
				structStack.push(dls);
			}

			@Override
			public void endDocument() throws SAXException
			{
				structStack.pop();
			}

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attribs) throws SAXException
			{
				DLStruct ns = new DLStruct();
				for (int i = 0; i < attribs.getLength(); i++)
				{
					String name = attribs.getLocalName(i);
					String val = attribs.getValue(i);
					try {ns.put(name, Double.parseDouble(val));}
					catch (NumberFormatException e) {ns.put(name,val);}
				}
				structStack.peek().putStruct(localName, ns);
				structStack.push(ns);
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException
			{
				structStack.pop();
			}
			
			@Override
			public void characters(char[] arg0, int arg1, int arg2) throws SAXException
			{
				DLStruct top = structStack.peek();
				String innerText = top.getString(XML_INNERTEXT_FIELD_NAME);
				if (innerText == null) innerText = "";
				top.put(XML_INNERTEXT_FIELD_NAME, innerText+substr(arg0,arg1,arg2));
			}
			
			private String substr(char[] arg0, int arg1, int arg2)
			{
				StringBuilder sb = new StringBuilder();
				int x = 0;
				while (x < arg2)
					sb.append(arg0[arg1+(x++)]);
				return sb.toString();
			}

		}
	}
}
