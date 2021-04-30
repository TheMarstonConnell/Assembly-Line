package com.mic.assembly;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.BadLocationException;

import com.mic.lib.IDETextPane;

import com.mic.material.utils.MaterialColors;
import com.mic.material.utils.MaterialDrawingUtils;

/**
 * Highlights current line in the text pane.
 * 
 * @author Marston Connell
 *
 */
public class LineHighlightPane extends BasicTextPaneUI {
	static IDETextPane tc;

	/**
	 * Initializes line highlighting pane.
	 * 
	 * @author Marston Connell
	 * @param JTextPane pane
	 */
	public LineHighlightPane(IDETextPane pane) {

		tc = pane;
		tc.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				tc.repaint();
			}
		});
	}

	public static ComponentUI createUI(JComponent c) {
		return new LineHighlightPane(tc);
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	}

	@Override
	protected void paintSafely(Graphics g) {
		// TODO Auto-generated method stub
		super.paintSafely(MaterialDrawingUtils.getAliasedGraphics(g));
	}

	@Override
	/**
	 * Paints line.
	 * 
	 * @author Marston Connell
	 */
	public void paintBackground(Graphics g) {

		super.paintBackground(g);

		try {
			Rectangle rect = modelToView(tc, tc.getCaretPosition());
			int y = rect.y;
			int h = rect.height;
			if(tc.dark) {
				g.setColor(MaterialColors.DARK_HIGHLIGHT);
			}else {
				g.setColor(MaterialColors.LIGHT_HIGHLIGHT);
			}
			g.fillRect(0, y, tc.getWidth(), h);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
}
