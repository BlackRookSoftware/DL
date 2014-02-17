/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl;

import java.util.Arrays;

class DLValue 
{
	public static final int
	TYPE_NUMBER_ARRAY = 0,
	TYPE_STRING_ARRAY = 1,
	TYPE_ID = 2;
	
	int type;
	Object val;
	
	DLValue(double d)
	{
		this(new double[]{d});
	}
	
	DLValue(double[] d)
	{
		type = TYPE_NUMBER_ARRAY;
		val = d;
	}

	DLValue(String s)
	{
		this(new String[]{s});
	}

	DLValue(String s, boolean id)
	{
		if (id)
			type = TYPE_ID;
		else
			type = TYPE_STRING_ARRAY;
		val = new String[]{s};
	}

	DLValue(String[] s)
	{
		type = TYPE_STRING_ARRAY;
		val = s;
	}
	
	public void append(double d)
	{
		append(new double[]{d});
	}
	
	public void append(double[] d)
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] da = (double[])val;
				double[] db = new double[da.length+d.length];
				System.arraycopy(da,0,db,0,da.length);
				for (int i = 0; i < d.length; i++)
					db[da.length+i] = d[i];
				val = db;
			}	
				break;
			case TYPE_STRING_ARRAY:
			{
				String[] sa = (String[])val;
				String[] sb = new String[sa.length+d.length];
				System.arraycopy(sa,0,sb,0,sa.length);
				for (int i = 0; i < d.length; i++)
					sb[sa.length+i] = d[i]+"";
				val = sb;
			}
				break;
			case TYPE_ID:
			default:
				break;
		}
	}

	public void append(String s)
	{
		append(new String[]{s});
	}
	
	public void append(String[] s)
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] da = (double[])val;
				String[] db = new String[da.length+s.length];
				for (int i = 0; i < da.length; i++)
					db[i] = da[i]+"";
				for (int i = 0; i < s.length; i++)
					db[da.length+i] = s[i];
				val = db;
				type = TYPE_STRING_ARRAY;
			}	
				break;
			case TYPE_STRING_ARRAY:
			{
				String[] sa = (String[])val;
				String[] sb = new String[sa.length+s.length];
				System.arraycopy(sa,0,sb,0,sa.length);
				for (int i = 0; i < s.length; i++)
					sb[sa.length+i] = s[i];
				val = sb;
			}
				break;
			case TYPE_ID:
			default:
				break;
		}
	}

	
	
	/**
	 * Returns this value's value as a double.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a double or NaN if not found.
	 */
	public double getDouble()
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
				return ((double[])val)[0];
			case TYPE_STRING_ARRAY:
			default:
				String s = ((String[])val)[0];
				if (s.equals(""))
					return 0;
				else
					return Double.parseDouble(((String[])val)[0]);
		}
	}

	/**
	 * Returns this value's value as an array of doubles.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as doubles or null if not found.
	 */
	public double[] getDoubleArray()
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] v = (double[])val;
				double[] o = new double[v.length];
				System.arraycopy(v, 0, o, 0, v.length);
				return o;
			}
			case TYPE_STRING_ARRAY:
			default:
			{
				String[] st = (String[])val;
				double[] out = new double[st.length];
				for (int i = 0; i < st.length; i++)
				{
					if (st[i].equals(""))
						out[i] = 0;
					else
						out[i] = Double.parseDouble(st[i]);
				}
				return out;
			}
		}
	}

	/**
	 * Returns this value's value as an array of doubles of specific length.
	 * @param length					the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> doubles or null if not found.
	 */
	public double[] getDoubleArray(int length)
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] v = (double[])val;
				double[] o = new double[length];
				System.arraycopy(v, 0, o, 0, Math.min(v.length,length));
				return o;
			}
			case TYPE_STRING_ARRAY:
			default:
			{
				String[] st = (String[])val;
				double[] out = new double[length];
				for (int i = 0; i < Math.min(st.length,length); i++)
					out[i] = Double.parseDouble(st[i]);
				return out;
			}
		}
	}

	/**
	 * Returns this value's value as a String.
	 * @return	the value of the field as a string or null if not found.
	 */
	public String getString()
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double temp = ((double[])val)[0];
				double d = temp-(int)temp;
				return (d != 0.0) ? temp+"" : (int)temp+""; 
			}
			case TYPE_STRING_ARRAY:
				return ((String[])val)[0];
			default:
				return val.toString();
		}
	}

	/**
	 * Returns this value's value as a String array.
	 * @return	the value of the field as strings or null if not found.
	 */
	public String[] getStringArray()
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] da = (double[])val;
				String[] out = new String[da.length];
				for (int i = 0; i < da.length; i++)
				{
					double temp = da[i];
					double d = temp-(int)temp;
					out[i] = (d != 0.0) ? temp+"" : (int)temp+""; 
				}
				return out;
			}
			case TYPE_STRING_ARRAY:
			{
				String[] s = (String[])val;
				String[] o = new String[s.length]; 
				for (int i = 0; i < s.length; i++)
					o[i] = new String(s[i]); 
				return o;
			}
			default:
				return new String[]{val.toString()};
		}
	}

	/**
	 * Returns this value's value as an array of Strings of specific length.
	 * @param length		the resultant array length.
	 * @return				the value of the field as an array of <i>length</i> strings or null if not found.
	 */
	public String[] getStringArray(int length)
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
			{
				double[] da = (double[])val;
				String[] out = new String[length];
				for (int i = 0; i < Math.min(length, da.length); i++)
				{
					double temp = da[i];
					double d = temp-(int)temp;
					out[i] = (d != 0.0) ? temp+"" : (int)temp+""; 
				}
				return out;
			}
			case TYPE_STRING_ARRAY:
			{
				String[] s = (String[])val;
				String[] o = new String[length]; 
				for (int i = 0; i < Math.min(length, s.length); i++)
					o[i] = new String(s[i]); 
				return o;
			}
			default:
				return new String[]{val.toString()};
		}
	}

	/**
	 * Returns this value's value as a float.
	 * @return				the value of the field as a float or NaN if not found.
	 */
	public float getFloat()
	{
		return (float)getDouble();
	}

	/**
	 * Returns this value's value as a float array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as floats or null if not found.
	 */
	public float[] getFloatArray()
	{
		double[] da = getDoubleArray();
		float[] out = new float[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (float)d;
		return out;
	}

	/**
	 * Returns this value's value as an array of floats of specific length.
	 * @param length					the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> floats or null if not found.
	 */
	public float[] getFloatArray(int length)
	{
		double[] da = getDoubleArray();
		if (da == null)
			return null;
		float[] out = new float[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (float)da[i];
		return out;
	}

	/**
	 * Returns this value's value as a long.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a long or NaN if not found.
	 */
	public long getLong()
	{
		return (long)getDouble();
	}

	/**
	 * Returns this value's value as a long array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as longs or null if not found.
	 */
	public long[] getLongArray()
	{
		double[] da = getDoubleArray();
		long[] out = new long[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (long)d;
		return out;
	}

	/**
	 * Returns this value's value as a long array.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field an array of <i>length</i> longs or null if not found.
	 */
	public long[] getLongArray(int length)
	{
		double[] da = getDoubleArray();
		if (da == null)
			return null;
		long[] out = new long[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (long)da[i];
		return out;
	}

	/**
	 * Returns this value's value as an int.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an int or 0 if not found.
	 */
	public int getInt()
	{
		return (int)getDouble();
	}

	/**
	 * Returns this value's value as an int array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as ints or null if not found.
	 */
	public int[] getIntArray()
	{
		double[] da = getDoubleArray();
		int[] out = new int[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (int)d;
		return out;
	}

	/**
	 * Returns this value's value as an array of ints of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> ints or null if not found.
	 */
	public int[] getIntArray(int length)
	{
		double[] da = getDoubleArray();
		if (da == null)
			return null;
		int[] out = new int[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (int)da[i];
		return out;
	}

	/**
	 * Returns this value's value as a short.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a short or 0 if not found.
	 */
	public short getShort()
	{
		return (short)getDouble();
	}

	/**
	 * Returns this value's value as a short array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as shorts or null if not found.
	 */
	public short[] getShortArray()
	{
		double[] da = getDoubleArray();
		short[] out = new short[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (short)d;
		return out;
	}

	/**
	 * Returns this value's value as an array of shorts of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> shorts or null if not found.
	 */
	public short[] getShortArray(int length)
	{
		double[] da = getDoubleArray();
		if (da == null)
			return null;
		short[] out = new short[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (short)da[i];
		return out;
	}

	/**
	 * Returns this value's value as a char.
	 * @return				the value of the field as a char or 0 if not found or if the field is an empty string.
	 */
	public char getChar()
	{
		if (type == DLValue.TYPE_NUMBER_ARRAY)
			return (char)getDouble();
		else
		{
			String s = getString();
			return s.length() > 0? s.charAt(0) : '\0';
		}
	}

	/**
	 * Returns this value's value as a char array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as chars or null if not found.
	 */
	public char[] getCharArray()
	{
		if (type == DLValue.TYPE_NUMBER_ARRAY)
		{
			double[] da = (double[])val;
			if (da == null)
				return null;
			char[] out = new char[da.length];
			int i = 0;
			for (double d : da)
				out[i++] = (char)d;
			return out;
		}
		else
		{
			String[] sa = (String[])val;
			if (sa == null)
				return null;
			char[] out = new char[sa.length];
			int i = 0;
			for (String s : sa)
				out[i++] = s.length() > 0? s.charAt(0) : '\0';
			return out;
		}
	}

	/**
	 * Returns this value's value as an array of chars of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return							the value of the field as an array of <i>length</i> chars or null if not found.
	 */
	public char[] getCharArray(int length)
	{
		if (type == DLValue.TYPE_NUMBER_ARRAY)
		{
			double[] da = (double[])val;
			if (da == null)
				return null;
			char[] out = new char[da.length];
			for (int i = 0; i < Math.min(da.length, length); i++)
				out[i] = (char)da[i];
			return out;
		}
		else
		{
			String[] sa = (String[])val;
			if (sa == null)
				return null;
			char[] out = new char[sa.length];
			for (int i = 0; i < Math.min(sa.length, length); i++)
				out[i] = sa[i].length() > 0? sa[i].charAt(0) : '\0';
			return out;
		}
	}

	/**
	 * Returns this value's value as a byte.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as a byte or 0 if not found.
	 */
	public byte getByte()
	{
		return (byte)getDouble();
	}

	/**
	 * Returns this value's value as a byte array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as bytes or null if not found.
	 */
	public byte[] getByteArray()
	{
		double[] da = getDoubleArray();
		byte[] out = new byte[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (byte)d;
		return out;
	}

	/**
	 * Returns this value's value as an array of bytes of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> bytes or null if not found.
	 */
	public byte[] getByteArray(int length)
	{
		double[] da = getDoubleArray();
		byte[] out = new byte[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (byte)da[i];
		return out;
	}

	/**
	 * Returns this value's value as a boolean.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				true if nonzero number or false if zero or Not-A-Number or not found.
	 */
	public boolean getBoolean()
	{
		double d = getDouble();
		if (Double.isNaN(d))
			return false;
		return d != 0;
	}

	/**
	 * Returns this value's value as a boolean array.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as booleans or null if not found.
	 */
	public boolean[] getBooleanArray()
	{
		double[] da = getDoubleArray();
		boolean[] out = new boolean[da.length];
		int i = 0;
		for (double d : da)
			out[i++] = (d != Double.NaN && d != 0);
		return out;
	}

	/**
	 * Returns this value's value as an array of booleans of specific length.
	 * @param length		the resultant array length.
	 * @throws NumberFormatException	if the field is not numerical or able to be parsed as numerical at all.
	 * @return				the value of the field as an array of <i>length</i> booleans or null if not found.
	 */
	public boolean[] getBooleanArray(int length)
	{
		double[] da = getDoubleArray();
		boolean[] out = new boolean[length];
		for (int i = 0; i < Math.min(da.length, length); i++)
			out[i] = (da[i] != Double.NaN && da[i] != 0);
		return out;
	}

	/**
	 * Copies this value completely, making another DLValue object.
	 */
	public DLValue copy()
	{
		switch (type)
		{
			case TYPE_NUMBER_ARRAY:
				return new DLValue((double[])val);
			case TYPE_STRING_ARRAY:
				return new DLValue((String[])val);
			case TYPE_ID:
			default:
				return new DLValue(((String[])val)[0],true);
		}
	}
	
	@Override
	public String toString()
	{
		String[] s = getStringArray();
		if (s.length == 1)
			return s[0];
		return Arrays.toString(s);
	}
	
	@Override
	public boolean equals(Object value)
	{
		if (value instanceof DLValue)
			return equals((DLValue)value);
		else 
			return super.equals(value);
	}
	
	/**
	 * Returns true if <code>value</code> is equal to this one, false otherwise.
	 * This actually just compares two values by their string representation.
	 */
	public boolean equals(DLValue value)
	{
		return toString().equals(value.toString());
	}
	
	public int getType()
	{
		return type;
	}

	public Object getVal()
	{
		return val;
	}
	
}
