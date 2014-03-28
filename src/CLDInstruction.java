/** CLDInstruction.java
 *  
 *  Instruction to clear the decimal flag
 */

import java.lang.Exception;

public class CLDInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public CLDInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific CLD mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0xD8:
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
			processor.clearDecimalModeFlag();
			break;
				
			default:
				break;
		}
	}
}