/** STXInstruction.java
 *  
 *  Instruction to store the content of X register to the memory
 */

import java.lang.Exception;

public class STXInstruction extends Instruction {	
	/**
	 * Constructor
	 * @param op
	 */
	public STXInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific STX mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0x86:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0x96:
				this.mode = Instruction.ZERO_PAGE_Y;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0x8E:
				this.mode = Instruction.ABSOLUTE;
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
		if (this.operand == null) {
			throw new Exception("Missing Operand");
		}
		
		/**
		 * Get reference to Processor.getBus()
		 * for reading/writing to memory
		 */
		Bus bus = Processor.getInstance().getBus();
		Processor processor = Processor.getInstance();
		int xRegister = processor.getXRegister();
		
		switch (this.getMode()) {
			case Instruction.ZERO_PAGE:
				bus.write(this.operand.getValue(), xRegister);
				break;
				
			case Instruction.ZERO_PAGE_Y:
				bus.write(this.operand.getValue() + processor.getYRegister(),
						xRegister);
				break;
				
			case Instruction.ABSOLUTE:
				bus.write(this.operand.getValue(), xRegister);
				break;
				
			default:
				break;
		}
	}
}
