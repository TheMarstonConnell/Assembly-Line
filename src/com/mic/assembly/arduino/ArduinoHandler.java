package com.mic.assembly.arduino;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoHandler {
	Arduino uno;

	public ArduinoHandler() {
		uno = new Arduino();
		
		SerialPort[] portNames = uno.getPorts();
		
		for(int i = 0; i < portNames.length; i ++) {
			String name = portNames[i].getSystemPortName();
			System.out.println(name);
			uno.setPortDescription(name);
			uno.setBaudRate(9600);
			if(uno.openConnection()) {
				System.out.println("Found Open Port");
				uno.serialRead();
				break;
			}
		}
		
//		uno.setPortDescription("COM3");
//		uno.setBaudRate(9600);
//		if(uno.openConnection()) {
//			System.out.println("Found Open Port");
//		}

	}

	

	public void setHigh(int port) {
		uno.serialWrite("1" + port);
	}

	public void setLow(int port) {
		uno.serialWrite("0" + port);
	}

	public boolean read(int port) {
		uno.serialWrite("2" + port);
		while (!(uno.getSerialPort().bytesAvailable() > 0)) {
			System.out.println("Waiting...");
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Bytes available:" + uno.getSerialPort().bytesAvailable());
		boolean read = uno.readBoolean();
		return read;
	}

}
