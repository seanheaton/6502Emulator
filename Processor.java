/**
 * 
 */

/**
 *
 */
public class Processor {

	// constants
	public static Integer FLAG_CARRY = 0x01;
	public static Integer FLAG_ZERO = 0x02;
	public static Integer FLAG_IRQ_DIS = 0x04;
	public static Integer FLAG_DEC = 0x08;
	public static Integer FLAG_BRK = 0x10;
	public static Integer FLAG_OVERFLOW = 0x40;
	public static Integer FLAG_NEG = 0x80;
	public static Integer STACK_ADDR = 0x100;

	// local variables
	private ProcessorState procState;
	private Bus bus;
	
	// Singleton Processor instance
	private static Processor procInstance;

	private Processor() {}
	
	public static Processor getInstance() {
		
		if (procInstance == null) {
			procInstance = new Processor();
		}
		
		return procInstance;
	}

	/**
	 * setBus - sets bus reference
	 * @param bus
	 */
	public void setBus(Bus b) {
		this.bus = b;
	}	

	/**
	 * getBus - gets bus reference
	 * @return a reference to the bus
	 */
	public Bus getBus() {
		return bus;
	}

	/**
	 * reset - resets the processor to initial values
	 */
	public void reset() {
		// Registers
		procState.sp = 0xff;

		// Clear instruction register.
		procState.ir = 0;

		// Clear status register bits.
		procState.carryFlg = false;
		procState.zeroFlg = false;
		procState.irqDisableFlg = false;
		procState.decModeFlg = false;
		procState.brkFlg = false;
		procState.overflowFlg = false;
		procState.negFlg = false;

		// Clear illegal opcode trap.
		procState.irInvalidOp = false;

		// Reset Program Counter
		procState.pc = 0;
		
		// Reset registers.
		procState.a = 0;
		procState.x = 0;
		procState.y = 0;
	}

	/**
	 * getProcessorState - returns a reference to current processorState object
	 * @return
	 */
	public ProcessorState getCpuState() {
		return procState;
	}

	/**
	 * getNegFlg - returns value of current negative flag
	 * @return value of current negative flag
	 */
	public boolean getNegFlg() {
		return procState.negFlg;
	}

	/**
	 * setNegFlg - sets value of current negative flag
	 * @param flag
	 */
	public void setNegFlgg(boolean flag) {
		procState.negFlg = flag;
	}

	/**
	 * clearNegFlag - sets value of negative flag to false
	 */
	public void clearNegativeFlag() {
		procState.negFlg = false;
	}

	/**
	 * getCarryFlg - returns value of current carry flag
	 * @return value of current carry flag
	 */
	public boolean getCarryFlg() {
		return procState.carryFlg;
	}

	/**
	 * getCarryBit - if carry flag is set returns 1
	 * @return
	 */
	public int getCarryBit() {
		if(procState.carryFlg==true)
			return 1;
		else
			return 0;
	}

	/**
	 * setcarryFlg - sets carry flag to value passed
	 * @param flag
	 */
	public void setcarryFlg(boolean flag) {
		procState.carryFlg = flag;
	}

	/**
	 * setsCarryFlg - sets carry flag to true
	 */
	public void setCarryFlg() {
		procState.carryFlg = true;
	}

	/**
	 * clearCarryFlg - clears the carry flag
	 */
	public void clearCarryFlg() {
		procState.carryFlg = false;
	}

	/**
	 * getZeroFlg - gets zero flag
	 * @return the zero flag
	 */
	public boolean getZeroFlg() {
		return procState.zeroFlg;
	}

	/**
	 * setZeroFlg - sets the zero flag to the value passed
	 * @param zeroFlag the zero flag to set
	 */
	public void setZeroFlg(boolean flag) {
		procState.zeroFlg = flag;
	}

	/**
	 * setZeroFlg - sets the Zero Flag to true
	 */
	public void setZeroFlg() {
		procState.zeroFlg = true;
	}

	/**
	 * clearZeroFlag - clears the Zero Flag
	 */
	public void clearZeroFlg() {
		procState.zeroFlg = false;
	}

	/**
	 * getIrqDisableFlg - gets current value of irq disable flag
	 * @return the irq disable flag
	 */
	public boolean getIrqDisableFlg() {
		return procState.irqDisableFlg;
	}

	/**
	 * setIrqDisableFlg - sets irq disable flag to true
	 */
	public void setIrqDisableFlg() {
		procState.irqDisableFlg = true;
	}

	/**
	 * clearIrqDisableFlg - clears the irq disable flag
	 */
	public void clearIrqDisableFlg() {
		procState.irqDisableFlg = false;
	}


	/**
	 * getDecModeFlg
	 * @return the decimal mode flag
	 */
	public boolean getDecModeFlg() {
		return procState.decModeFlg;
	}

	/**
	 * setDecModeFlg - sets the decimal mode flag to true.
	 */
	public void setDecModeFlg() {
		procState.decModeFlg = true;
	}

	/**
	 * clearDecModeFlg - clears the Decimal Mode Flag.
	 */
	public void clearDecimalModeFlag() {
		procState.decModeFlg = false;
	}

	/**
	 * getBrkFlg - returns the break flag value
	 * @return the break flag
	 */
	public boolean getBrkFlg() {
		return procState.brkFlg;
	}

	/**
	 * setBrkFlg - sets the break flag to true
	 */
	public void setBrkFlg() {
		procState.brkFlg = true;
	}

	/**
	 * clearBrkFlg - clears the break flag
	 */
	public void clearBrkFlg() {
		procState.brkFlg = false;
	}

	/**
	 * getOverflowFlag - returns the current value of the overflow flag
	 * @return the overflow flag
	 */
	public boolean getOverflowFlg() {
		return procState.overflowFlg;
	}

	/**
	 * setOverflowFlg - sets the overflow flag to the value passed
	 * @param overflowFlag the overflow flag to set
	 */
	public void setOverflowFlg(boolean flag) {
		procState.overflowFlg = flag;
	}

	/**
	 * setOverflowFlg - sets the overflow flag to true
	 */
	public void setOverflowFlg() {
		procState.overflowFlg = true;
	}

	/**
	 * clearOverflowFlg - clears the overflow flag
	 */
	public void clearOverflowFlg() {
		procState.overflowFlg = false;
	}

	/**
	 * setInvalidOp - set the invalid op flag
	 */
	public void setInvalidOp() {
		procState.irInvalidOp = true;
	}

	/**
	 * clearInvalidOp - clear the invalid op flag
	 */
	public void clearInvalidOp() {
		procState.irInvalidOp = false;
	}

	/**
	 * getAccumulator - returns value of accumulator
	 * @return accumulator value
	 */
	public int getAccumulator() {
		return procState.a;
	}

	/**
	 * setAccumulator - sets the value of accumulator
	 * @param val
	 */
	public void setAccumulator(int val) {
		procState.a = val;
	}

	/**
	 * getXRegister - returns the value of the x-register
	 * @return x register value
	 */
	public int getXRegister() {
		return procState.x;
	}

	/**
	 * setXRegister - sets the value of the x-register
	 * @param val
	 */
	public void setXRegister(int val) {
		procState.x = val;
	}

	/**
	 * getYRegister - returns the value of the y-register
	 * @return y register value
	 */
	public int getYRegister() {
		return procState.y;
	}

	/**
	 * setYRegister - sets the value of the y-register
	 * @param val
	 */
	public void setYRegister(int val) {
		procState.y = val;
	}

	/**
	 * getProgramCounter - gets the value of the program counter register
	 * @return value of program counter
	 */
	public int getProgramCounter() {
		return procState.pc;
	}

	/**
	 * setProgramCounter - sets the value of the program counter register
	 * @param val
	 */
	public void setProgramCounter(int val) {
		procState.pc = val;
	}

	/**
	 * getStackPointer - returns value of the stack pointer
	 * @return value of stack pointer
	 */
	public int getStackPointer() {
		return procState.sp;
	}

	/**
	 * setStackPointer - sets value of the stack pointer
	 * @param val value to set stack pointer to
	 */
	public void setStackPointer(int val) {
		procState.sp = val;
	}

	/**
	 * getInstruction - returns current instruction register value
	 * @return value of instruction register
	 */
	public int getInstructionRegister() {
		return procState.ir;
	} 

	/**
	 * incrementPC - increment the program counter, rolling over if necessary
	 */
     void incrementPC() {
         if (procState.pc == 0xffff)
             procState.pc = 0;
         else
             ++procState.pc;
     }	
	
	/**
	 * pushStack - push data onto stack
	 * @param data to push onto the stack
	 * @throws IllegalAddressException
	 */
	void pushStack(int data) throws IllegalAddressException {
		
		// write the data item to the stack
		bus.write(STACK_ADDR + procState.sp, data);

		// increment stack pointer, rolling over if full
		if (procState.sp == 0)
			procState.sp = 0xff;
		else
			--procState.sp;
	}  
		
	/**
	 * popStack - return top of the stack
	 * @return top item on stack
	 * @throws IllegalAddressException
	 */
    int popStack() throws IllegalAddressException {
        if (procState.sp == 0xff)
            procState.sp = 0x00;
        else
            ++procState.sp;
        
        // return data at stack location + stack pointer offset
        return bus.read(STACK_ADDR + procState.sp);
    }
    
    /**
     * peekStack - returns the value currently at the top of the stack
     * @return value on top of the stack
     * @throws IllegalAddressException
     */
    int stackPeek() throws IllegalAddressException {
        return bus.read(STACK_ADDR + procState.sp + 1);
    }
    

    /**
     * getProcessorStatusFlg - returns the value of the processor status register in byte format
     * @return procStatusFlg as a byte
     */
    public Integer getProcessorStatusFlg() {
    	// start with default setting (bit 5 always set)
        Integer status = 0x20;
        
        if (procState.carryFlg) {
            status |= FLAG_CARRY;
        }
        if (procState.zeroFlg) {
            status |= FLAG_ZERO;
        }
        if (procState.irqDisableFlg) {
            status |= FLAG_IRQ_DIS;
        }
        if (procState.decModeFlg) {
            status |= FLAG_DEC;
        }
        if (procState.brkFlg) {
            status |= FLAG_BRK;
        }
        if (procState.overflowFlg) {
            status |= FLAG_OVERFLOW;
        }
        if (procState.negFlg) {
            status |= FLAG_NEG;
        }
        return status;
    }  
    
    /**
     * getProcStatusString - returns the status register as a string
     * @return a string representation of the status register
     */
    public String getProcStatusString() {
        String status = "[";
        
        // Negative flag, bit 7
        if(procState.negFlg)
        	status += "N";
        else
        	status += ".";
        
        // Overflow flag, bit 6
        if(procState.overflowFlg)
        	status += "O";
        else
        	status += ".";

        // Bit 5 is always 1
        status += ".";

        // break flag, bit 4
        if(procState.brkFlg)
        	status += "B";
        else
        	status += ".";

        // decimal mode flag, bit 3
        if(procState.decModeFlg)
        	status += "D";
        else
        	status += ".";

        // irq disable flag, bit 2
        if(procState.irqDisableFlg)
        	status += "I";
        else
        	status += ".";

        // zero flag, bit 1
        if(procState.zeroFlg)
        	status += "Z";
        else
        	status += ".";

        // carry flag, bit 0
        if(procState.carryFlg)
        	status += "C";
        else
        	status += ".";

        status += "]";
        
        return status;
    }
        
    /**
     * getAddress - given a hi and low byte, return the address
     * @param low
     * @param hi
     * @return
     */
    public Integer getAddress(int low, int hi) {
    	Integer result = 0x0;
    	// shift hi byte 8 places, or result with low and with 0xFFFF 
    	result = (hi << 8) | low & 0xffff;
        return result;
    }
    
    /**
     * getXAddress - given a hi and low byte, return the absolute with x-register offset
     * @param low
     * @param hi
     * @return
     */
    public Integer getXAddress(int low, int hi) {
    	Integer result = 0x0;
    	result = getAddress(low,hi) + procState.x & 0xffff;
        return result;
    }
    
    /**
     * getYAddress - given a hi and low byte, return the absolute with y-register offset
     * @param low
     * @param hi
     * @return
     */
    public int yAddress(int low, int hi) {
    	Integer result = 0x0;
    	result = getAddress(low, hi) + procState.y & 0xffff; 
        return result;
    }
    
    /**
     * getZeroPageXAddress - given a single byte, return the zero page x-register offset address.
     * @param aByte
     * @return
     */
    public Integer getZeroPageXAddress(int aByte) {
    	Integer addr = 0x0;
    	addr = (aByte + procState.x) & 0xff;
        return addr;
    }
    

    /**
     * getZeroPageYAddress - given a single byte, return the zero page y-register offset address.
     * @param aByte
     * @return
     */
    public Integer getZeroPageYAddress(int aByte) {
    	Integer addr = 0x0;
    	addr = (aByte + procState.y) & 0xff;
        return addr;
    }    

    /**
     * getRelAddress - given a single byte, return the offset address
     * @param offset
     * @return
     */
    public Integer relAddress(int offset) {
        // Cast the offset to a signed byte to handle negative offsets
    	Integer addr = 0x0;
    	addr = (procState.pc + (byte) offset) & 0xffff;
        return addr;
    }    
}
