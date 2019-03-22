package com.mic.assembly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mic.lib.IDETextPane;

public class AssemblyWindow extends JPanel {

	private static final long serialVersionUID = -7118218294554300449L;

	private final String[] possibleCommands = { "inp", "out", "lda", "sta", "ldm", "stm", "add", "sub", "mul", "div",
			"key", "end", "tra", "tre", "tne", "tlt", "tgt", "tle", "tge", "lal", "lml", "adl", "sbl", "mpl", "dvl",
			"lax", "stx", "inx", "dex", "lay", "sty", "iny", "dey", "gsb", "ret", "def", "rep", "drw", "cxp", "cyp",
			"red", "grn", "blu" };

	protected IDETextPane code;
	private JButton runButton;
	private JButton compileCode;
	private JTable memory;
	private String[] commands;

	// graphics
	int xPos = 0;
	int yPos = 0;
	int red = 255;
	int blue = 255;
	int green = 255;
	DrawingPane graphicsPane;

	public int currentKeyDown = 0;

	public JLabel codeTitle;
	public JScrollPane scrollPane;

	JTextField input;
	private JDialog f = null;
	JTextField AC;
	JTextField MQ;
	JTextField xReg;
	JTextField yReg;
	private RunThread rt;
	DefaultTableModel mdl;
	public boolean useNums = true;
	public boolean stepCode = false;
	private HashMap<String, Integer> errors;

	private HashMap<String, Integer> pointers;

	int recentChange = 0;

	private boolean checkIfCommand(String com) {
		for (int i = 0; i < possibleCommands.length; i++) {
			if (possibleCommands[i].equals(com)) {
				return true;
			}
		}
		return false;
	}

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

		code = new IDETextPane(possibleCommands);

		TextLineNumber tln = new TextLineNumber(code.highLighter);
		tln.setMinimumDisplayDigits(3);

		runButton = new JButton("Run Program");
		compileCode = new JButton("Compile Program");
		scrollPane = new JScrollPane(code);
		scrollPane.setRowHeaderView(tln);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		codeTitle = new JLabel("Machine Language");
		codeTitle.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setColumnHeaderView(codeTitle);

		mdl = new DefaultTableModel();
		mdl.addColumn("Register");
		mdl.addColumn("Operation");
		for (int x = 0; x < 1000; x++) {
			mdl.insertRow(x, new Object[] { x, 000000 });

		}

		memory = new JTable();

		memory.setModel(mdl);

		JScrollPane memoryDisplay = new JScrollPane(memory);
		memoryDisplay.setPreferredSize(new Dimension(400, 300));

		graphicsPane = new DrawingPane();
		graphicsPane.setPreferredSize(new Dimension(400, 300));

		Graphics g = graphicsPane.toDraw.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 200);
		g.dispose();
		graphicsPane.repaint();
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

		JButton stopRunning = new JButton("      Stop      ");
		stopRunning.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopCode();
			}

		});

		// Add Components to this panel.
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Code"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		p.add(scrollPane);
		add(p, c);

		p = new JPanel();
		p.setBorder(BorderFactory.createTitledBorder("Memory Locations and Registries"));
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		p.add(memoryDisplay);
		add(p, c);

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
		JLabel j = new JLabel("AC: ");
		j.setToolTipText("<html>03/lda - Load into AC<br>04/sta - Store from AC<br>07/add - Adds to AC<br>08/sub - Subtract from AC</html>");
		j.setHorizontalAlignment(JLabel.CENTER);
		add(j, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(runButton, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		
		AC.setToolTipText("<html>03/lda - Load into AC<br>04/sta - Store from AC<br>07/add - Adds to AC<br>08/sub - Subtract from AC</html>");
		add(AC, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("MQ: ");
		j.setToolTipText("<html>05/ldm - Load into MQ<br>06/stm - Store from MQ<br>09/mul - Multiplies MQ by<br>10/sdiv - Divide MQ by</html>");
		j.setHorizontalAlignment(JLabel.CENTER);
		add(j, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		MQ.setToolTipText("<html>05/ldm - Load into MQ<br>06/stm - Store from MQ<br>09/mul - Multiplies MQ by<br>10/div - Divide MQ by</html>");
		add(MQ, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("X Register: ");
		j.setToolTipText("<html>28/lax - Load AC into X<br>29/sxm - Store X into AC<br>31/inx - Increments X by 1<br>32/dex - Decrement X by 1</html>");
		j.setHorizontalAlignment(JLabel.CENTER);
		add(j, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		xReg.setToolTipText("<html>28/lax - Load AC into X<br>29/stx - Store X into AC<br>31/inx - Increments X by 1<br>32/dex - Decrement X by 1</html>");
		add(xReg, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("Y Register: ");
		j.setToolTipText("<html>-28/lay - Load AC into Y<br>-29/sty - Store Y into AC<br>-31/iny - Increments Y by 1<br>-32/dey - Decrement Y by 1</html>");
		j.setHorizontalAlignment(JLabel.CENTER);
		add(j, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		yReg.setToolTipText("<html>-28/lay - Load AC into Y<br>-29/sty - Store Y into AC<br>-31/iny - Increments Y by 1<br>-32/dey - Decrement Y by 1</html>");
		add(yReg, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(stopRunning, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("Output: ");
		j.setHorizontalAlignment(JLabel.CENTER);
		add(j, c);

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

		f = new JDialog();
		f.setAlwaysOnTop(true);
		f.setTitle("Graphics Window");
		JPanel pa = new JPanel();
		pa.setBorder(BorderFactory.createTitledBorder("Graphics"));
		pa.add(graphicsPane);

		f.add(pa);
		f.pack();
		f.setResizable(true);
		f.setLocationRelativeTo(null);
		f.setVisible(false);
		f.addKeyListener(new MKeyListener(this));


	}

	public void showError(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void fixCode() {

		for (Entry<String, Integer> entry : errors.entrySet()) {
			String command = "";
			int oldIndex = entry.getValue();
			String line = entry.getKey();

			int ln = line.indexOf('#');
			if (ln < 0) {
				ln = line.length();
			}

			line = line.substring(0, ln);

			boolean complete = false;
			String translation = null;
			while (!complete) {
				translation = line.trim().substring(0, 3);
				complete = true;
				if (translation.equals("inp")) {
					command = command + "01";
				} else if (translation.equals("out")) {
					command = command + "02";
				} else if (translation.equals("lda")) {
					command = command + "03";
				} else if (translation.equals("sta")) {
					command = command + "04";
				} else if (translation.equals("ldm")) {
					command = command + "05";
				} else if (translation.equals("stm")) {
					command = command + "06";
				} else if (translation.equals("add")) {
					command = command + "07";
				} else if (translation.equals("sub")) {
					command = command + "08";
				} else if (translation.equals("mul")) {
					command = command + "09";
				} else if (translation.equals("div")) {
					command = command + "10";
				} else if (translation.equals("key")) {
					command = command + "11";
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

			ln = line.indexOf('#');
			if (ln < 0) {
				ln = line.length();
			}

			if (translation.equals("lal") || translation.equals("lml") || translation.equals("adl")
					|| translation.equals("sbl") || translation.equals("mpl") || translation.equals("dvl")) {
				command = command + String.format("%03d", Integer.valueOf(line.substring(3, ln)));
			} else {

				command = command + String.format("%03d", pointers.get(line.substring(3, ln).trim()));

			}
			commands[oldIndex] = command;

		}

	}

	private void stopCode() {
		rt.run = false;
	}

	public void compile() {
		errors = new HashMap<String, Integer>();
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
						if (line.charAt(0) == '#') {

							line = " ";

						}

						int ln = line.indexOf('#');
						if (ln < 0) {
							ln = line.length();
						}
						boolean neg = false;
						if (line.trim().charAt(0) == '-') {
							neg = true;
						}
						commands[j] = line.substring(0, ln).replaceAll("[^\\d-]", "");
						if (neg) {
							line = "-" + line;
						}
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
					for (int x = 0; x < lines.length; x++) {
						String line = lines[x];

						if (!(j >= 999)) {
							if (line.charAt(0) == '#') {
								line = "";
							}

							line = line.replaceAll("\t", "");
							line = line.replaceAll("=", "");
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
									} else if (translation.equals("key")) {
										command = command + "11";
										j++;
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
									} else if (translation.equals("dvl")) {
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
									} else if (translation.equals("rep")) {
										command = command + "40";
									} else if (translation.equals("drw")) {
										command = command + "41";
									} else if (translation.equals("cxp")) {
										command = command + "43";
									} else if (translation.equals("cyp")) {
										command = command + "44";
									} else if (translation.equals("red")) {
										command = command + "45";
									} else if (translation.equals("grn")) {
										command = command + "46";
									} else if (translation.equals("blu")) {
										command = command + "47";
									} else {

										complete = false;
										pointers.put(translation, oldIndex);
										line = line.substring(3);
									}

								}

								int ln = line.indexOf('#');
								if (ln < 0) {
									ln = line.length();
								}

								if (translation.equals("inp") || translation.equals("sta") || translation.equals("stm")
										|| translation.equals("key")) {
									if (!pointers.containsKey(line.substring(3, ln).trim())) {
										command = command + String.format("%03d", openSpace);
										pointers.put(line.substring(3, ln).trim(), openSpace);
									} else {
										command = command
												+ String.format("%03d", pointers.get(line.substring(3, ln).trim()));
									}
								} else if (translation.equals("lal") || translation.equals("lml")
										|| translation.equals("adl") || translation.equals("sbl")
										|| translation.equals("mpl") || translation.equals("dvl")) {
									command = command + String.format("%03d", Integer.valueOf(line.substring(3, ln)));
								} else if (translation.equals("end") || translation.equals("lax")
										|| translation.equals("stx") || translation.equals("inx")
										|| translation.equals("dex") || translation.equals("lay")
										|| translation.equals("sty") || translation.equals("iny")
										|| translation.equals("dey") || translation.equals("rep")
										|| translation.equals("drw")) {
									command = command + "000";
								} else {
									if (pointers.get(line.substring(3, ln).trim()) == null) {

										errors.put(line, oldIndex);
									} else {
										command = command
												+ String.format("%03d", pointers.get(line.substring(3, ln).trim()));
									}
								}

							}

							commands[oldIndex] = command;
							j++;
						}

					}

					fixCode();

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
		Graphics g = graphicsPane.toDraw.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 1000, 500);
		g.dispose();
		graphicsPane.repaint();
		code.update();
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

					if (line.charAt(0) == '#') {
						line = "";
					}

					line = line.replaceAll("\\s+", "");

					if (!line.equals("")) {

						boolean complete = false;
						String translation = null;
						while (!complete) {
							translation = line.trim().substring(0, 3);
							complete = true;
							boolean lit = false;
							if (checkIfCommand(translation)) {

								if (translation.equals("lal") || translation.equals("lml") || translation.equals("adl")
										|| translation.equals("sbl") || translation.equals("mpl")
										|| translation.equals("dvl")) {
									lit = true;
								}
								int ln = line.indexOf('#');
								if (ln < 0) {
									ln = line.length();
								}
								String sub = line.substring(3, ln);

								if (lit) {
									sub = "=" + sub;
								}

								if (backupLine.indexOf('#') >= 0) {

									newLine = newLine + "\t" + translation + "\t" + sub + "\t"
											+ backupLine.substring(backupLine.indexOf('#')).trim();
								} else {
									newLine = newLine + "\t" + translation + "\t" + sub + "\t";
								}

							} else {
								complete = false;
								line = line.substring(3);
								newLine = newLine + translation;
							}
						}
						copiedCode = copiedCode + newLine + "\n";
					}

				}

			}
		} else {
			return newCode;
		}
		return copiedCode;
	}

	public void cleanUp() {
		String newCode = copyCode();
		code.setText(newCode.replaceAll("=", ""));
	}

	public void displayGraphics() {
		if (!f.isActive()) {
			f.setVisible(true);
		}
	}

	private void runCode() {

		AC.setText("");
		MQ.setText("");
		xReg.setText("");
		yReg.setText("");
		input.setText("");

		rt = new RunThread(this);
		rt.start();

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

}