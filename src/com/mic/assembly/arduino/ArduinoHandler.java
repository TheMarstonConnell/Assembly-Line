package com.mic.assembly.arduino;

public class ArduinoHandler {
	Arduino uno;

	public ArduinoHandler() {
		uno = new Arduino("COM7", 9600);
		uno.openConnection();

	}

	public void setHigh(int port) {
		uno.serialWrite("1" + port);
	}

	public void setLow(int port) {
		uno.serialWrite("0" + port);
	}

	public String read(int port) {
		uno.serialWrite("2" + port);
		while (!(uno.getSerialPort().bytesAvailable() > 0)) {
			System.out.println("Waiting...");
		}
		String read = uno.serialRead();
		return read;		
	}

}
