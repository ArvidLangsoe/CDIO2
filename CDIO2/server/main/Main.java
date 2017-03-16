package main;

import controller.ServerMainController;

public class Main {
	
	public static void main(String[] args) {
		ServerMainController controller = new ServerMainController();
		//String ip = "169.254.2.2";
		String ip = "10.16.139.112";
		controller.startConnection(ip, true);
		controller.run();
	}
}
