package com.mic.assembly;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Graphics display for machine.
 * @author Marston Connell
 *
 */
public class DrawingPane extends JPanel {
	
	private static final long serialVersionUID = 2940823515672424561L;
	BufferedImage toDraw;

	/**
	 * Inits area to draw.
	 * @author Marston Connell
	 */
	public DrawingPane() {
		toDraw = new BufferedImage(100, 75, BufferedImage.TYPE_INT_ARGB);

	}
	
	
	@Override
	/**
	 * Draws image to screen.
	 * @author Marston Connell
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(toDraw, 0, 0, 400, 300, null);
	}

}
