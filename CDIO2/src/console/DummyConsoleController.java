package console;

public class DummyConsoleController implements IWeightInterfaceController{

	@Override
	public void run() {
		// TODO Some logic to catch some input and notify the observers
		
	}

	@Override
	public void registerObserver(IWeightInterfaceObserver uiObserver) {
		// TODO keep track of observers
	}

	@Override
	public void unRegisterObserver(IWeightInterfaceObserver uiObserver) {
		// TODO Remove observer from list	
	}


	@Override
	public void showMessagePrimaryDisplay(String string) {
		// TODO Show a message to the user on the display		
	}


}
