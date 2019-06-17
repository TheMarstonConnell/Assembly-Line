package com.mic.assembly;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JOptionPane;

/**
 * Where all running of user made programs take place.
 * 
 * @author Marston Connell
 *
 */
public class RunThread extends Thread {

	AssemblyWindow aw;
	public boolean run = true;

	/**
	 * Links thread to AssemblyWindow.
	 * 
	 * @author Marston Connell
	 * @param AssemblyWindow aw
	 */
	public RunThread(AssemblyWindow aw) {
		this.aw = aw;

	}

	/**
	 * Handles all runtime calculations and I/O.
	 * @author Marston Connell
	 */
	@Override
	public void run() {
		super.run();

		try {

			Graphics g = aw.graphicsPane.toDraw.getGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 1000, 500);
			g.dispose();
			aw.graphicsPane.repaint();

			for (int i = 0; i < 1000; i++) {
				if (run) {
					int curI = i;
					boolean cont = false;
					for (int j = i; j < 1000; j++) {
						if (aw.mdl.getValueAt(j, 1) != "" && aw.mdl.getValueAt(j, 1) != " ") {
							cont = true;
						}
					}
					if (!cont)
						return;
					if (aw.mdl.getValueAt(i, 1) != "" && aw.mdl.getValueAt(i, 1) != " ") {
						if (((String) aw.mdl.getValueAt(i, 1)).trim().length() >= 5) {
							int m = Integer.valueOf(((String) aw.mdl.getValueAt(i, 1)).substring(2, 5).trim());
							int num;

							if (((String) aw.mdl.getValueAt(i, 1)).trim().charAt(0) != '-') {
								
								switch (Integer.valueOf(((String) aw.mdl.getValueAt(i, 1)).substring(0, 2))) {
								case 0:
									return;
								case 1:
									String response = null;
									while (response == null || response.trim().equals("")) {
										response = JOptionPane.showInputDialog("Enter Input");
									}
									aw.mdl.setValueAt(response, m, 1);
									aw.cd.MEMupdate = true;
									aw.cd.InputUpdate = true;
									aw.cd2.MEMupdate = true;
									aw.cd2.InputUpdate = true;
									aw.cd.setCurrentAction("Taking input");
									aw.cd2.setCurrentAction("Taking input");

									break;
								case 2:
									if (aw.input.getText().equals("")) {
										aw.input.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									} else {
										aw.input.setText(
												aw.input.getText() + ", " + ((String) aw.mdl.getValueAt(m, 1)).trim());

									}
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Writing output");
									aw.cd2.setCurrentAction("Writing output");


									break;
								case 3:
									aw.AC.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.MEMtoAC = true;
									aw.cd.ACupdate = true;
									aw.cd2.MEMtoAC = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Loading AC");
									aw.cd2.setCurrentAction("Loading AC");

									break;
								case 4:

									aw.mdl.setValueAt(aw.AC.getText(), m, 1);
									aw.cd.ACtoMEM = true;
									aw.cd.MEMupdate = true;
									aw.cd2.ACtoMEM = true;
									aw.cd2.MEMupdate = true;
									aw.cd.setCurrentAction("Saving AC");
									aw.cd2.setCurrentAction("Saving AC");

									break;
								case 5:
									aw.MQ.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									aw.cd.MEMtoMQ = true;
									aw.cd.MQupdate = true;
									aw.cd2.MEMtoMQ = true;
									aw.cd2.MQupdate = true;
									aw.cd.setCurrentAction("Loading MQ");
									aw.cd2.setCurrentAction("Loading MQ");

									break;
								case 6:

									aw.mdl.setValueAt(aw.MQ.getText(), m, 1);
									aw.cd.MQtoMEM = true;
									aw.cd.MEMupdate = true;
									aw.cd2.MQtoMEM = true;
									aw.cd2.MEMupdate = true;
									aw.cd.setCurrentAction("Saving MQ");
									aw.cd2.setCurrentAction("Saving MQ");

									break;
								case 7:

									num = Integer.valueOf(aw.AC.getText())
											+ Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoAC = true;
									aw.cd.ACupdate = true;
									aw.cd2.MEMtoAC = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Adding to AC");
									aw.cd2.setCurrentAction("Adding to AC");

									break;
								case 8:
									num = Integer.valueOf(aw.AC.getText())
											- Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoAC = true;
									aw.cd.ACupdate = true;
									aw.cd2.MEMtoAC = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Subtracting from AC");
									aw.cd2.setCurrentAction("Subtracting from AC");

									break;
								case 9:
									num = Integer.valueOf(aw.MQ.getText())
											* Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.MQ.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoMQ = true;
									aw.cd.MQupdate = true;
									aw.cd2.MEMtoMQ = true;
									aw.cd2.MQupdate = true;
									aw.cd.setCurrentAction("Multiplying MQ");
									aw.cd2.setCurrentAction("Multiplying MQ");

									break;
								case 10:
									try {
										num = Integer.valueOf(aw.MQ.getText())
												/ Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
										aw.MQ.setText(String.valueOf(num).trim());
										aw.recentChange = num;
										aw.cd.MEMtoMQ = true;
										aw.cd.MQupdate = true;
										aw.cd2.MEMtoMQ = true;
										aw.cd2.MQupdate = true;
										aw.cd.setCurrentAction("Dividing MQ");
										aw.cd2.setCurrentAction("Dividing MQ");

									} catch (NumberFormatException n) {

										aw.showError("Runtime Error", "Number format exception at index: " + m);
									} catch (ArithmeticException ae) {
										aw.showError("Arithmetic Error", "Cannot divide by zero!");
									}

									break;
								case 11:
									aw.mdl.setValueAt(String.valueOf(aw.currentKeyDown), m, 1);
									aw.cd.MEMupdate = true;
									aw.cd2.MEMupdate = true;
									aw.cd.InputUpdate = true;
									aw.cd2.InputUpdate = true;
									aw.cd.setCurrentAction("Grabbing held key");
									aw.cd2.setCurrentAction("Grabbing held key");

									break;
								case 21:
									i = m - 1;
									aw.cd.setCurrentAction("Transfering");
									aw.cd2.setCurrentAction("Transfering");

									break;
								case 22:
									if (aw.recentChange == 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c=0");
									aw.cd2.setCurrentAction("Transfering if c=0");

									break;
								case 23:
									if (aw.recentChange != 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c<>0");
									aw.cd2.setCurrentAction("Transfering if c<>0");

									break;
								case 24:
									if (aw.recentChange < 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c<0");
									aw.cd2.setCurrentAction("Transfering if c<0");

									break;
								case 25:
									if (aw.recentChange > 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c>0");
									aw.cd2.setCurrentAction("Transfering if c>0");

									break;
								case 26:
									if (aw.recentChange <= 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c<=0");
									aw.cd2.setCurrentAction("Transfering if c<=0");

									break;
								case 27:
									if (aw.recentChange >= 0) {
										i = m - 1;
									}
									aw.cd.setCurrentAction("Transfering if c>=0");
									aw.cd2.setCurrentAction("Transfering if c>=0");

									break;

								case 28:
									aw.AC.setText(((String) aw.xReg.getText().trim()));
									aw.recentChange = Integer.valueOf(aw.xReg.getText());
									aw.cd.XtoAC = true;
									aw.cd.ACupdate = true;
									aw.cd2.XtoAC = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Loading AC with X");
									aw.cd2.setCurrentAction("Loading AC with X");

									break;
								case 29:
									aw.xReg.setText(aw.AC.getText());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.ACtoX = true;
									aw.cd.Xupdate = true;
									aw.cd2.Xupdate = true;
									aw.cd.setCurrentAction("Loading X with AC");
									aw.cd2.setCurrentAction("Loading X with AC");

									break;
								case 31:
									num = Integer.valueOf(aw.xReg.getText()) + 1;
									aw.xReg.setText(String.valueOf(num));
									aw.recentChange = num;
									aw.cd.Xupdate = true;
									aw.cd2.Xupdate = true;
									aw.cd.setCurrentAction("Increasing X");
									aw.cd2.setCurrentAction("Increasing X");

									break;
								case 32:
									num = Integer.valueOf(aw.xReg.getText()) - 1;
									aw.xReg.setText(String.valueOf(num));
									aw.recentChange = num;
									aw.cd.Xupdate = true;
									aw.cd2.Xupdate = true;
									aw.cd.setCurrentAction("Decreasing X");
									aw.cd2.setCurrentAction("Decreasing X");

									break;
								case 40:
									aw.graphicsPane.repaint();
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Repainting display");
									aw.cd2.setCurrentAction("Repainting display");

									break;
								case 41:
									g = aw.graphicsPane.toDraw.getGraphics();
									g.setColor(new Color(aw.red, aw.blue, aw.green));
									g.fillRect(aw.xPos, aw.yPos, 1, 1);
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Drawing pixel");
									aw.cd2.setCurrentAction("Drawing pixel");

									g.dispose();
									break;
								case 43:
									aw.xPos = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting X position");
									aw.cd2.setCurrentAction("Setting X position");

									break;
								case 44:
									aw.yPos = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting Y position");
									aw.cd2.setCurrentAction("Setting Y position");

									break;
								case 45:
									aw.red = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting red value");
									aw.cd2.setCurrentAction("Setting red value");

									break;
								case 46:
									aw.blue = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting green value");
									aw.cd2.setCurrentAction("Setting green value");

									break;
								case 47:
									aw.green = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting blue value");
									aw.cd2.setCurrentAction("Setting blue value");

									break;
								case 51:
									aw.am.arduino.setHigh(m);
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting pin to high");
									aw.cd2.setCurrentAction("Setting pin to high");

									break;
								case 52:
									aw.am.arduino.setLow(m);
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd.setCurrentAction("Setting pin to low");
									aw.cd2.setCurrentAction("Setting pin to low");

									break;
								case 53:
									if(aw.am.arduino.read(m)) {
										aw.AC.setText("1");
									}else {
										aw.AC.setText("0");

									}
									aw.cd.DisplayUpdate = true;
									aw.cd2.DisplayUpdate = true;
									aw.cd2.ACupdate = true;
									aw.cd.ACupdate = true;
									aw.cd.setCurrentAction("Reading pin");
									aw.cd2.setCurrentAction("Reading pin");

//									System.out.println();
									break;
								case 60:
									Thread.sleep(m);
									aw.cd.setCurrentAction("Waiting...");
									aw.cd2.setCurrentAction("Waiting...");

									break;
								default:
									JOptionPane.showMessageDialog(aw,
											"Error on line " + i
													+ ". Please refer to 'File' -> 'Help' for available commands.",
											"Runtime Error", JOptionPane.WARNING_MESSAGE);
									return;
								}
								// if negative operator
							} else {
								m = Integer.valueOf(((String) aw.mdl.getValueAt(i, 1)).substring(3, 6).trim());
								switch (Integer.valueOf(((String) aw.mdl.getValueAt(i, 1)).substring(1, 3))) {
								case 21:
									aw.AC.setText((String.valueOf(m)).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.ACupdate = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Literal into AC");
									aw.cd2.setCurrentAction("Literal into AC");

									break;
								case 22:
									aw.MQ.setText((String.valueOf(m)).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									aw.cd.MQupdate = true;
									aw.cd2.MQupdate = true;
									aw.cd.setCurrentAction("Literal into MQ");
									aw.cd2.setCurrentAction("Literal into MQ");

									break;
								case 23:

									num = Integer.valueOf(aw.AC.getText())
											+ Integer.valueOf((String.valueOf(m)).trim());
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.ACupdate = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Literal added to AC");
									aw.cd2.setCurrentAction("Literal added to AC");


									break;
								case 24:
									num = Integer.valueOf(aw.AC.getText())
											- Integer.valueOf((String.valueOf(m)).trim());
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.ACupdate = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Literal subtracted from AC");
									aw.cd2.setCurrentAction("Literal subtracted from AC");

									break;
								case 25:
									num = Integer.valueOf(aw.MQ.getText())
											* Integer.valueOf((String.valueOf(m)).trim());
									aw.MQ.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									aw.cd.MQupdate = true;
									aw.cd2.MQupdate = true;
									aw.cd.setCurrentAction("Literal multiplied to MQ");
									aw.cd2.setCurrentAction("Literal multiplied to MQ");


									break;
								case 26:
									try {
										num = Integer.valueOf(aw.MQ.getText())
												/ Integer.valueOf((String.valueOf(m)).trim());
										aw.MQ.setText(String.valueOf(num).trim());
										aw.recentChange = Integer.valueOf(aw.MQ.getText());
										aw.cd.MQupdate = true;
										aw.cd2.MQupdate = true;
										aw.cd.setCurrentAction("MQ divided by literal");
										aw.cd2.setCurrentAction("MQ divided by literal");

									} catch (NumberFormatException n) {
										aw.showError("Runtime Error", "Number format exception at index: " + m);
									} catch (ArithmeticException ae) {
										aw.showError("Runtime Error", "Cannot divide by zero!");
									}
									break;
								case 28:
									aw.AC.setText(((String) aw.yReg.getText().trim()));
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.YtoAC = true;
									aw.cd.ACupdate = true;
									aw.cd2.YtoAC = true;
									aw.cd2.ACupdate = true;
									aw.cd.setCurrentAction("Y into AC");
									aw.cd2.setCurrentAction("Y into AC");

									break;
								case 29:
									aw.yReg.setText(aw.AC.getText());
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									aw.cd.ACtoY = true;
									aw.cd2.ACtoY = true;
									aw.cd.Yupdate = true;
									aw.cd2.Yupdate = true;
									aw.cd.setCurrentAction("AC into Y");
									aw.cd2.setCurrentAction("AC into Y");

									break;
								case 31:
									num = Integer.valueOf(aw.yReg.getText()) + 1;
									aw.yReg.setText(String.valueOf(num));
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									aw.cd.Yupdate = true;
									aw.cd2.Yupdate = true;
									aw.cd.setCurrentAction("Increasing Y");
									aw.cd2.setCurrentAction("Increasing Y");

									break;
								case 32:
									num = Integer.valueOf(aw.yReg.getText()) - 1;
									aw.yReg.setText(String.valueOf(num));
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									aw.cd.Yupdate = true;
									aw.cd2.Yupdate = true;
									aw.cd.setCurrentAction("Decreasing Y");
									aw.cd2.setCurrentAction("Decreasing Y");

									break;
								}

							}
						}
					}
					aw.cd.repaint();
					aw.cd2.repaint();

					if (aw.stepCode) {
						JOptionPane.showMessageDialog(aw, "Completed step in code on line " + curI + ".", "Stepping",
								JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					aw.clearWindow();
					return;
				}

			}
			aw.clearWindow();

		} catch (Exception e) {
			AssemblyMachine.LogError(e);
		}
		aw.clearWindow();

	}

}
