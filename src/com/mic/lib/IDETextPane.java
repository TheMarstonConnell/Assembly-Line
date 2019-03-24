package com.mic.lib;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.mic.assembly.LineHighlightPane;

/**
 * The styled TextPane that highlights syntax and colours comments.
 * 
 * @author Marston Connell
 *
 */
public class IDETextPane extends JTextPane {

	private static final long serialVersionUID = 1L;
	private int newestLine = 0;
	private String[] possibleCommands;

	final StyleContext cont = StyleContext.getDefaultStyleContext();
	final AttributeSet COMMAND = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
	final AttributeSet POINTER = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			new Color(150, 55, 214));
	final AttributeSet COMMENT = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
			new Color(63, 127, 95));
	final AttributeSet BOLD = cont.addAttribute(cont.getEmptySet(), StyleConstants.Bold, true);
	final AttributeSet PLAIN = cont.removeAttribute(BOLD, StyleConstants.Bold);

	final AttributeSet BASIC = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

	public LineHighlightPane highLighter;
	private JTextField error;

	public IDETextPane(String[] commands, JTextField errorText) {
		this(commands);
		this.error = errorText;

	}

	/**
	 * Initilizes pane with available commands.
	 * 
	 * @author Marston Connell
	 * @param String[] commands
	 */
	public IDETextPane(String[] commands) {
		setForeground(Color.black);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.possibleCommands = commands;
		setDocument(new DefaultStyledDocument() {

			private static final long serialVersionUID = 1L;

			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				fixColors();
			}

			public void remove(int offset, int len) throws BadLocationException {
				super.remove(offset, len);

				fixColors();

			}
		});

		highLighter = new LineHighlightPane(this);

		this.setUI(highLighter);
	}

	/**
	 * Forces update on pane.
	 * 
	 * @author Marston Connell
	 */
	public void update() {
		try {
			fixColors();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Finds last non-alphebetic character in text given.
	 * 
	 * @author Marston Connell
	 * @param String text
	 * @param        int index
	 * @return index of character.
	 */
	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	/**
	 * Finds first alphebetic character in given text.
	 * 
	 * @author Marston Connell
	 * @param text
	 * @param index
	 * @return index of character
	 */
	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	/**
	 * Checks if string matches available command for hihglighting.
	 * 
	 * @author Marston Connell
	 * @param text
	 * @return true if command.
	 */
	private boolean checkIfCommand(String text) {
		for (int i = 0; i < possibleCommands.length; i++) {
			if (possibleCommands[i].equals(text)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Applies highlighting and colouring to every possible word in text.
	 * 
	 * @author Marston Connell
	 * @throws BadLocationException
	 */
	private void fixColors() throws BadLocationException {
		newestLine = 0;
		DefaultStyledDocument doc = (DefaultStyledDocument) this.getDocument();

		String text = this.getDocument().getText(0, doc.getLength());

		int before = findLastNonWordChar(text, 0);
		if (before < 0)
			before = 0;
		int after = findFirstNonWordChar(text, 0 + text.length());
		int wordL = before;
		int wordR = before;
		while (wordR < after) {
			if (text.charAt(wordR) == '\n') {
				newestLine = wordR;
			}

			if (wordR + 1 >= doc.getLength() || wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
				if (wordR > newestLine)
					if (text.substring(newestLine, wordR + 1).contains("#")) {

						doc.setCharacterAttributes(wordL, (wordR + 1) - wordL, COMMENT, true);
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);

					} else if (!Pattern.compile("[0-9]").matcher(text.substring(wordL, wordR + 1).trim()).find()) {
						if (checkIfCommand(text.substring(wordL, wordR + 1).trim())) {
							doc.setCharacterAttributes(wordL, wordR + 1 - wordL, COMMAND, true);
							doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);
						} else {
							if (text.substring(wordL, wordR + 1).trim().length() == 3) {
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, POINTER, true);
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, BOLD, false);

							} else {
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, BASIC, true);
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);
							}

						}
					} else {
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, BASIC, true);
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);
					}
				wordL = wordR;
			}
			wordR++;
		}

		if (error != null) {
			error.setText(checkErrors());
		}

	}

	private String checkErrors() {
		if (!Pattern.compile("[0-9]").matcher(this.getText().trim()).find()) {
			String codeo = getText();
			String[] lines = codeo.split("\n");
			int error = 0;

			for (String line : lines) {
				if (line.trim().length() > 0) {
					boolean command = false;
					for (String theCommand : possibleCommands) {
						if (line.contains(theCommand) || line.trim().startsWith("#")) {
							command = true;
						}

					}

					if (!command) {
						return "No valid commands present at line " + error;
					}

				}

				error++;

			}

		}
		return "";
	}
}
