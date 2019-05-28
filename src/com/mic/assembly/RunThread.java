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
									while (response == null) {
										response = JOptionPane.showInputDialog("Enter Input");
									}
									aw.mdl.setValueAt(response, m, 1);
									break;
								case 2:
									if (aw.input.getText().equals("")) {
										aw.input.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									} else {
										aw.input.setText(
												aw.input.getText() + ", " + ((String) aw.mdl.getValueAt(m, 1)).trim());

									}

									break;
								case 3:
									aw.AC.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.MEMtoAC = true;

									break;
								case 4:

									aw.mdl.setValueAt(aw.AC.getText(), m, 1);
									aw.cd.ACtoMEM = true;

									break;
								case 5:
									aw.MQ.setText(((String) aw.mdl.getValueAt(m, 1)).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									aw.cd.MEMtoMQ = true;

									break;
								case 6:

									aw.mdl.setValueAt(aw.MQ.getText(), m, 1);
									aw.cd.MQtoMEM = true;

									break;
								case 7:

									num = Integer.valueOf(aw.AC.getText())
											+ Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoAC = true;
									break;
								case 8:
									num = Integer.valueOf(aw.AC.getText())
											- Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoAC = true;

									break;
								case 9:
									num = Integer.valueOf(aw.MQ.getText())
											* Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									aw.MQ.setText(String.valueOf(num).trim());
									aw.recentChange = num;
									aw.cd.MEMtoMQ = true;

									break;
								case 10:
									try {
										num = Integer.valueOf(aw.MQ.getText())
												/ Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
										aw.MQ.setText(String.valueOf(num).trim());
										aw.recentChange = num;
										aw.cd.MEMtoMQ = true;

									} catch (NumberFormatException n) {

										aw.showError("Runtime Error", "Number format exception at index: " + m);
									} catch (ArithmeticException ae) {
										aw.showError("Arithmetic Error", "Cannot divide by zero!");
									}

									break;
								case 11:
									aw.mdl.setValueAt(String.valueOf(aw.currentKeyDown), m, 1);
									break;
								case 21:
									i = m - 1;
									break;
								case 22:
									if (aw.recentChange == 0) {
										i = m - 1;
									}
									break;
								case 23:
									if (aw.recentChange != 0) {
										i = m - 1;
									}
									break;
								case 24:
									if (aw.recentChange < 0) {
										i = m - 1;
									}
									break;
								case 25:
									if (aw.recentChange > 0) {
										i = m - 1;
									}
									break;
								case 26:
									if (aw.recentChange <= 0) {
										i = m - 1;
									}
									break;
								case 27:
									if (aw.recentChange >= 0) {
										i = m - 1;
									}
									break;

								case 28:
									aw.AC.setText(((String) aw.xReg.getText().trim()));
									aw.recentChange = Integer.valueOf(aw.xReg.getText());
									aw.cd.XtoAC = true;
									break;
								case 29:
									aw.xReg.setText(aw.AC.getText());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									aw.cd.ACtoX = true;
									break;
								case 31:
									num = Integer.valueOf(aw.xReg.getText()) + 1;
									aw.xReg.setText(String.valueOf(num));
									aw.recentChange = num;
									break;
								case 32:
									num = Integer.valueOf(aw.xReg.getText()) - 1;
									aw.xReg.setText(String.valueOf(num));
									aw.recentChange = num;
									break;
								case 40:
									aw.graphicsPane.repaint();
									break;
								case 41:
									g = aw.graphicsPane.toDraw.getGraphics();
									g.setColor(new Color(aw.red, aw.blue, aw.green));
									g.fillRect(aw.xPos, aw.yPos, 1, 1);
									g.dispose();
									break;
								case 43:
									aw.xPos = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									break;
								case 44:
									aw.yPos = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									break;
								case 45:
									aw.red = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									break;
								case 46:
									aw.blue = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
									break;
								case 47:
									aw.green = Integer.valueOf((String) aw.mdl.getValueAt(m, 1));
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
									break;
								case 22:
									aw.MQ.setText((String.valueOf(m)).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									break;
								case 23:

									num = Integer.valueOf(aw.AC.getText())
											+ Integer.valueOf((String.valueOf(m)).trim());
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									break;
								case 24:
									num = Integer.valueOf(aw.AC.getText())
											- Integer.valueOf((String.valueOf(m)).trim());
									aw.AC.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.AC.getText());
									break;
								case 25:
									num = Integer.valueOf(aw.MQ.getText())
											* Integer.valueOf((String.valueOf(m)).trim());
									aw.MQ.setText(String.valueOf(num).trim());
									aw.recentChange = Integer.valueOf(aw.MQ.getText());
									break;
								case 26:
									try {
										num = Integer.valueOf(aw.MQ.getText())
												/ Integer.valueOf((String.valueOf(m)).trim());
										aw.MQ.setText(String.valueOf(num).trim());
										aw.recentChange = Integer.valueOf(aw.MQ.getText());

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

									break;
								case 29:
									aw.yReg.setText(aw.AC.getText());
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									aw.cd.ACtoY = true;

									break;
								case 31:
									num = Integer.valueOf(aw.yReg.getText()) + 1;
									aw.yReg.setText(String.valueOf(num));
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									break;
								case 32:
									num = Integer.valueOf(aw.yReg.getText()) - 1;
									aw.yReg.setText(String.valueOf(num));
									aw.recentChange = Integer.valueOf(aw.yReg.getText());
									break;
								}

							}
						}
					}
					aw.repaint();
					if (aw.stepCode) {
						JOptionPane.showMessageDialog(aw, "Completed step in code on line " + curI + ".", "Stepping",
								JOptionPane.PLAIN_MESSAGE);
					}
				} else {
					return;
				}

			}
		} catch (Exception e) {
			AssemblyMachine.LogError(e);
		}
	}
}
