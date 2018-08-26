package cnc.mach4.panel.ui.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import cnc.mach4.panel.ModBusSingleton;
import javafx.application.Platform;
import javafx.scene.control.ListView;

public class InputRegisterUpdateThread extends Thread {

	 private ModBusSingleton modbushandler = ModBusSingleton.getInstance() ; 
	 private ListView<Integer> listInputRegister = null;
	 
	    public InputRegisterUpdateThread(ListView<Integer> list) {
	        this.listInputRegister = list;
	    	setDaemon(true);
	        setName("Thread InputRegisterUpdateThread" );

	    }
	 
		@Override
		public void run() {
	 
	        while (!this.isInterrupted()) {
	             
	            // UI updaten
	            Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                	if( listInputRegister != null ){
	                		for( int i=0 ; i < 128 ; i++ ){
	                			 listInputRegister.getItems().set(i, modbushandler.inputRegisters[i]) ;
	                		}
	                	}
	                }
	            });
	 
	            try {
	                sleep(100);
	            } catch (InterruptedException ex) {
	                Logger.getLogger(InputRegisterUpdateThread.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	 
	    }

}
