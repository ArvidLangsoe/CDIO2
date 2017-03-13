package controller;

import socket.SocketController;
import weight.IWeightInterfaceController;
import weight.gui.WeightGUI;
import socket.ISocketController;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {
	//Needed if the GUI is not located on the pc.
	//private static boolean gui= true;

	public static void main(String[] args) {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightController = new WeightGUI();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
