/** Instruction.java
 *  
 *  This is an abstract class with basic functionality to be implemented
 *  in particular 6502 instruction set objects
 */

public abstract class Instruction {

	// Standard addressing modes
	public static final int INVALID = -1;	
	public static final int IMMEDIATE = 1;
	public static final int ZERO_PAGE = 2;
	public static final int ZERO_PAGE_X = 3;
	public static final int ZERO_PAGE_Y = 4;
	public static final int ABSOLUTE = 5;
	public static final int ABSOLUTE_X = 6;
	public static final int ABSOLUTE_Y = 7;
	public static final int INDIRECT_X = 8;
	public static final int INDIRECT_Y = 9;
	public static final int ACCUMULATOR = 10;
	public static final int IMPLIED = 11;
	public static final int RELATIVE = 12;	
	public static final int INDIRECT = 13;	
	
	// Standard operations enum	
	public static enum operation {
		ADC,AND,ASL,BCC,BCS,BEQ,BIT,
		BMI,BNE,BPL,BRK,BVC,BVS,CLC,
		CLD,CLI,CLV,CMP,CPX,CPY,DEC,
		DEX,DEY,EOR,INC,INX,INY,JMP,
		JSR,LDA,LDX,LDY,LSR,NOP,ORA,
		PHA,PHP,PLA,PLP,ROL,ROR,RTI,
		RTS,SBC,SEC,SED,SEI,STA,STX,
		STY,TAX,TAY,TSX,TXA,TXS,TYA
	}	
	
	// opcode byte lengths
	public static Integer[] opModeBytes = {0,2,2,2,2,3,3,3,2,2,1,1,2,3};		
	
	protected Integer opCode;
	protected Operand operand;
	protected int mode;
	protected int instructionLength = 1;
	protected boolean hasOperand;
	protected boolean requiresPadding;
	protected int address;
	

	
	// To be implemented by the specific Instruction classes
	public abstract void execute() throws Exception;
	
	public boolean requiresOperand() {
		return this.hasOperand;
	}
	
	public boolean requiresPadding() {
		if (this.mode == Instruction.ZERO_PAGE || this.mode == Instruction.ZERO_PAGE_X ||
				this.mode == Instruction.ZERO_PAGE_Y) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// Length of the instruction, including opcode byte
	public int length() {
		return this.instructionLength;
	}
	
	// Number of operands, which is length minus the opcode
	public int getNumOperands() {
		return this.instructionLength - 1;
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public int getAddr() {
		return this.address;
	}
	
	/**
	 * Set the memory location of this instruction 
	 * @param addr
	 */
	public void setAddr(int addr) {
		this.address = addr;
	}
	
	/**
	 * Operands fetched from memory are set via this method
	 * @param op
	 */
	public void setOperand(Operand op) {
		this.operand = op;
	}
	
	@Override
	public String toString() {		
		return Integer.toHexString(this.opCode) + " " + Integer.toHexString(this.operand.getValue());
	}
}
