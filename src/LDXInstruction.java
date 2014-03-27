/** LDXInstruction.java
 *  
 *  Instruction to load an operand into the X register
 */

import java.lang.Exception;

public class LDXInstruction extends Instruction {
	
	private Integer loadValue = null;
	
	/**
	 * Constructor
	 * @param op
	 */
	public LDXInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific LDX mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0xA2:
				this.mode = Instruction.IMMEDIATE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0xA6:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0xB6:
				this.mode = Instruction.ZERO_PAGE_Y;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0xAE:
				this.mode = Instruction.ABSOLUTE;
				this.instructionLength = 3;
				this.hasOperand = true;
				break;
				
			case 0xBE:
				this.mode = Instruction.ABSOLUTE_Y;
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
		
		switch (this.getMode()) {
			case Instruction.IMMEDIATE:
				this.setLoadValue(this.operand.getValue());
				break;
				
			case Instruction.ZERO_PAGE:
				this.setLoadValue(bus.read(this.operand.getValue()));
				break;
				
			case Instruction.ZERO_PAGE_Y:
				this.setLoadValue(bus.read(this.operand.getValue() +
						processor.getYRegister()));
				break;
				
			case Instruction.ABSOLUTE:
				this.setLoadValue(bus.read(this.operand.getValue()));
				break;
				
			case Instruction.ABSOLUTE_Y:
				this.setLoadValue(bus.read(this.operand.getValue() +
						processor.getYRegister()));
				break;
				
			default:
				break;
		}
		
		/**
		 * Write this.getLoadValue() to the X register
		 */
		processor.setXRegister(this.getLoadValue());
		
		// zero flag
		if (processor.getXRegister() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((processor.getXRegister() & (1 << 7)) != 0) {
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
