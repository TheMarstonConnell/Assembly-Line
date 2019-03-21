package com.mic.lib;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.mic.assembly.LineHighlightPane;

public class IDETextPane extends JTextPane {

	int newestLine = 0;
	int lastNewLine = 0;
	String[] possibleCommands;

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

	public IDETextPane(String[] commands) {
		setForeground(Color.black);
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.possibleCommands = commands;
		setDocument(new DefaultStyledDocument() {

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

	public void update() {
		try {
			fixColors();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	private boolean checkIfCommand(String text) {
		for (int i = 0; i < possibleCommands.length; i++) {
			if (possibleCommands[i].equals(text)) {
				return true;
			}
		}
		return false;
	}

	private void fixColors() throws BadLocationException {
		lastNewLine = 0;
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
				lastNewLine = newestLine;
				newestLine = wordR;
			}

			if (wordR + 1 >= doc.getLength() || wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
				if (wordR > newestLine)
					if (text.substring(newestLine, wordR + 1).contains("#")) {

						doc.setCharacterAttributes(wordL, (wordR + 1) - wordL, COMMENT, false);
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);

					} else if(!Pattern.compile( "[0-9]" ).matcher( text.substring(wordL, wordR + 1).trim()  ).find()){
						if (checkIfCommand(text.substring(wordL, wordR + 1).trim())) {
							doc.setCharacterAttributes(wordL, wordR + 1 - wordL, COMMAND, false);
							doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);
						} else {
							if (text.substring(wordL, wordR + 1).trim().length() == 3){
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, POINTER, false);
								doc.setCharacterAttributes(wordL, wordR + 1 - wordL, BOLD, false);

							} 

						}
					}else {
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, BASIC, false);
						doc.setCharacterAttributes(wordL, wordR + 1 - wordL, PLAIN, false);
					}
				wordL = wordR;
			}
			wordR++;
		}

	}
}
