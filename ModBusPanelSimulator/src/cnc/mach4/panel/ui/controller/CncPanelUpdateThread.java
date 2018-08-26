package cnc.mach4.panel.ui.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import cnc.mach4.panel.ModBusSingleton;

public class CncPanelUpdateThread extends Thread {
	
	CncPanelController  cncPanelController ;	
	ModBusSingleton mb = ModBusSingleton.getInstance() ;
			
	
	public CncPanelUpdateThread(CncPanelController cncPanelController) {
		super();
		this.cncPanelController = cncPanelController;
	}


	@Override
	public void run() {
		
		
        while (!this.isInterrupted()) {
            
            // UI updaten
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	cncPanelController.updateCoilView();
                	cncPanelController.updateinRegView();
                }
            });
 
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(CncPanelUpdateThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

		
		
		
	}
}
