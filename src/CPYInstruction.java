/** CPYInstruction.java
 *  
 *  Instruction to compare the memory content and the Y register content
 */

import java.lang.Exception;

public class CPYInstruction extends Instruction {
	
	private Integer loadValue = null;
	
	/**
	 * Constructor
	 * @param op
	 */
	public CPYInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific CMX mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
			case 0xC0:
				this.mode = Instruction.IMMEDIATE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
				
			case 0xC4:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0xCC:
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
		int yRegister = processor.getYRegister();
		
		switch (this.getMode()) {
			case Instruction.IMMEDIATE:
				this.setLoadValue(yRegister - this.operand.getValue());
				break;
				
			case Instruction.ZERO_PAGE:
				this.setLoadValue(yRegister -
						bus.read(this.operand.getValue()));
				break;
				
			case Instruction.ABSOLUTE:
				this.setLoadValue(yRegister -
						bus.read(this.operand.getValue()));
				break;
		
			default:
				break;
		}
		
		if (this.getLoadValue() < 0) {
			this.setLoadValue(this.getLoadValue() + 256);
		}
		
		// zero flag
		if (this.getLoadValue() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative and carry flags
		if (this.getLoadValue() > 127) {
			processor.setNegFlgg(true);
			processor.clearCarryFlg();
		} else {
			processor.clearNegativeFlag();
			processor.setCarryFlg();
		}
		
	}
	
	public Integer getLoadValue() {
		return this.loadValue;
	}
	
	public void setLoadValue(Integer value) {
		this.loadValue = value;
	}
}
