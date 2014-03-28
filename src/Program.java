/** Program.java
 *  
 *  This class represents an entire assembly language program written for the
 *  6502 processor. It contains a list of Instruction objects to be executed
 *  in turn, by the processor.
 */

import java.util.List;
import java.util.LinkedList;
import java.lang.StringBuilder;

public class Program {

	private Object [] instructions;
	private int instrCounter;
	private Processor proc = null;
	private Bus bus = null;
	private InstructionFactory instrFactory = null;
	private boolean cancelled = false;
	
	/**
	 * 
	 * @param startAddr
	 */
	public Program(Integer startAddr) {
		
		/**
		 * Processor and Bus objects are used to read/write to
		 * memory and registers
		 */
		this.proc = Processor.getInstance();
		this.bus = proc.getBus();
		this.instrFactory = InstructionFactory.getInstance();
		this.instrCounter = 0;
		
		// Get instructions from memory and build Instruction array
		instructions = this.buildProgram(startAddr);
		
	}
	
	/**
	 * Builds the Instruction array
	 * @param addr
	 * @return
	 */
	private Object [] buildProgram(int addr) {
		
		List<Instruction> instrList = new LinkedList<>();
		int newByte;
		
		try {
			/*
			 *  Iterate through memory space. The int addr keeps track
			 *  of the current memory address being read
			 */
			while ( (newByte = this.bus.read(addr)) != 0x00 ) {
				
				int instrLength = 0;				
				Instruction newInstr = this.instrFactory.getInstruction(newByte);
				
				if (newInstr != null) {
					// Set the address attribute of the Instruction object
					newInstr.setAddr(addr);
					// Get the length attribute of the particular Instruction type
					instrLength = newInstr.length();
					/**
					 * Retrieve and set the operands associated with this
					 * instruction, based on the instruction length
					 */
					if (newInstr.requiresOperand() && instrLength == 2) {
						// Get the next address space as an operand
						int ops = this.bus.read(++addr);
						if (newInstr.requiresPadding) {
							// Zero page argument that needs padded zeros
							String concat = "00" + Integer.toHexString(ops);
							int newOps = Integer.parseInt(concat, 16);
							newInstr.setOperand(new Operand(newOps));
						}
						else {
							newInstr.setOperand(new Operand(ops));
						}
						addr++;
					}
					else if (newInstr.requiresOperand() && instrLength == 3) {
						// Get the next two address spaces as an operand
						Integer [] ops = {this.bus.read(++addr), this.bus.read(++addr)};
						
						// Account for little endian storage
						String concat = Integer.toHexString(ops[1]) + Integer.toHexString(ops[0]);
						int newOps = Integer.parseInt(concat, 16);
						newInstr.setOperand(new Operand(newOps,2));
						addr++;
					}
					else {
						// One-byte instruction; just move to the next address
						addr++;
					}
					instrList.add(newInstr);
				}
				else {
					System.err.println("Unknown instruction opcode at " + addr);
					addr++;
				}
			}
		}
		catch (IllegalAddressException ex) {
			System.err.println("Error reading byte in Program build: " + ex);
		}
		
		return instrList.toArray();
	}
	
	/**
	 * Iterate through the Instructions in the array that was built.
	 * Call the execute() method on each Instruction.
	 */
	public void executeProgram(int speed) {

		this.resetCounter();
		
		for (Object instr : this.instructions) {
			
			if (this.cancelled) {
				break;
			}
			
			Instruction current = (Instruction)instr;
			proc.setProgramCounter(current.getAddr());
			try {
				current.execute();
			}
			catch (Exception ex) {
				System.err.println("Problem executing instruction");
			}
			// Advance the PC past the current instruction and its operands
			proc.setProgramCounter(proc.getProgramCounter() + current.length());
			
			this.updateMem();
			this.updateTrace();
			
			// Advance the instruction counter
			this.instrCounter++;
			try {
				wait(speed*10);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * This method is used to step through the program. It is called whenever
	 * the user clicks the Step button on the user interface. The instrCounter
	 * keeps track of the current place in the list of Instructions.
	 */
	public void executeProgramStep() {
		
		Instruction current = (Instruction)this.instructions[this.getCounter()];
		
		proc.setProgramCounter(current.getAddr());
		
		try {
			current.execute();
		}
		catch (Exception ex) {
			System.err.println("Problem executing instruction");
		}
		
		// Advance the PC past the current instruction and its operands
		proc.setProgramCounter(proc.getProgramCounter() + current.length());
		
		this.updateMem();
		this.updateTrace();
		
		// Advance the instruction counter
		this.instrCounter++;
	}
	
	
	public int getCounter() {
		return this.instrCounter;
	}
	
	// Reset the counter to 0
	public void resetCounter() {
		this.instrCounter = 0;
	}
	
	public void setCounter(int newCounter) {
		this.instrCounter = newCounter;
	}
	
	private void updateMem() {
		StringBuilder memVals = new StringBuilder();
		try {
			for (int i = 0x0000; i <= 0xffff; i++) {
				if ( (i > 0) && ((i % 10) == 0) ) {
					memVals.append("\n" + Integer.toHexString(this.bus.read(i)));
				}
				else {
					memVals.append(Integer.toHexString(this.bus.read(i)));
				}
			}
		}
		catch (IllegalAddressException e) {
			e.printStackTrace();
		}
		
		MemoryWindow.textPaneMemory.setText(memVals.toString());
	}
	
	private void updateTrace() {
		StringBuilder inst = new StringBuilder();
		if (this.instrCounter == 0) {
			inst.append(instructions[instrCounter].toString());
		}
		else if (this.instrCounter == 1) {
			inst.append(instructions[instrCounter-1].toString() + "\n"
					+  instructions[instrCounter].toString());
		}
		else {
			inst.append(instructions[instrCounter-2].toString() + "\n"
					+ instructions[instrCounter-1].toString() + "\n"
					+ instructions[instrCounter].toString());
		}
		
		TraceLogWindow.textPaneTraceLog.setText(inst.toString());
	}
	
	public void cancel() {
		this.cancelled = true;
	}
}
