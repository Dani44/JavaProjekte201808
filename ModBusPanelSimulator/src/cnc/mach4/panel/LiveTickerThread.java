package cnc.mach4.panel;


public class LiveTickerThread implements Runnable {

	private ModBusSingleton modbusServer ;
	
	
	
	public ModBusSingleton getModbusServer() {
		return modbusServer;
	}


	public void setModbusServer(ModBusSingleton modbusServer) {
		this.modbusServer = modbusServer;
	}

	private volatile int counter = 0 ;
	
	public int getCounter() {
		return counter;
	}


	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void run() {		
		try {
			while( true ){
				counter ++ ;
				modbusServer.inputRegisters[ InputRegisters.PANEL_LIVE_TICKER ] = counter ; 
				Thread.sleep(250);
			}
		} catch (InterruptedException e) {

		}
		
	}

}
