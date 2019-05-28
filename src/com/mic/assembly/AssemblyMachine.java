package com.mic.assembly;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

import plaf.material.MaterialLookAndFeel;
import plaf.material.utils.MaterialFonts;

/**
 * The main emulator for the entire machine.
 * 
 * @author Marston Connell
 *
 */
public class AssemblyMachine {

	public static AssemblyMachine assemblyMachine;

	public JFrame frame;
	CardLayout cl;
	final static String EDITOR = "edit";
	final static String RUNTIME = "run";
	AssemblyWindow aw;
	JDialog helpFrame;
	public MaterialLookAndFeel ui;
	JCheckBoxMenuItem codeStep;
	JCheckBoxMenuItem darkModeButton;
	JRadioButtonMenuItem assembler;
	JRadioButtonMenuItem machine;
	public boolean sendStats = false;

	/**
	 * Initializes Menu bar for frame.
	 * 
	 * @author Marston Connell
	 * @return JMenuBar
	 * @throws IOException
	 */
	private JMenuBar createMenu() throws IOException {

		// Menu bar
		JMenuBar menuBar = new JMenuBar();

		// Menu Item
		JMenu file = new JMenu("File");

		// Submenus and actions
		JMenuItem copy = new JMenuItem("Copy to clipboard");
		JMenuItem newButton = new JMenuItem("New");
		JMenuItem save = new JMenuItem("Save to disk");
		JMenuItem load = new JMenuItem("Load from file");
		JMenuItem wiki = new JMenuItem("Wiki");

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choice = new JFileChooser();
				choice.setFileFilter(new FileNameExtensionFilter("Assembly Line File", "asm"));
				choice.setSelectedFile(new File("MyProgram.asm"));
				choice.showSaveDialog(aw);
				if (choice.getSelectedFile().getName().indexOf('.') < 0) {
					choice.setSelectedFile(new File(choice.getSelectedFile() + ".asm"));
				} else {
					if (!choice.getSelectedFile().getName().substring(choice.getSelectedFile().getName().indexOf('.'))
							.equals(".asm")) {
						choice.setSelectedFile(new File(choice.getSelectedFile().getName().substring(0,
								choice.getSelectedFile().getName().indexOf('.')) + ".asm"));
					}
				}
				try (PrintWriter out = new PrintWriter(choice.getSelectedFile())) {
					out.println(aw.useNums + System.lineSeparator() + aw.copyCode().trim());
				} catch (FileNotFoundException e1) {
				}
			}
		});

		wiki.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/TheMarstonConnell/Assembly-Line/wiki"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		copy.addActionListener(new ActionListener() {

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

		// Init help pop-up
		helpFrame = new JDialog();
		helpFrame.setAlwaysOnTop(true);
		helpFrame.setTitle("Help");
		JPanel root = new JPanel();
		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setBackground(Color.white);

		// Reads help file from file instead of hard coding it.
		InputStream is = AssemblyMachine.class.getResourceAsStream("/misc/help.txt");
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String fileAsString = sb.toString();

		text.setText(fileAsString);
		JScrollPane sp = new JScrollPane(text);
		sp.setPreferredSize(new Dimension(600, 400));
		text.setCaretPosition(0);
		root.add(sp);
		helpFrame.add(root);
		// Display the window
		helpFrame.pack();
		helpFrame.setLocationRelativeTo(null);
		helpFrame.setVisible(false);
		helpFrame.setResizable(false);

		JMenuItem help = new JMenuItem("Help");
		help.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!helpFrame.isVisible()) {
					helpFrame.setVisible(true);
				}
			}

		});

		file.add(newButton);
		file.add(copy);
		file.add(save);
		file.add(load);
		file.add(help);
		file.add(wiki);

		// Menu item
		JMenu edit = new JMenu("Edit");

		// Submenus and actions
		JMenuItem cleanUp = new JMenuItem("Refactor");
		cleanUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				aw.cleanUp();
			}

		});

		JMenu langType = new JMenu("Language");
		codeStep = new JCheckBoxMenuItem("Step through code");
		codeStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.stepCode = ((JCheckBoxMenuItem) e.getSource()).isSelected();
				saveUserPrefs();
			}

		});

		// TODO fix darkmode
		darkModeButton = new JCheckBoxMenuItem("Dark Mode");
		darkModeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.darkmode = ((JCheckBoxMenuItem) e.getSource()).isSelected();
				saveUserPrefs();
				updateLAF(aw.darkmode);
				aw.code.update();
			}

		});

		ButtonGroup group = new ButtonGroup();
		machine = new JRadioButtonMenuItem("Machine Language");
		group.add(machine);
		machine.setSelected(true);
		machine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = true;
				aw.codeTitle.setText("Machine Language");
				aw.scrollPane.setColumnHeaderView(aw.codeTitle);

				aw.code.update();
				saveUserPrefs();
			}

		});
		langType.add(machine);

		assembler = new JRadioButtonMenuItem("Assembler");
		assembler.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = false;
				aw.codeTitle.setText("Assembler");
				aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				aw.code.update();
				saveUserPrefs();
			}

		});
		group.add(assembler);
		langType.add(assembler);

		JMenuItem graphics = new JMenuItem("Display Graphics");
		graphics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.displayGraphics();

			}

		});

		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choice = new JFileChooser();
				choice.setFileFilter(new FileNameExtensionFilter("Assembly Line File", "asm"));
				choice.setSelectedFile(new File("MyProgram.asm"));
				choice.showOpenDialog(aw);

				FileInputStream fis;
				String str = null;
				try {
					fis = new FileInputStream(choice.getSelectedFile());

					byte[] data = new byte[(int) choice.getSelectedFile().length()];
					fis.read(data);
					fis.close();
					str = new String(data, "UTF-8");

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (str.trim().startsWith("true")) {
					machine.setSelected(true);
					assembler.setSelected(false);

					aw.useNums = true;
					aw.codeTitle.setText("Machine Language");
					aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				} else {
					machine.setSelected(false);
					assembler.setSelected(true);
					aw.useNums = false;
					aw.codeTitle.setText("Assembler");
					aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				}
				aw.code.setText(str.replace("true", "").replace("false", "").trim());
			}
		});

		// Menu Item
		JMenu run = new JMenu("Run");

		// Submenus and actions
		JMenuItem compile = new JMenuItem("Compile");
		JMenuItem runProg = new JMenuItem("Run");
		JMenuItem compRun = new JMenuItem("Compile and Run");

		compile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.compile();

			}

		});
		runProg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.runCode();

			}

		});
		compRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.compile();
				aw.runCode();

			}

		});

		run.add(compile);
		run.add(runProg);
		run.add(compRun);

		// Key bindings
		newButton.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
		load.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
		wiki.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.Event.CTRL_MASK));
		help.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.Event.CTRL_MASK));
		cleanUp.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.Event.CTRL_MASK));
		graphics.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.Event.CTRL_MASK));
		compile.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.Event.CTRL_MASK));
		runProg.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.Event.CTRL_MASK));
		compRun.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.Event.CTRL_MASK));

		edit.add(darkModeButton);
		edit.add(langType);
		edit.add(codeStep);
		edit.addSeparator();
		edit.add(cleanUp);
		edit.addSeparator();
		edit.add(graphics);

		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(run);

		return menuBar;
	}

	/**
	 * Creates and displays layout to display including frame.
	 * 
	 * @author Marston Connell
	 * @throws IOException
	 */
	private void createAndShowGUI() throws IOException {

		// Create and set up the window.
		frame = new JFrame("Assembly Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cl = new CardLayout();
		JPanel root = new JPanel(cl);
		frame.add(root);

		aw = new AssemblyWindow(MaterialFonts.REGULAR);

		// Add contents to the window.
		root.add(aw, EDITOR);
		cl.show(root, EDITOR);

		frame.setJMenuBar(createMenu());

		// Display the window
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);

		try {
			frame.setIconImage(ImageIO.read(frame.getClass().getResource("/assemblyIcon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public AssemblyMachine() throws IOException {
		updateLAF(false);

		createAndShowGUI();
		loadUserPrefs();

	}

	/**
	 * Program starting point.
	 * 
	 * @author Marston Connell
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * Custom debug stream.
		 */
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

		try {
			assemblyMachine = new AssemblyMachine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void updateLAF(boolean dark) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		ui = new MaterialLookAndFeel(dark);
		try {
			UIManager.setLookAndFeel(ui);
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}

		
		if (frame != null) {
			aw.code.dark = dark;

		SwingUtilities.updateComponentTreeUI(frame);
		SwingUtilities.updateComponentTreeUI(helpFrame);
		SwingUtilities.updateComponentTreeUI(aw.graphicsPane);
		aw.tln.setCurrentLineForeground(ui.colors.currentText);
		aw.setBorder(MaterialFonts.REGULAR, ui.colors.currentText);
		aw.code.updateColor(ui.colors.currentText, ui.colors.currentAccent);
		aw.memory.getTableHeader().setBackground(ui.colors.currentPrimary);
		aw.scrollPane.getColumnHeader().setBackground(ui.colors.currentBackground);
		aw.scrollPane.setBackground(ui.colors.currentBackground);
		aw.memoryDisplay.setBackground(ui.colors.currentBackground);
		}

		// frame.pack();
		// helpFrame.pack();
	}

	/**
	 * Copy wrapper for Window.
	 * 
	 * @author Marston Connell
	 */
	private void copy() {
		StringSelection stringSelection = new StringSelection(aw.copyCode());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public void saveUserPrefs() {
		File dir = getAppData();
		dir.mkdirs();

		try (PrintWriter out = new PrintWriter(new File(dir, "userData.dat"))) {
			out.println(aw.useNums + System.lineSeparator() + aw.darkmode + System.lineSeparator() + aw.stepCode);
		} catch (FileNotFoundException e1) {
		}

	}

	public static void LogError(Exception e) {
		File dir = new File(getAppData(), "logs");
		dir.mkdirs();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		try (PrintWriter out = new PrintWriter(new File(dir, dateFormat.format(date) + ".log"))) {
			out.print(e.toString());
			out.close();
		} catch (FileNotFoundException e1) {

		}

		assemblyMachine.aw.showMessage("Error Report",
				"Find the log at %appdata%/AssemblyLine/logs/ with this time and date. Please upload contents of file to https://github.com/TheMarstonConnell/Assembly-Line/issues.");

	}

	public void loadUserPrefs() {
		File dir = getAppData();
		boolean useNums = true;
		boolean darkMode = false;
		boolean step = false;
		BufferedReader reader;
		File readingFile = new File(dir, "userData.dat");
		if (readingFile.exists()) {
			try {
				reader = new BufferedReader(new FileReader(readingFile));
				int x = 0;
				String line = reader.readLine();
				while (line != null) {
					switch (x) {
					case 0:
						useNums = Boolean.parseBoolean(line.trim());
						break;
					case 1:
						darkMode = Boolean.parseBoolean(line.trim());
						break;
					case 2:
						step = Boolean.parseBoolean(line.trim());
						break;
					}
					x++;
					System.out.println(line);
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			runFirstTimeSetUp();
		}

		readingFile = new File(dir, "tos.dat");
		try {
			reader = new BufferedReader(new FileReader(readingFile));
			String line = reader.readLine();
			sendStats = Boolean.parseBoolean(line.trim());
			System.out.println(sendStats);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		aw.darkmode = darkMode;
		darkModeButton.setSelected(darkMode);
		try {
			updateLAF(aw.darkmode);
		} catch (Exception e) {

		}
		aw.code.update();
		codeStep.setSelected(step);
		aw.stepCode = step;
		aw.useNums = useNums;
		if (useNums) {
			aw.codeTitle.setText("Machine Language");
			aw.scrollPane.setColumnHeaderView(aw.codeTitle);

			aw.code.update();
			machine.setSelected(true);
			assembler.setSelected(false);
		} else {
			aw.codeTitle.setText("Assembler");
			aw.scrollPane.setColumnHeaderView(aw.codeTitle);
			aw.code.update();
			machine.setSelected(false);
			assembler.setSelected(true);
		}
	}

	private void runFirstTimeSetUp() {

		Object[] options = { "Send data", "Don't Send" };
		int n = JOptionPane.showOptionDialog(frame,
				"Would you like to send all error data to our servers to better improve our software?",
				"Error Tracking", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				options[1]);

		File dir = getAppData();
		dir.mkdirs();

		try (PrintWriter out = new PrintWriter(new File(dir, "tos.dat"))) {
			out.println(n == 0);

		} catch (FileNotFoundException e1) {
		}

		try (PrintWriter out = new PrintWriter(new File(dir, "userData.dat"))) {
			out.println(aw.useNums + System.lineSeparator() + aw.darkmode + System.lineSeparator() + aw.stepCode);
		} catch (FileNotFoundException e1) {
		}

	}

	private static File getAppData() {
		File dir = new File(System.getenv("APPDATA"));
		return new File(dir, "AssemblyLine");
	}

}
