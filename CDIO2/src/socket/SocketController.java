package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import socket.SocketInMessage.SocketMessageType;

public class SocketController implements ISocketController {
	Set<ISocketObserver> observers = new HashSet<ISocketObserver>();
	// TODO Maybe add some way to keep track of multiple connections?
	private BufferedReader inStream;
	private DataOutputStream outStream;

	@Override
	public void registerObserver(ISocketObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unRegisterObserver(ISocketObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void sendMessage(SocketOutMessage message) {
		if (outStream != null) {
			if(message != null)
				sendResponse(message.getMessage());
		} else {
			System.out.println("The connection between the server and the client is closed !");
		}
	}

	@Override
	public void run() {
		// TODO some logic for listening to a socket //(Using try with resources
		// for auto-close of socket)
		try (ServerSocket listeningSocket = new ServerSocket(Port)) {
			while (true) {
				waitForConnections(listeningSocket);
			}
		} catch (IOException e1) {
			// TODO Maybe notify MainController?
			e1.printStackTrace();
		}

	}

	private void waitForConnections(ServerSocket listeningSocket) {
		System.out.println("Socket Server opened, waiting ...");
		try {
			Socket activeSocket = listeningSocket.accept(); // Blocking call
			inStream = new BufferedReader(new InputStreamReader(activeSocket.getInputStream()));
			outStream = new DataOutputStream(activeSocket.getOutputStream());

			String inLine;

			// .readLine is a blocking call
			// TODO How do you handle simultaneous input and output on socket?
			// TODO this only allows for one open connection - how would you
			// handle multiple connections?

			System.out.println(activeSocket.getRemoteSocketAddress() + "> Client connected to the server");
			while (true) {
				inLine = inStream.readLine();
				System.out.println(inLine);
				if (inLine == null)
					break;
				String[] commandLine = inLine.split(" ", 2);
				String cmd = commandLine[0];

				String responseType = ""; //used to know what the server socket shall
				// send to the client socket after retrieving a request from it.
				// Set this value to null means that the response could not be sent
				// at this moment because needs some others values from the rest of the program

				switch (cmd) {

				case "RM20": // Display a message in the secondary display and
					// wait for response
					// TODO implement logic for RM command

					// Special Type
					if (inLine.split(" ").length > 1 && !(commandLine[1].isEmpty())) {

						String displayMessage = commandLine[1].split("\"")[1];

						System.out.println("The GUI shall display : " + '"' + displayMessage + '"' + " and is now waiting for GUI response.");

						notifyObservers(new SocketInMessage(SocketMessageType.RM208, displayMessage));
						responseType = null;

					} else {
						responseType = " ES";
					}

					break;
				case "D":// Command to display a user-made message on the
					// primary display using the "D" command.
					// TODO Refactor to make sure that faulty messages doesn't
					// break the system.

					if (inLine.split(" ").length > 1 && !(commandLine[1].isEmpty()) && commandLine[1].length() <= 7) {
						String displayMessage = commandLine[1].split("\"")[1];

						System.out.println("The GUI shall display : " + '"' + displayMessage + '"');

						notifyObservers(new SocketInMessage(SocketMessageType.D, displayMessage));
						responseType = " A";
					} else {
						responseType = " ES";
					}
					break;
				case "DW": // Command to clear the primary display on the
					// weight.
					// TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.DW, ""));

					responseType = " A";

					break;
				case "P111": // Command to show a message in the secondary
					// display.
					// TODO implement

					if (inLine.split(" ").length > 1 && !(commandLine[1].isEmpty()) && commandLine[1].length() <= 32) {

						String displayMessage = commandLine[1].split("\"")[1];

						System.out.println("The GUI shall display : " + '"' + displayMessage + '"');

						notifyObservers(new SocketInMessage(SocketMessageType.P111, displayMessage));
						responseType = " A";
					} else {
						responseType = " ES";
					}

					break;
				case "T": // Tares the currently read weight of nothing but an
					// empty container on the scale.
					// TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.T, "T S"));

					// Special type
					responseType = null;

					break;
				case "S": // Command to request the current load on the weight
					// to be displayed.
					// TODO implement
					notifyObservers(new SocketInMessage(SocketMessageType.S, ""));

					// Special type
					responseType = " S";

					break;
				case "K":

					// Changes the weights current button-mode between one of
					// four possible.

					// K1: The function is completed, but the function's code is
					// not sent (Standard).

					// K2: The function is not completed, and the function's
					// code is not sent (Buttons are inactive).

					// K3: The function is not completed, but the function's
					// code is sent.

					// K4: The function is completed, and the function's code is
					// sent.

					if (inLine.split(" ").length > 1) {
						notifyObservers(new SocketInMessage(SocketMessageType.K, inLine.split(" ")[1]));
					}
					break;
				case "B": // Set the load
					// TODO implement
					if(!(commandLine[1].isEmpty())){

						String displayMessage = commandLine[1];

						System.out.println("The GUI shall display : " + '"' + displayMessage + '"');

						try{
							Double.parseDouble(displayMessage);
							notifyObservers(new SocketInMessage(SocketMessageType.P111, displayMessage));
							
							cmd = "D";
							responseType = "B";
						}catch (Exception e){
							responseType = " ES";
						}
					}else{
						responseType = " ES";
					}

					break;
				case "Q": // Quit
					responseType = " A";

					notifyObservers(new SocketInMessage(SocketMessageType.Q, ""));
					break;
				default: // Something went wrong?
					// TODO implement
					responseType = " ES";
					break;
				}

				if(responseType != null)
					sendResponse(cmd + responseType);
			}
		} catch (IOException e) {
			// TODO maybe notify mainController?
			e.printStackTrace();
		}
	}

	private void notifyObservers(SocketInMessage message) {
		for (ISocketObserver socketObserver : observers) {
			socketObserver.notify(message);
		}
	}
	//Returns the response to the outStream.
	private void sendResponse(String message) {
		try{
			outStream.writeBytes(message + "\n");
		}catch (IOException e){
			System.out.println("Cannot send response to the Socket Client \n" + e.getMessage());
		}
	}

}
