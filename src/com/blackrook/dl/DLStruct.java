/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.blackrook.commons.Common;
import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.hash.HashedQueueMap;
import com.blackrook.commons.linkedlist.Queue;

/**
 * The data structure abstraction for a set of DataLang Structures.
 * @author Matthew Tropiano
 */
public class DLStruct extends HashedQueueMap<String,DLStruct>
{
	public static final int DEFAULT_CAPACITY = 5;
	public static final float DEFAULT_REHASH = 0.85f;
	
	/** Blank queue. */
	protected static final Queue<DLStruct> BLANK_QUEUE = new Queue<DLStruct>();
	
	/** Total number of structs. */
	protected int numStructs;
	/** The field table. */
	protected HashMap<String,DLValue> fieldTable;
	
	/** Total number of fields. */
	protected int numFields;
	/** The value associated with this struct. */
	protected DLValue structValue;


	/**
	 * Construct a new DLStruct.
	 */
	public DLStruct()
	{
		this(DEFAULT_CAPACITY,DEFAULT_REHASH);
	}

	/**
	 * Construct a new DLStruct.
	 * @param rehash	the rehashing ratio of the table.
	 */
	public DLStruct(float rehash)
	{
		this(DEFAULT_CAPACITY,rehash);
	}

	/**
	 * Construct a new DLStruct.
	 * @param capacity	the capacity of the table
	 */
	public DLStruct(int capacity)
	{
		this(capacity,DEFAULT_REHASH);
	}

	/**
	 * Construct a new DLStruct.
	 * @param capacity	the capacity of the table
	 * @param rehash	the rehashing ratio of the table.
	 */
	public DLStruct(int capacity, float rehash)
	{
		super(capacity,rehash);
		fieldTable = new HashMap<String,DLValue>(capacity,rehash);
	}
	
	/**
	 * Check to see if this DLStruct contains a specific field.
	 * @param fieldname	the name of the field.
	 * @return			true if the field exists in this struct, false otherwise.
	 */
	public boolean containsField(String fieldname)
	{
		return fieldTable.containsKey(fieldname);
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the struct.
	 */
	void set(DLValue value)
	{
		structValue = value;
	}
	
	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the struct.
	 */
	public void put(double value)
	{
		set(new DLValue(value));
	}
	
	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the struct.
	 */
	public void put(String value)
	{
		if (value == null)
			set(null);
		else
			set(new DLValue(value));
	}

	/**
	 * Sets a value to this DLStruct.
	 * NOTE: "true" booleans are added as 1, "false" as 0.
	 * @param value			the value of the field.
	 */
	public void put(boolean[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] d = new double[value.length];
			int i = 0;
			for (boolean b : value)
				d[i++] = b?1:0;
			set(new DLValue(d));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(char[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			set(new DLValue(val));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(short[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			set(new DLValue(val));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(int[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			set(new DLValue(val));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(float[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			set(new DLValue(val));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(long[] value)
	{
		if (value == null)
			set(null);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			set(new DLValue(val));
		}
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(double[] value)
	{
		if (value == null)
			set(null);
		else
			set(new DLValue(value));
	}

	/**
	 * Sets a value to this DLStruct.
	 * @param value			the value of the field.
	 */
	public void put(String[] value)
	{
		if (value == null)
			set(null);
		else
			set(new DLValue(value));
	}

	/**
	 * Appends a value to this struct.
	 * Appending a boolean to a list of Strings will cast it to a String.
	 * @param value			the value to append to the struct.
	 */
	public void append(boolean value)
	{
		append(value?1:0);
	}
	
	/**
	 * Appends a value to this struct.
	 * Appending a number to a list of Strings will cast it to a String.
	 * @param value			the value to append to the struct.
	 */
	public void append(double value)
	{
		structValue.append(value);
	}
	
	/**
	 * Appends a value to this struct.
	 * Appending a String to a list of numbers will cast the entire field as a list of Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(String value)
	{
		if (value == null) return;
		
		structValue.append(value);
	}

	/**
	 * Appends a series of values to this struct.
	 * Appending booleans to a list of Strings will cast them to a Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(boolean[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i]?1:0;
		append(d);
	}
	
	/**
	 * Appends a series of values to this struct.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(char[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(d);
	}
	
	/**
	 * Appends a series of values to this struct.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(int[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(d);
	}

	/**
	 * Appends a series of values to this struct.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(float[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(d);
	}

	/**
	 * Appends a series of values to this struct.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(long[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(d);
	}

	/**
	 * Appends a series of values to this struct.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(double[] value)
	{
		if (value == null) return;
		
		structValue.append(value);
	}
	
	/**
	 * Appends a series of values to this struct.
	 * Appending Strings to a list of numbers will cast the entire field as a list of Strings.
	 * @param value			the value to append to the struct.
	 */
	public void append(String[] value)
	{
		if (value == null) return;
		
		structValue.append(value);
	}

	/**
	 * Returns this struct's value as a double.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a double or NaN if not found.
	 */
	public double getDouble()
	{
		DLValue value = getValue();
		if (value == null)
			return Double.NaN;
		return value.getDouble();
	}
	
	/**
	 * Returns this struct's value as an array of doubles.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as doubles or null if not found.
	 */
	public double[] getDoubleArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getDoubleArray();
	}

	/**
	 * Returns this struct's value as an array of doubles of specific length.
	 * @param length					the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> doubles or null if not found.
	 */
	public double[] getDoubleArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getDoubleArray(length);
	}

	/**
	 * Returns this struct's value as a String.
	 * @return	the value of the field as a string or null if not found.
	 */
	public String getString()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getString();
	}

	/**
	 * Returns this struct's value as a String array.
	 * @return	the value of the field as strings or null if not found.
	 */
	public String[] getStringArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getStringArray();
	}

	/**
	 * Returns this struct's value as an array of Strings of specific length.
	 * @param length		the resultant array length.
	 * @return				the value of the field as an array of <i>length</i> strings or null if not found.
	 */
	public String[] getStringArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getStringArray(length);
	}

	/**
	 * Returns this struct's value as a float.
	 * @return				the value of the field as a float or NaN if not found.
	 */
	public float getFloat()
	{
		DLValue value = getValue();
		if (value == null)
			return Float.NaN;
		return value.getFloat();
	}	
	/**
	 * Returns this struct's value as a float array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as floats or null if not found.
	 */
	public float[] getFloatArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getFloatArray();
	}	

	/**
	 * Returns this struct's value as an array of floats of specific length.
	 * @param length					the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> floats or null if not found.
	 */
	public float[] getFloatArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getFloatArray(length);
	}	

	/**
	 * Returns this struct's value as a long.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a long or NaN if not found.
	 */
	public long getLong()
	{
		DLValue value = getValue();
		if (value == null)
			return 0;
		return value.getLong();
	}	
	
	/**
	 * Returns this struct's value as a long array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as longs or null if not found.
	 */
	public long[] getLongArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getLongArray();
	}	

	/**
	 * Returns this struct's value as a long array.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field an array of <i>length</i> longs or null if not found.
	 */
	public long[] getLongArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getLongArray(length);
	}	

	/**
	 * Returns this struct's value as an int.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an int or 0 if not found.
	 */
	public int getInt()
	{
		DLValue value = getValue();
		if (value == null)
			return 0;
		return value.getInt();
	}	

	/**
	 * Returns this struct's value as an int array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as ints or null if not found.
	 */
	public int[] getIntArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getIntArray();
	}	

	/**
	 * Returns this struct's value as an array of ints of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> ints or null if not found.
	 */
	public int[] getIntArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getIntArray(length);
	}	

	/**
	 * Returns this struct's value as a short.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a short or 0 if not found.
	 */
	public short getShort()
	{
		DLValue value = getValue();
		if (value == null)
			return 0;
		return value.getShort();
	}	

	/**
	 * Returns this struct's value as a short array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as shorts or null if not found.
	 */
	public short[] getShortArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getShortArray();
	}	
	
	/**
	 * Returns this struct's value as an array of shorts of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> shorts or null if not found.
	 */
	public short[] getShortArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getShortArray(length);
	}	
	
	/**
	 * Returns this struct's value as a char.
	 * @return				the value of the field as a char or 0 if not found or if the field is an empty string.
	 */
	public char getChar()
	{
		DLValue v = structValue;
		if (v == null)
			return '\0';
		return v.getChar();
	}	

	/**
	 * Returns this struct's value as a char array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as chars or null if not found.
	 */
	public char[] getCharArray()
	{
		DLValue v = structValue;
		if (v == null)
			return null;
		return v.getCharArray();
	}	
	
	/**
	 * Returns this struct's value as an array of chars of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> chars or null if not found.
	 */
	public char[] getCharArray(int length)
	{
		DLValue v = structValue;
		if (v == null)
			return null;
		return v.getCharArray(length);
	}	
	
	/**
	 * Returns this struct's value as a byte.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a byte or 0 if not found.
	 */
	public byte getByte()
	{
		DLValue value = getValue();
		if (value == null)
			return 0;
		return value.getByte();
	}	

	/**
	 * Returns this struct's value as a byte array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as bytes or null if not found.
	 */
	public byte[] getByteArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getByteArray();
	}	

	/**
	 * Returns this struct's value as an array of bytes of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> bytes or null if not found.
	 */
	public byte[] getByteArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getByteArray(length);
	}	

	/**
	 * Returns this struct's value as a boolean.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				true if nonzero number or false if zero or Not-A-Number or not found.
	 */
	public boolean getBoolean()
	{
		DLValue value = getValue();
		if (value == null)
			return false;
		return value.getBoolean();
	}	

	/**
	 * Returns this struct's value as a boolean array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as booleans or null if not found.
	 */
	public boolean[] getBooleanArray()
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getBooleanArray();
	}	

	/**
	 * Returns this struct's value as an array of booleans of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> booleans or null if not found.
	 */
	public boolean[] getBooleanArray(int length)
	{
		DLValue value = getValue();
		if (value == null)
			return null;
		return value.getBooleanArray(length);
	}	

	public DLValue getValue()
	{
		return structValue;		
	}
	
	/**
	 * Returns the names of all of the fields in this struct in an array of Strings.
	 */
	public String[] getAllFields()
	{
		String[] out = new String[fieldTable.size()];
		int i = 0;
		for (ObjectPair<String,DLValue> hp : fieldTable)
			out[i++] = hp.getKey();
		return out;
	}
	
	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 * @since 2.0.1, made public.
	 */
	public void put(String fieldName, DLValue value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
			fieldTable.put(fieldName, value);
	}

	/**
	 * Adds a field value to this DLStruct.
	 * NOTE: "true" booleans are added as 1, "false" as 0.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, boolean[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] d = new double[value.length];
			int i = 0;
			for (boolean b : value)
				d[i++] = b?1:0;
			put(fieldName,new DLValue(d));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, char[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			put(fieldName,new DLValue(val));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, short[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			put(fieldName,new DLValue(val));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, int[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			put(fieldName,new DLValue(val));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, float[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			put(fieldName,new DLValue(val));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, long[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
		{
			double[] val = new double[value.length];
			for (int i = 0; i < value.length; i++)
				val[i] = value[i];
			put(fieldName,new DLValue(val));
		}
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, double[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
			put(fieldName,new DLValue(value));
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, String[] value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
			put(fieldName,new DLValue(value));
	}

	/**
	 * Adds a field value to this DLStruct.
	 * NOTE: "true" booleans are added as 1, "false" as 0.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, boolean value)
	{
		put(fieldName,new DLValue(value?1:0));
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, double value)
	{
		put(fieldName,new DLValue(value));
	}

	/**
	 * Adds a field value to this DLStruct.
	 * @param fieldName		the name of the field.
	 * @param value			the value of the field.
	 */
	public void put(String fieldName, String value)
	{
		if (value == null)
			fieldTable.removeUsingKey(fieldName);
		else
			put(fieldName,new DLValue(value));
	}

	/**
	 * Appends a value to a field.
	 * Appending a boolean to a list of Strings will cast it to a String.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, boolean value)
	{
		append(fieldName,value?1:0);
	}

	/**
	 * Appends a value to a field.
	 * Appending a number to a list of Strings will cast it to a String.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, double value)
	{
		if (containsField(fieldName))
			fieldTable.get(fieldName).append(value);
		else
			put(fieldName,value);
	}

	/**
	 * Appends a value to a field.
	 * Appending a String to a list of numbers will cast the entire field as a list of Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, String value)
	{
		if (value == null) return;
		
		if (containsField(fieldName))
			fieldTable.get(fieldName).append(value);
		else
			put(fieldName,value);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending booleans to a list of Strings will cast them to a Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, boolean[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i]?1:0;
		append(fieldName,d);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, char[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(fieldName,d);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, int[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(fieldName,d);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, float[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(fieldName,d);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, long[] value)
	{
		if (value == null) return;
		
		double[] d = new double[value.length];
		for (int i = 0; i < value.length; i++)
			d[i] = value[i];
		append(fieldName,d);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending numbers to a list of Strings will cast them to Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, double[] value)
	{
		if (value == null) return;
		
		if (containsField(fieldName))
			fieldTable.get(fieldName).append(value);
		else
			put(fieldName,value);
	}

	/**
	 * Appends a series of values to a field.
	 * Appending Strings to a list of numbers will cast the entire field as a list of Strings.
	 * @param fieldName		the name of the field.
	 * @param value			the value to append to the field.
	 */
	public void append(String fieldName, String[] value)
	{
		if (value == null) return;
		
		if (containsField(fieldName))
			fieldTable.get(fieldName).append(value);
		else
			put(fieldName,value);
	}

	/**
	 * Returns a field's value as a double.
	 * @param fieldName					the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a double or NaN if not found.
	 */
	public double getDouble(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return Double.NaN;
		return value.getDouble();
	}
	
	/**
	 * Returns a field's value as an array of doubles.
	 * @param fieldName					the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as doubles or null if not found.
	 */
	public double[] getDoubleArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getDoubleArray();
	}

	/**
	 * Returns a field's value as an array of doubles of specific length.
	 * @param length					the resultant array length.
	 * @param fieldName					the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> doubles or null if not found.
	 */
	public double[] getDoubleArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getDoubleArray(length);
	}

	/**
	 * Returns a field's value as a double.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as a double or defaultVal if not found.
	 */
	public double getDouble(String fieldName, double defaultVal)
	{
		double s = getDouble(fieldName);
		return (!Double.isNaN(s)?s:defaultVal);
	}

	/**
	 * Returns a field's value as a double array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as doubles or defaultVal if not found.
	 */
	public double[] getDoubleArray(String fieldName, double[] defaultVal)
	{
		double[] s = getDoubleArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of doubles of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as an array of <i>length</i> doubles or defaultVal if not found.
	 */
	public double[] getDoubleArray(int length, String fieldName, double[] defaultVal)
	{
		double[] s = getDoubleArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a String.
	 * @param fieldName					the name of the field
	 * @return	the value of the field as a string or null if not found.
	 */
	public String getString(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getString();
	}

	/**
	 * Returns a field's value as a String array.
	 * @param fieldName					the name of the field
	 * @return	the value of the field as strings or null if not found.
	 */
	public String[] getStringArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getStringArray();
	}

	/**
	 * Returns a field's value as an array of Strings of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @return				the value of the field as an array of <i>length</i> strings or null if not found.
	 */
	public String[] getStringArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getStringArray(length);
	}

	/**
	 * Returns a field's value as a String.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as a string or defaultVal if not found.
	 */
	public String getString(String fieldName, String defaultVal)
	{
		String s = getString(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a String.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as strings or defaultVal if not found.
	 */
	public String[] getStringArray(String fieldName, String[] defaultVal)
	{
		String[] s = getStringArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of Strings of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as an array of <i>length</i> strings or defaultVal if not found.
	 */
	public String[] getStringArray(int length, String fieldName, String[] defaultVal)
	{
		String[] s = getStringArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a float.
	 * @param fieldName		the name of the field
	 * @return				the value of the field as a float or NaN if not found.
	 */
	public float getFloat(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return Float.NaN;
		return value.getFloat();
	}	
	/**
	 * Returns a field's value as a float array.
	 * @param fieldName					the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as floats or null if not found.
	 */
	public float[] getFloatArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getFloatArray();
	}	

	/**
	 * Returns a field's value as an array of floats of specific length.
	 * @param length					the resultant array length.
	 * @param fieldName					the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> floats or null if not found.
	 */
	public float[] getFloatArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getFloatArray(length);
	}	

	/**
	 * Returns a field's value as a float.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as a float or defaultVal if not found.
	 */
	public float getFloat(String fieldName, float defaultVal)
	{
		float s = getFloat(fieldName);
		return (!Float.isNaN(s)?s:defaultVal);
	}

	/**
	 * Returns a field's value as a float array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as floats or defaultVal if not found.
	 */
	public float[] getFloatArray(String fieldName, float[] defaultVal)
	{
		float[] s = getFloatArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of floats of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return	the value of the field as an array of <i>length</i> floats or defaultVal if not found.
	 */
	public float[] getFloatArray(int length, String fieldName, float[] defaultVal)
	{
		float[] s = getFloatArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a long.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a long or NaN if not found.
	 */
	public long getLong(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return 0;
		return value.getLong();
	}	
	
	/**
	 * Returns a field's value as a long array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as longs or null if not found.
	 */
	public long[] getLongArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getLongArray();
	}	

	/**
	 * Returns a field's value as a long array.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field an array of <i>length</i> longs or null if not found.
	 */
	public long[] getLongArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getLongArray(length);
	}	

	/**
	 * Returns a field's value as a long.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as a long or defaultVal if not found.
	 */
	public long getLong(String fieldName, long defaultVal)
	{
		long s = getLong(fieldName);
		return (!Float.isNaN(s)?s:defaultVal);
	}

	/**
	 * Returns a field's value as a long array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as longs or defaultVal if not found.
	 */
	public long[] getLongArray(String fieldName, long[] defaultVal)
	{
		long[] s = getLongArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a long array.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field an array of <i>length</i> longs or defaultVal if not found.
	 */
	public long[] getLongArray(int length, String fieldName, long[] defaultVal)
	{
		long[] s = getLongArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an int.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an int or 0 if not found.
	 */
	public int getInt(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return 0;
		return value.getInt();
	}	

	/**
	 * Returns a field's value as an int array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as ints or null if not found.
	 */
	public int[] getIntArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getIntArray();
	}	

	/**
	 * Returns a field's value as an array of ints of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> ints or null if not found.
	 */
	public int[] getIntArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getIntArray(length);
	}	

	/**
	 * Returns a field's value as an int.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a int or defaultVal if not found.
	 */
	public int getInt(String fieldName, int defaultVal)
	{
		double s = getDouble(fieldName);
		return (!Double.isNaN(s)?(int)s:defaultVal);
	}

	/**
	 * Returns a field's value as an int array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as ints or defaultVal if not found.
	 */
	public int[] getIntArray(String fieldName, int[] defaultVal)
	{
		int[] s = getIntArray(fieldName);
		return (s!=null?s:defaultVal);
	}
	
	/**
	 * Returns a field's value as an array of ints of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> ints or null if not found.
	 */
	public int[] getIntArray(int length, String fieldName, int[] defaultVal)
	{
		int[] s = getIntArray(length, fieldName);
		return (s!=null?s:defaultVal);
	}	

	/**
	 * Returns a field's value as a short.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a short or 0 if not found.
	 */
	public short getShort(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return 0;
		return value.getShort();
	}	

	/**
	 * Returns a field's value as a short array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as shorts or null if not found.
	 */
	public short[] getShortArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getShortArray();
	}	
	
	/**
	 * Returns a field's value as an array of shorts of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> shorts or null if not found.
	 */
	public short[] getShortArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getShortArray(length);
	}	
	
	/**
	 * Returns a field's value as a short.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as a short or defaultVal if not found.
	 */
	public short getShort(String fieldName, short defaultVal)
	{
		double s = getDouble(fieldName);
		return (!Double.isNaN(s)?(short)s:defaultVal);
	}

	/**
	 * Returns a field's value as a short array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as shorts or defaultVal if not found.
	 */
	public short[] getShortArray(String fieldName, short[] defaultVal)
	{
		short[] s = getShortArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of shorts of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> shorts or null if not found.
	 */
	public short[] getShortArray(int length, String fieldName, short[] defaultVal)
	{
		short[] s = getShortArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}	
	
	/**
	 * Returns a field's value as a char.
	 * @param fieldName		the name of the field
	 * @return				the value of the field as a char or 0 if not found or if the field is an empty string.
	 */
	public char getChar(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return '\0';
		return value.getChar();
	}	

	/**
	 * Returns a field's value as a char array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as chars or null if not found.
	 */
	public char[] getCharArray(String fieldName)
	{
		DLValue v = getValue(fieldName);
		if (v == null)
			return null;
		return v.getCharArray();
	}	
	
	/**
	 * Returns a field's value as an array of chars of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> chars or null if not found.
	 */
	public char[] getCharArray(int length, String fieldName)
	{
		DLValue v = getValue(fieldName);
		if (v == null)
			return null;
		return v.getCharArray(length);
	}	
	
	/**
	 * Returns a field's value as a char.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as a char or defaultVal if not found.
	 */
	public char getChar(String fieldName, char defaultVal)
	{
		String s = getString(fieldName);
		if (s != null) return s.charAt(0);
		return defaultVal;
	}

	/**
	 * Returns a field's value as a char array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as chars or defaultVal if not found.
	 */
	public char[] getCharArray(String fieldName, char[] defaultVal)
	{
		char[] s = getCharArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of chars of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> chars or null if not found.
	 */
	public char[] getCharArray(int length, String fieldName, char[] defaultVal)
	{
		char[] s = getCharArray(length, fieldName);
		return (s!=null?s:defaultVal);
	}	
	
	/**
	 * Returns a field's value as a byte.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a byte or 0 if not found.
	 */
	public byte getByte(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return 0;
		return value.getByte();
	}	

	/**
	 * Returns a field's value as a byte array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as bytes or null if not found.
	 */
	public byte[] getByteArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getByteArray();
	}	

	/**
	 * Returns a field's value as an array of bytes of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> bytes or null if not found.
	 */
	public byte[] getByteArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getByteArray(length);
	}	

	/**
	 * Returns a field's value as a byte.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as a byte or defaultVal if not found.
	 */
	public byte getByte(String fieldName, byte defaultVal)
	{
		double s = getDouble(fieldName);
		return (!Double.isNaN(s)?(byte)s:defaultVal);
	}

	/**
	 * Returns a field's value as a byte array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as bytes or defaultVal if not found.
	 */
	public byte[] getByteArray(String fieldName, byte[] defaultVal)
	{
		byte[] s = getByteArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of bytes of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as an array of <i>length</i> bytes or defaultVal if not found.
	 */
	public byte[] getByteArray(int length, String fieldName, byte[] defaultVal)
	{
		byte[] s = getByteArray(length,fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as a boolean.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				true if nonzero number or false if zero or Not-A-Number or not found.
	 */
	public boolean getBoolean(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return false;
		return value.getBoolean();
	}	

	/**
	 * Returns a field's value as a boolean array.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as booleans or null if not found.
	 */
	public boolean[] getBooleanArray(String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getBooleanArray();
	}	

	/**
	 * Returns a field's value as an array of booleans of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> booleans or null if not found.
	 */
	public boolean[] getBooleanArray(int length, String fieldName)
	{
		DLValue value = getValue(fieldName);
		if (value == null)
			return null;
		return value.getBooleanArray(length);
	}	

	/**
	 * Returns a field's value as a boolean.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				true if nonzero number or defaultVal if zero or Not-A-Number or not found.
	 */
	public boolean getBoolean(String fieldName, boolean defaultVal)
	{
		double d = getDouble(fieldName);
		if (Double.isNaN(d))
			return defaultVal;
		return d != 0;
	}

	/**
	 * Returns a field's value as a boolean array.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as booleans or defaultVal if not found.
	 */
	public boolean[] getBooleanArray(String fieldName, boolean[] defaultVal)
	{
		boolean[] s = getBooleanArray(fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Returns a field's value as an array of booleans of specific length.
	 * @param length		the resultant array length.
	 * @param fieldName		the name of the field
	 * @param defaultVal	if this field doesn't exist, return this.
	 * @return				the value of the field as an array of <i>length</i> booleans or defaultVal if not found.
	 */
	public boolean[] getBooleanArray(int length, String fieldName, boolean[] defaultVal)
	{
		boolean[] s = getBooleanArray(length, fieldName);
		return (s!=null?s:defaultVal);
	}

	/**
	 * Gets the value of a field.
	 * @since 2.0.1, made public.
	 */
	public DLValue getValue(String fieldName)
	{
		DLValue value = fieldTable.get(fieldName);
		if (value == null) return null;
		while (value != null && value.type == DLValue.TYPE_ID)
			value = fieldTable.get(((String[])value.val)[0]);
		return value;		
	}
	
	/**
	 * Returns the number of distinct fields in this struct. 
	 */
	public int getNumberOfFields()
	{
		return numFields;
	}
	
	/**
	 * Returns a full copy of this structure. Kinda expensive and recursive.
	 */
	public DLStruct copy()
	{
		DLStruct out = new DLStruct(storageArray.length, rehashRatio);
		copyInto(out);
		return out;
	}
	
	/**
	 * Returns a full copy of this structure. Into another existing struct.
	 * Kinda expensive and recursive.
	 */
	public void copyInto(DLStruct dls)
	{
		if (getValue() != null)
			dls.set(getValue().copy());
		for (ObjectPair<String,DLValue> hp : fieldTable)
			dls.put(new String(hp.getKey()),hp.getValue().copy());
		
		for (ObjectPair<String, Queue<DLStruct>> hp : this)
			for (DLStruct str : hp.getValue())
				dls.putStruct(hp.getKey(),str.copy());
	}

	/**
	 * Returns a Queue of all of the structs of a particular name
	 * in the order in which they were added to the struct.
	 * Will never return null.
	 */
	public Queue<DLStruct> getStructs(String typename)
	{
		Queue<DLStruct> out = get(typename);
		return out != null ? out : BLANK_QUEUE;
	}

	/**
	 * Adds a new, empty DLStruct of type typename.
	 * @param typename	the type of the struct.
	 * @return			a reference to the created struct.
	 */
	public DLStruct putStruct(String typename)
	{
		return putStruct(typename,
				new DLStruct(DLStruct.DEFAULT_CAPACITY, DLStruct.DEFAULT_REHASH));
	}

	/**
	 * Adds a DLStruct under the type typename.
	 * @param typename	the type of the struct.
	 * @param dls		the struct to add.
	 * @return			a reference to the added struct.
	 */
	public DLStruct putStruct(String typename, DLStruct dls)
	{
		enqueue(typename, dls);
		numStructs++;
		return dls;
	}

	/**
	 * Returns true if this table contains at least one DLStruct of type <i>typename</i>.
	 */
	public boolean containsType(String typename)
	{
		return containsKey(typename);
	}

	/**
	 * Returns the string representation of this struct.
	 * @since 2.3.0, this has a proper {@link #toString()} method.
	 */
	@Override
	public String toString()
	{
		StringWriter sw = new StringWriter();
		BufferedWriter bw = new BufferedWriter(sw);
		try {
			DLWriter.write(this, bw);
		} catch (IOException e) {
			return e.getMessage();
			} finally {
			Common.close(bw);
		}
		return sw.getBuffer().toString(); 
	}

}
