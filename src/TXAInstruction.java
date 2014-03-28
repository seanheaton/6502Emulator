/** TXAInstruction.java
 *  
 *  Instruction to copy the contents of the X register into the accumulator
 */

import java.lang.Exception;

public class TXAInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public TXAInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific TXA mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x8A:
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
			processor.setAccumulator(processor.getXRegister());
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
		if (processor.getXRegister() > 127) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
	}
}