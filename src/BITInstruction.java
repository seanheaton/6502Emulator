/** BITInstruction.java
 *  
 *  Instruction is similar to AND but it doesn't load the result to accumulator
 */

import java.lang.Exception;

public class BITInstruction extends Instruction {
	
	private Integer loadValue = null;
	
	/**
	 * Constructor
	 * @param op
	 */
	public BITInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific BIT mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {				
			case 0x24:
				this.mode = Instruction.ZERO_PAGE;
				this.instructionLength = 2;
				this.hasOperand = true;
				break;
			
			case 0x2C:
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
		int accumulator = processor.getAccumulator();
		
		switch (this.getMode()) {
		case Instruction.ZERO_PAGE:
			this.setLoadValue(bus.read(this.operand.getValue()) &
					accumulator);
			break;
			
		case Instruction.ABSOLUTE:
			this.setLoadValue(bus.read(this.operand.getValue()) &
					accumulator);
			break;
				
			default:
				break;
		}
		
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
		
		// overflow flag
		if ((this.operand.getValue() & (1 << 6)) != 0) {
			processor.setOverflowFlg();
		} else {
			processor.clearOverflowFlg();
		}
	}
	
	public Integer getLoadValue() {
		return this.loadValue;
	}
	
	public void setLoadValue(Integer value) {
		this.loadValue = value;
	}
}
