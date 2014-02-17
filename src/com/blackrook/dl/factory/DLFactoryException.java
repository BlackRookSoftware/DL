/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl.factory;

public class DLFactoryException extends RuntimeException
{
	private static final long serialVersionUID = -5463002855611154475L;

	public DLFactoryException()
	{
		super("Bad type requested.");
	}

	public DLFactoryException(String s)
	{
		super(s);
	}
}
