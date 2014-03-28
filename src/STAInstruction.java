/** STAInstruction.java
 *  
 *  Instruction to store the content of accumulator to the memory
 */

import java.lang.Exception;

public class STAInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public STAInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific STA mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x85:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0x95:
				this.mode = Instruction.ZERO_PAGE_X;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0x8D:
				this.mode = Instruction.ABSOLUTE;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			case 0x9D:
				this.mode = Instruction.ABSOLUTE_X;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
			
			case 0x99:
				this.mode = Instruction.ABSOLUTE_Y;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			case 0x81:
				this.mode = Instruction.INDIRECT_X;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0x91:
				this.mode = Instruction.INDIRECT_Y;
				this.instructionLength = 2;
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
		if (this.operand == null) {
			throw new Exception("Missing Operand");
		}
		
		/**
		 * Get reference to Processor.getBus()
		 * for reading/writing to memory
		 */
		Bus bus = Processor.getInstance().getBus();
		Processor processor = Processor.getInstance();
		int accumulator = processor.getAccumulator();
		
		switch (this.getMode()) {
			case Instruction.ZERO_PAGE:
				bus.write(this.operand.getValue(), accumulator);
				break;
				
			case Instruction.ZERO_PAGE_X:
				bus.write(this.operand.getValue() + processor.getXRegister(),
						accumulator);
				break;
				
			case Instruction.ABSOLUTE:
				bus.write(this.operand.getValue(), accumulator);
				break;
				
			case Instruction.ABSOLUTE_X:
				bus.write(this.operand.getValue() + processor.getXRegister(),
						accumulator);
				break;
				
			case Instruction.ABSOLUTE_Y:
				bus.write(this.operand.getValue() + processor.getYRegister(),
						accumulator);
				break;
				
			case Instruction.INDIRECT_X:
				bus.write(this.operand.getValue(), processor.getXRegister() +
						accumulator);
				break;
				
			case Instruction.INDIRECT_Y:
				bus.write(this.operand.getValue(), processor.getYRegister() +
						accumulator);
				break;
				
			default:
				break;
		}
	}
}
