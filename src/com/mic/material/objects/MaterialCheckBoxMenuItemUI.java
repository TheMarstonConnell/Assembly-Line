package com.mic.material.objects;


import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

import com.mic.material.utils.MaterialDrawingUtils;

import java.awt.*;

/**
 * @author https://github.com/vincenzopalazzo
 */

public class MaterialCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {

	public static ComponentUI createUI (JComponent c) {
		return new MaterialCheckBoxMenuItemUI ();
	}

	@Override
	public void installUI (JComponent c) {
		super.installUI (c);
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		super.paint (MaterialDrawingUtils.getAliasedGraphics (g), c);
	}

	@Override
	protected void paintMenuItem (Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap) {
		JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) c;
		checkBoxMenuItem.setFont (UIManager.getFont ("MenuItem.font"));
		checkBoxMenuItem.setBackground (UIManager.getColor ("MenuItem.background"));
		checkBoxMenuItem.setForeground (UIManager.getColor ("MenuItem.foreground"));
		checkBoxMenuItem.setHorizontalAlignment (SwingConstants.LEFT);
		checkBoxMenuItem.setVerticalAlignment (SwingConstants.CENTER);
		checkBoxMenuItem.setBorder (UIManager.getBorder ("MenuItem.border"));
		c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		if (checkBoxMenuItem.isSelected ()) {
			super.paintMenuItem (MaterialDrawingUtils.getAliasedGraphics (g), checkBoxMenuItem, UIManager.getIcon ("CheckBoxMenuItem.selectedCheckIcon"), arrowIcon, background, foreground, defaultTextIconGap);
			return;
		}
		super.paintMenuItem (MaterialDrawingUtils.getAliasedGraphics (g), checkBoxMenuItem, UIManager.getIcon ("CheckBoxMenuItem.checkIcon"), arrowIcon, background, foreground, defaultTextIconGap);
	}
}
