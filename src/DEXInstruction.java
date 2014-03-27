/** DEXInstruction.java
 *  
 *  Instruction to subtract 1 from the value held in X register
 */

import java.lang.Exception;

public class DEXInstruction extends Instruction {
	/**
	 * Constructor
	 * @param op
	 */
	public DEXInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific DEX mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {				
			case 0xCA:
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
				processor.setXRegister(processor.getXRegister() - 1);
				if (processor.getXRegister() < 0) {
					processor.setXRegister(processor.getXRegister() + 256);
				}
				break;
			
			default:
				break;
		}
		
		// zero flag
		if (processor.getXRegister() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((processor.getXRegister() & (1 << 7)) != 0) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
	}
}
