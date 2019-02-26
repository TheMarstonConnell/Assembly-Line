package com.mic.assembly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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
	public boolean useNums = true;
	public boolean stepCode = false;

	HashMap<String, Integer> pointers;

	int recentChange = 0;

	public AssemblyWindow() {
		super(new GridBagLayout());

		System.setOut(new java.io.PrintStream(System.out) {

			private StackTraceElement getCallSite() {
				for (StackTraceElement e : Thread.currentThread().getStackTrace())
					if (!e.getMethodName().equals("getStackTrace") && !e.getClassName().equals(getClass().getName()))
						return e;
				return null;
			}

			@Override
			public void println(String s) {
				println((Object) s);
			}

			@Override
			public void println(Object o) {
				StackTraceElement e = getCallSite();
				String callSite = e == null ? "??"
						: String.format("%s.%s(%s:%d)", e.getClassName(), e.getMethodName(), e.getFileName(),
								e.getLineNumber());
				super.println(callSite.replaceAll("com.mic.assembly.", "") + ":" + o);
			}
		});

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
		pointers.clear();
		for (int x = 0; x < commands.length; x++) {
			commands[x] = "";
		}
		String codeo = code.getText();
		String[] lines = codeo.split("\n");
		if (codeo.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter code before attempting to compile.", "Compile Error",
					JOptionPane.WARNING_MESSAGE);
		} else {
			if (useNums) {
				// When using number opcodes and registries

				int j = 0;
				for (String line : lines) {
					if (!(j >= 999)) {
						if (line.charAt(0) == '/') {
							line = " ";
						}

						int ln = line.indexOf('/');
						if (ln < 0) {
							ln = line.length();
						}

						commands[j] = line.substring(0, ln);

					}
					j++;
				}

				for (int i = 0; i < commands.length; i++) {
					mdl.setValueAt(commands[i], i, 1);
				}

			} else {

				// When using text opcodes rather than number codes

				if (!code.getText().replaceAll("\\d", "").trim().equals("")) {

					int j = 0;
					for (String line : lines) {

						if (!(j >= 999)) {
							if (line.charAt(0) == '/') {
								line = "";
							}

							line = line.replaceAll("\\s+", "");
							int openSpace = findOpenSpace(commands, j);

							int oldIndex = j;
							String command = "";

							if (!line.equals("")) {

								boolean complete = false;
								String translation = null;
								while (!complete) {
									translation = line.trim().substring(0, 3);
									complete = true;
									if (translation.equals("inp")) {
										command = command + "01";
										j++;
									} else if (translation.equals("out")) {
										command = command + "02";
									} else if (translation.equals("lda")) {
										command = command + "03";
									} else if (translation.equals("sta")) {
										command = command + "04";
										j++;
									} else if (translation.equals("ldm")) {
										command = command + "05";
									} else if (translation.equals("stm")) {
										command = command + "06";
										j++;
									} else if (translation.equals("add")) {
										command = command + "07";
									} else if (translation.equals("sub")) {
										command = command + "08";
									} else if (translation.equals("mul")) {
										command = command + "09";
									} else if (translation.equals("div")) {
										command = command + "10";
									} else if (translation.equals("tra")) {
										command = command + "21";
									} else if (translation.equals("tre")) {
										command = command + "22";
									} else if (translation.equals("tne")) {
										command = command + "23";
									} else if (translation.equals("tlt")) {
										command = command + "24";
									} else if (translation.equals("tgt")) {
										command = command + "25";
									} else if (translation.equals("tle")) {
										command = command + "26";
									} else if (translation.equals("tge")) {
										command = command + "27";
									} else if (translation.equals("lal")) {
										command = command + "-21";
									} else if (translation.equals("lml")) {
										command = command + "-22";
									} else if (translation.equals("adl")) {
										command = command + "-23";
									} else if (translation.equals("sbl")) {
										command = command + "-24";
									} else if (translation.equals("mpl")) {
										command = command + "-25";
									} else if (translation.equals("cvl")) {
										command = command + "-26";
									} else if (translation.equals("lax")) {
										command = command + "28";
									} else if (translation.equals("stx")) {
										command = command + "29";
									} else if (translation.equals("inx")) {
										command = command + "31";
									} else if (translation.equals("dex")) {
										command = command + "32";
									} else if (translation.equals("lay")) {
										command = command + "-28";
									} else if (translation.equals("sty")) {
										command = command + "-29";
									} else if (translation.equals("iny")) {
										command = command + "-31";
									} else if (translation.equals("dey")) {
										command = command + "-32";
									} else if (translation.equals("gsb")) {
										command = command + "-27";
									} else if (translation.equals("ret")) {
										command = command + "-30";
									} else if (translation.equals("def")) {
										command = command + "30";
									} else if (translation.equals("end")) {
										command = command + "00";
									} else {
										complete = false;
										pointers.put(translation, oldIndex);
										line = line.substring(3);
									}

								}

								int ln = line.indexOf('/');
								if (ln < 0) {
									ln = line.length();
								}

								if (translation.equals("inp") || translation.equals("sta")
										|| translation.equals("stm")) {
									command = command + String.format("%03d", openSpace);
									pointers.put(line.substring(3, ln), openSpace);
								} else if (translation.equals("lal") || translation.equals("lml")
										|| translation.equals("adl") || translation.equals("sbl")
										|| translation.equals("mpl") || translation.equals("dvl")) {
									command = command + String.format("%03d", Integer.valueOf(line.substring(3, ln)));
								} else {
									command = command
											+ String.format("%03d", pointers.get(line.substring(3, ln).trim()));
								}
							}

							commands[oldIndex] = command;
							j++;

						}

					}

					for (int i = 0; i < commands.length; i++) {
						mdl.setValueAt(commands[i], i, 1);
					}

				} else {
					JOptionPane.showMessageDialog(this,
							"Either switch compile modes in 'Edit' or use proper syntax. You can always check witch syntax to use under 'File' -> 'Help'.",
							"Compile Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	public void clearCode() {
		AC.setText("");
		MQ.setText("");
		xReg.setText("");
		yReg.setText("");
		input.setText("");
		code.setText("");
		for (int x = 0; x < commands.length; x++) {
			commands[x] = "";
		}
		for (int i = 0; i < commands.length; i++) {
			mdl.setValueAt(commands[i], i, 1);
		}
	}

	public String copyCode() {
		String newCode = code.getText();
		String copiedCode = "";
		String[] lines = newCode.split("\n");

		if (!useNums) {
			if (!code.getText().replaceAll("\\d", "").trim().equals("")) {

				newCode = "";
				for (String line : lines) {
					String backupLine = line;
					String newLine = "";

					if (line.charAt(0) == '/') {
						line = "";
					}

					line = line.replaceAll("\\s+", "");

					String command = "";

					if (!line.equals("")) {

						boolean complete = false;
						String translation = null;
						while (!complete) {
							translation = line.trim().substring(0, 3);
							complete = true;
							if (translation.equals("inp") || translation.equals("out") || translation.equals("lda")
									|| translation.equals("sta") || translation.equals("ldm")
									|| translation.equals("stm") || translation.equals("add")
									|| translation.equals("sub") || translation.equals("mul")
									|| translation.equals("div") || translation.equals("tra")
									|| translation.equals("tre") || translation.equals("tne")
									|| translation.equals("tlt") || translation.equals("tgt")
									|| translation.equals("tle") || translation.equals("tge")
									|| translation.equals("lal") || translation.equals("lml")
									|| translation.equals("adl") || translation.equals("sbl")
									|| translation.equals("mpl") || translation.equals("cvl")
									|| translation.equals("lax") || translation.equals("stx")
									|| translation.equals("inx") || translation.equals("dex")
									|| translation.equals("lay") || translation.equals("sty")
									|| translation.equals("iny") || translation.equals("dey")
									|| translation.equals("ret") || translation.equals("def")
									|| translation.equals("end") || translation.equals("gsb")) {

								System.out.println(translation);
								int ln = line.indexOf('/');
								if (ln < 0) {
									ln = line.length();
								}
								System.out.println(backupLine);

								if (backupLine.indexOf('/') >= 0) {

									newLine = newLine + "\t" + translation + "\t" + line.substring(3, ln) + "\t"
											+ backupLine.substring(backupLine.indexOf('/')).replace('/', ' ').trim();
								} else {
									newLine = newLine + "\t" + translation + "\t" + line.substring(3, ln) + "\t";
								}

							} else {
								complete = false;
								System.out.println("Label: " + translation);
								line = line.substring(3);
								newLine = newLine + translation;
							}
						}
						System.out.println(newLine);
						copiedCode = copiedCode + newLine + "\n";
					}
					

				}

			}
		}else {
			return newCode;
		}

		System.out.println("\n" + copiedCode);

		return copiedCode;
	}
	
	public void cleanUp() {
		String newCode = copyCode();
		code.setText(newCode);
	}

	private void runCode() {

		AC.setText("");
		MQ.setText("");
		xReg.setText("");
		yReg.setText("");
		input.setText("");

		for (int i = 0; i < 1000; i++) {
			boolean cont = false;
			for (int j = i; j < 1000; j++) {
				if (mdl.getValueAt(j, 1) != "" && mdl.getValueAt(j, 1) != " ") {
					cont = true;
				}
			}
			if (!cont)
				return;
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
						default:
							JOptionPane.showMessageDialog(this,
									"Error on line " + i + ". Please refer to 'File' -> 'Help' for available commands.",
									"Runtime Error", JOptionPane.WARNING_MESSAGE);
							return;
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
			if (stepCode) {
				JOptionPane.showMessageDialog(this, "Completed step in code.", "Stepping", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

	private int findOpenSpace(String[] array, int reserved) {
		for (int x = 0; x < array.length; x++) {
			if (x != reserved) {
				if (array[x].trim().equals("")) {
					if (!pointers.isEmpty()) {
						boolean contains = false;
						for (Entry<String, Integer> entry : pointers.entrySet()) {
							if (entry.getValue() == x) {
								contains = true;
							}
						}
						if (!contains) {
							return x;
						}

					} else {
						return x;
					}
				}
			}
		}
		return -1;

	}

	private int findOpenSpace(String[] array) {
		for (int x = 0; x < array.length; x++) {
			if (array[x].trim().equals("")) {
				if (!pointers.isEmpty()) {
					for (Entry<String, Integer> entry : pointers.entrySet()) {
						System.out.println(entry.getValue());
						boolean contains = false;
						if (entry.getValue().equals(x)) {
							contains = true;
						}
						if (!contains)
							return x;

					}
				} else {
					return 0;
				}
			}
		}
		return -1;
	}

}