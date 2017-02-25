package controller;

import socket.SocketController;
import socket.ISocketController;
import weight.IWeightInterfaceController;
import weight.gui.WeightGUI;
/**
 * Simple class to fire up application and inject implementations
 * @author Christian
 *
 */
public class Main {

	public static void main(String[] args) {
		ISocketController socketHandler = new SocketController();
		IWeightInterfaceController weightCOntroller = new WeightGUI();
		//Injecting socket and uiController into mainController - Replace with improved versions...
		IMainController mainCtrl = new MainController(socketHandler, weightCOntroller);
		//.init and .start could be merged
		mainCtrl.start();
		
	}
}
