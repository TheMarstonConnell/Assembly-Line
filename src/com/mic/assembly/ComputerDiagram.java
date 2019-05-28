package com.mic.assembly;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class ComputerDiagram extends JPanel {

	public boolean ACtoX = false;
	public boolean XtoAC = false;
	public boolean ACtoY = false;
	public boolean YtoAC = false;
	public boolean MEMtoAC = false;
	public boolean ACtoMEM = false;
	public boolean MEMtoMQ = false;
	public boolean MQtoMEM = false;
	
	// Drawing the diagram that shows the travel of data.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Color light = Color.green;
		g.setColor(Color.black);

		// Memory//
		g.drawRect(getWidth() / 2 - getWidth() / 6, getHeight() / 2 - getWidth() / 6, getWidth() / 6, getWidth() / 3);
		// Memory//

		// AC//
		g.drawRect(getWidth() / 2 + getWidth() / 6, getHeight() / 2 - getWidth() / 10, getWidth() / 10,
				getWidth() / 10);

		// out of AC
		if(ACtoMEM) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		g.fillRect(getWidth() / 2, getHeight() / 2 - getWidth() / 10 - 2, 4, 5);
		g.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 10, getWidth() / 2 + getWidth() / 6,
				getHeight() / 2 - getWidth() / 10);
		
		// into AC
		if(MEMtoAC) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		g.drawLine(getWidth() / 2, getHeight() / 2 - getWidth() / 10 + 5, getWidth() / 2 + getWidth() / 6,
				getHeight() / 2 - getWidth() / 10 + 5);
		g.fillRect(getWidth() / 2 + getWidth() / 6 - 3, getHeight() / 2 - getWidth() / 10 + 4, 4, 5);
		
		// AC//

		// X//
		g.setColor(Color.black);
		g.drawRect(getWidth() / 2 + getWidth() / 6, getHeight() / 2 - getWidth() / 3, getWidth() / 12, getWidth() / 12);

		//Into X
		if(ACtoX) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		
		g.drawLine(getWidth() / 2 + getWidth() / 6 + 2, getHeight() / 2 - getWidth() / 10,
				getWidth() / 2 + getWidth() / 6 + 2, getHeight() / 2 - getWidth() / 3 + getWidth() / 12);
		g.fillRect(getWidth() / 2 + getWidth() / 6, getHeight() / 2 - getWidth() / 3  + getWidth() / 12, 5, 4);
		
		//Out of X
		if(XtoAC) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		g.drawLine(getWidth() / 2 + getWidth() / 6 + 10, getHeight() / 2 - getWidth() / 10,
				getWidth() / 2 + getWidth() / 6 + 10, getHeight() / 2 - getWidth() / 3 + getWidth() / 12);
		g.fillRect(getWidth() / 2 + getWidth() / 6 + 8, getHeight() / 2 - getWidth() / 10 - 3, 5, 4);
		
		// X//

		// Y//
		g.setColor(Color.black);
		g.drawRect(getWidth() / 2 + getWidth() / 3, getHeight() / 2 - getWidth() / 3, getWidth() / 12, getWidth() / 12);

		// Out of y
		if(YtoAC) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		drawBentLine(
				new Point[] {
						new Point(getWidth() / 2 + getWidth() / 6 + getWidth() / 10, getHeight() / 2 - getWidth() / 10),
						new Point(getWidth() / 2 + getWidth() / 3, getHeight() / 2 - getWidth() / 10), new Point(
								getWidth() / 2 + getWidth() / 3, getHeight() / 2 - getWidth() / 3 + getWidth() / 12) },
				g);
		g.fillRect(getWidth() / 2 + getWidth() / 6 + getWidth() / 10, getHeight() / 2 - getWidth() / 10 - 2, 4, 5);

		// into Y
		if(ACtoY) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		drawBentLine(new Point[] {
				new Point(getWidth() / 2 + getWidth() / 6 + getWidth() / 10, getHeight() / 2 - getWidth() / 10 + 8),
				new Point(getWidth() / 2 + getWidth() / 3 + 8, getHeight() / 2 - getWidth() / 10 + 8),
				new Point(getWidth() / 2 + getWidth() / 3 + 8, getHeight() / 2 - getWidth() / 3 + getWidth() / 12) },
				g);
		g.fillRect(getWidth() / 2 + getWidth() / 3 + 6, getHeight() / 2 - getWidth() / 3 + getWidth() / 12, 5, 4);

		// Y//

		// MQ//
		g.setColor(Color.black);
		g.drawRect(getWidth() / 2 + getWidth() / 6, getHeight() / 2 + getWidth() / 8, getWidth() / 10, getWidth() / 10);

		// Out of MQ
		if(MQtoMEM) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		g.drawLine(getWidth() / 2, getHeight() / 2 + getWidth() / 8, getWidth() / 2 + getWidth() / 6,
				getHeight() / 2 + getWidth() / 8);
		g.fillRect(getWidth() / 2, getHeight() / 2 + getWidth() / 8 - 2, 4, 5);

		// Into MQ
		if(MEMtoMQ) {
			g.setColor(light);
		}else {
			g.setColor(Color.black);
		}
		g.drawLine(getWidth() / 2, getHeight() / 2 + getWidth() / 8 + 5, getWidth() / 2 + getWidth() / 6,
				getHeight() / 2 + getWidth() / 8 + 5);
		g.fillRect(getWidth() / 2 + getWidth() / 6 - 3, getHeight() / 2 + getWidth() / 8 + 3, 4, 5);
		// MQ//
		
		ACtoMEM = false;
		MEMtoAC = false;
		MEMtoMQ = false;
		MQtoMEM = false;
		ACtoX = false;
		XtoAC = false;
		ACtoY = false;
		YtoAC = false;

	}

	private void drawBentLine(Point[] points, Graphics g) {
		for (int x = 1; x < points.length; x++) {
			g.drawLine(points[x - 1].x, points[x - 1].y, points[x].x, points[x].y);
		}
	}

}
