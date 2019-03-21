package com.mic.lib;

public class NumberConversions {
	public static int binaryToDecimal(long num) {
		String n = String.valueOf(num);
		return Integer.parseInt(n, 2);
	}
	public static void main(String[] args) {
		System.out.println(binaryToDecimal(1000));
	}
}
