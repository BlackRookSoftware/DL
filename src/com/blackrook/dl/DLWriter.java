/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl;

import static com.blackrook.dl.DLValue.TYPE_NUMBER_ARRAY;
import static com.blackrook.dl.DLValue.TYPE_STRING_ARRAY;

import java.io.*;
import java.util.Iterator;

import com.blackrook.commons.Common;


/**
 * Writes DL structures out to files.
 * @author Matthew Tropiano
 */
public class DLWriter 
{
	/**
	 * Writes a DL structure to a file represented by a string.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param filename		the name of the file to dump this crap into.
	 * @throws IOException	if the stream can't be written to somehow.
	 */
	public static void write(DLStruct dlst, String filename) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(new File(filename));
		write(dlst, fos);		
		fos.close();
	}

	/**
	 * Writes a DL structure to a file stream.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param f				the file to dump this crap into.
	 * @throws IOException	if the stream can't be written to somehow.
	 */
	public static void write(DLStruct dlst, File f) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(f);
		write(dlst, fos);
		fos.close();
	}

	/**
	 * Writes a DL structure to a file stream.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param outStream		the stream to dump this crap into.
	 * @throws IOException	if the stream can't be written to somehow.
	 */
	public static void write(DLStruct dlst, FileOutputStream outStream) throws IOException
	{
		export(dlst,new PrintWriter(outStream,true));		
	}

	/**
	 * Writes a DL structure to an output stream.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param outStream		the stream to dump this crap into.
	 * @throws IOException	if the stream can't be written to somehow.
	 * @since 1.1.0
	 */
	public static void write(DLStruct dlst, OutputStream outStream) throws IOException
	{
		export(dlst,new PrintWriter(outStream,true));		
	}

	/**
	 * Writes a DL structure to a print stream.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param outStream		the stream to dump this crap into.
	 * @throws IOException	if the stream can't be written to somehow.
	 */
	public static void write(DLStruct dlst, PrintStream outStream) throws IOException
	{
		export(dlst,new PrintWriter(outStream,true));		
	}
	
	/**
	 * Writes a DL structure to a buffered writer.
	 * @param dlst			the struct that is the root of the DL structure.
	 * @param outBuffer		the buffer to dump this crap into.
	 * @throws IOException	if the buffer can't be written to somehow.
	 */
	public static void write(DLStruct dlst, BufferedWriter outBuffer) throws IOException
	{
		export(dlst,new PrintWriter(outBuffer,true));
	}
	
	/**
	 * Export the root DL structure struct to a printstream. 
	 * @param dlst		the struct that is the root of the DL structure.
	 * @param out		the print stream to send the DL structure to.
	 */
	private static void export(DLStruct dlst, PrintWriter out) throws IOException
	{
		export(null,dlst,out,-1);
		out.close();
	}
	
	private static void export(String type, DLStruct dls, PrintWriter out, int tabs) throws IOException
	{
		String tabstr = "";
		
		for (int i = 0; i < tabs; i++)
			tabstr += "\t";
		
		DLValue dlv;
		
		if (type != null)
		{
			out.print(tabstr+type);
			dlv = dls.getValue();
			if (dlv != null)
			{
				switch (dlv.getType())
				{
					case TYPE_NUMBER_ARRAY:
						double[] d = ((double[])dlv.getVal());
						if (d.length == 0)
						{
							//do nothing
						}
						else if (d.length == 1)
						{
							out.print(" ");
							if ((d[0]-(long)d[0])!=0.0)
								out.print(d[0]);
							else
								out.print((long)d[0]);
						}
						else
						{
							out.print(" [");
							for (int i = 0; i < d.length; i++)
							{
								String n = i == d.length-1 ? "]" : ", ";
								if ((d[i]-(long)d[i])!=0.0)
									out.print(d[i]+n);
								else
									out.print((long)d[i]+n);
							}
						}
						out.println();
						break;
						
					case TYPE_STRING_ARRAY:
						String[] stra = ((String[])dlv.getVal());
						if (stra.length == 0)
						{
							// do nothing
						}
						else if (stra.length == 1)
						{
							out.print(" \""+Common.withEscChars(((String[])dlv.getVal())[0])+"\"");						
						}
						else
						{
							out.print(" [");
							for (int i = 0; i < stra.length; i++)
							{
								String n = i == stra.length-1 ? "]" : ", ";
								out.print("\""+Common.withEscChars(stra[i])+"\""+n);						
							}
						}
						out.println();
						break;
	
				}
			}
			else
				out.println();
		
			out.println(tabstr+"{");
		}
		
		for (String s : dls.getAllFields())
		{
			dlv = dls.getValue(s);
			switch (dlv.getType())
			{
				case TYPE_NUMBER_ARRAY:
					double[] d = ((double[])dlv.getVal());
					if (d.length == 0)
					{
						out.print(tabstr+"\t"+s+" 0");
					}
					else if (d.length == 1)
					{
						if ((d[0]-(long)d[0])!=0.0)
							out.print(tabstr+"\t"+s+" "+d[0]);
						else
							out.print(tabstr+"\t"+s+" "+(long)d[0]);
					}
					else
					{
						out.print(tabstr+"\t"+s+" "+"[");
						for (int i = 0; i < d.length; i++)
						{
							String n = i == d.length-1 ? "]" : ", ";
							if ((d[i]-(long)d[i])!=0.0)
								out.print(d[i]+n);
							else
								out.print((long)d[i]+n);
						}
					}
					out.print(";");
					out.println();
					break;
					
				case TYPE_STRING_ARRAY:
					String[] stra = ((String[])dlv.getVal());
					if (stra.length == 0)
					{
						out.print(tabstr+"\t"+s+" \"\"");
					}
					else if (stra.length == 1)
					{
						out.print(tabstr+"\t"+s+" \""+Common.withEscChars(((String[])dlv.getVal())[0])+"\"");						
					}
					else
					{
						out.print(tabstr+"\t"+s+" "+"[");
						for (int i = 0; i < stra.length; i++)
						{
							String n = i == stra.length-1 ? "]" : ", ";
							out.print("\""+Common.withEscChars(stra[i])+"\""+n);						
						}
					}
					out.print(";");
					out.println();
					break;
					
				case DLValue.TYPE_ID:
					out.print(tabstr+"\t"+s+" "+((String[])dlv.getVal())[0]);
					break;
			}
		}

//		out.println();
		
		Iterator<String> it = dls.keyIterator();
		while (it.hasNext())
		{
			String s = it.next();
			for (DLStruct st : dls.get(s))
			{
				export(s,st,out,tabs+1);
			}
		}
		
		if (type != null)
			out.println(tabstr+"}");
		out.println();
	}
}
