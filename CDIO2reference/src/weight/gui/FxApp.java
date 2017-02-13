package weight.gui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import weight.IWeightInterfaceController.InputType;

public class FxApp extends Application {
	private Text txtload, txtbottom;
	private Text[] txtsft = new Text[6];
	private Text[] txtinfo = new Text[4];
	private TextField userInput;
	private Slider slider, dtop, dbottom;
	private ProgressBar pbload;
	private Button btnzero, btntara, btnshift; //, btnexit, btncancel; 
	private Button[] btnsft = new Button[6];
	private Button[] btnnum = new Button[10];
	public static final String[] str_lower = {".", "abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vxy", "z"};
	public static final String[] str_upper = {".", "ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VXY", "Z"};
	private InputType inputType = InputType.NUMBERS;
	private boolean userInputInprogress = false, userinputAllowNegative = false;
	private boolean userInputPlaceholderTentative = false, userInputTypeLocked = false;
//	private Callback callback;
	private int caretPosition = 0;
	private Local_GUI l;

	public static void go(){
		launch();
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("local.fxml"));
			StackPane root = (StackPane) loader.load();

			Scene scene = new Scene(root,974,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setX(0);
			primaryStage.setY(0);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override public void handle(WindowEvent t) { System.exit(0); }
			});

			txtload = (Text) loader.getNamespace().get("txt_load");
			txtinfo[0] = (Text) loader.getNamespace().get("txt_info_1");
			txtinfo[1] = (Text) loader.getNamespace().get("txt_info_2");
			txtinfo[2] = (Text) loader.getNamespace().get("txt_info_3");
			txtinfo[3] = (Text) loader.getNamespace().get("txt_info_4");
			txtbottom = (Text) loader.getNamespace().get("txt_bottom");

			userInput = (TextField) loader.getNamespace().get("userInput");
			userInput.caretPositionProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					/* 
					 * when button is pressed, the textfield looses focus and caret is moved to 0.
					 * Hence the previousCaretPosition is the actual position.
					 */
					System.out.println("onChange "+oldValue+" --> "+newValue+"    ("+caretPosition+")");
					if(newValue.intValue() == 0 && caretPosition != 0){
						System.out.println("Caret forced");
						userInput.positionCaret(caretPosition);
					}
				}
			});
			userInput.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					if(userInputPlaceholderTentative){
						userInputPlaceholderTentative = false;
						userInput.setText(e.getText());
						caretPosition = 1;
						userInput.positionCaret(caretPosition);
					}
				}
			});

			for(int i=0; i < 6; i++){
				txtsft[i] = (Text) loader.getNamespace().get("txt_softkey_"+(i+1));
				btnsft[i] = (Button) loader.getNamespace().get("btn_softkey_"+(i+1));
				final int j = i;
				btnsft[i].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						onSoftKeyPressed(j); 
					}
				});
			}

			slider = (Slider) loader.getNamespace().get("slider");
			slider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					onSliderValueChange(newValue.doubleValue());
				}
			});

			btntara = (Button) loader.getNamespace().get("btn_tara");
			btntara.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onTaraButtonPressed(); }
			});
			
//			btncancel.setVisible(false);
			//			btncancel = (Button) loader.getNamespace().get("btncancel");
			//			btncancel.setOnAction(new EventHandler<ActionEvent>() { 
			//				@Override public void handle(ActionEvent event) { 
			//				}
			//			});

			final FxAppInputBtnHandler inputHandler = new FxAppInputBtnHandler();
			for(int i=0; i < 10; i++){
				final int btn = i;
				btnnum[i] = (Button) loader.getNamespace().get("btn_"+(i));
				btnnum[i].setOnAction(new EventHandler<ActionEvent>() { 
					@Override public void handle(ActionEvent event) { onNumBtnPressed(inputHandler, btn); }
				});
			}

			btnshift = (Button) loader.getNamespace().get("btn_shift");
			btnshift.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) { onShiftBtnPressed(); }
			});

			btnzero = (Button) loader.getNamespace().get("btn_zero");
			btnzero.setOnAction(new EventHandler<ActionEvent>() { 
				@Override public void handle(ActionEvent event) { onZeroButtonPressed(); }
			});

			dtop = (Slider) loader.getNamespace().get("delta_top");
			dbottom = (Slider) loader.getNamespace().get("delta_bottom");
			pbload = (ProgressBar) loader.getNamespace().get("pb_load");
//			turnOffDeltabar();

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		Local_GUI.getInstance().setApp(this);
	}
	public FxApp() {} 

	public void setSim(Local_GUI l){
		this.l = l;
	}


	//output
	private void onSliderValueChange(Double newValue){ l.onSliderValueChange(newValue); }
	private void onTaraButtonPressed(){ l.onTaraButtonPressed(); }
	private void onZeroButtonPressed(){ l.onZeroButtonPressed(); }
	//	public String getUserInput(){ return userInput.getText(); }
	//	public void setUserInput(String str){ userInput.setText(str); }
	private void onNumBtnPressed(final FxAppInputBtnHandler inputHandler, final int btn) {
		System.out.println("FxApp.110 CaretPosition = "+caretPosition);
		if(userInputInprogress){
			String txt = userInput.getText();
			int lengthBefore = txt.length();
			txt = inputHandler.onButtonPressed(txt, btn, caretPosition, inputType);
			userInput.setText(txt);
			int lengthAfter = txt.length();
			if(lengthAfter > lengthBefore) caretPosition += 1;
		}
		userInput.requestFocus();
		userInput.positionCaret(caretPosition);
	}
	private void onShiftBtnPressed() {
		toggle_input_type();
		userInput.requestFocus();
		userInput.positionCaret(caretPosition);
	}
	private void onSoftKeyPressed(int i){
		
	}

	//input
	public void printLoad(final String load) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtload.setText(load);
			}
		});
	}
//	public void printInfo(final int pos, final String msg) {
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				txtinfo[pos-1].setText(msg);
//				if(pos == 3){
//					txtinfo[3].setVisible(true);
//					userInput.setVisible(false);
//					txtbottom.setVisible(false);
//				}
//			}
//		});
//	}
	public void printBottom(final String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				txtbottom.setText(msg);
				txtinfo[3].setVisible(false);
				userInput.setVisible(false);
				txtbottom.setVisible(true);
			}
		});
	}
	public void softkeysHide() {
		for(Text t : txtsft) { t.setText(""); }
	}
	public void softkeysShow(String[] sftkeys, int firstSoftkey, boolean[] sftkeysChecked) {
		int i = 0;
		while(i < txtsft.length && i+firstSoftkey < sftkeys.length){
			int index = i+firstSoftkey;
			boolean checked = false;
			try{
				checked = sftkeysChecked[index];
			} catch(ArrayIndexOutOfBoundsException e) {
				checked = false;
			}
			txtsft[i].setText(sftkeys[index] + (checked ? "<" : ""));
			i++;
		}
	}

	// UserInput
//	public void getUserInput(String msg, String placeholder, boolean placeholderIsTentative, InputType inputType,
//			boolean inputTypeLocked, boolean allowNegative, Callback callback) { 
//		this.callback = callback;
//		userInputInprogress = true;
//
//		for(Text t : txtinfo) t.setText("");
//		txtinfo[0].setText(msg);
//		txtinfo[3].setVisible(false);
//		userInput.setText(placeholder);
//		userInput.setVisible(true);
//		txtbottom.setVisible(false);
//		caretPosition = placeholder.length();
//		userInput.positionCaret(caretPosition);
//		userInputPlaceholderTentative = placeholderIsTentative;
//		userinputAllowNegative = allowNegative;
//
//		String[] texts = l.getDefaultTexts();
//		String okStr = texts[0];
//		String cancelStr = texts[1];
//		String eraseStr = texts[2];
//		softkeysShow(new String[] {allowNegative ? "-/+" : "", eraseStr, "<--", "-->", okStr, cancelStr}, 0, new boolean[]{});
//
//		switch(inputType){
//		case LOWER: setButtonsLower(); break;
//		case UPPER: setButtonsUpper(); break;
//		case NUMBERS: setButtonsNumbers(); break;
//		}
//		this.userInputTypeLocked = inputTypeLocked;
//
//		Platform.runLater(new Runnable() { @Override public void run() { userInput.requestFocus(); } });
//	}
//	private void softkeyPressedUserInput(int i) {
//		switch(i){
//		case 0:	if(userinputAllowNegative) toggelNegative(); break;  // +/- 
//		case 1: backspace(); break; // Erase
//		case 2: moveCaret(false); break; // <--
//		case 3: moveCaret(true); break; // -->
//		case 4: // OK
//			userInputInprogress = false;
//			for(Text t : txtinfo) t.setText("");
//			softkeysHide();
//			String userinput = userInput.getText();
//			userInput.setText("");
//			callback.onSuccess(userinput);
//			break;
//		case 5: // Cancel
//			userInputInprogress = false;
//			for(Text t : txtinfo) t.setText("");
//			softkeysHide();
//			userInput.setText("");
//			callback.onFailure();
//			break;
//		}
//	}
//	private void moveCaret(boolean forward){
//		if(forward && caretPosition < userInput.getText().length()){
//			caretPosition += 1;
//		} else if(!forward && caretPosition > 0){
//			caretPosition -= 1;
//		}
//		userInputPlaceholderTentative = false;
//		userInput.requestFocus();
//		userInput.positionCaret(caretPosition);
//	}
//	private void backspace(){
//		if(caretPosition > 0){
//			caretPosition -= 1;
//			String pre = userInput.getText().substring(0, caretPosition);
//			String post = userInput.getText().substring(caretPosition+1);
//			userInput.setText(pre+post);
//			userInput.requestFocus();
//			userInput.positionCaret(caretPosition);
//		}
//	}
//	private void toggelNegative() {
//		String txt = userInput.getText();
//		txt = txt.startsWith("-") ? txt.substring(1) : "-"+txt;
//		userInput.setText(txt);
//		userInput.requestFocus();
//		userInput.positionCaret(caretPosition);
//	}
//
//	// Deltabar
//	public void setDeltabar(double target, double tol, double netto) {
//		double dtopMin = target - (2*tol);
//		double dtopMax = target + (2*tol);
//		dtop.setMin(dtopMin);
//		dtop.setMax(dtopMax);
//		dtop.setValue(netto);
//		dtop.setShowTickMarks(true);
//		dtop.setMajorTickUnit(tol);
//		dtop.setVisible(true);
//
//		double dbottomMin = 0;
//		double dbottomMax = 2*target;
//		dbottom.setMin(dbottomMin);
//		dbottom.setMax(dbottomMax);
//		dbottom.setValue(netto);
//		dbottom.setVisible(true);
//	}
//	public void updateDelta(double netto) {
////		netto /= 1000d;
//		double target = dbottom.getMax() / 2d;
//		double progress =  netto / target;
//		pbload.setProgress(progress);
//		dtop.setValue(netto);
//		dbottom.setValue(netto);
//	}
//	public void resetDeltabar() {
//		dtop.setVisible(false);
//		dbottom.setVisible(false);
//	}
//	public void turnOffDeltabar(){
//		dtop.setVisible(false);
//		dbottom.setVisible(false);
//		pbload.setProgress(0);
//	}

	//internal
	private void toggle_input_type(){
		if(userInputTypeLocked) return;
		switch(inputType){
		case LOWER: setButtonsUpper(); break;
		case UPPER: setButtonsNumbers(); break;
		case NUMBERS: setButtonsLower(); break;
		}
	}


	public void setButtonsLower(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(str_lower[i]);
				}
				inputType = InputType.LOWER;
			}
		});
	}
	public void setButtonsUpper(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(str_upper[i]);
				}
				inputType = InputType.UPPER;
			}
		});
	}
	public void setButtonsNumbers(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<btnnum.length; i++){
					btnnum[i].setText(""+i);
				}
				inputType = InputType.NUMBERS;
			}
		});
	}












}
