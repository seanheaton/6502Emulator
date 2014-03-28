/** CLIInstruction.java
 *  
 *  Instruction to clear the interrupt disable flag
 */

import java.lang.Exception;

public class CLIInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public CLIInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific CLI mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x58:
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
			processor.clearIrqDisableFlg();
			break;
				
			default:
				break;
		}
	}
}