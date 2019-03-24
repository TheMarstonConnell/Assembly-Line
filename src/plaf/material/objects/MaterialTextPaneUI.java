package plaf.material.objects;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;

import plaf.material.utils.MaterialDrawingUtils;

/**
 * @author https://github.com/vincenzopalazzo
 */

public class MaterialTextPaneUI extends BasicTextPaneUI {

	public static ComponentUI createUI (JComponent c) {
		return new MaterialTextPaneUI ();
	}

	@Override
	public void installUI (JComponent c) {
		super.installUI (c);
	}
	
	@Override
		protected void paintSafely(Graphics g) {
			// TODO Auto-generated method stub
			super.paintSafely(MaterialDrawingUtils.getAliasedGraphics (g));
		}
	

}
