package com.mic.assembly;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
	 
	public class AssemblyMachine{
		static JFrame frame;
		static CardLayout cl;
		final static String EDITOR = "edit";
		final static String RUNTIME = "run";
		static AssemblyWindow aw;
		
	    private static void createAndShowGUI() {
	    	
	        //Create and set up the window.
	        frame = new JFrame("Assembly Machine");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	        cl = new CardLayout();
	    	JPanel root = new JPanel(cl);
	        frame.add(root);

	    	aw = new AssemblyWindow();
	        
	        //Add contents to the window.
	        root.add(aw, EDITOR);
	        cl.show(root, EDITOR);
	        

	        //Display the window
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setResizable(false);

	    }
	 
	    public static void main(String[] args) {
	        createAndShowGUI();

	    }
	    
	    
	}

