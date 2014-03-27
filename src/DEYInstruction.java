/** DEYInstruction.java
 *  
 *  Instruction to subtract 1 from the value held in Y register
 */

import java.lang.Exception;

public class DEYInstruction extends Instruction {
	/**
	 * Constructor
	 * @param op
	 */
	public DEYInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific DEY mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {				
			case 0x88:
				this.mode = Instruction.IMPLIED;
				this.instructionLength = 1;
				this.hasOperand = false;
				break;
							
			default:
				this.mode = 0;
				this.instructionLength = 1;
				this.hasOperand = false;
				break;
		}
	}
	
	public void execute() throws Exception {
		Processor processor = Processor.getInstance();
		
		switch (this.getMode()) {				
			case Instruction.IMPLIED:
				processor.setYRegister(processor.getYRegister() - 1);
				if (processor.getYRegister() < 0) {
					processor.setYRegister(processor.getYRegister() + 256);
				}
				break;
			
			default:
				break;
		}
		
		// zero flag
		if (processor.getYRegister() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((processor.getYRegister() & (1 << 7)) != 0) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
	}
}