package controller;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import socket.SocketOutMessage;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;
import weight.KeyPress.KeyPressType;
/**
 * MainController - integrating input from socket and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class MainController implements IMainController, ISocketObserver, IWeightInterfaceObserver {

	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;
	private KeyState keyState = KeyState.K1;
	private double weight;
	private String input;

	public MainController(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.init(socketHandler, uiController);
		weight = 0;
		input = "";
	}

	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.socketHandler = socketHandler;
		this.weightController=uiController;
	}

	@Override
	public void start() {
		if (socketHandler!=null && weightController!=null){
			//Makes this controller interested in messages from the socket
			socketHandler.registerObserver(this);
			
			//Starts socketHandler in own thread
			new Thread(socketHandler).start();

		//Makes this controller interested in messages from the GUI
			weightController.registerObserver(this);

			//Starts weightController in own thread
			new Thread(weightController).start();

		} else {
			System.err.println("No controllers injected!");
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		switch (message.getType()) {
		case B:
			notifyWeightChange(weight + Double.parseDouble(message.getMessage()));
			socketHandler.sendMessage(new SocketOutMessage("DB"));
			break;
		case D:
			weightController.showMessagePrimaryDisplay(message.getMessage()); 
			break;
		case Q:
			System.exit(0);
			break;
		case RM204:
			break;
		case RM208:
			weightController.showMessageSecondaryDisplay(message.getMessage());
			setupSoftButtons();
			break;
		case S:
			socketHandler.sendMessage(new SocketOutMessage("S S " + weight + " kg"));
			break;
		case T: 
			socketHandler.sendMessage(new SocketOutMessage("T S " + weight + " kg" ));
			notifyWeightChange(0);
			break;
		case DW:
			weightController.showMessagePrimaryDisplay(weight + " kg");
			break;
		case K:
			handleKMessage(message);
			break;
		case P111:
			weightController.showMessageSecondaryDisplay(message.getMessage()); 
			break;
		}

	}

	private void handleKMessage(SocketInMessage message) {
		switch (message.getMessage()) {
		case "1" :
			this.keyState = KeyState.K1;
			break;
		case "2" :
			this.keyState = KeyState.K2;
			break;
		case "3" :
			this.keyState = KeyState.K3;
			break;
		case "4" :
			this.keyState = KeyState.K4;
			break;
		default:
			socketHandler.sendMessage(new SocketOutMessage("ES"));
			break;
		}
	}
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		//TODO implement logic for handling input from ui
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			
			if (keyPress.getKeyNumber() == 2)
			{
				input = input.substring(0, input.length()-1);
				weightController.showMessageSecondaryDisplay(input);
			}
			if (keyPress.getKeyNumber() == 3)
			{
				socketHandler.sendMessage(new SocketOutMessage("RM20 A " + input));
				input = "";
				clearSoftButtons();
			}
			break;
		case TARA:
			//Not implemented
			break;
		case TEXT:
			input = input + keyPress.getCharacter();
			weightController.showMessageSecondaryDisplay(input);
			break;
		case ZERO:
			//Not implemented
			break;
		case C:
			//Not implemented
			break;
		case EXIT:
			//Not implemented
			break;
		case SEND:
			if (keyState.equals(KeyState.K4) || keyState.equals(KeyState.K3) ){
				socketHandler.sendMessage(new SocketOutMessage("K A 3"));
			}
			break;
		}
	}
	
	@Override
	public void notifyWeightChange(double newWeight) {
		weight = newWeight;
		weightController.showMessagePrimaryDisplay(weight + " kg");
	}
	
	public void setupSoftButtons(){
		String[] texts=new String[]{"","","Erase","Ok"};
		
		weightController.setSoftButtonTexts(texts);
	}
	public void clearSoftButtons(){
		String[] texts=new String[]{" "," "," "," "};
		weightController.setSoftButtonTexts(texts);
		
	}

}
