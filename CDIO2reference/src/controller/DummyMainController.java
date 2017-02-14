package controller;

import java.util.Locale;

import socket.ISocketController;
import socket.ISocketObserver;
import socket.SocketInMessage;
import socket.SocketOutMessage;
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
	private WeightState weightState = WeightState.READY;
	private String weightInput = "";
	double load;
	double tare;
	
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
		switch(weightState){
		case READY:
			handleMessageReady(message);
			break;
		case RM20:
			socketHandler.sendMessage(new SocketOutMessage("RM20 waiting for input"));
			break;		
		}	
		
	}
	
	private void handleMessageReady(SocketInMessage message) {
		switch(message.getType()){
		case B:
			load = Double.parseDouble(message.getMessage());
			showWeight();
			break;
		case D:
			weightController.showMessagePrimaryDisplay(message.getMessage());
			break;
		case Q:
			System.exit(1);
			break;
		case RM204:
		case RM208:
			weightController.showMessagePrimaryDisplay(message.getMessage());
			weightState=WeightState.RM20;
			weightController.setSoftButtonTexts(new String[]{"OK"});;
			socketHandler.sendMessage(new SocketOutMessage("RM20 B"));
			break;
		case S:
			System.out.println("sending S S message");
			socketHandler.sendMessage(new SocketOutMessage("S S " + weightString()));
			break;
		case T:
			tare = load;
			showWeight();
			break;
		}
	}

	//Listening for UI input
	@Override
	public void notifyKeyPress(KeyPress keyPress) {
		switch (keyPress.getType()) {
		case SOFTBUTTON:
			System.out.println("Softbutton " + keyPress.getKeyNumber() + " pressed");
			if (weightState==WeightState.RM20 && keyPress.getKeyNumber()==0){
				weightState= WeightState.READY;
				socketHandler.sendMessage(new SocketOutMessage("RM20 A "+ weightInput));
				weightInput="";
				weightController.showMessageSecondaryDisplay(weightInput);
				weightController.showMessagePrimaryDisplay("");
			}
			break;
		case TEXT:
			System.out.println("Character " + keyPress.getCharacter() + " pressed");
			weightInput+=keyPress.getCharacter();
			weightController.showMessageSecondaryDisplay(weightInput);
			break;
		case TARA:
			System.out.println("TARA pressed");
			tare = load;
			showWeight();
			break;
		case ZERO:
			System.out.println("ZERO pressed");
			if (weightState==WeightState.RM20){
				weightState= WeightState.READY;
				socketHandler.sendMessage(new SocketOutMessage("RM20 A "+ weightInput));
				weightInput="";
				weightController.showMessageSecondaryDisplay(weightInput);
				weightController.showMessagePrimaryDisplay("");
			}
			break;
		case C:
			System.out.println("C pressed");
			break;

		}

	}

	@Override
	public void notifyWeightChange(double newWeight) {
		load = newWeight;
		showWeight();
		
	}

	private void showWeight() {
		weightController.showMessagePrimaryDisplay(weightString());
	}

	private String weightString() {
		return String.format(Locale.US, "%.4f", load-tare);
	}

}
