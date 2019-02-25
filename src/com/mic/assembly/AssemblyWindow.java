package com.mic.assembly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class AssemblyWindow extends JPanel {

	protected JTextPane code;
	JButton runButton;
	JButton compileCode;
	JTable memory;
	String[] commands;
	JTextField input;
	JTextField AC;
	JTextField MQ;
	JTextField xReg;
	JTextField yReg;
	String inputText = "";
	DefaultTableModel mdl;
	boolean useNums = false;

	HashMap<String, Integer> pointers;

	int recentChange = 0;

	public AssemblyWindow() {
		super(new GridBagLayout());

		commands = new String[1000];
		pointers = new HashMap<String, Integer>();

		this.setBorder(new EmptyBorder(20, 20, 20, 20));

		code = new JTextPane();
		TextLineNumber tln = new TextLineNumber(code);
		tln.setMinimumDisplayDigits(3);

		runButton = new JButton("Run Program");
		compileCode = new JButton("Compile Program");
		JScrollPane scrollPane = new JScrollPane(code);
		scrollPane.setRowHeaderView(tln);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		JLabel tf = new JLabel("OCREG");
		scrollPane.setColumnHeaderView(tf);

		mdl = new DefaultTableModel();
		mdl.addColumn("Register");
		mdl.addColumn("Operation");
		for (int x = 0; x < 1000; x++) {
			mdl.insertRow(x, new Object[] { x, 000000 });

		}

		memory = new JTable();

		memory.setModel(mdl);

		JScrollPane memoryDisplay = new JScrollPane(memory);
		memoryDisplay.setPreferredSize(new Dimension(400, 200));

		AC = new JTextField();
		AC.setEditable(false);
		AC.setBackground(Color.white);

		MQ = new JTextField();
		MQ.setEditable(false);
		MQ.setBackground(Color.white);

		xReg = new JTextField();
		xReg.setEditable(false);
		xReg.setBackground(Color.white);
		yReg = new JTextField();
		yReg.setEditable(false);
		yReg.setBackground(Color.white);

		input = new JTextField();
		input.setEditable(false);
		input.setBackground(Color.white);

		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		add(scrollPane, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		add(memoryDisplay, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(compileCode, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("AC: "), c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(AC, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("MQ: "), c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(MQ, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("X Register: "), c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(xReg, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("Y Register: "), c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(yReg, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(runButton, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(new JLabel("Output: "), c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(input, c);

		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				runCode();
			}
		});

		compileCode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				compile();

			}
		});

	}

	public void compile() {
		for (int x = 0; x < commands.length; x++) {
			commands[x] = "";
		}
		String codeo = code.getText();
		String[] lines = codeo.split("\n");
		if (codeo.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter code before attempting to compile.",
					"Warning: Compile Error", JOptionPane.WARNING_MESSAGE);
		} else {
			if (useNums) {

				int j = 0;
				for (String line : lines) {
					if (!(j >= 999)) {
						if (line.charAt(0) == '/') {
							line = " ";
						}
						commands[findOpenSpace(commands)] = line.substring(0, line.indexOf(' '));

					}
				}

				for (int i = 0; i < commands.length; i++) {
					mdl.setValueAt(commands[i], i, 1);
				}

			} else {
				int j = 0;
				for (String line : lines) {
					
					if (!(j >= 999)) {
						if (line.charAt(0) == '/') {
							line = " ";
						}

						line = line.replaceAll("\\s+", "");
						int openSpace = findOpenSpace(commands);

						String command = "";
						boolean complete = false;
						while (!complete) {
							String translation = line.trim().substring(0, 3);
							complete = true;
							if (translation.equals("inp")) {
								command = command + "01";
							} else {
								complete = false;
								System.out.println(translation);
								pointers.put(translation, j);
								line = line.substring(3);
							}

						}
						
						int ln = line.indexOf('/');
						if(ln < 0) {
							ln = line.length();
						}
						
						command = command + String.format("%03d", openSpace);
						pointers.put(line.substring(3, ln), openSpace);
						System.out.println(
								"Adding pointer to " + openSpace + " at " + line.substring(3, ln));

						commands[j] = command;
						j++;
					}
					
					
				}

				for (int i = 0; i < commands.length; i++) {
					mdl.setValueAt(commands[i], i, 1);
				}
				
			}
		}
	}

	private void runCode() {

		AC.setText("");
		MQ.setText("");
		xReg.setText("");
		yReg.setText("");
		input.setText("");

		for (int i = 0; i < 1000; i++) {
			if (mdl.getValueAt(i, 1) != "" && mdl.getValueAt(i, 1) != " ") {
				if (((String) mdl.getValueAt(i, 1)).trim().length() >= 5) {
					int m = Integer.valueOf(((String) mdl.getValueAt(i, 1)).substring(2, 5).trim());
					int num;

					if (((String) mdl.getValueAt(i, 1)).trim().charAt(0) != '-') {
						switch (Integer.valueOf(((String) mdl.getValueAt(i, 1)).substring(0, 2))) {
						case 0:
							return;
						case 1:
							String response = null;
							while (response == null) {
								response = JOptionPane.showInputDialog("Enter Input");
							}
							mdl.setValueAt(response, m, 1);
							break;
						case 2:
							input.setText("Output: " + ((String) mdl.getValueAt(m, 1)).trim());

							break;
						case 3:
							AC.setText(((String) mdl.getValueAt(m, 1)).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 4:

							mdl.setValueAt(AC.getText(), m, 1);

							break;
						case 5:
							MQ.setText(((String) mdl.getValueAt(m, 1)).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 6:

							mdl.setValueAt(MQ.getText(), m, 1);

							break;
						case 7:

							num = Integer.valueOf(AC.getText()) + Integer.valueOf((String) mdl.getValueAt(m, 1));
							AC.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 8:
							num = Integer.valueOf(AC.getText()) - Integer.valueOf((String) mdl.getValueAt(m, 1));
							AC.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 9:
							num = Integer.valueOf(MQ.getText()) * Integer.valueOf((String) mdl.getValueAt(m, 1));
							MQ.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 10:
							num = Integer.valueOf(MQ.getText()) / Integer.valueOf((String) mdl.getValueAt(m, 1));
							MQ.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 21:
							i = m - 1;
							break;
						case 22:
							if (recentChange == 0) {
								i = m - 1;
							}
							break;
						case 23:
							if (recentChange != 0) {
								i = m - 1;
							}
							break;
						case 24:
							if (recentChange < 0) {
								i = m - 1;
							}
							break;
						case 25:
							if (recentChange > 0) {
								i = m - 1;
							}
							break;
						case 26:
							if (recentChange <= 0) {
								i = m - 1;
							}
							break;
						case 27:
							if (recentChange >= 0) {
								i = m - 1;
							}
							break;

						case 28:
							AC.setText(((String) xReg.getText().trim()));
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 29:
							xReg.setText(AC.getText());
							recentChange = Integer.valueOf(xReg.getText());

							break;
						case 31:
							num = Integer.valueOf(xReg.getText()) + 1;
							xReg.setText(String.valueOf(num));
							recentChange = Integer.valueOf(xReg.getText());
							break;
						case 32:
							num = Integer.valueOf(xReg.getText()) - 1;
							xReg.setText(String.valueOf(num));
							recentChange = Integer.valueOf(xReg.getText());
							break;
						}

						// if negative operator
					} else {
						m = Integer.valueOf(((String) mdl.getValueAt(i, 1)).substring(3, 6).trim());
						switch (Integer.valueOf(((String) mdl.getValueAt(i, 1)).substring(1, 3))) {
						case 21:
							AC.setText((String.valueOf(m)).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 22:
							MQ.setText((String.valueOf(m)).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 23:

							num = Integer.valueOf(AC.getText()) + Integer.valueOf((String.valueOf(m)).trim());
							AC.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 24:
							num = Integer.valueOf(AC.getText()) - Integer.valueOf((String.valueOf(m)).trim());
							AC.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 25:
							num = Integer.valueOf(MQ.getText()) * Integer.valueOf((String.valueOf(m)).trim());
							MQ.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 26:
							num = Integer.valueOf(MQ.getText()) / Integer.valueOf((String.valueOf(m)).trim());
							MQ.setText(String.valueOf(num).trim());
							recentChange = Integer.valueOf(MQ.getText());
							break;
						case 28:
							AC.setText(((String) yReg.getText().trim()));
							recentChange = Integer.valueOf(AC.getText());
							break;
						case 29:
							yReg.setText(AC.getText());
							recentChange = Integer.valueOf(yReg.getText());

							break;
						case 31:
							num = Integer.valueOf(yReg.getText()) + 1;
							yReg.setText(String.valueOf(num));
							recentChange = Integer.valueOf(yReg.getText());
							break;
						case 32:
							num = Integer.valueOf(yReg.getText()) - 1;
							yReg.setText(String.valueOf(num));
							recentChange = Integer.valueOf(yReg.getText());
							break;
						}

					}
				}
			}
		}
	}

	private int findOpenSpace(String[] array) {
		for (int x = 0; x < array.length; x++) {
			if (array[x].equals("")) {
				if (!pointers.isEmpty()) {
					for (Entry<String, Integer> entry : pointers.entrySet()) {
						System.out.println(entry.getValue());
						boolean contains = false;
						if (entry.getValue().equals(x)) {
							System.out.println("Warning");
							contains = true;
						}
						if (!contains)
							return x + 1;

					}
				}else {
					return 1;
				}
			}
		}
		return -1;
	}
}