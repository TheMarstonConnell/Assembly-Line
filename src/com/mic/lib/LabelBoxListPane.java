package com.mic.lib;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelBoxListPane extends JPanel{
	public LabelBoxListPane() {
		super();
		GridLayout lay = new GridLayout(0 , 2);
		lay.setVgap(45);
		this.setLayout(lay);
		
	}
	
	public void addLabelBox(JLabel l, JComponent j) {
		this.add(l);
		this.add(j);
	}
}
