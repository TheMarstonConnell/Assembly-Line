package plaf.material.objects;


import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

import plaf.material.utils.MaterialDrawingUtils;

import java.awt.Graphics;

public class MaterialSeparatorUI extends BasicSeparatorUI {

	public static ComponentUI createUI (JComponent c) {
		return new MaterialSeparatorUI ();
	}

	@Override
	public void installUI (JComponent c) {
		super.installUI (c);
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		super.paint (MaterialDrawingUtils.getAliasedGraphics (g), c);
	}
}
