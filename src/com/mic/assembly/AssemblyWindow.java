package com.mic.assembly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class AssemblyWindow extends JPanel {

	protected JTextPane code;
	JButton runButton;
	JButton compileCode;
	JTable memory;
	String[] commands;

	// graphics
	int xPos = 0;
	int yPos = 0;
	int red = 255;
	int blue = 255;
	int green = 255;
	BufferedImage graphics;
	DrawingPane graphicsPane;

	public int currentKeyDown = 0;

	JTextField input;
	JDialog f = null;
	JTextField AC;
	JTextField MQ;
	JTextField xReg;
	JTextField yReg;
	String inputText = "";
	RunThread rt;
	DefaultTableModel mdl;
	public boolean useNums = true;
	public boolean stepCode = false;
	HashMap<String, Integer> errors;
	boolean styling = false;

	HashMap<String, Integer> pointers;

	int recentChange = 0;
	private Color color;

	private void styleText() throws BadLocationException {
		StyleContext context = new StyleContext();
		Style style = context.addStyle("comments", null);
		StyleConstants.setForeground(style, Color.BLACK);

		String codeo = code.getText();
		code.getDocument().remove(0, code.getDocument().getLength());
		String[] lines = codeo.split("\n");
		if (codeo.equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter code before attempting to compile.", "Compile Error",
					JOptionPane.WARNING_MESSAGE);
		} else {
			// When using text opcodes rather than number codes
			for (int x = 0; x < lines.length; x++) {
				String line = lines[x];
				
				String command = "";
				
				int ln = line.indexOf('/');
				if (ln > 0) {
					
					code.getDocument().insertString(0, "", style);
					
				}else {
					code.getDocument().insertString(0, "", style);
				}

			}

		}

		styling = false;

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

		code = new JTextPane();

		Document d = code.getDocument();
		d.addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!styling) {
					styling = true;
					try {
						styleText();
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!styling) {
					styling = true;
					try {
						styleText();
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		TextLineNumber tln = new TextLineNumber(code);
		tln.setMinimumDisplayDigits(3);

		runButton = new JButton("Run Program");
		compileCode = new JButton("Compile Program");
		JScrollPane scrollPane = new JScrollPane(code);
		scrollPane.setRowHeaderView(tln);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 300));
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
		memoryDisplay.setPreferredSize(new Dimension(400, 300));

		graphicsPane = new DrawingPane();
		graphicsPane.setPreferredSize(new Dimension(400, 300));

		Graphics g = graphicsPane.toDraw.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 200);
		g.dispose();
		graphicsPane.repaint();
		this.color = Color.white;

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

		JButton stopRunning = new JButton("Stop");
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
		add(new JLabel("AC: "), c);

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
		add(stopRunning, c);

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

	private void fixCode() {

		for (Entry<String, Integer> entry : errors.entrySet()) {
			String command = "";
			int oldIndex = entry.getValue();
			String line = entry.getKey();

			int ln = line.indexOf('/');
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

			ln = line.indexOf('/');
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
				int p = 0;
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
					p++;
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
							if (line.charAt(0) == '/') {
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
									} else if (translation.equals("cvl")) {
										command = command + "-26";
									} else if (translation.equals("dvl")) {
										command = command + "-27";
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

								int ln = line.indexOf('/');
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

					if (!line.equals("")) {

						boolean complete = false;
						String translation = null;
						while (!complete) {
							translation = line.trim().substring(0, 3);
							complete = true;
							boolean lit = false;
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
									|| translation.equals("end") || translation.equals("gsb")
									|| translation.equals("cxp") || translation.equals("rep")
									|| translation.equals("drw") || translation.equals("cyp")
									|| translation.equals("red") || translation.equals("grn")
									|| translation.equals("blu") || translation.equals("key")) {

								if (translation.equals("lal") || translation.equals("lml") || translation.equals("adl")
										|| translation.equals("sbl") || translation.equals("mpl")
										|| translation.equals("dvl")) {
									lit = true;
								}
								int ln = line.indexOf('/');
								if (ln < 0) {
									ln = line.length();
								}
								String sub = line.substring(3, ln);

								if (lit) {
									sub = "=" + sub;
								}

								if (backupLine.indexOf('/') >= 0) {

									newLine = newLine + "\t" + translation + "\t" + sub + "\t"
											+ backupLine.substring(backupLine.indexOf('/')).trim();
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

	private int findOpenSpace(String[] array) {
		for (int x = 0; x < array.length; x++) {
			if (array[x].trim().equals("")) {
				if (!pointers.isEmpty()) {
					for (Entry<String, Integer> entry : pointers.entrySet()) {
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