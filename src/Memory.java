/**
 * 
 */

import java.util.Arrays;


/**
 *
 */
public class Memory implements Comparable<Memory> {
	//constants
	private static Integer MEM_ZERO = 0x0;

	// private variables
	private String name;
	private Integer membytes[];
	private boolean readOnly;
	private MemoryRange memRange;
	private Bus bus;

	public Memory(int address, int size) throws OutOfRangeException {
		this(address, size, false);
	}

	public Memory(Integer address,Integer size,boolean ro) throws OutOfRangeException {
		this.memRange = new MemoryRange(address, address + size - 1);
		this.membytes = new Integer[size];
		this.readOnly = ro;
		clearMem();
	}

	/**
	 * clearMem - clears memory array by setting it all to 0x00
	 */
	private void clearMem() {
		Arrays.fill(this.membytes, MEM_ZERO);
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public Bus getBus() {
		return this.bus;
	}

	public MemoryRange getMemoryRange() {
		return memRange;
	}

	public Integer getEndAddress() {
		return memRange.getEndAddress();
	}

	public int getStartAddress() {
		return memRange.getStartAddress();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int read(int address) throws IllegalAddressException {
		return this.membytes[address];
	}

	public void write(int address, int data) throws IllegalAddressException {
		if (readOnly) {
			throw new IllegalAddressException("Cannot write to read-only memory at address " + address);
		} else {
			this.membytes[address] = data;
		}
	}

	@Override
	public int compareTo(Memory other) {
		if (other == null) {
			throw new NullPointerException("Null comparison.");
		}
		if (this == other)
			return 0;
		return getMemoryRange().compareTo(other.getMemoryRange());	 
	}  
}
