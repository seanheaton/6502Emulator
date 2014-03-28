/** ADCInstruction.java
 *  
 *  Instruction to add the content of a memory location to the  accumulator
 *  together with the carry bit
 */

import java.lang.Exception;

public class ADCInstruction extends Instruction {	
	
	private Integer loadValue = null;
	
	/**
	 * Constructor
	 * @param op
	 */
	public ADCInstruction(Integer code) {
		this.opCode = code;
		this.operand = null;
		
		/**
		 * Use the opcode to find the specific ADC mode to
		 * use in interpreting the operand, as well as the
		 * overall length of the instruction. The instruction
		 * length can be used by the processor in determining
		 * where the next instruction begins in memory.
		 */
		switch (code) {
		case 0x69:
			this.mode = Instruction.IMMEDIATE;
			this.instructionLength = 2;
			this.hasOperand = true;
			break;
			
		case 0x65:
			this.mode = Instruction.ZERO_PAGE;
			this.instructionLength = 2;
			this.hasOperand = true;
			break;
			
		case 0x75:
			this.mode = Instruction.ZERO_PAGE_X;
			this.instructionLength = 2;
			this.hasOperand = true;
			break;
		
		case 0x6D:
			this.mode = Instruction.ABSOLUTE;
			this.instructionLength = 3;
			this.hasOperand = true;
			break;
			
		case 0x7D:
			this.mode = Instruction.ABSOLUTE_X;
			this.instructionLength = 3;
			this.hasOperand = true;
			break;
		
		case 0x79:
			this.mode = Instruction.ABSOLUTE_Y;
			this.instructionLength = 3;
			this.hasOperand = true;
			break;
			
		case 0x61:
			this.mode = Instruction.INDIRECT_X;
			this.instructionLength = 2;
			this.hasOperand = true;
			break;
			
		case 0x71:
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
		int carryBit = processor.getCarryBit();
		int accumulator = processor.getAccumulator();
		int negFlagOld = accumulator & (1 << 7);
		
		switch (this.getMode()) {
		case Instruction.IMMEDIATE:
			this.setLoadValue(this.operand.getValue() + accumulator +
					carryBit);
			break;
			
		case Instruction.ZERO_PAGE:
			this.setLoadValue(bus.read(this.operand.getValue()) +
					accumulator + carryBit);
			break;
			
		case Instruction.ZERO_PAGE_X:
			this.setLoadValue(bus.read(this.operand.getValue() +
					processor.getXRegister()) + accumulator + carryBit);
			break;
			
		case Instruction.ABSOLUTE:
			this.setLoadValue(bus.read(this.operand.getValue()) +
					accumulator + carryBit);
			break;
			
		case Instruction.ABSOLUTE_X:
			this.setLoadValue(bus.read(this.operand.getValue() +
					processor.getXRegister()) + accumulator + carryBit);
			break;
			
		case Instruction.ABSOLUTE_Y:
			this.setLoadValue(bus.read(this.operand.getValue() +
					processor.getYRegister()) + accumulator + carryBit);
			break;
			
		case Instruction.INDIRECT_X:
			this.setLoadValue(bus.read(this.operand.getValue()) +
					processor.getXRegister() + accumulator + carryBit);
			break;
			
		case Instruction.INDIRECT_Y:
			this.setLoadValue(bus.read(this.operand.getValue()) +
					processor.getYRegister() + accumulator + carryBit);
			break;
			
		default:
			break;
		}
		
		processor.getCpuState().a = this.getLoadValue() % 256;
		
		// carry flag
		if (this.getLoadValue() > 255) {
			processor.setCarryFlg();
		} else {
			processor.clearCarryFlg();
		}
		
		// zero flag
		if (processor.getAccumulator() == 0) {
			processor.setZeroFlg();
		} else {
			processor.clearZeroFlg();
		}
		
		// negative flag
		if ((processor.getAccumulator() & (1 << 7)) != 0) {
			processor.setNegFlgg(true);
		} else {
			processor.clearNegativeFlag();
		}
		
		// overflow flag
		if ((processor.getAccumulator() & (1 << 7)) != negFlagOld){
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
