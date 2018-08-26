package cnc.mach4.panel;

public class Coils {
	
	public static final int COIL_COUNT  = 80 ;	
	
	public static final int ARRAY_SHIFT = 1 ;
	
	public static final int OPERATION_RESET = 	ARRAY_SHIFT   + 0  ;
	public static final int OPERATION_EMERGECY = ARRAY_SHIFT  + 1  ;
	public static final int SPINDLE_CW = 	ARRAY_SHIFT       + 2  ;
	public static final int SPINDLE_STOP = 	ARRAY_SHIFT       + 3  ;
	public static final int SPINDLE_CCW = 	ARRAY_SHIFT       + 4  ;
	
	public static final int OP_REF_ALL         = 	ARRAY_SHIFT  + 5    ;	
	public static final int OP_REF_SINGLE_AXIS = 	ARRAY_SHIFT  + 6    ;	
	public static final int OP_HOME_ALL 	   = 	ARRAY_SHIFT  + 7    ;	
	public static final int OP_HOME_SINGLE_AXIS = 	ARRAY_SHIFT  + 8    ;
	
	public static final int OP_CYCLE_START		 = 	ARRAY_SHIFT  + 9    ;	
	public static final int OP_CYCLE_REWIND		 = 	ARRAY_SHIFT  + 10    ;
	public static final int OP_CYCLE_PAUSE		 = 	ARRAY_SHIFT  + 11    ;
	public static final int OP_CYCLE_STOP		 = 	ARRAY_SHIFT  + 12    ;

	public static final int OP_PROBE		     = 	ARRAY_SHIFT  + 13    ;
	public static final int OP_CTRL_PROBE		 = 	ARRAY_SHIFT  + 14    ;

	public static final int OP_MACRO1		     = 	ARRAY_SHIFT  + 15    ;
	public static final int OP_CTRL_MACRO1		 = 	ARRAY_SHIFT  + 16    ;	
	
	public static final int OP_MACRO2		     = 	ARRAY_SHIFT  + 17    ;
	public static final int OP_CTRL_MACRO2		 = 	ARRAY_SHIFT  + 18    ;	

	public static final int OP_MACRO3		     = 	ARRAY_SHIFT  + 19    ;
	public static final int OP_CTRL_MACRO3		 = 	ARRAY_SHIFT  + 20    ;	


	public static final int MACH_MACHINE_DISABLED =  ARRAY_SHIFT  + 64  ;	
	public static final int MACH_EMERGENCY  =  ARRAY_SHIFT        + 65  ;	
	public static final int MACH_IS_MOVING  = ARRAY_SHIFT  		  + 66  ;	
	public static final int MACH_IS_STOPPED =  ARRAY_SHIFT        + 67  ;	
	
	public static final int MACH_SPINDLE_CW  = ARRAY_SHIFT        + 70  ;
	public static final int MACH_SPINDLE_CCW = ARRAY_SHIFT        + 71  ;	
	
	public static final int MACH_ENABLE_X   = ARRAY_SHIFT         + 72  ;
	public static final int MACH_ENABLE_Y   = ARRAY_SHIFT         + 73  ;	
	public static final int MACH_ENABLE_Z   = ARRAY_SHIFT         + 74  ;
	public static final int MACH_ENABLE_A   = ARRAY_SHIFT         + 75  ;	

}
