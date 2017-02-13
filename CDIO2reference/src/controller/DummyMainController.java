package controller;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;
/**
 * MainController - integrating input from socket and ui. Implements ISocketObserver and IUIObserver to handle this.
 * @author Christian Budtz
 * @version 0.1 2017-01-24
 *
 */
public class DummyMainController implements IMainController, ISocketObserver, IWeightInterfaceObserver {

	private ISocketController socketHandler;
	private IWeightInterfaceController weightController;
	
	public DummyMainController(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.init(socketHandler, uiController);
	}

	@Override
	public void init(ISocketController socketHandler, IWeightInterfaceController uiController) {
		this.socketHandler = socketHandler;
		this.weightController=uiController;
	}

	@Override
	public void start() {
		if (socketHandler!=null && weightController!=null){
		//Make this controller interested in messages from the socket
		socketHandler.registerObserver(this);
		//Start socketHandler in own thread
		new Thread(socketHandler).start();
		//Sign up for consoleInput
		weightController.registerObserver(this);
		//Start uiController in own thread
		new Thread(weightController).start();
		} else {
			System.err.println("No controllers injected!");
			//TODO handle with some exception?
		}
	}

	//Listening for socket input
	@Override
	public void notify(SocketInMessage message) {
		//TODO implement logic for handling input from socket
		System.out.println("Message from Socket received:" + message);
		weightController.showMessagePrimaryDisplay("Message received" + message); //Some dummy code
	}
	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			System.out.println("Softbutton " + keyPress.getKeyNumber() + " pressed");
			break;
		case TEXT:
			System.out.println("Character " + keyPress.getCharacter() + " pressed");
			break;
		case TARA:
			System.out.println("TARA pressed");
			break;
		}

	}

	@Override
	public void notifyWeightChange(double newWeight) {
		weightController.showMessagePrimaryDisplay(String.valueOf(newWeight) + " KG");
	}

}
