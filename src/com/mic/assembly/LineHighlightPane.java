package com.mic.assembly;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class LineHighlightPane extends BasicTextPaneUI {
	JTextPane tc;

	public LineHighlightPane(JTextPane t) {

		tc = t;
		tc.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {

				tc.repaint();
			}
		});
	}

	@Override
	public void paintBackground(Graphics g) {

		super.paintBackground(g);

		try {
			Rectangle rect = modelToView(tc, tc.getCaretPosition());
			int y = rect.y;
			int h = rect.height;
			g.setColor(new Color(179, 206, 249));
			g.fillRect(0, y, tc.getWidth(), h);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
}
