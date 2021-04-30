package com.mic.material.objects;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

import com.mic.material.utils.MaterialDrawingUtils;

import java.awt.Graphics;

public class MaterialMenuUI extends BasicMenuUI {

	public static ComponentUI createUI (JComponent c) {
		return new MaterialMenuUI ();
	}

	@Override
	public void installUI (JComponent c) {
		super.installUI (c);

		JMenu menu = (JMenu) c;
		menu.setFont (UIManager.getFont ("Menu.font"));
		menu.setBorder (UIManager.getBorder ("Menu.border"));
		menu.setBackground (UIManager.getColor ("Menu.background"));
		menu.setForeground (UIManager.getColor ("Menu.foreground"));
		menu.setOpaque (UIManager.getBoolean ("Menu.opaque"));
		
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		super.paint (MaterialDrawingUtils.getAliasedGraphics (g), c);
	}
}