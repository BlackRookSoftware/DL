DataLanguage (C) Black Rook Software, All rights reserved.
http://www.blackrooksoftware.com

==== Dependent Libraries:

- Black Rook Commons v2.14.0+
- Black Rook Common Lang v2.3.0+
http://www.blackrooksoftware.com

==== Introduction

The purpose of the DataLanguage project is to create a hierarchical means for 
organizing data that is to be read into programs in order to set up internal 
data structures/settings and also a means to export said data into a user-
readable or editable format. It is similar to XML in the sense that it does
the same thing, except that DL uses a freeform Curly-Brace syntax rather than
a markup language Angle-Bracket syntax.

==== Structure

Each data field in a DL structure can either be interpreted as a String, char,
int, float, double, boolean, or long, or as arrays of such data. Strings are
written encased in double-quotes; special characters are escaped by 
backslashes.

The data is castable between seemingly incompatible fields as such:

Strings cast as numeric data will attempt to parse the number's value from the
String. If the value is null or the empty string, it is 0. Strings cast as
"chars" will only return the first character, if the String's length is not
0. Casting singular values as arrays will wrap the value in an array. Arrays
cast as singular values will only get the array's first index value.
Arrays cast as Strings, however, will return the values of the array bound
by square brackets, separated by commas.

In terms of value equality, the string-representations of the values are
compared in order to determine value equality.

If one were to represent a book with 200 pages titled "The Red Book" written 
by John Doe, one might write it as such:


book
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}


Of course, the above code is not form strict, as it is lexically scanned when 
interpreted, so the following has the same meaning...

book { title "The Red Book"; pages 200; author "John Doe";}

... even though it is probably less readable to the everyday coder.

Field names and structure names in the structure are written using 
alphanumeric characters plus the underscore ("_") character (starting with 
either a letter or the underscore). The following are examples of valid names:

x _size foo0 bar7 length margin_size

Following the field name is the value, which may be a valid number or
character string. Character strings are enclosed in double-quotes.

Structures can also have a structure value applied to them as a means of
identifying them or giving them additional meaning, as such:


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}


As you can see, the value is placed directly after the structure's name. It can
be any value type (including arrays).

Structures can also contain other structures that store their own separate 
data:


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	
	chapter 1
	{
		title "Introduction";
		page 3;
		}	

	chapter 2
	{
		title "The Yellow Pages";
		page 27;
		}	
	}


Structures can copy data from another structure using an inheritance clause 
as such:


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}

book 2 : book 0
{
	title "The Green Book";
	}
	

The data actually looks like this after interpretation...


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}

book 2
{
	title "The Green Book";
	pages 200;
	author "John Doe";
	}


... but be warned: structures don't maintain a record of how they inherit 
their information, and also inherit ALL structures declared within the 
"parent" structures. Structures can only inherit data from a single structure,
and only structures that are declared on the same "level" as the inheriting
one and with a unique structure value.

You don't have to add structure information after an inheritance
clause. For example, the following is perfectly legal:


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}

book 2 : book 0;


... which would yield:


book 0
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}

book 2
{
	title "The Red Book";
	pages 200;
	author "John Doe";
	}


==== Archetypes

Another thing that DL supports is something called "Archetyping." If a user
was coding DL by hand and didn't want to type the same code over and over
again, archetyping can save time. A structure is archetyped as follows:

.pair (x,y);

That's a period at the beginning, the structure name, and the field names
separated by commas and enclosed by parenthesis, ended by a semicolon.

Archetype use is invoked in this manner:


pair (5, 7);


The above code actually yields this:


pair
{
	x 5;
	y 7;
	}


... placing the appropriate values in the respective fields outlined by the
archetype. A valued structure can be declared as such:


pair 10.5 (4, 5);


...which yields:


pair 10.5
{
	x 4;
	y 5;
	}


New structures can be made using archetypes as a base. The following code...


pair 11 (3, 4)
{
	z 10;
	comment "This is an ordered triple.";
	}


...actually yields:


pair 11
{
	x 3;
	y 4;
	z 10;
	comment "This is an ordered triple.";
	}

As of version 2.1.0, you do not need to fill in all fields of an archetype.
The following code, in this case:


pair (5);


...would yield:


pair
{
	x 5;
	}

And "y," not provided, would not be added.


As of version 2.3.0, multiple inheritance and multiple archetyping is now
possible. Data is copied or applied in clause order. Given the following code:


.triplet(x, y, z);

triplet 0
{
	x 1;
	y 2;
	z 3;
	}


You would be able to declare:


triplet 1 : triplet 0 (4, 5, 6);
triplet 2 (4, 5) : triplet 0;
triplet 3 : triplet 0 (4, 5, 6) (10, 20);
triplet 4 : triplet 0 : triplet 1 
{
	w 800;
	}


Which would yield these equivalent structures:


triplet 0
{
	x 1;
	y 2;
	z 3;
	}

triplet 1
{
	x 4;
	y 5;
	z 6;
	}

triplet 2
{
	x 1;
	y 2;
	z 3;
	}

triplet 3
{
	x 10;
	y 20;
	z 6;
	}

triplet 4
{
	x 4;
	y 5;
	z 6;
	w 800;
	}


The archetype followed is that of the first type in the declaration.

C-style comments can be added to the code, which are stripped out at the 
time of interpretation. Comments can be either enclosed by
/* and */ delimiters or started by // and terminated by a new line.

/* This is a comment. */

/*
So is this.
It is a multi-line comment.
*/

// this is a comment, too.


==== Library

Contained in this release is a series of libraries that allow reading, writing,
and extracting data in DL structures, found in the com.blackrook.dl package. 

The DLStruct class is used for the reading and manipulation of the data once it
has been read into Java. The structures themselves can also be created here
without using user-generated data structures in its linguistic format.
DLReader and DLWriter are utility classes used to read/write DL to/from files,
respectively.

==== Other

Copyright (c) 2009-2014 Black Rook Software.
All rights reserved. This program and the accompanying materials
are made available under the terms of the GNU Lesser Public License v2.1
which accompanies this distribution, and is available at
http://www.gnu.org/licenses/old-licenses/gpl-2.0.html

A copy of the LGPL should have been included in this release (blackrook-license.txt).
If it was not, please contact us for a copy, or to notify us of a distribution
that has not included it. 
 
Contributors:
	Matt Tropiano, Black Rook Software - initial API and implementation

Support: support@blackrooksoftware.com
