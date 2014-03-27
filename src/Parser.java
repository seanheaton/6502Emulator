/**
 * 
 */

/**
 *
 */
public class Parser {

//	
//	<input_line> = <label><operation><operand><comment> | <comment>
//	<label> = <empty> | <1-15 alphanumeric identifier>
//	<comment> = <semicolon prefixed comment>
//	<operation> = <valid CPU instruction> | <psuedo-instruction>
//	<operand> = <empty> | <immediate_value> | <address>
//	<immediate_value> = <#><byte>
//	<byte> = <$><0-9a-f><0-9a-f>
//	<address> = <byte> or <word>
//	<word> = <$><0-9a-f><0-9a-f><0-9a-f><0-9a-f>
	
	private String inputLine = "";
	private int currentPos = 0;
	private Character currentToken;
	private String label = "";
	private String operation = "";
	private String rawOperand = "";
	private Integer operand = 0x0;
	private String comment = "";
	private String currentLexeme = "";
	private Integer addressMode = 0;
	
	/**
	 * 
	 */
	public Parser() {
	}

	/**
	 * ParseLine - 
	 * @param in
	 */
	public void ParseLine(String in){
		this.reset();		
		inputLine = in;
		currentToken = inputLine.charAt(0);
		
		// If line is not a comment, process the rest of the line
		if(!produceComment()){
			
			// try to produce a label
			produceLabel();
			
			// try to produce the operation
			produceOperation();
			
			// try to produce the operand
			produceOperand();
			
			// check for a trailing comment
			produceComment();	
		}
		
		// parse operand and save address mode and int operand
		this.addressMode = this.produceAddressMode();
		this.operand = this.parseOperand();
	}

	/**
	 * reset - resets the parser
	 */
	private void reset() {
		currentPos = 0;
		label = "";
		operation = "";
		rawOperand = "";
		comment = "";
		operand = 0;
		addressMode = 0;
	}

	/**
	 * produceComment - given a token, produces a comment, consumes entire line from token position to end of line.
	 * @return
	 */
	private boolean produceComment(){
		while(Character.isWhitespace(currentToken))
			if(this.hasNextToken())
				this.getNextToken();		
		if(currentToken==';'){
			comment = inputLine.substring(currentPos);
			return true;			
		}
		else
			return false;
	}

	/**
	 * produceOperation - produces the operation from the raw input line
	 */
	private void produceOperation() {

		// check if operation has already been found
		if(!operation.equals(""))
			return;
		
		// operation not found yet
		else {

			currentLexeme = "";
			// Consume leading whitespace
			while(Character.isWhitespace(currentToken))
				if(this.hasNextToken())
					this.getNextToken();

			// Test if lexeme is label or operand
			while(!Character.isWhitespace(currentToken) && currentPos<inputLine.length()){
				currentLexeme += currentToken;
				if(this.hasNextToken())
					this.getNextToken();
				else
					// end of line
					break;
			}
			// lexeme has been created, test if it's a valid operation
			if(isOperation(currentLexeme)){
				operation = currentLexeme;
				return;			
			}
			else
				System.out.println("DEBUG: OPERATION NOT VALID");
		}
	}	

	/**
	 * produceOperand - produces an operand from a line of input
	 */
	private void produceOperand() {

		// check if operation has already been found
		if(!operation.equals("")){
			// reset currentLexeme
			currentLexeme = "";
			
			// Test for end of line, i.e. no operand
			if(!this.hasNextToken())
				return;
			
			// Consume leading whitespace
			while(Character.isWhitespace(currentToken))
				if(this.hasNextToken())
					this.getNextToken();

			// Consume all characters to next whitespace
			while(!Character.isWhitespace(currentToken)){
				currentLexeme += currentToken;
				if(this.hasNextToken())
					this.getNextToken();
				else
					break;
			}
			
			// Check to be sure that this is a trailing comment or an operand
			if(!currentLexeme.startsWith(";"))
				rawOperand = currentLexeme;
		}	
	}



	/**
	 * produceLabel - given a currentToken, produces a label if it exists, consumes whitespaces
	 */
	private void produceLabel() {
		// clear currentLexeme
		currentLexeme = "";
		// Consume leading whitespace
		while(Character.isWhitespace(currentToken)){
			if(this.hasNextToken())
				this.getNextToken();
		}
		// Test if lexeme is label or operation
		while(!Character.isWhitespace(currentToken)){
			currentLexeme += currentToken;
			if(this.hasNextToken())
				this.getNextToken();
			else
				break;
		}
		if(isOperation(currentLexeme) || 
				currentLexeme.equals("ORG") || 
				currentLexeme.equals("END")){
			
			operation = currentLexeme;
			return;			
		}
		else
			label = currentLexeme;
	}
	
	/**
	 * 
	 * @param op
	 * @return
	 */
	private boolean isOperation(String op) {
		return InstructionLookup.isOpcodeName(op);
	}

	/**
	 * isCommentLine - check if the line starts with a ;
	 * @param str
	 * @return true if the line starts with a ;
	 */
	private static boolean isCommentLine(String str){
		if(str.startsWith(";"))
			return true;
		else
			return false;
	}	

	/**
	 * check if the string array contains non-whitespace characters in field[0]
	 * @param t
	 * @return true if field[0] is not whitespace
	 */
	private static boolean hasLabel(String[] t){
		if(t[0].trim().length() > 0 && !t[0].contains(";"))
			return true;
		return false;
	}

	/**
	 * 
	 * @return the address mode of the instruction
	 */
	public Integer getAddressMode() {
		return addressMode;
	}
	
	/**
	 * 
	 * @return the operand
	 */
	public Integer getOperand() {
		return operand;
	}	
	
	/**
	 * @return the inputLine
	 */
	public String getInputLine() {
		return inputLine;
	}

	/**
	 * @param inputLine the inputLine to set
	 */
	public void setInputLine(String inputLine) {
		this.inputLine = inputLine;
	}

	public boolean hasNextToken(){
		if(currentPos<inputLine.length()-1)
			return true;
		else
			return false;
	}
	
	public void getNextToken(){
		currentPos++;
		currentToken = inputLine.charAt(currentPos);
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the operand
	 */
	public String getRawOperand() {
		return rawOperand;
	}

	/**
	 * @param operand the operand to set
	 */
	public void setOperand(String operand) {
		this.rawOperand = operand;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

//	#		....	immediate	 	OPC #$BB	 	operand is byte (BB)
//	impl		....	implied	 	OPC	 	operand implied
//	A		....	Accumulator	 	OPC A	 	operand is AC

//	abs		absolute				OPC $HHLL	 	operand is address $HHLL
//	abs,X	absolute, X-indexed		OPC $HHLL,X	 	operand is address incremented by X with carry
//	abs,Y	absolute, Y-indexed	 	OPC $HHLL,Y	 	operand is address incremented by Y with carry
//	rel		relative				OPC $BB	 	branch target is PC + offset (BB), bit 7 signifies negative offset
//	zpg		zeropage				OPC $LL	 	operand is of address; address hibyte = zero ($00xx)
//	zpg,X	zeropage, X-indexed		OPC $LL,X	 	operand is address incremented by X; address hibyte = zero ($00xx); no page transition
//	zpg,Y	zeropage, Y-indexed		OPC $LL,Y	 	operand is address incremented by Y; address hibyte = zero ($00xx); no page transition			
//
//	ind		indirect				OPC ($HHLL)	 	operand is effective address; effective address is value of address
//	X,ind	X-indexed, indirect		OPC ($BB,X)	 	operand is effective zeropage address; effective address is byte (BB) incremented by X without carry
//	ind,Y	indirect, Y-indexed		OPC ($LL),Y	 	operand is effective address incremented by Y with carry; effective address is word at zeropage address
	
	
	private Integer produceAddressMode() {

		// if implied return
		if(rawOperand.equals(""))
			if(operation.equals("ASL") || operation.equals("LSR") ||
					operation.equals("ROL") || operation.equals("ROR"))
					return Instruction.ACCUMULATOR;
			else
				return Instruction.IMPLIED;			
		
		// If immediate return
		if(rawOperand.startsWith("#"))
			return Instruction.IMMEDIATE;
		
		// if accumulator return
		if(rawOperand.equals("A"))
			return Instruction.ACCUMULATOR;
				
		// Absolute, relative, zeropage
		if(rawOperand.startsWith("$") && !rawOperand.contains(",")){
			if(rawOperand.length()>3)
				return Instruction.ABSOLUTE;
			else {
				if(operation.equals("BCC") || operation.equals("BCS") ||
						operation.equals("BEQ") || operation.equals("BMI") ||
						operation.equals("BNE") || operation.equals("BPL") ||
						operation.equals("BVC") || operation.equals("BVS"))
					return Instruction.RELATIVE;
				else
					return Instruction.ZERO_PAGE;
			}
		}
		
		// Absolute-x,absolute-y,zeropage-x,zeropage-y
		if(rawOperand.startsWith("$") && rawOperand.contains(",")){
			if(rawOperand.length()>5){
				// Absolute-x
				if(rawOperand.endsWith("X"))
					return Instruction.ABSOLUTE_X;
				// Absolute-y
				else
					if(rawOperand.endsWith("Y"))
						return Instruction.ABSOLUTE_Y;
			}
			else {
				// zeropage-x
				if(rawOperand.endsWith("X"))
					return Instruction.ZERO_PAGE_X;
				// zeropage-y				
				else
					if(rawOperand.endsWith("Y"))
						return Instruction.ZERO_PAGE_Y;
			}
		}
		
		// X-indexed, indirect & indirect, Y-indexed
		if(rawOperand.startsWith("(") && rawOperand.contains(",")){
			if(rawOperand.endsWith("X)"))
				return Instruction.INDIRECT_X;
			else
				if(rawOperand.endsWith("Y"))
					return Instruction.INDIRECT_Y;
				else
					if(rawOperand.endsWith(")"))
							return Instruction.INDIRECT;
		}
		
		// X-indexed, indirect & indirect, Y-indexed
		if(rawOperand.startsWith("(") && !rawOperand.contains(",")){
			if(rawOperand.endsWith(")"))
				return Instruction.INDIRECT;
		}
		// operand is a label
		return Instruction.RELATIVE;
	}

//	#		....	immediate	 	OPC #$BB	 	operand is byte (BB)
//	impl		....	implied	 	OPC	 	operand implied
//	A		....	Accumulator	 	OPC A	 	operand is AC

//	abs		absolute				OPC $HHLL	 	operand is address $HHLL
//	abs,X	absolute, X-indexed		OPC $HHLL,X	 	operand is address incremented by X with carry
//	abs,Y	absolute, Y-indexed	 	OPC $HHLL,Y	 	operand is address incremented by Y with carry
//	rel		relative				OPC $BB	 	branch target is PC + offset (BB), bit 7 signifies negative offset
//	zpg		zeropage				OPC $LL	 	operand is of address; address hibyte = zero ($00xx)
//	zpg,X	zeropage, X-indexed		OPC $LL,X	 	operand is address incremented by X; address hibyte = zero ($00xx); no page transition
//	zpg,Y	zeropage, Y-indexed		OPC $LL,Y	 	operand is address incremented by Y; address hibyte = zero ($00xx); no page transition			
//
//	ind		indirect				OPC ($HHLL)	 	operand is effective address; effective address is value of address
//	X,ind	X-indexed, indirect		OPC ($BB,X)	 	operand is effective zeropage address; effective address is byte (BB) incremented by X without carry
//	ind,Y	indirect, Y-indexed		OPC ($LL),Y	 	operand is effective address incremented by Y with carry; effective address is word at zeropage address
		
	
	private Integer parseOperand(){
		// psuedo-instructions
		if(operation.equals("ORG"))
			return Integer.parseInt(rawOperand.substring(1,5), 16);
		
		// Immediate address
		if(rawOperand.startsWith("#")) {
			if(rawOperand.startsWith("#$"))
				// hex-value literal i.e. #$00
				return Integer.parseInt(rawOperand.substring(2),16);
			else
				// binary-value literal i.e. #00
				return Integer.parseInt(rawOperand.substring(1),10);
		}
		else {
			if(addressMode==Instruction.ABSOLUTE || addressMode==Instruction.ABSOLUTE_X || addressMode==Instruction.ABSOLUTE_Y)
				return Integer.parseInt(rawOperand.substring(1,5), 16);
			if(addressMode==Instruction.ZERO_PAGE || addressMode==Instruction.ZERO_PAGE_X || addressMode==Instruction.ZERO_PAGE_Y)
				return Integer.parseInt(rawOperand.substring(1,3),16);
			if(addressMode==Instruction.INDIRECT)
				return Integer.parseInt(rawOperand.substring(2,6),16);
			if(addressMode==Instruction.INDIRECT_X || addressMode==Instruction.INDIRECT_Y)
				return Integer.parseInt(rawOperand.substring(2,4),16);
		}
		return null;
	}

	
	public static String getIndex(String rawOperand){
		String strParts[] = rawOperand.split(",");
		if(strParts.length>1)
			return strParts[1];
		else
			return null;
	}
}
