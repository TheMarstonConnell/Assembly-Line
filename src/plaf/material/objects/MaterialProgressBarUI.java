package plaf.material.objects;


import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import plaf.material.utils.MaterialBorders;
import plaf.material.utils.MaterialColors;
import plaf.material.utils.MaterialDrawingUtils;

import java.awt.Graphics;

/**
 * @author https://github.com/vincenzopalazzo
 */

public class MaterialProgressBarUI extends BasicProgressBarUI {

	public static ComponentUI createUI (JComponent c) {
		return new MaterialProgressBarUI ();
	}

	@Override
	public void installUI (JComponent c) {
		super.installUI (c);

		JProgressBar progressBar = (JProgressBar) c;
		progressBar.setBorder (MaterialBorders.LIGHT_LINE_BORDER);
		progressBar.setBackground (MaterialColors.GRAY_200);
		progressBar.setForeground (MaterialColors.LIGHT_BLUE_400);
	}

	@Override
	public void paint (Graphics g, JComponent c) {
		super.paint (MaterialDrawingUtils.getAliasedGraphics (g), c);
	}

}
