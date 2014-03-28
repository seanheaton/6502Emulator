/**
 * Assembler.java
 *
 * Parses 6502 assembler instructions.
 */

import java.io.*;
import java.util.*;

/**
 *
 */
public class Assembler {
	private HashMap<String,Integer> symbolTable;
	private Integer locationCounter;
	private BufferedReader fileReader;
	private File file;
	private Parser parser;
	private Integer origin = 0;
	private Bus bus;
	public Emulator6502 view;
	
	public Assembler() {
		symbolTable = new HashMap<String,Integer>();
		parser = new Parser();
		locationCounter = 0;
	}
	
	public Assembler(Bus aBus) {
		this();		
		bus = aBus;
	}
	
	public void outputError(String errmsg){
		view.textPaneConsole.setText(view.textPaneConsole.getText() + errmsg + "\n");
	}
	
	public void OpenFile(File f) throws FileNotFoundException {
		this.file = f;
		try{
			fileReader = new BufferedReader(new FileReader(this.file));
		}
		catch(FileNotFoundException ex){			
			outputError("ERROR: File not found " + this.file.getName());
		}	    
	}
	
	public void CloseFile() throws IOException {
		try{
			fileReader.close();
		}
		catch(IOException ex){
			System.out.println("File not open yet");
		}	    
	}	
	
	/**
	 * Assemble
	 * @throws IOException
	 */
	public void Assemble() throws IOException{
		//call firstpass
		this.outputError("Starting FirstPass()");
		FirstPass();
		
		//call secondpass
		this.outputError("Starting SecondPass()");
		try {
			SecondPass();
		} catch (IllegalAddressException e) {
			this.outputError(e.toString());
		}
		
		this.outputError("Successfully Assembled File!");
	}
	
	/**
	 * FirstPass - completes the first pass of the assembler, storing symbolic addresses in the symbol table
	 * @throws IOException
	 */
	private void FirstPass() throws IOException {
				
		String line = null;

		while ((line = fileReader.readLine()) != null) {
			// call parser with line
			parser.ParseLine(line);
			
			// Check if label exists 
			if (!parser.getLabel().equals("")){
				//label exists.. add to symbolTable along with locationCounter
				symbolTable.put(parser.getLabel(), locationCounter);
				
				// increment locationCounter
				int bytes = Instruction.opModeBytes[parser.getAddressMode()];
				locationCounter += bytes;
			}
			else
				// no label in this field, check if operation is ORG statement
				if(parser.getOperation().equals("ORG")){
					locationCounter = parser.getOperand();
					origin = locationCounter;
				}
				else
					// check if pass is complete
					if(parser.getOperation().equals("END"))
							return; // pass complete
					else
						// check if operation exists, if true increment locationCounter the number of bytes
						// necessary for the operation + operand
						if(!parser.getOperation().equals("")){
							int bytes = Instruction.opModeBytes[parser.getAddressMode()];
							locationCounter += bytes;
						}
							
		}
	}
	
	public Integer getOrigin() {
		return origin;
	}

	/**
	 * SecondPass - completes the second pass of the assembler, emitting the final machine code
	 * @throws IOException
	 * @throws IllegalAddressException 
	 */
	private void SecondPass() throws IOException, IllegalAddressException {
		// DEBUG
		System.out.println("DEBUG: Starting SecondPass()");
		
		String line = null;
		
		// reopen file
		this.CloseFile();
		this.OpenFile(this.file);
		
		locationCounter = 0;

		while ((line = fileReader.readLine()) != null) {
			// call parser with line
			parser.ParseLine(line);
			
			// check if operation is a psuedo-instruction
			if(InstructionLookup.isPsuedoInstruction(parser.getOperation())) {
				if(parser.getOperation().equals("ORG"))
					// ORG statement, reset locationCounter
					locationCounter = parser.getOperand();
				else
					// END statement, return from SecondPass
					if(parser.getOperation().equals("END"))
						return;				
			}
			// operation is not a psuedo-instruction
			else{
				Integer addrMode = parser.getAddressMode();
				String op = parser.getOperation();
				// If operation is valid
				if(!op.equals("")){
					
					// calculate locationCounter increment value
					int byteCount = Instruction.opModeBytes[parser.getAddressMode()];
					
					// output location, and opcode
					try {
						bus.write(locationCounter, InstructionLookup.getOpCode(op, addrMode-1));
					} catch (InvalidInstructionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					////////////////////////////////////////
					// Output encoded operands		
					////////////////////////////////////////
					
					// Handle symbolic addresses
					if(addrMode==Instruction.RELATIVE && symbolTable.containsKey(parser.getRawOperand())){
						byte relative = this.getRelativeAddress(symbolTable.get(parser.getRawOperand()), locationCounter);
						bus.write(locationCounter, relative);
					}
					
					// Handle normal operands
					if(parser.getOperand()!=null){
						if(byteCount<=2){
							bus.write(locationCounter, parser.getOperand());
						}
						else {
							bus.write(locationCounter, toLittleEndian(parser.getOperand())[0]);
							bus.write(locationCounter+1, toLittleEndian(parser.getOperand())[1]);
						}
					}
					
					// increment the location counter
					locationCounter += byteCount;	
					System.out.println();
				}
			}
		}
	}
	
	/**
	 * getRelativeAddress - returns the relative offset for a branch statement
	 * @param symbolAddress
	 * @param currentAddress
	 * @return
	 */
	private byte getRelativeAddress(Integer symbolAddress, Integer currentAddress){
		byte result = (byte)(symbolAddress-currentAddress);
		return result;
	}
	
	/**
	 * toLittleEndian - converts two bytes to little-endian mode
	 * @param bytes integer to convert
	 * @return two integers representing the hi and lo bytes
	 */
	private static Integer[] toLittleEndian(Integer bytes){
		String hi,lo;
		String twobytes = Integer.toHexString(bytes);
		hi = twobytes.substring(0,2);
		lo = twobytes.substring(2);
		Integer[] result = {Integer.parseInt(lo,16),Integer.parseInt(hi,16)};
		return result;
	}
}
