package cnc.mach4.panel.ui.controller;

import java.net.URL;
import java.util.Formatter;
import java.util.ResourceBundle;

import cnc.mach4.panel.Coils;
import cnc.mach4.panel.InputRegisters;
import cnc.mach4.panel.ModBusSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class CncPanelController implements Initializable{

	 
	private final ObservableList<String> coilList  = FXCollections.observableArrayList( );
	private final ObservableList<String>  inRegList = FXCollections.observableArrayList( );

	private ModBusSingleton mb ; 
	
	private boolean bCtrlModus = false ;
	
	
	@FXML private RadioButton rbAxis0 ;
	@FXML private RadioButton rbAxis1 ;	
	@FXML private RadioButton rbAxis2 ;
	@FXML private RadioButton rbAxis3 ;
	@FXML private RadioButton rbAxis4 ;	
	@FXML private RadioButton rbAxis5 ;	

	
	@FXML private RadioButton rbIncrement0 ;
	@FXML private RadioButton rbIncrement1 ;	
	@FXML private RadioButton rbIncrement2 ;
	@FXML private RadioButton rbIncrement3 ;
	@FXML private RadioButton rbIncrement4 ;	
	@FXML private RadioButton rbIncrement5 ;	
	
	
	
	@FXML	private Slider sldFeed      ;
	@FXML	private Button btnMachReset ;
	@FXML	private Button btnMachEmergency;
	
	
	
	@FXML	private Button btnSpindleCW ;
	@FXML	private Button btnSpindleStop ;
	@FXML	private Button btnSpindleCCW ;
	@FXML	private Button btnSpindleSpeed ;


	@FXML	private Button btnCtrl ;
	@FXML	private Button btnOpHome ;
	@FXML	private Button btnOpRef ;

	@FXML	private Button btnOpStart ;
	@FXML	private Button btnOpPause ;

	@FXML	private Button btnOpProbe ;
	@FXML	private Button btnOpMacro1 ;
	@FXML	private Button btnOpMacro2 ;
	@FXML	private Button btnOpMacro3 ;
	
	
	
	
	// Switches
	// ------------------------------------------------------------------------------------------------------------------
	@FXML public void handleBtnMachEmergency(ActionEvent event) { mb.coils[Coils.OPERATION_EMERGECY] = !mb.coils[ Coils.OPERATION_EMERGECY ] ; }
	
	// CtrlButton
	// ------------------------------------------------------------------------------------------------------------------
	@FXML public void handleBtnCtrl(ActionEvent event) { 
		bCtrlModus = !bCtrlModus ; 
		if( bCtrlModus ) {
			btnCtrl.setStyle("-fx-background-color: blue;");
			btnOpRef.setStyle("-fx-background-color: blue;");	
			btnOpHome.setStyle("-fx-background-color: blue;");	
			btnOpStart.setStyle("-fx-background-color: blue;");	
			btnOpStart.setText("Rewind");

			btnOpPause.setStyle("-fx-background-color: blue;");	
			btnOpPause.setText("Stop");
			
			btnOpProbe.setStyle("-fx-background-color: blue;");	
			btnOpMacro1.setStyle("-fx-background-color: blue;");	
			btnOpMacro2.setStyle("-fx-background-color: blue;");	
			btnOpMacro3.setStyle("-fx-background-color: blue;");	
			
			
		} else {
			btnCtrl.setStyle("-fx-background-color: lightgreen;");	
			btnOpRef.setStyle("-fx-background-color: green;");	
			btnOpHome.setStyle("-fx-background-color: green;");	
			btnOpStart.setStyle("-fx-background-color: Green;");	
			btnOpStart.setText("Start");

			btnOpPause.setStyle("-fx-background-color: Red;");	
			btnOpPause.setText("Pause");

			btnOpProbe.setStyle("-fx-background-color: orange;");	
			btnOpMacro1.setStyle("-fx-background-color: orange;");	
			btnOpMacro2.setStyle("-fx-background-color: orange;");	
			btnOpMacro3.setStyle("-fx-background-color: orange;");	

			
			
		}
		
	}	

	
	
	// PushButtons
	// ------------------------------------------------------------------------------------------------------------------
	@FXML public void handleBtnMachReset(ActionEvent event) { PushButtonPressedThread.pushCoil(		Coils.OPERATION_RESET	); }	

	
	@FXML public void handleBtnSpindleCW(ActionEvent event) { PushButtonPressedThread.pushCoil(		Coils.SPINDLE_CW 	);}
	@FXML public void handleBtnSpindleStop(ActionEvent event) { PushButtonPressedThread.pushCoil(	Coils.SPINDLE_STOP	);}
	@FXML public void handleBtnSpindleCCW(ActionEvent event) { PushButtonPressedThread.pushCoil(	Coils.SPINDLE_CCW	);}
	@FXML public void handleBtnSpindleSpeed(ActionEvent event) { ; }	

	@FXML public void handleBtnOpRef(ActionEvent event) { 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_REF_ALL	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_REF_SINGLE_AXIS	);
			handleBtnCtrl(event) ;
		}
	}
	
	@FXML public void handleBtnOpHome(ActionEvent event) { 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_HOME_ALL	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_HOME_SINGLE_AXIS	);
			handleBtnCtrl(event) ;
		}
	}

	
	@FXML public void handleBtnOpStart(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_CYCLE_START	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CYCLE_REWIND);
			handleBtnCtrl(event) ;
		}
	}
	
	
	@FXML public void handleBtnOpPause(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_CYCLE_PAUSE	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CYCLE_STOP);
			handleBtnCtrl(event) ;
		}
	}
	
	@FXML public void handleBtnProbe(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_PROBE	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CTRL_PROBE);
			handleBtnCtrl(event) ;
		}
	}
	
	@FXML public void handleBtnMacro1(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_MACRO1	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CTRL_MACRO1);
			handleBtnCtrl(event) ;
		}
	}
	
	@FXML public void handleBtnMacro2(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_MACRO2	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CTRL_MACRO2);
			handleBtnCtrl(event) ;
		}
	}
	
	@FXML public void handleBtnMacro3(ActionEvent event) 
	{ 
		if( !bCtrlModus ) {
			PushButtonPressedThread.pushCoil( Coils.OP_MACRO3	);			
		} else {
			PushButtonPressedThread.pushCoil( Coils.OP_CTRL_MACRO3);
			handleBtnCtrl(event) ;
		}
	}

	
	@FXML private ListView<String> listViewCoils ;
	@FXML private ListView<Integer> listViewInRegs;

	
	public ObservableList<String> getInRegList() {
		return inRegList ;
	}
    
	public ObservableList<String> getCoilList() {
		return coilList ;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		mb = ModBusSingleton.getInstance() ;
	
		sldFeed.setMaxHeight(50);
		sldFeed.setMaxWidth(50);
		sldFeed.setRotate(-90);
		sldFeed.setMin(0);
		sldFeed.setMax(250);
		sldFeed.setValue( mb.inputRegisters[InputRegisters.PANEL_FEED_RATIO] );
		
		rbAxis1.setSelected(true);
		rbIncrement5.setSelected(true);
		
		coilList.clear(); 
		for(int i=0 ; i < Coils.COIL_COUNT ; i++){ 
			coilList.add( ""  ); 
	    }

		inRegList.clear(); 
		for( int i=0 ; i < InputRegisters.REGISTER_COUNT ; i++ ){
			inRegList.add(""); 
	    }
		
		CncPanelUpdateThread cncPanelUpdateThread = new CncPanelUpdateThread( this ) ;
		cncPanelUpdateThread.start();
		
		
	}
	
	
	public void updateCoilView(){
		
		
		for( int i=0 ; i < Coils.COIL_COUNT ; i++ ){
			coilList.set( i, String.format("%03d: %b", i , mb.coils[ i + Coils.ARRAY_SHIFT ] ).toUpperCase() ); 
		}
		
		if( mb.coils[ Coils.MACH_EMERGENCY ] ){
			btnMachEmergency.setStyle("-fx-background-color: green;");
		} else {
			btnMachEmergency.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);");
		}

		mb.inputRegisters[InputRegisters.PANEL_FEED_RATIO] = (int) sldFeed.getValue();
		
		if(rbAxis0.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 0 ;}
		if(rbAxis1.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 1 ;}
		if(rbAxis2.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 2 ;}
		if(rbAxis3.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 3 ;}
		if(rbAxis4.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 4 ;}
		if(rbAxis5.isSelected()){mb.inputRegisters[InputRegisters.PANEL_AXIS ] = 5 ;}
		
		if(rbIncrement0.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 0 ;}
		if(rbIncrement1.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 1 ;}
		if(rbIncrement2.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 2 ;}
		if(rbIncrement3.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 3 ;}
		if(rbIncrement4.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 4 ;}
		if(rbIncrement5.isSelected()){mb.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT ] = 5 ;}		
		
		

	}
	
	public void updateinRegView(){
		for( int i=0 ; i < InputRegisters.REGISTER_COUNT ; i++ ){
			inRegList.set( i, String.format("%04d: %6d", i , mb.inputRegisters[i + InputRegisters.ARRAY_SHIFT ] )); 			 

		}

	}
	

}
