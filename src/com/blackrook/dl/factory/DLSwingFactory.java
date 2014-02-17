/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.dl.factory;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.JTextComponent;

import com.blackrook.commons.list.List;
import com.blackrook.dl.DLStruct;

/**
 * A factory class used for making swing components from DL structures.
 * NOTE: This is very experimental.
 * @author Matthew Tropiano
 */
public final class DLSwingFactory
{
	public static final String
	BLANK_TEXT = "UNNAMED",
	STRUCTNAME_JMENUBAR = "JMenuBar",
	STRUCTNAME_JMENU = "JMenu",
	STRUCTNAME_JMENUITEM = "JMenuItem",
	STRUCTNAME_JBUTTON = "JButton",
	STRUCTNAME_JTOGGLEBUTTON = "JToggleButton",
	STRUCTNAME_JRADIOBUTTON = "JRadioButton",
	STRUCTNAME_JCHECKBOX = "JCheckBox",
	STRUCTNAME_NUMBERSPINNER = "NumberSpinner",
	STRUCTNAME_JTEXTFIELD = "JTextField";

	/**
	 * Creates a new JMenuBar from a DLStruct and all of its contained "JMenus."
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs(), readMenuItem(), readMenu() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the menu's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStructTable to get the info from (best read through DLReader).
	 * @return the new JMenuBar
	 * @see #readMenu(DLStruct dlst)
	 * @see #readMenuItem(DLStruct dlst)
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JMenuBar readMenuBar(DLStruct dlst)
	{
		JMenuBar jmb = new JMenuBar();

		setJComponentAttribs(jmb,dlst);
		for (DLStruct dl : dlst.get(STRUCTNAME_JMENU))
			jmb.add(readMenu(dl));
		
		return jmb;
	}

	/**
	 * Creates a new JMenu from a DLStruct and all of its contained "JMenuItems."
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs(), readMenuItem() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the menu's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStructTable to get the info from (best read through DLReader).
	 * @return the new JMenu
	 * @see #readMenuItem(DLStruct dlst)
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JMenu readMenu(DLStruct dlst)
	{
		JMenu jm = new JMenu(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jm,dlst);
		setAbstractButtonAttribs(jm,dlst);
		
		for (DLStruct dl : dlst.get(STRUCTNAME_JMENUITEM))
			jm.add(readMenuItem(dl));
		
		return jm;
	}

	/**
	 * Creates a new JPopupMenu from a DLStruct and all of its contained "JMenuItems."
	 * If any component field is left out, the default is used.
	 * <p>Fields:</p>
	 * <p>
	 * <i>NONE</i>
	 * </p>
	 * @param dlst	the DLStructTable to get the info from (best read through DLReader).
	 * @return the new JPopupMenu
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 */
	public static JPopupMenu readPopupMenu(DLStruct dlst)
	{
		JPopupMenu jm = new JPopupMenu(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jm,dlst);
		
		for (DLStruct dl : dlst.get(STRUCTNAME_JMENUITEM))
			jm.add(readMenuItem(dl));
		
		return jm;
	}

	/**
	 * Creates a new JMenuItem from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the menu item's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStructTable to get the info from (best read through DLReader).
	 * @return the new JMenuItem.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JMenuItem readMenuItem(DLStruct dlst)
	{
		JMenuItem jmi = new JMenuItem(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jmi,dlst);
		setAbstractButtonAttribs(jmi,dlst);
		return jmi;
	}
	
	/**
	 * Creates a series of buttons from a DLStructTable.
	 * This reads all structs of type "JButton" from the table.
	 * @param dlst	the DLStructTable to get the info from.
	 * @return a bunch of JButtons.
	 */
	public static JButton[] readButtons(DLStruct dlst)
	{
		List<JButton> blist = new List<JButton>();
		
		for (DLStruct dls : dlst.get(STRUCTNAME_JBUTTON))
			blist.add(readButton(dls));
		
		JButton[] out = new JButton[blist.size()];
		blist.toArray(out);
		return out; 
	}

	/**
	 * Creates a new JButton from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the button's text.<br>
	 * <b>defaultCapable</b> <i>bool</i> - sets if this button is default-capable.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to get the info from (best read through DLReader).
	 * @return the new JButton.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JButton readButton(DLStruct dlst)
	{
		JButton jb = new JButton(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jb,dlst);
		setAbstractButtonAttribs(jb,dlst);
		
		if (dlst.containsField("defaultCapable"))
			jb.setDefaultCapable(dlst.getBoolean("defaultCapable"));

		return jb;
	}

	/**
	 * Creates a series of toggle buttons from a DLStruct.
	 * This reads all structs of type "JToggleButton" from the table.
	 * @param dlst	the DLStruct to get the info from.
	 * @return a bunch of JToggleButtons.
	 */
	public static JToggleButton[] readToggleButtons(DLStruct dlst)
	{
		List<JToggleButton> blist = new List<JToggleButton>();
		
		for (DLStruct dls : dlst.get(STRUCTNAME_JTOGGLEBUTTON))
			blist.add(readToggleButton(dls));
		
		JToggleButton[] out = new JToggleButton[blist.size()];
		blist.toArray(out);
		return out; 
	}

	/**
	 * Creates a new JToggleButton from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the button's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to get the info from (best read through DLReader).
	 * @return the new JToggleButton.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JToggleButton readToggleButton(DLStruct dlst)
	{
		JToggleButton jb = new JToggleButton(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jb,dlst);
		setAbstractButtonAttribs(jb,dlst);
		return jb;
	}

	/**
	 * Creates a series of radio buttons from a DLStruct.
	 * This reads all structs of type "JRadioButton" from the table.
	 * @param dlst	the DLStruct to get the info from.
	 * @return a bunch of JRadioButtons.
	 */
	public static JRadioButton[] readRadioButtons(DLStruct dlst)
	{
		List<JRadioButton> blist = new List<JRadioButton>();
		
		for (DLStruct dls : dlst.get(STRUCTNAME_JRADIOBUTTON))
			blist.add(readRadioButton(dls));
		
		JRadioButton[] out = new JRadioButton[blist.size()];
		blist.toArray(out);
		return out; 
	}

	/**
	 * Creates a new JRadioButton from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the button's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to get the info from (best read through DLReader).
	 * @return the new JRadioButton.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JRadioButton readRadioButton(DLStruct dlst)
	{
		JRadioButton jb = new JRadioButton(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jb,dlst);
		setAbstractButtonAttribs(jb,dlst);
		return jb;
	}

	/**
	 * Creates a series of checkboxes from a DLStruct.
	 * This reads all structs of type "JCheckBox" from the table.
	 * @param dlst	the DLStruct to get the info from.
	 * @return a bunch of JCheckBoxes.
	 */
	public static JCheckBox[] readCheckBoxes(DLStruct dlst)
	{
		List<JCheckBox> cblist = new List<JCheckBox>();
		
		for (DLStruct dls : dlst.get(STRUCTNAME_JCHECKBOX))
			cblist.add(readCheckBox(dls));
		
		JCheckBox[] out = new JCheckBox[cblist.size()];
		cblist.toArray(out);
		return out; 
	}
	
	/**
	 * Creates a new JCheckBox from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs(), setAbstractButtonAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the checkbox's text.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to use.
	 * @return	the new JCheckBox.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	 */
	public static JCheckBox readCheckBox(DLStruct dlst)
	{
		JCheckBox jcb = new JCheckBox(dlst.getString("text",BLANK_TEXT));
		setJComponentAttribs(jcb,dlst);
		setAbstractButtonAttribs(jcb,dlst);
		return jcb;
	}
	
	/**
	 * Creates a series of text fields from a DLStruct.
	 * This reads all structs of type "JTextField" from the table.
	 * @param dlst	the DLStruct to get the info from.
	 * @return a bunch of JTextFields.
	 */
	public static JTextField[] readTextFields(DLStruct dlst)
	{
		List<JTextField> tflist = new List<JTextField>();
		
		for (DLStruct dls : dlst.get(STRUCTNAME_JTEXTFIELD))
			tflist.add(readTextField(dls));
		
		JTextField[] out = new JTextField[tflist.size()];
		tflist.toArray(out);
		return out; 
	}

	/**
	 * Creates a new JTextField from a DLStruct.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>columns</b> <i>int</i> - sets the field's number of columns.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to use.
	 * @return	the new JTextField.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 * @see #setJTextComponentAttribs(JTextComponent comp, DLStruct dlst)
	 */
	public static JTextField readTextField(DLStruct dlst)
	{
		JTextField jtf = new JTextField();
		setJComponentAttribs(jtf,dlst);
		setJTextComponentAttribs(jtf,dlst);

		if (dlst.containsField("columns")) jtf.setColumns(dlst.getInt("columns"));
		
		return jtf;
	}
	
	
	/**
	 * Creates a new JSpinner that uses the SpinnerNumberModel.
	 * If any component field is left out, the default is used.
	 * See setJComponentAttribs() for additional fields.
	 * <p>Fields:</p>
	 * <p>
	 * <b>value</b> <i>double</i> - sets the spinner's initial value.<br>
	 * <b>min</b> <i>double</i> - sets the spinner's minimum value.<br>
	 * <b>max</b> <i>double</i> - sets the spinner's maximum value.<br>
	 * <b>step</b> <i>double</i> - sets the spinner's stepping amount.<br>
	 * </p>
	 *
	 * @param dlst	the DLStruct to use.
	 * @return	the new JTextField.
	 * @see #setJComponentAttribs(JComponent comp, DLStruct dlst)
	 */
	public static JSpinner readNumberSpinner(DLStruct dlst)
	{
		JSpinner s = new JSpinner();
		setJComponentAttribs(s,dlst);
		
		s.setModel(new SpinnerNumberModel(
				dlst.getDouble("value",0),
				dlst.getDouble("min",0),
				dlst.getDouble("max",100),
				dlst.getDouble("step",1)
				));
		
		return s;
	}
	
	/**
	 * Sets the attributes of common JComponent types.
	 * If any component field is left out, the default is used.
	 * <p>Fields:</p>
	 * <p>
	 * <b>backgroundColor</b> <i>float:[r,g,b,a]</i> - sets the background color of the component.<br>
	 * <b>foregroundColor</b> <i>float:[r,g,b,a]</i> - sets the foreground color of the component.<br>
	 * <b>size</b> <i>int:[x,y]</i> - sets the size of this component.<br>
	 * <b>maxSize</b> <i>int:[x,y]</i> - sets the maximum size of this component.<br>
	 * <b>minSize</b> <i>int:[x,y]</i> - sets the minimum size of this component.<br>
	 * <b>preferredSize</b> <i>int:[x,y]</i> - sets the preferred size of this component.<br>
	 * <br>
	 * <b>name</b> <i>string</i> - sets this component's name.<br>
	 * <b>toolTipText</b> <i>string</i> - sets this component's tooltip text.<br>
	 * <b>enabled</b> <i>bool</i> - sets if this component is enabled.<br>
	 * <b>focusable</b> <i>bool</i> - sets if this component can get focus.<br>
	 * <b>enableFocusTraversalKeys</b> <i>bool</i> - sets if this component's focus traversal keys are enabled.<br>
	 * <b>ignoreRepaint</b> <i>bool</i> - sets if this component should ignore repaint calls.<br>
	 * <b>visible</b> <i>bool</i> - sets if this component is visible.<br>
	 * <b>opaque</b> <i>bool</i> - sets if this component is opaque.<br>
	 * <b>autoscrolls</b> <i>bool</i> - sets if this component autoscrolls.<br>
	 * </p>
	 */
	public static void setJComponentAttribs(JComponent comp, DLStruct dlst)
	{
		if (dlst.containsField("backgroundColor"))
			comp.setBackground(getColor(dlst,"backgroundColor"));

		if (dlst.containsField("foregroundColor"))
			comp.setBackground(getColor(dlst,"foregroundColor"));

		if (dlst.containsField("size"))
		{
			int[] i = getSize(dlst,"size");
			comp.setSize(i[0],i[1]);
		}

		if (dlst.containsField("maxSize")) comp.setMaximumSize(getDimension(dlst,"maxSize"));
		if (dlst.containsField("minSize")) comp.setMinimumSize(getDimension(dlst,"minSize"));
		if (dlst.containsField("preferredSize")) comp.setPreferredSize(getDimension(dlst,"preferredSize"));
		
		if (dlst.containsField("name")) comp.setName(dlst.getString("name"));
		if (dlst.containsField("toolTipText")) comp.setToolTipText(dlst.getString("toolTipText"));
		if (dlst.containsField("enabled")) comp.setEnabled(dlst.getBoolean("enabled"));
		if (dlst.containsField("focusable")) comp.setFocusable(dlst.getBoolean("focusable"));
		if (dlst.containsField("enableFocusTraversalKeys")) comp.setFocusTraversalKeysEnabled(dlst.getBoolean("enableFocusTraversalKeys"));
		if (dlst.containsField("ignoreRepaint")) comp.setIgnoreRepaint(dlst.getBoolean("ignoreRepaint"));
		if (dlst.containsField("visible")) comp.setVisible(dlst.getBoolean("visible"));
		if (dlst.containsField("opaque")) comp.setOpaque(dlst.getBoolean("opaque"));
		if (dlst.containsField("autoscrolls")) comp.setAutoscrolls(dlst.getBoolean("autoscrolls"));
	}
	
	/**
	 * Sets the attributes of common AbstractButton types.
	 * If any component field is left out, the default is used.
	 * <p>Fields:</p>
	 * <p>
	 * <b>mnemonic</b> <i>string</i> - sets this abstractbutton's mnemonic.<br>
	 * <b>mnemonicIndex</b> <i>int</i> - sets this abstractbutton's highlighted mnemonic index.<br>
	 * <b>actionCommand</b> <i>string</i> - sets this abstractbutton's action command.<br>
	 * <b>borderPainted</b> <i>bool</i> - sets if this button's border is painted.<br>
	 * <b>contentAreaFilled</b> <i>bool</i> - sets if this button's content area is filled.<br>
	 * </p>
	 */
	public static void setAbstractButtonAttribs(AbstractButton ab, DLStruct dlst)
	{
		String mn;
		if (dlst.containsField("mnemonic") && (mn = dlst.getString("mnemonic")).length()>0)
			ab.setMnemonic(mn.charAt(0));
		
		if (dlst.containsField("mnemonicIndex")) ab.setDisplayedMnemonicIndex(dlst.getInt("mnemonicIndex"));
		if (dlst.containsField("actionCommand")) ab.setActionCommand(dlst.getString("actionCommand"));
		if (dlst.containsField("borderPainted")) ab.setBorderPainted(dlst.getBoolean("borderPainted"));
		if (dlst.containsField("contentAreaFilled")) ab.setContentAreaFilled(dlst.getBoolean("contentAreaFilled"));
	}
	
	/**
	 * Sets the attributes of common JTextComponent types.
	 * If any component field is left out, the default is used.
	 * <p>Fields:</p>
	 * <p>
	 * <b>text</b> <i>string</i> - sets the field's text.<br>
	 * <b>focusAccelerator</b> <i>string</i> - sets the field's focus accelerator.<br>
	 * <b>dragEnabled</b> <i>bool</i> - sets if this field is drag-enabled.<br>
	 * <b>editable</b> <i>bool</i> - sets if this field is editable.<br>
	 * <b>selectionColor</b> <i>float:[r,g,b,a]</i> - sets this field's selection color.<br>
	 * <b>selectedTextColor</b> <i>float:[r,g,b,a]</i> - sets this field's selection color.<br>
	 * <b>selectionStart</b> <i>int</i> - sets this field's selected text starting point.<br>
	 * <b>selectionEnd</b> <i>int</i> - sets this field's selected text ending point.<br>
	 * <b>caretColor</b> <i>float:[r,g,b,a]</i> - sets this field's caret color.<br>
	 * <b>caretPosition</b> <i>int</i> - sets this field's caret position.<br>
	 * </p>
	 */
	public static void setJTextComponentAttribs(JTextComponent jtc, DLStruct dlst)
	{
		String mn;
		if (dlst.containsField("focusAccelerator") && (mn = dlst.getString("focusAccelerator")).length()>0)
			jtc.setFocusAccelerator(mn.charAt(0));

		if (dlst.containsField("text")) jtc.setText(dlst.getString("text"));
		if (dlst.containsField("dragEnabled")) jtc.setDragEnabled(dlst.getBoolean("dragEnabled"));
		if (dlst.containsField("editable")) jtc.setEditable(dlst.getBoolean("editable"));
		if (dlst.containsField("selectionColor"))
			jtc.setSelectionColor(getColor(dlst,"selectionColor"));
		if (dlst.containsField("selectedTextColor"))
			jtc.setSelectedTextColor(getColor(dlst,"selectedTextColor"));
		if (dlst.containsField("selectionStart"))
			jtc.setSelectionStart(dlst.getInt("selectionStart"));
		if (dlst.containsField("selectionEnd"))
			jtc.setSelectionEnd(dlst.getInt("selectionEnd"));
		if (dlst.containsField("caretColor"))
			jtc.setCaretColor(getColor(dlst,"caretColor"));
		if (dlst.containsField("caretPosition"))
			jtc.setCaretPosition(dlst.getInt("caretPosition"));
	}
	
	/**
	 * Returns a color from a defined field.
	 * @param dlst	the DLStruct to use.
	 * @param field	the color's field name.
	 * @return		the Color.
	 */
	protected static Color getColor(DLStruct dlst, String field)
	{
		float[] f = dlst.getFloatArray(field);
		float[] color = new float[]{0,0,0,1};
		System.arraycopy(f,0,color,0,Math.min(f.length,4));
		return new Color(color[0],color[1],color[2],color[3]);
	}
	
	/**
	 * Returns a (corrected) size from a defined field.
	 * @param dlst	the DLStruct to use.
	 * @param field	the size's field name.
	 * @return		the size.
	 */
	protected static int[] getSize(DLStruct dlst, String field)
	{
		return dlst.getIntArray(2,field); 
	}

	/**
	 * Returns a Dimension from a defined field.
	 * @param dlst	the DLStruct to use.
	 * @param field	the Dimension's field name.
	 * @return		the new Dimension.
	 */
	protected static Dimension getDimension(DLStruct dlst, String field)
	{
		int[] i = getSize(dlst,field);
		return new Dimension(i[0],i[1]);
	}
}
