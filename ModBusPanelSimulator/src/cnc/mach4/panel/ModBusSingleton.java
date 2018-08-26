package cnc.mach4.panel;

import java.util.List;

import javafx.scene.control.ListView;
import de.re.easymodbus.server.ModbusServer;

public class ModBusSingleton extends ModbusServer {
	private static ModBusSingleton instance;

	public static ListView<Integer>  holdingRegisterListView = new ListView<Integer>() ; 
	
	
	  // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes 
	  // Objekt erzeugt und dieses zurückliefert.
	  public static ModBusSingleton getInstance () {
	    if (ModBusSingleton.instance == null) {
	    	ModBusSingleton.instance = new ModBusSingleton();
	    	initData() ;
	    }
	    


	    return ModBusSingleton.instance;
	  }
	  
	  public static void initData(){
		  
		  instance.inputRegisters[InputRegisters.PANEL_FEED_RATIO] = 100;
		  instance.inputRegisters[InputRegisters.PANEL_AXIS] = 1 ;
		  instance.inputRegisters[InputRegisters.PANEL_MPG_INCREMENT] = 1;

		  
		  
	  }
	
}
