package socket;

public interface IServerSocketController {

	public void sendCommand(String command);
	public String getLine();
	public void flush();
	public String getCurrentLine();

}
