package com.mic.lib;

/**
 * Number conversion utility.
 * @author Marston Connell
 *
 */
public class NumberConversions {
	/**
	 * Converts binary number to decimal number.
	 * @author Marston Connell
	 * @param num
	 * @return
	 */
	public static int binaryToDecimal(long num) {
		String n = String.valueOf(num);
		return Integer.parseInt(n, 2);
	}
	/**
	 * Converts decimal number to binary number.
	 * @author Marston Connell
	 * @param num
	 * @return
	 */
	public static long decimalToBinary(int num) {
		String n = Integer.toBinaryString(num);
		return Long.parseLong(n);
	}
	
	public static void main(String[] args) {
		int dec = 24;
		System.out.println(dec);
		long bin = decimalToBinary(dec);
		System.out.println(bin);
		dec = binaryToDecimal(bin);
		System.out.println(dec);
		
	}
}
