
package cnc.mach4.panel;

import java.io.IOException;

import de.re.easymodbus.server.ICoilsChangedDelegator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PanelSimulator extends Application implements de.re.easymodbus.server.ICoilsChangedDelegator, de.re.easymodbus.server.IHoldingRegistersChangedDelegator, de.re.easymodbus.server.INumberOfConnectedClientsChangedDelegator, de.re.easymodbus.server.ILogDataChangedDelegator {
	
	volatile ModBusSingleton modbusServer  ;
	volatile ICoilsChangedDelegator coilListener ;	
	volatile LiveTickerThread liveTicker = new LiveTickerThread() ;
	Thread tLifeTicker ;
	
	@Override 
	@SuppressWarnings("deprecation")
	public void stop() throws Exception {
		tLifeTicker.interrupt();
		modbusServer.StopListening();
		modbusServer.stop();
		modbusServer.destroy();
	};
    
	
	@Override
	public void init() throws Exception {
		super.init();
		modbusServer = ModBusSingleton.getInstance() ;
		modbusServer.setNotifyCoilsChanged(this);
		modbusServer.setNotifyHoldingRegistersChanged(this);
		modbusServer.setNotifyNumberOfConnectedClientsChanged(this);
		modbusServer.setNotifyLogDataChanged(this);
		modbusServer.setPort(502);
		
		try {
			modbusServer.Listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		modbusServer.setNotifyCoilsChanged(coilListener);
        
		liveTicker.setModbusServer(modbusServer);
        
		tLifeTicker = new Thread(liveTicker,"ThreadLifeTicker") ;
		tLifeTicker.start(); 
		
		
	};
	
	
    @Override
    public void start(Stage stage) throws Exception {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("/ui/MainApplication.fxml"));
        
        stage.setTitle("SA CNC Panel V2 TCP/IP");
        Scene scene = new Scene(root, 600, 600) ;
        

        String css = this.getClass().getResource("/ui/cncpanel.css").toExternalForm(); 
        scene.getStylesheets().add(css);
        
        
        stage.setScene(scene );
        stage.show();
        

    }
    
    public static void main(String[] args) {
    	Application.launch(PanelSimulator.class, args);
    	
    	
    }


	@Override
	public void logDataChangedEvent() {
		
	}


	@Override
	public void NumberOfConnectedClientsChanged() {
	
	}


	@Override
	public void holdingRegistersChangedEvent() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void coilsChangedEvent() {
		System.out.println("Coil geändert");
		
	}

    
    
    
}
