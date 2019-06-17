package com.mic.assembly;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class LoadScreen extends JFrame {
	JProgressBar load;
	JLabel text;

	public LoadScreen() {
		this.setSize(1000, 600);
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);

		GridBagConstraints c = new GridBagConstraints();
		text = new JLabel("Assembly Line");
		text.setFont(new Font("Sans Serif", Font.PLAIN, 70));
		text.setHorizontalAlignment(JLabel.CENTER);
		load = new JProgressBar();
//		load.setMinimumSize(new Dimension(100, 200));

		this.setLayout(new GridBagLayout());

		c.gridheight = 1;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.HORIZONTAL;

		this.add(load, c);

		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 0;
		c.weightx = 0.2;
		this.add(text, c);

		this.setVisible(true);
		
		load.setMaximum(5);

	}
	
	public void increase() {
		load.setValue(load.getValue() + 1);
	}

}
