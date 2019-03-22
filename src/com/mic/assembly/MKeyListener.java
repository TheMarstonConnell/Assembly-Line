package com.mic.assembly;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Listens to current key being pressed down.
 * @author Marston Connell
 *
 */
public class MKeyListener extends KeyAdapter {

	AssemblyWindow aw;

	/**
	 * Links new listener to AssemblyWindow.
	 * @author Marston Connell
	 * @param AssemblyWindow aw
	 * @see AssemblyWindow
	 */
	public MKeyListener(AssemblyWindow aw) {
		super();
		this.aw = aw;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		super.keyPressed(event);
		aw.currentKeyDown = event.getKeyCode();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		super.keyReleased(e);
		aw.currentKeyDown = 0;
	}
}