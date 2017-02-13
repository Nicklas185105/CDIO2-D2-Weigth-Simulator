package weight.gui;

import java.util.ArrayList;
import java.util.Arrays;

import weight.IWeightInterfaceController;
import weight.IWeightInterfaceObserver;
import weight.KeyPress;

public class Local_GUI implements IWeightInterfaceController {
	private static Local_GUI instance;
	private ArrayList<IWeightInterfaceObserver> observers = new ArrayList<IWeightInterfaceObserver>(); 
	private FxApp fxApp;

	public Local_GUI() { instance = this; }
	@Override public void run() { FxApp.go(); }
	public static Local_GUI getInstance() { return instance; }
	public void setApp(FxApp fxApp) { this.fxApp = fxApp; fxApp.setSim(this); }
	

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
