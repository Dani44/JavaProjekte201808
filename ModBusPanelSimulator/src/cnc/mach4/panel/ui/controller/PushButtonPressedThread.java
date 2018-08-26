package cnc.mach4.panel.ui.controller;


import cnc.mach4.panel.ModBusSingleton;

public class PushButtonPressedThread extends Thread{

	
	
	
	private ModBusSingleton modbushandler = ModBusSingleton.getInstance() ;
	 private int coilNumber ;
	 
	 public static void pushCoil( int coil){
		 PushButtonPressedThread thread = new PushButtonPressedThread(coil) ;
		 thread.start();
	 }

	 
	 public PushButtonPressedThread(int coilNumber) {
		super();
		this.coilNumber = coilNumber;
	 }


	 public int getCoilNumber() {
	  	  return coilNumber;
	 }




	public void setCoilNumber(int coilNumber) {
		this.coilNumber = coilNumber;
	}




	@Override
	 public void run() {
		for( int i = 0 ; i < 2 ; i++){
			modbushandler.coils[this.coilNumber] = !modbushandler.coils[this.coilNumber] ;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		super.run();
	 }
	
	

}
