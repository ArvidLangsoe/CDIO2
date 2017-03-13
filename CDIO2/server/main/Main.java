package main;

import java.util.Scanner;
import controller.ServerMainController;

public class Main {
	

	public static void main(String[] args) {
		ServerMainController controller = new ServerMainController();
//		System.out.println("Please type the Ip of the weight");
		String ip = "169.254.2.2";
		controller.startConnection(ip);
		controller.run();
	}

	private static void manualCommand(ServerMainController controller) {
		System.out.println("how many returns do you expect? (1 or 2): else type:\"exit\"");
		String weightReturns = getInput();
		int weightReturnAmount;
		if (getInput() == "exit") {
			return;
		} else {
			weightReturnAmount = Integer.parseInt(weightReturns);
		}
		System.out.println("Please type a command: else type:\"exit\"");
		String command = getInput();
		if (getInput() == "exit") {
			return;
		} else {
			controller.manualSendCommand(command);
		}
		for (int i = 0; weightReturnAmount < i; i++) {
			System.out.println(controller.manualGetLine());
		}
		manualCommand(controller);
	}

	private static int getChoice() {
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.nextLine().toLowerCase();
		keyboard.close();
		return Integer.parseInt(input);
	}

	private static String getInput() {
		Scanner keyboard = new Scanner(System.in);
		String input = keyboard.nextLine();
		keyboard.close();
		return input;
	}

	public static void startWeightMeasure(ServerMainController controller) {
		controller.login();

		System.out.println("Is this login true? (y/n)");
		if (getInput() == "y")
			controller.measureWeight();
		else {
			startWeightMeasure(controller);
		}

	}

}
