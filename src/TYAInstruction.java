/** TYAInstruction.java
 *  
 *  Instruction to copy the contents of the Y register into the accumulator
 */

import java.lang.Exception;

public class TYAInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public TYAInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific TYA mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x98:
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
			processor.setAccumulator(processor.getYRegister());
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
		if (processor.getYRegister() > 127) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
	}
}