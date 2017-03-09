package controller;
import java.io.IOException;
import java.util.Scanner;

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

	public void run() {
		while(true)
		{
			login();
			measureWeight();
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

		try {
			socket.sendCommand("K 3");
			checkAnswer("K A");
			socket.sendCommand("RM20 8 \"Enter UserID\" \"\" \"&3\"");
			checkAnswer("RM20 B");
			checkAnswer("RM20 A");
			socket.sendCommand("P111 \"Username\"");
			checkAnswer("P111 A");
			checkAnswer("K F 4");
		} catch (WrongAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void measureWeight()
	{
		while(true) {
			try {
				socket.sendCommand("RM20 8 \"Enter Batch Number\" \"\" \"&3\"");
				checkAnswer("RM20 B");
				checkAnswer("RM20 A");
				socket.sendCommand("P111 \"ProductName\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
				socket.sendCommand("P111 \"Clear the weight\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
				socket.sendCommand("T");
				checkAnswer("T S");
				socket.sendCommand("P111 \"Put Tara on the weight\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
				socket.sendCommand("S");
				checkAnswer("S S");
				socket.sendCommand("T");
				checkAnswer("T S");
				socket.sendCommand("P111 \"Put the Product one the Tara\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
				socket.sendCommand("S");
				checkAnswer("S S");
				socket.sendCommand("T");
				checkAnswer("T S");
				socket.sendCommand("P111 \"Clear The weight\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
				socket.sendCommand("S");
				checkAnswer("S S");
				socket.sendCommand("P111 \"Measurement OK\"");
				checkAnswer("P111 A");
				checkAnswer("K F 4");
			} catch (WrongAnswerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public String checkAnswer(String expectedAnswer) throws WrongAnswerException{
		String out = "";
		String answer = socket.getLine();
		System.out.println(answer);
		String[] answerArr = answer.split(" ");
		if(answerArr.length > 2) {
			String[] expectedArr = expectedAnswer.split(" ");
			for (int i = 0; i < 2; i++)
			{
				if(!expectedArr[i].equals(answerArr[i])) {
					throw new WrongAnswerException("Expected answer: " + expectedAnswer + " but was: " + answer);
				}
			}

			out = answerArr[2];
		}
		else if(!answer.equals(expectedAnswer)) {
			throw new WrongAnswerException("Expected answer: " + expectedAnswer + " but was: " + answer);
		}


		return out;
	}

	class WrongAnswerException extends Exception {

		private static final long serialVersionUID = 6680381402613971943L;

		WrongAnswerException(String message) {
			super(message);
		}
	}
}


