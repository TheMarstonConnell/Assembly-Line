package com.mic.assembly;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The main emulator for the entire machine.
 * 
 * @author Marston Connell
 *
 */
public class AssemblyMachine {
	static JFrame frame;
	static CardLayout cl;
	final static String EDITOR = "edit";
	final static String RUNTIME = "run";
	static AssemblyWindow aw;
	static JDialog helpFrame;

	/**
	 * Initializes Menu bar for frame.
	 * 
	 * @author Marston Connell
	 * @return JMenuBar
	 */
	private static JMenuBar createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem copy = new JMenuItem("Copy to clipboard");
		JMenuItem newButton = new JMenuItem("New");
		JMenuItem save = new JMenuItem("Save to disk");
		JMenuItem load = new JMenuItem("Load from file");
		JMenuItem wiki = new JMenuItem("Wiki");
		
		newButton.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_N, 
		        java.awt.Event.CTRL_MASK));

		copy.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_C, 
		        java.awt.Event.CTRL_MASK));
		
		save.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_S, 
		        java.awt.Event.CTRL_MASK));
		
		load.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_O, 
		        java.awt.Event.CTRL_MASK));
		
		wiki.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_W, 
		        java.awt.Event.CTRL_MASK));
		
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

		helpFrame = new JDialog();
		helpFrame.setAlwaysOnTop(true);
		helpFrame.setTitle("Help");

		JPanel root = new JPanel();

		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setBackground(Color.white);
		text.setText("Help Keys \r\nadr=address co(adr 45) = contents of address 45\r\n" + "   \r\n"
				+ "McnCode	AsmCode	Example	Description\r\n" + "	\r\n"
				+ "01	inp	01023	user input goes into adr 23\r\n" + "02	out	02034	co(adr 34) displayed\r\n"
				+ "03	lda	03012	co(adr 12) goes into AC\r\n" + "04	sta	04056	con(AC) goes into adr 56\r\n"
				+ "05	ldm	05023	co(adr 23) goes into MQ\r\n" + "06	stm	06045	co(MQ) goes into adr 45\r\n"
				+ "07	add	07065	co(adr 65) added to AC\r\n" + "08	sub	08043	co(adr 43) subtracted from AC\r\n"
				+ "09	mul	09123	MQ multiplied by co(adr 123)\r\n" + "10	div	10035	MQ divided by co(adr 35)\r\n"
				+ "11	key	11023	gets current key code and sends it to co(adr 23)\r\n"
				+ "00	end	00000	programs stops executing\r\n" + "\r\n" + "Transfer codes" + "\r\n"
				+ "21	tra	21034	always transfer to adr 34\r\n" + "22	tre	22056	go to adr 56 if cc=0\r\n"
				+ "23	tne	23099	go to adr 99 if cc<>0\r\n" + "24	tlt	24067	go to adr 67 if cc<0\r\n"
				+ "25	tgt	25078	go to adr 78 if cc>0\r\n" + "26	tle	26068	go to adr 68 if cc<=0\r\n"
				+ "27	tge	27073	go to adr 73 if cc>=0\r\n" + "\r\n"
				+ "Literals - the address part is the value used" + "\r\n"
				+ "-21	lal	-21006	6 is put into the AC\r\n" + "-22	lml	-22056	56 is put into the MQ\r\n"
				+ "-23	adl	-23034	34 is added to the AC\r\n" + "-24	sbl	-24789	789 is subtracted from AC\r\n"
				+ "-25	mpl	-25098	MQ is multiplied by 98\r\n" + "-26	dvl	-26102	MQ is divided by 102\r\n" + "\r\n"
				+ "Registers" + "\r\n" + "28	lax	28000	load AC with co(X reg)\r\n"
				+ "29	stx	29000	store co(AC) into X reg\r\n" + "31	inx	31000	increment ( X = X + 1)\r\n"
				+ "32	dex	32000	decrement ( X = X - 1)\r\n" + "-28	lay	-28000	load AC with co(Y reg)\r\n"
				+ "-29	sty	-29000	store co(AC) into Y reg\r\n" + "-31	iny	-31000	increment (Y = Y + 1)\r\n"
				+ "-32	dey	-32000	decrement (Y = Y - 1)\r\n" + "\r\n" + "Procedures" + "\r\n"
				+ "-27	gsb	-27067	execute co(adr 67) next\r\n" + "-30	ret	-30000	return to line after gsb\r\n"
				+ "\r\n" + "Arrays" + "\r\n" + "30	def	30015	define array of size 15\r\n" + "\r\n" + "Graphics"
				+ "\r\n" + "40	rep	40000	repaint display\r\n"
				+ "41	drw	41000	draws pixel at loaded x and y coordinates of saved colour\r\n"
				+ "43	cxp	43045	sets x position to 45\r\n" + "44	cyp	44056	sets y position to 56\r\n"
				+ "45	red	45123	sets red value to 123\r\n" + "46	grn	46312	sets green value to 312\r\n"
				+ "47	blu	47213	sets blue value to 213\r\n" + "\r\n" + "Condition Codes (CC)" + "\r\n"
				+ "-Set by most recent change to any of AC, MQ, X, Y registers.\r\n"
				+ "CC=-1 if a change to a negative\r\n" + "CC=0 if a change to zero\r\n"
				+ "CC=1 if a change to a positive\r\n" + "\r\n" + "");

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
		help.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_H, 
		        java.awt.Event.CTRL_MASK));
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

		JMenu edit = new JMenu("Edit");

		JMenuItem cleanUp = new JMenuItem("Refactor");
		cleanUp.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_I, 
		        java.awt.Event.CTRL_MASK));
		cleanUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				aw.cleanUp();
			}

		});

		JMenu langType = new JMenu("Language");
		JCheckBoxMenuItem codeStep = new JCheckBoxMenuItem("Step through code");
		codeStep.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.stepCode = ((JCheckBoxMenuItem) e.getSource()).isSelected();
			}

		});

		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem machine = new JRadioButtonMenuItem("Machine Language");
		group.add(machine);
		machine.setSelected(true);
		machine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = true;
				aw.codeTitle.setText("Machine Language");
				aw.scrollPane.setColumnHeaderView(aw.codeTitle);

				aw.code.update();

			}

		});
		langType.add(machine);

		JRadioButtonMenuItem assembler = new JRadioButtonMenuItem("Assembler");
		assembler.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				aw.useNums = false;
				aw.codeTitle.setText("Assembler");
				aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				aw.code.update();

			}

		});
		group.add(assembler);
		langType.add(assembler);

		JMenuItem graphics = new JMenuItem("Display Graphics");
		graphics.setAccelerator(KeyStroke.getKeyStroke(
		        java.awt.event.KeyEvent.VK_G, 
		        java.awt.Event.CTRL_MASK));
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
				if(str.trim().startsWith("true")) {
					machine.setSelected(true);
					assembler.setSelected(false);

					aw.useNums = true;
					aw.codeTitle.setText("Machine Language");
					aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				}else {
					machine.setSelected(false);
					assembler.setSelected(true);
					aw.useNums = false;
					aw.codeTitle.setText("Assembler");
					aw.scrollPane.setColumnHeaderView(aw.codeTitle);
				}
				aw.code.setText(str.replace("true", "").replace("false", "").trim());
			}
		});
		
		edit.add(langType);
		edit.add(codeStep);
		edit.addSeparator();
		edit.add(cleanUp);
		edit.addSeparator();
		edit.add(graphics);

		menuBar.add(file);
		menuBar.add(edit);

		return menuBar;
	}

	
	/**
	 * Creates and displays layout to display including frame.
	 * @author Marston Connell
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		frame = new JFrame("Assembly Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel root = new JPanel(new BorderLayout());
		frame.add(root);

		aw = new AssemblyWindow();

		// Add contents to the window.
		root.add(aw, BorderLayout.CENTER);

		frame.setJMenuBar(createMenu());

		// Display the window
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
//		frame.setResizable(false);

		aw.code.requestFocus();
		
		try {
			frame.setIconImage(ImageIO.read(frame.getClass().getResource("/assemblyIcon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Program starting point.
	 * @author Marston Connell
	 * @param args
	 */
	public static void main(String[] args) {
		createAndShowGUI();

	}

	/**
	 * Copy wrapper for Window.
	 * @author Marston Connell
	 */
	private static void copy() {
		StringSelection stringSelection = new StringSelection(aw.copyCode());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

}
