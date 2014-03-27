/**
 * 
 */

/**
 *
 */
public class ProcessorState {

	// registers
	public Integer a;
	public Integer x;
	public Integer y;
	// stack pointer
	public Integer sp;
	
	// program counter
	public Integer pc;
	
	// instruction register
	public Integer ir;
	public Integer irSize;
	public Integer[] irArgs;
	public boolean irInvalidOp;
	
	// previous program counter
	public Integer prevPc;
	
	// status register flags
	public boolean carryFlg;
	public boolean negFlg;
	public boolean zeroFlg;
	public boolean irqDisableFlg;
	public boolean decModeFlg;
	public boolean brkFlg;
	public boolean overflowFlg;
	
	public ProcessorState(){
		//TODO: complete
	}

	public ProcessorState(ProcessorState ps){
		//TODO: complete		
	}	
	
	public String toString(){
		//TODO: complete		
		return null;		
	}
	
	public Integer getStatusRegister(){
		//TODO: complete		
		return null;			
	}
}
