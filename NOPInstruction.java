/** NOPInstruction.java
 *  
 *  Empty instruction, doing nothing
 */

import java.lang.Exception;

public class NOPInstruction extends Instruction {
	
	/**
	 * Constructor
	 * @param op
	 */
	public NOPInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific NOP mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0xEA:
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
		// Doing nothing, no affect on registers and processor flags
	}
}
