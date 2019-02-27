package com.mic.assembly;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MKeyListener extends KeyAdapter {

	AssemblyWindow aw;

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