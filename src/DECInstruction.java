/** DECInstruction.java
 *  
 *  Instruction to subtract 1 from the value held at a specified memory location
 */

import java.lang.Exception;

public class DECInstruction extends Instruction {
	
	private Integer loadValue = null;
	
	/**
	 * Constructor
	 * @param op
	 */
	public DECInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific DEC mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {				
			case 0xC6:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0xD6:
				this.mode = Instruction.ZERO_PAGE_X;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0xCE:
				this.mode = Instruction.ABSOLUTE;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			case 0xDE:
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
		if (this.operand == null) {
			throw new Exception("Missing Operand");
		}
		
		/**
		 * Get reference to Processor.getBus()
		 * for reading/writing to memory
		 */
		Bus bus = Processor.getInstance().getBus();
		Processor processor = Processor.getInstance();
		int address = 0;
		
		switch (this.getMode()) {				
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
		
		// read from bus
		this.setLoadValue(bus.read(address) - 1);
		
		if (this.getLoadValue() < 0) {
			this.setLoadValue(this.getLoadValue() + 256);
		}
		
		/**
		 * Write this.getLoadValue() to the address
		 */
		bus.write(address, this.getLoadValue());
		
		// zero flag
		if (this.getLoadValue() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((this.getLoadValue() & (1 << 7)) != 0) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
	}
	
	public Integer getLoadValue() {
		return this.loadValue;
	}
	
	public void setLoadValue(Integer value) {
		this.loadValue = value;
	}
}
