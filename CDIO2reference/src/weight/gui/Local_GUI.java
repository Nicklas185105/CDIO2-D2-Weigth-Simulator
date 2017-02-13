package weight.gui;

import java.util.ArrayList;
import java.util.Arrays;

import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

//import application.iSim.BadParam;
//import application.iSim.BadState;
//import application.iSim.Callback;	
//import application.iSim.InputType;
//import application.iSim.Unit;

public class Local_GUI implements IWeightInterfaceController {
	private static Local_GUI instance;
	private ArrayList<IWeightInterfaceObserver> observers = new ArrayList<IWeightInterfaceObserver>(); 
	private FxApp fxApp;

	public Local_GUI() { instance = this; }
	@Override public void run() { FxApp.go(); }
	public static Local_GUI getInstance() { return instance; }
	public void setApp(FxApp fxApp) { this.fxApp = fxApp; fxApp.setSim(this); }
	
	
	

	

	// load
//	public void setMsg(String msg) { fxApp.printLoad(msg); }
//	public void outputLoad(double newBrutto) { observer.setLoad(newBrutto, Unit.G); }
//	public void outputTara() { 
//		for (IWeightInterfaceObserver o : observers) {
//			o.notifyKeyPress(keypress);
//		}
//	}
//	public void outputZero() { observer.zero(); }
//	
//	// User input
//	public void getUserInput(String msg, String placeholder, boolean placeholderIsTentative, InputType inputType,
//			boolean inputTypeLocked, boolean allowNegative, Callback callback) {
//		fxApp.getUserInput(msg, placeholder, placeholderIsTentative, inputType, inputTypeLocked, allowNegative, callback);
//	}
//	public String[] getDefaultTexts() { return observer.getDefaultTexts(); }
//	
//	// softkeys
//	public void hideSoftkeys() { fxApp.softkeysHide(); }
//	public void showSoftkeys(String[] sftkeys, int firstSoftkey, boolean[] sftkeysChecked){ fxApp.softkeysShow(sftkeys, firstSoftkey, sftkeysChecked); }
//	public void outputSoftkeyPressed(int i) { observer.SoftkeyPressed(i); }
//	
//	// Info
//	public void setInfoText(int pos, String msg) { fxApp.printInfo(pos, msg); }
//	
//	// Bottom
//	public void setBottomText(String msg) { fxApp.printBottom(msg); }
//
//	// Deltabar
//	public void resetDeltabar() { fxApp.resetDeltabar(); }
//	public void turnOffDeltabar() { fxApp.turnOffDeltabar(); }
//	public void setDeltabar(double target, double tol, double netto) { fxApp.setDeltabar(target, tol, netto); }
//	public void updateDeltabar(double netto) { fxApp.updateDelta(netto); }
//	
//	public void cancel(){
//		
//	}
	@Override
	public void registerObserver(IWeightInterfaceObserver uiObserver) {
		this.observers.add(uiObserver);
	}
	@Override
	public void unRegisterObserver(IWeightInterfaceObserver uiObserver) {
		this.observers.remove(uiObserver);
	}
	@Override
	public void showMessagePrimaryDisplay(String string) {
		fxApp.printLoad(string);
	}
	@Override
	public void showMessageSecondaryDisplay(String string) {
		fxApp.printBottom(string);
	}
	@Override
	public void changeInputType(InputType type) {
		switch(type){
		case LOWER: fxApp.setButtonsLower(); break;
		case NUMBERS: fxApp.setButtonsNumbers(); break;
		case UPPER: fxApp.setButtonsUpper(); break;
		default: fxApp.setButtonsLower(); break;
		}
		
	}
	@Override
	public void setSoftButtonTexts(String[] texts) {
		int firstSoftkey = 0;
		boolean[] sftkeysChecked = new boolean[texts.length];
		Arrays.fill(sftkeysChecked, false);
		fxApp.softkeysShow(texts, firstSoftkey, sftkeysChecked);
		
	}
	public void onSliderValueChange(Double newValue) {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyWeightChange(newValue / 1000);
		}
		
	}
	public void onTaraButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Tara());
		}
		
	}
	public void onZeroButtonPressed() {
		for (IWeightInterfaceObserver o : observers) {
			o.notifyKeyPress(KeyPress.Zero());
		}
	}
	
	
	
	
	
	
	


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
