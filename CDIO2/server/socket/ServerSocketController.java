package socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerSocketController implements IServerSocketController {

	Socket socket;
	DataOutputStream outToWeight;
	BufferedReader inFromWeight;
	String currentLine;
	
	public ServerSocketController(String ip, int port) throws UnknownHostException, IOException{
			socket=new Socket(ip,port);	
			outToWeight=new DataOutputStream(socket.getOutputStream());
			inFromWeight=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
	}
	
	@Override
	public void sendCommand(String command) {
		try {
			outToWeight.writeBytes(command+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getLine() {
		String weightMessage=null;
		try {
			while(!inFromWeight.ready());
			
			weightMessage =inFromWeight.readLine();
			currentLine = weightMessage;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weightMessage;
	}

	public String getCurrentLine()
	{
		return currentLine;
	}
}
