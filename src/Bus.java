/**
 * 
 */

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 */
public class Bus {

    // default address to load programs 
    public static int DEFAULT_LOAD_ADDR = 0x0200;
	
    // start and end address for bus, assume 64K of memory 0x0000 to 0xffff
    private int startAddress = 0x0000;
    private int endAddress = 0xffff;	
	
	// local variables
	private Processor processor;
	private SortedSet<Memory> memDev;
	
	/**
	 * Bus class argumented constructor
	 * @param size
	 */
	public Bus(Integer size){
        this(0, size - 1);
	}
	
	/**
	 * Bus class argumented constructor
	 * @param start
	 * @param end
	 */
	public Bus(Integer start,Integer end){
        memDev = new TreeSet<Memory>();
        startAddress = start;
        endAddress = end;		
	}
	
	/**
	 * getStartAddress - returns start address of the bus
	 * @return
	 */
    public Integer getStartAddress() {
        return startAddress;
    }

    /**
     * getEndAddress - returns end address of the bus
     * @return
     */
    public int getEndAddress() {
        return endAddress;
    }	
	
    /**
     * addMemDevice - Add a physical memory device to the bus. 
     * @param device
     */
    public void addMemDevice(Memory m){
        // Add the device
        m.setBus(this);
        memDev.add(m);
    }
    
    /**
     * removeMemDevice - removes a physical memory device from the bus.
     * @param device
     */
    public void removeMemDevice(Memory m) {
        if (memDev.contains(m)) {
            memDev.remove(m);
        }
    }    
    
    /**
     * addProcessor - adds a processor to the bus
     * @param proc
     */
    public void addProcessor(Processor proc) {
        this.processor = proc;
        proc.setBus(this);
    }
    
    public int read(int address) throws IllegalAddressException {
        for (Memory m : memDev) {
            MemoryRange range = m.getMemoryRange();
            if (range.includes(address)) {
                // Compute offset into this device's address space.
                int devAddr = address - range.getStartAddress();
                return m.read(devAddr);
            }
        }
        throw new IllegalAddressException("Bus read error. No illegal address " + String.format("$%04X", address));
    }

    public void write(int address, int value) throws IllegalAddressException {
        for (Memory m : memDev) {
            MemoryRange range = m.getMemoryRange();
            if (range.includes(address)) {
                // Compute offset into this device's address space.
                int devAddr = address - range.getStartAddress();
                m.write(devAddr, value);
                return;
            }
        }
        throw new IllegalAddressException("Bus write error. Illegal address " + String.format("$%04X", address));
    }    
}
