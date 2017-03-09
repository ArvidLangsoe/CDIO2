package main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import controller.ServerMainController;
import socket.ServerSocketController;

public class testmain {
	static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) {
		ServerSocketController socket;
		try {
			socket = new ServerSocketController("10.16.138.248", 8000);

			while (true) {
				String s = getInput();
				if (s.equals("$")) {
					System.out.println(socket.getLine());
				}else if(s.equals("@")){
					break;
				}
				else {
					socket.sendCommand(s);
				}
			}
			keyboard.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getInput() {
		while(!keyboard.hasNext());
		String input = keyboard.nextLine();

		return input;
	}

}
