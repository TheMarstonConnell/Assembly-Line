package com.mic.assembly;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class AssemblyMachine {
	static JFrame frame;
	static CardLayout cl;
	final static String EDITOR = "edit";
	final static String RUNTIME = "run";
	static AssemblyWindow aw;

	private static void createAndShowGUI() {

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem save = new JMenuItem("Copy to clipboard");
		JMenuItem newButton = new JMenuItem("New");
		
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				copy();
			}

		});
		newButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				aw.clearCode();
			}

		});

		file.add(newButton);
		file.add(save);

		JMenu edit = new JMenu("Edit");
		
		JMenu langType = new JMenu("Language");
		JCheckBoxMenuItem codeStep = new JCheckBoxMenuItem("Step through code");
		codeStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.stepCode = ((JCheckBoxMenuItem) e.getSource()).isSelected();
				System.out.println(aw.stepCode);
			}
	    	
	    });
		
		ButtonGroup group = new ButtonGroup();

	    JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem("Machine Language");
	    group.add(menuItem);
	    menuItem.setSelected(true);
	    menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = true;
				
			}
	    	
	    });
	    langType.add(menuItem);

	    menuItem = new JRadioButtonMenuItem("Assembler");
	    menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = false;
				
			}
	    	
	    });
	    group.add(menuItem);
	    langType.add(menuItem);

		edit.add(langType);
		edit.add(codeStep);
		
		menuBar.add(file);
		menuBar.add(edit);

		// Create and set up the window.
		frame = new JFrame("Assembly Machine");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cl = new CardLayout();
		JPanel root = new JPanel(cl);
		frame.add(root);

		aw = new AssemblyWindow();

		// Add contents to the window.
		root.add(aw, EDITOR);
		cl.show(root, EDITOR);

		frame.setJMenuBar(menuBar);

		// Display the window
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(true);

	}

	public static void main(String[] args) {
		createAndShowGUI();

	}
	

	private static void copy() {
		System.out.println("Copy");
		StringSelection stringSelection = new StringSelection(aw.copyCode());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

}
