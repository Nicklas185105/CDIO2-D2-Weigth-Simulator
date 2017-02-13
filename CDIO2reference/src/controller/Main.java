package controller;

import socket.DummySocketHandler;
import socket.ISocketController;
import weight.DummyConsoleController;
import weight.IWeightInterfaceController;
import weight.gui.Local_GUI;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {

	public static void main(String[] args) {
		ISocketController socketHandler = new DummySocketHandler();
		IWeightInterfaceController uiController = new Local_GUI();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new DummyMainController(socketHandler, uiController);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
