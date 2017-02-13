package weight.gui;

import weight.IWeightInterfaceController.InputType;

public class FxAppInputBtnHandler {
	private int lastBtnPressed = -1;
	private long lastTimePressed = -1;
	private String btnValue;
	
	public String onButtonPressed(String txt, int btn, int caretPos, InputType input_type){
		String pre = txt.substring(0, caretPos);
		String post = txt.substring(caretPos);
		if(InputType.LOWER == input_type || InputType.UPPER == input_type){
			if(btn == lastBtnPressed && System.currentTimeMillis() < lastTimePressed+1000){
				btnValue = nextValue(btn, btnValue, input_type);
				pre = pre.subSequence(0, pre.length()-1)+btnValue;
			} else {
				btnValue = ""+(InputType.LOWER == input_type ? FxApp.str_lower[btn].charAt(0) : FxApp.str_upper[btn].charAt(0));
				pre += btnValue;
			}

			lastTimePressed = System.currentTimeMillis();
			lastBtnPressed = btn;
		} else {
			pre += btn;
		}
		System.out.println("pre:  "+pre);
		System.out.println("post: "+post);
		return pre+post;
	}
	private String nextValue(int btn, String currentValue, InputType input_type){
		String str;
		switch(input_type){
		case LOWER: str = FxApp.str_lower[btn]; break;
		case UPPER: str = FxApp.str_upper[btn]; break;
		case NUMBERS:
		default: return currentValue;
		}
		int currentIndex = str.indexOf(currentValue.charAt(0));
		int index = (currentIndex +1) % str.length();
		return ""+str.charAt(index);
	}
	
	public void reset(){
		lastBtnPressed = -1;
		lastTimePressed = -1;
		btnValue = null;
	}

}
