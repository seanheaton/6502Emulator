/** SEIInstruction.java
 *  
 *  Instruction to set the interrupt disable flag
 */

import java.lang.Exception;

public class SEIInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public SEIInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific SEI mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x78:
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
			processor.setIrqDisableFlg();
			break;
				
			default:
				break;
		}
	}
}