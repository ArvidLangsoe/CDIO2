package controller;
import java.io.IOException;
import socket.IServerSocketController;
import socket.ServerSocketController;

public class ServerMainController {
	IServerSocketController socket = null;

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
		socket.getLine();
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
			RM20Command("Enter UserID");
			P111Command("Username");
		} catch (WrongAnswerException e) {
			login();
		}
	}

	public void measureWeight()
	{
		
		while(true) {
			double tara = 0;
			double netto = 0;
			double brutto = 0;
			double actualBrutto = 0;
			final double TOLERANCE = 0.01;

			try {
				RM20Command("Enter Batch Number");
				P111Command("Product Name");
				P111Command("Clear the weight");
				socket.sendCommand("T");
				checkAnswer("T S");

				tara = getMeasurement("Put Tara on the weight");
				netto = getMeasurement("Put the Product on the Tara");
				brutto = tara + netto;

				actualBrutto = getMeasurement("Clear The weight");
				if(TOLERANCE > Math.abs(brutto + actualBrutto)) {
					socket.sendCommand("P111 \"Measurement OK\"");
					checkAnswer("P111 A");
					checkAnswer("K C 4");
				}
				else {
					socket.sendCommand("P111 \"Measurement NOT OK\"");
					checkAnswer("P111 A");
					checkAnswer("K C 4");
				}
	
				
			} catch (WrongAnswerException e) {
				String lastLine = socket.getCurrentLine();
				
				if (lastLine.equals("K R 3"))
				{
					return;
				}
				else if (lastLine.equals("K C 2"))
				{
					//restart measurement
				}
				else //If error.
				{
					System.out.println("ERROR Last line was: "+lastLine);
					//restart measurement
				}
				
			}

		}
	}

	public String RM20Command(String message) throws WrongAnswerException {
		String out = "";
		socket.sendCommand("RM20 8 \"" + message + "\" \"\" \"&3\"");
		checkAnswer("RM20 B");
		out = checkAnswer("RM20 A");


		return out;
	}

	public void P111Command(String message) throws WrongAnswerException {
		socket.sendCommand("P111 \"" + message + "\"");
		checkAnswer("P111 A");
		checkAnswer("K C 4");

	}

	public double getMeasurement(String message) {
		double measurement = -999999;
		try {
			socket.sendCommand("P111 \"" + message + "\"");
			checkAnswer("P111 A");
			checkAnswer("K C 4");
			socket.sendCommand("S");
			measurement = Double.parseDouble(checkAnswer("S S"));
			socket.sendCommand("T");
			checkAnswer("T S");
		} catch (WrongAnswerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return measurement;
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
			

			out = answerArr[answerArr.length-1];
			if(answerArr[1].equals("C")){
				if(!answerArr[2].equals(expectedArr[2])){
					throw new WrongAnswerException("Expected answer: " + expectedAnswer + " but was: " + answer);
				}
			}
			if(out.equals("kg")){
				out = answerArr[answerArr.length-2];
			}
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


