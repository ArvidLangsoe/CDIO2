package main;

import java.util.Scanner;
import controller.ServerMainController;

public class Main {

	public static void main(String[] args) 
	{	
		ServerMainController controller = new ServerMainController();

		System.out.println("Please type the Ip of the weight");
		controller.startConnection(getInput());
		startWeightMeassure(controller);
		
		


	}
	private static String getInput()
	{
		Scanner Keyboard = new Scanner(System.in);
		String input =Keyboard.nextLine();
		return input;
	}
	public static void startWeightMeassure(ServerMainController controller)
	{
		controller.login();
		
		System.out.println("Is this login true? (y/n)");
		if(getInput()=="y")
		controller.meassureWeight();
		else
		{
			startWeightMeassure(controller);
		}
		
	}

}
