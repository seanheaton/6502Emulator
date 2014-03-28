/** ROLInstruction.java
 *  
 *  Instruction to rotate the bits left
 */

import java.lang.Exception;

public class ROLInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public ROLInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific ROL mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x2A:
				this.mode = Instruction.ACCUMULATOR;
				this.instructionLength = 1;
				this.hasOperand = false;
				break;
				
			case 0x26:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0x36:
				this.mode = Instruction.ZERO_PAGE_X;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0x2E:
				this.mode = Instruction.ABSOLUTE;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			case 0x3E:
				this.mode = Instruction.ABSOLUTE_X;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			default:
				this.mode = 0;
				this.instructionLength = 1;
				this.hasOperand = false;
				break;
		}
	}
	
	public void execute() throws Exception {
		if (this.operand == null && this.hasOperand) {
			throw new Exception("Missing Operand");
		}
		
		/**
		 * Get reference to Processor.getBus()
		 * for reading/writing to memory
		 */
		Bus bus = Processor.getInstance().getBus();
		Processor processor = Processor.getInstance();
		int address = 0;
		int result = 0;
		
		switch (this.getMode()) {
		case Instruction.ACCUMULATOR:
			break;
			
		case Instruction.ZERO_PAGE:
			address = this.operand.getValue();
			break;
			
		case Instruction.ZERO_PAGE_X:
			address = this.operand.getValue() + processor.getXRegister();
			break;
			
		case Instruction.ABSOLUTE:
			address = this.operand.getValue();
			break;
			
		case Instruction.ABSOLUTE_X:
			address = this.operand.getValue() + processor.getXRegister();
			break;
			
			default:
				break;
		}

		if (this.getMode() == Instruction.ACCUMULATOR) {
			result = processor.getAccumulator() << 1;
		} else {
			result = bus.read(address) << 1;
		}
		
		if (processor.getCarryFlg()) {
			result++;
		}
		
		// carry flag
		if ((result & (1 << 8)) != 0) {
			processor.setCarryFlg();
		} else {
			processor.clearCarryFlg();
		}
				
		result %= 256;
		
		// zero flag
		if (result == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((result & (1 << 7)) != 0) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
		
		/**
		 * Write the result to the memory
		 */
		if (this.getMode() == Instruction.ACCUMULATOR) {
			processor.setAccumulator(result);
		} else {
			bus.write(address, result);
		}
	}
}
