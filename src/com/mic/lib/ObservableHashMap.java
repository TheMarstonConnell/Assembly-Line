package com.mic.lib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Action;

public class ObservableHashMap<K, V> extends HashMap {

	ActionListener action;

	public ObservableHashMap() {
		super();
	}

	@Override
	public Object put(Object key, Object value) {
		if (action != null) {
			action.actionPerformed(new ActionEvent(key, 0, "ObjectAddedEvent"));
		}
		return super.put(key, value);
	}

	public void addKeysChangedListener(ActionListener al) {
		action = al;
	}

}
