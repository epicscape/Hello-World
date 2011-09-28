package org.dementhium.tools;

import java.util.Scanner;

import org.dementhium.mysql.MD5Encryption;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class MD5Gen {

	
	public static void main(String[] args) {
		System.out.println("To hex: ");
		String generate = new Scanner(System.in).next();
		
		System.out.println("MD5 for " + generate + " is .........");
		
		String encrypted = MD5Encryption.encrypt(generate);
		
		System.out.println(encrypted);
		
		//bf8c144140b15befb8ce662632a7b76e
		//c1a5298f939e87e8f962a5edfc206918
	}
	
}
