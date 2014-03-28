/**
 * 
 */

/**
 *
 */
public class MemoryRange implements Comparable<MemoryRange>{

	private Integer startAddress;
	private Integer endAddress;
	
	public MemoryRange(Integer start, Integer end){
		this.startAddress = start;
		this.endAddress = end;
	}
	
	public Integer getStartAddress(){
		return startAddress;
	}
	
	public Integer getEndAddress(){
		return endAddress;
	}

	@Override
	public int compareTo(MemoryRange other) {
	    if (other == null) {
	        throw new NullPointerException("Null comparison");
	      }
	      if (this == other) {
	        return 0;
	      }
	      Integer thisStart = new Integer(this.getStartAddress());
	      Integer otherStart = new Integer(other.getStartAddress());
	      return thisStart.compareTo(otherStart);
	}

	public boolean includes(int address) {
		if(address <= endAddress && address >= startAddress)
			return true;
		else
			return false;
	}
	
}
