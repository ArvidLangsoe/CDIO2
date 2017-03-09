package controller;
import java.io.IOException;

import socket.ServerSocketController;

public class ServerMainController {
	ServerSocketController socket = null;
	public void startConnection(String ip)
	{

		int port =8000;

		try {
			socket =new ServerSocketController(ip,port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void manualSendCommand(String input)
	{
	socket.sendCommand(input);	
	}
	public String manualGetLine()
	{
	return socket.getLine();	
	}
	public void login()
	{		
		socket.sendCommand("K 3");
		checkAnswer(socket.getLine());
		socket.sendCommand("RM20 8 \"Enter UserID\" \"\" \"&3\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("P111 \"Username\"");
		socket.getLine();
		socket.getLine();
	}

	public void measureWeight()
	{
		socket.sendCommand("RM20 8 \"Enter Batch Number\" \"\" \"&3\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("P111 \"ProductName\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("P111 \"Clear the weight\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("T");
		socket.getLine();
		socket.sendCommand("P111 \"Put Tara on the weight\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("S");
		socket.getLine();
		socket.sendCommand("T");
		socket.getLine();
		socket.sendCommand("P111 \"Put the Product one the Tara\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("S");
		socket.getLine();
		socket.sendCommand("T");
		socket.getLine();
		socket.sendCommand("P111 \"Clear The weight\"");
		socket.getLine();
		socket.getLine();
		socket.sendCommand("S");
		socket.getLine();
		socket.sendCommand("P111 \"Measurement OK\"");
		socket.getLine();
		socket.getLine();
	}
	
	public boolean checkAnswer(String answer) {
		boolean success = false;
		if (answer.equals("K A")) {
			success = true;
		}
		return success;
	}
}


