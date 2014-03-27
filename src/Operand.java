/** Operand.java
 *  
 *  This class represents the operand of a 6502 assembly instruction.
 *  Values are assumed to be in hex.
 */


public class Operand {

	private int length; // default length
	private int value;
	/**
	 * 
	 * @param rawOperand
	 */
	public Operand(int rawOperand) {	
		this(rawOperand, 1);
	}
	
	/**
	 * Overloaded constructor to specify length
	 * @param rawOperand
	 * @param len
	 */
	public Operand(int rawOperand, int len) {
		this.value = rawOperand;
		this.length = len;
	}
	
	/**
	 * Returns the length of operand
	 * @return int
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Returns the value of the operand
	 * @return Integer
	 */
	public int getValue() {
		
		return this.value;
	}
	
	public void setLength(int len) {
		this.length = len;
	}
}