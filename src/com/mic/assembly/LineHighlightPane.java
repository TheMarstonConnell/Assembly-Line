package com.mic.assembly;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.BadLocationException;

/**
 * Highlights current line in the text pane.
 * @author Marston Connell
 *
 */
public class LineHighlightPane extends BasicTextPaneUI {
	JTextPane tc;

	/**
	 * Initializes line highlighting pane.
	 * @author Marston Connell
	 * @param JTextPane pane
	 */
	public LineHighlightPane(JTextPane pane) {

		tc = pane;
		tc.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				tc.repaint();
			}
		});
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
			g.setColor(new Color(232, 242, 254));
			g.fillRect(0, y, tc.getWidth(), h);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
}
