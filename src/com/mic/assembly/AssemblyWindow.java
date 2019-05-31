package com.mic.assembly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.mic.lib.IDETextPane;

/**
 * The panel that displays and runs all editing and outputs from program.
 * 
 * @author Marston Connell
 *
 */
public class AssemblyWindow extends JPanel {

	private static final long serialVersionUID = -7118218294554300449L;

	private final String[] possibleCommands = { "inp", "out", "lda", "sta", "ldm", "stm", "add", "sub", "mul", "div",
			"key", "end", "tra", "tre", "tne", "tlt", "tgt", "tle", "tge", "lal", "lml", "adl", "sbl", "mpl", "dvl",
			"lax", "stx", "inx", "dex", "lay", "sty", "iny", "dey", "gsb", "ret", "def", "rep", "drw", "cxp", "cyp",
			"red", "grn", "blu" };

	protected IDETextPane code;
	private JButton runButton;
	private JButton compileCode;
	JTable memory;
	private String[] commands;
	public TextLineNumber tln;
	Timer refresher;

	// graphics
	int xPos = 0;
	int yPos = 0;
	int red = 255;
	int blue = 255;
	int green = 255;
	DrawingPane graphicsPane;

	public int currentKeyDown = 0;

	public boolean darkmode = false;

	public JLabel codeTitle;
	public JScrollPane scrollPane;

	// panels
	JPanel codep;
	JPanel memp;
	JScrollPane memoryDisplay;
	ComputerDiagram cd;
	ComputerDiagram cd2;

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

	private JDialog diagFrame;

	/**
	 * Checks if string is an available command.
	 * 
	 * @author Marston Connell
	 * @param com
	 * @return true if string is command.
	 */
	private boolean checkIfCommand(String com) {
		for (int i = 0; i < possibleCommands.length; i++) {
			if (possibleCommands[i].equals(com)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Initializes the panel for inside the frame with all the buttons and other
	 * inputs/outputs.
	 * 
	 * @author Marston Connell
	 */
	public AssemblyWindow(Font font) {
		super(new GridBagLayout());

		commands = new String[1000];
		pointers = new HashMap<String, Integer>();

		this.setBorder(new EmptyBorder(20, 20, 20, 20));

		code = new IDETextPane(possibleCommands);

		tln = new TextLineNumber(code.highLighter);
		tln.setMinimumDisplayDigits(3);

		runButton = new JButton("Run Program");
		compileCode = new JButton("Compile Program");
		scrollPane = new JScrollPane(code);
		scrollPane.setRowHeaderView(tln);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		codeTitle = new JLabel("Machine Language");
		codeTitle.setOpaque(false);
		// codeTitle.setFont(new Font("Monospaced", Font.PLAIN, 12));
		scrollPane.setColumnHeaderView(codeTitle);

		mdl = new DefaultTableModel();
		mdl.addColumn("Register");
		mdl.addColumn("Operation");
		for (int x = 0; x < 1000; x++) {
			mdl.insertRow(x, new Object[] { x, 000000 });

		}

		memory = new JTable();

		memory.setModel(mdl);

		memoryDisplay = new JScrollPane(memory);
		memoryDisplay.setPreferredSize(new Dimension(220, 300));

		graphicsPane = new DrawingPane();
		graphicsPane.setPreferredSize(new Dimension(400, 300));

		Graphics g = graphicsPane.toDraw.createGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 200);
		g.dispose();
		graphicsPane.repaint();
		AC = new JTextField();
		AC.setEditable(false);
		AC.setOpaque(false);

		MQ = new JTextField();
		MQ.setEditable(false);
		MQ.setOpaque(false);

		xReg = new JTextField();
		xReg.setEditable(false);
		xReg.setOpaque(false);

		yReg = new JTextField();
		yReg.setEditable(false);
		yReg.setOpaque(false);

		input = new JTextField();
		input.setEditable(false);
		input.setOpaque(false);

		JButton stopRunning = new JButton("Stop");
		stopRunning.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopCode();
			}

		});

		JPanel registers = new JPanel(new GridBagLayout());

		// Add Components to this panel.
		codep = new JPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 6;
		codep.add(scrollPane);
		add(codep, c);

		memp = new JPanel();
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 6;
		memp.add(memoryDisplay);
		add(memp, c);

		c.insets.left = 2;
		c.insets.right = 2;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(compileCode, c);

		c.insets.left = 0;
		c.insets.right = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		JLabel j = new JLabel("AC: ");
		j.setToolTipText(
				"<html>03/lda - Load into AC<br>04/sta - Store from AC<br>07/add - Adds to AC<br>08/sub - Subtract from AC</html>");
		j.setHorizontalAlignment(JLabel.LEFT);
		registers.add(j, c);

		c.ipady = 0;
		c.ipadx = 40;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		AC.setToolTipText(
				"<html>03/lda - Load into AC<br>04/sta - Store from AC<br>07/add - Adds to AC<br>08/sub - Subtract from AC</html>");
		registers.add(AC, c);

		c.ipady = 0;
		c.ipadx = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("MQ: ");
		j.setToolTipText(
				"<html>05/ldm - Load into MQ<br>06/stm - Store from MQ<br>09/mul - Multiplies MQ by<br>10/sdiv - Divide MQ by</html>");
		j.setHorizontalAlignment(JLabel.LEFT);
		registers.add(j, c);

		c.ipady = 0;
		c.ipadx = 40;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		MQ.setToolTipText(
				"<html>05/ldm - Load into MQ<br>06/stm - Store from MQ<br>09/mul - Multiplies MQ by<br>10/div - Divide MQ by</html>");
		registers.add(MQ, c);

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("X Register: ");
		j.setToolTipText(
				"<html>28/lax - Load AC into X<br>29/sxm - Store X into AC<br>31/inx - Increments X by 1<br>32/dex - Decrement X by 1</html>");
		j.setHorizontalAlignment(JLabel.LEFT);
		registers.add(j, c);

		c.ipady = 0;
		c.ipadx = 40;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		xReg.setToolTipText(
				"<html>28/lax - Load AC into X<br>29/stx - Store X into AC<br>31/inx - Increments X by 1<br>32/dex - Decrement X by 1</html>");
		registers.add(xReg, c);

		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("Y Register: ");
		j.setToolTipText(
				"<html>-28/lay - Load AC into Y<br>-29/sty - Store Y into AC<br>-31/iny - Increments Y by 1<br>-32/dey - Decrement Y by 1</html>");
		j.setHorizontalAlignment(JLabel.LEFT);
		registers.add(j, c);

		c.ipady = 0;
		c.ipadx = 40;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		yReg.setToolTipText(
				"<html>-28/lay - Load AC into Y<br>-29/sty - Store Y into AC<br>-31/iny - Increments Y by 1<br>-32/dey - Decrement Y by 1</html>");
		registers.add(yReg, c);

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		j = new JLabel("Output: ");
		j.setHorizontalAlignment(JLabel.LEFT);
		registers.add(j, c);

		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridheight = 3;
		cd = new ComputerDiagram(this);
		cd.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					openDiagram();
				}
			}
		});
		// cd.setMinimumSize(new Dimension(0, 200));
		add(cd, c);

		c.ipady = 0;
		c.ipadx = 40;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.gridheight = 1;
		registers.add(input, c);

		c.ipady = 0;
		c.ipadx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 3;
		add(registers, c);

		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.gridheight = 1;
		j = new JLabel("© Marston Connell - 2019");
		j.setHorizontalAlignment(JLabel.LEFT);
		add(j, c);

		c.insets.left = 2;
		c.insets.right = 2;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(stopRunning, c);

		c.insets.left = 2;
		c.insets.right = 2;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 7;
		c.gridwidth = 1;
		c.gridheight = 1;
		add(runButton, c);

		setBorder(font, Color.black);

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

		diagFrame = new JDialog();
//		diagFrame.setAlwaysOnTop(true);
		diagFrame.setTitle("Diagram");

		
		cd.createToolTip();
		cd.setToolTipText("Double click to enlarge the diagram.");
		
		cd2 = new ComputerDiagram(this);
		diagFrame.add(cd2);
//		diagFrame.pack();
		diagFrame.setResizable(false);
		diagFrame.setSize(280, 400);
		diagFrame.setLocationRelativeTo(null);
		diagFrame.setVisible(false);

		f.add(pa);
		f.pack();
		f.setResizable(true);
		f.setLocationRelativeTo(null);
		f.setVisible(false);
		f.addKeyListener(new MKeyListener(this));

		refresher = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cd.repaint();
				refresher.stop();
			}
		});

	}

	/**
	 * @author Marston Connell
	 * Opens diagram to be its own separated window and bigger.
	 */
	public void openDiagram() {
		
		diagFrame.setVisible(true);
	}

	/**
	 * Adds borders with titles to components
	 * @author Marston Connell
	 * @param f
	 * @param c
	 */
	public void setBorder(Font f, Color c) {
		TitledBorder b = BorderFactory.createTitledBorder("Memory Locations and Registries");
		b.setTitleColor(c);
		b.setTitleFont(f);

		memp.setBorder(b);

		b = BorderFactory.createTitledBorder("Code");
		b.setTitleColor(c);
		b.setTitleFont(f);
		b.setTitle("Code");

		codep.setBorder(b);

	}

	/**
	 * Displays error wrapper for other classes.
	 * 
	 * @author Marston Connell
	 * @param title
	 * @param message
	 */
	public void showError(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays a simple message to user.
	 * 
	 * @author Marston Connell
	 * @param title
	 * @param message
	 */
	public void showMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Fixes refactors and pointer errors.
	 * 
	 * @author Marston Connell
	 */
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

	/**
	 * Marks every error returned in the code and reports to user.
	 * @author Marston Connell
	 * @return
	 */
	public boolean checkForErrors() {
		for (int i = 0; i < 1000; i++) {

			if (((String) mdl.getValueAt(i, 1)).contains("null")) {
				showError("Compile Error", "Null Pointer error at line: " + i);
				return false;
			}

		}
		return true;
	}

	/**
	 * Stops currently running code from running any longer.
	 * 
	 * @author Marston Connell
	 */
	private void stopCode() {
		if (rt != null) {
			rt.run = false;
		}
	}

	/**
	 * Compiles code into Machine Code and inserts it into 'memory'.
	 * 
	 * @author Marston Connell
	 */
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

						if (line.trim().length() > 0) {
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
									try {
										translation = line.trim().substring(0, 3);
									} catch (StringIndexOutOfBoundsException sie) {
										showError("No Valid Commands", "No valid command found on line: " + x);
									}
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

					checkForErrors();

				} else {
					JOptionPane.showMessageDialog(this,
							"Either switch compile modes in 'Edit' or use proper syntax. You can always check witch syntax to use under 'File' -> 'Help'.",
							"Compile Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}

	/**
	 * Wrapper to log errors.
	 * @author Marston Connell
	 * @param e
	 */
	public void logError(Exception e) {
		AssemblyMachine.LogError(e);

	}

	/**
	 * Wipes code from text pane and all other info in registries.
	 * 
	 * @author Marston Connell
	 */
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

	/**
	 * Reformats code to be copied to use in the 'Mythical Machine' emulator, or
	 * just this one.
	 * 
	 * @author Marston Connell
	 * @return All reformatted code
	 */
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

	/**
	 * Refactors code.
	 * 
	 * @author Marston Connell
	 */
	public void cleanUp() {
		String newCode = copyCode();
		code.setText(newCode.replaceAll("=", ""));
	}

	/**
	 * Displays graphics window without double screens.
	 * 
	 * @author Marston Connell
	 */
	public void displayGraphics() {
		if (!f.isActive()) {
			f.setVisible(true);
		}
	}

	/**
	 * Displays diagram window without double screens.
	 * 
	 * @author Marston Connell
	 */
	public void dnlargeDiagram() {
		if (!diagFrame.isActive()) {
			diagFrame.setVisible(true);
		}
	}

	/**
	 * Starts code running thread.
	 * 
	 * @author Marston Connell
	 */
	void runCode() {
		AC.setText("");
		MQ.setText("");
		xReg.setText("");
		yReg.setText("");
		input.setText("");
		red = 255;
		blue = 255;
		green = 255;
		rt = new RunThread(this);
		rt.start();

	}

	public void clearWindow() {
		System.out.println("Refreshing diagram");
		refresher.start();

	}

	/**
	 * Finds next open space in memory without interfering with pointers.
	 * 
	 * @author Marston Connell
	 * @param array
	 * @param reserved
	 * @return index of free space in memory
	 */
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