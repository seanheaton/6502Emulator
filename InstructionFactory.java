/** InstructionFactory.java
 *  
 *  This is a Singleton Factory class designed to return a 6502 assembly code
 *  Instruction object. Based on the line of code passed to the getInstruction()
 *  method, a switch statement determines the appropriate instruction, and returns
 *  an Instruction object of that type.
 */

/**
 * 
 *
 */

public class InstructionFactory {
	
	// A singleton instance of the factory
	private static InstructionFactory instance;
	
	private InstructionFactory() {}
	
	// The factory instance is constructed via this public getter method
	public static InstructionFactory getInstance() {
		
		if (instance == null) {
			instance = new InstructionFactory();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param line
	 * @return newInstruction
	 * 
	 * Based on the code provided, this method determines the
	 * type of instruction. A switch statement is used to identify
	 * and build an Instruction object of the appropriate type.
	 */
	public Instruction getInstruction(Integer opcode) {
		
		Instruction newInstruction = null;
		
		
		/**
		 * Check the list of known 6502 instruction types for the opcode,
		 * referred to by instr. If found, construct an Instruction 
		 * object of that type.
		 */
		switch(opcode) {
        case (0x69):
            newInstruction = new ADCInstruction(0x69);
            break;
        case (0x65):
            newInstruction = new ADCInstruction(0x65);
            break;
        case (0x75):
            newInstruction = new ADCInstruction(0x75);
            break;
        case (0x6D):
            newInstruction = new ADCInstruction(0x6D);
            break;
        case (0x7D):
            newInstruction = new ADCInstruction(0x7D);
            break;
        case (0x79):
            newInstruction = new ADCInstruction(0x79);
            break;
        case (0x61):
            newInstruction = new ADCInstruction(0x61);
            break;
        case (0x71):
            newInstruction = new ADCInstruction(0x71);
            break;
        case (0x29):
            newInstruction = new ANDInstruction(0x29);
            break;
        case (0x25):
            newInstruction = new ANDInstruction(0x25);
            break;
        case (0x35):
            newInstruction = new ANDInstruction(0x35);
            break;
        case (0x2D):
            newInstruction = new ANDInstruction(0x2D);
            break;
        case (0x3D):
            newInstruction = new ANDInstruction(0x3D);
            break;
        case (0x39):
            newInstruction = new ANDInstruction(0x39);
            break;
        case (0x21):
            newInstruction = new ANDInstruction(0x21);
            break;
        case (0x31):
            newInstruction = new ANDInstruction(0x31);
            break;
        case (0x0A):
            newInstruction = new ASLInstruction(0x0A);
            break;
        case (0x06):
            newInstruction = new ASLInstruction(0x06);
            break;
        case (0x16):
            newInstruction = new ASLInstruction(0x16);
            break;
        case (0x0E):
            newInstruction = new ASLInstruction(0x0E);
            break;
        case (0x1E):
            newInstruction = new ASLInstruction(0x1E);
            break;
        case (0x24):
            newInstruction = new BITInstruction(0x24);
            break;
        case (0x2C):
            newInstruction = new BITInstruction(0x2C);
            break;/*
        case (0x10):
            newInstruction = new BPLInstruction(0x10);
            break;
        case (0x30):
            newInstruction = new BMIInstruction(0x30);
            break;
        case (0x50):
            newInstruction = new BVCInstruction(0x50);
            break;
        case (0x70):
            newInstruction = new BVSInstruction(0x70);
            break;
        case (0x90):
            newInstruction = new BCCInstruction(0x90);
            break;
        case (0xB0):
            newInstruction = new BCSInstruction(0xB0);
            break;
        case (0xD0):
            newInstruction = new BNEInstruction(0xD0);
            break;
        case (0xF0):
            newInstruction = new BEQInstruction(0xF0);
            break; */
        case (0xC9):
            newInstruction = new CMPInstruction(0xC9);
            break;
        case (0xC5):
            newInstruction = new CMPInstruction(0xC5);
            break;
        case (0xD5):
            newInstruction = new CMPInstruction(0xD5);
            break;
        case (0xCD):
            newInstruction = new CMPInstruction(0xCD);
            break;
        case (0xDD):
            newInstruction = new CMPInstruction(0xDD);
            break;
        case (0xD9):
            newInstruction = new CMPInstruction(0xD9);
            break;
        case (0xC1):
            newInstruction = new CMPInstruction(0xC1);
            break;
        case (0xD1):
            newInstruction = new CMPInstruction(0xD1);
            break;
        case (0xE0):
            newInstruction = new CPXInstruction(0xE0);
            break;
        case (0xE4):
            newInstruction = new CPXInstruction(0xE4);
            break;
        case (0xEC):
            newInstruction = new CPXInstruction(0xEC);
            break;
        case (0xC0):
            newInstruction = new CPYInstruction(0xC0);
            break;
        case (0xC4):
            newInstruction = new CPYInstruction(0xC4);
            break;
        case (0xCC):
            newInstruction = new CPYInstruction(0xCC);
            break;
        case (0xC6):
            newInstruction = new DECInstruction(0xC6);
            break;
        case (0xD6):
            newInstruction = new DECInstruction(0xD6);
            break;
        case (0xCE):
            newInstruction = new DECInstruction(0xCE);
            break;
        case (0xDE):
            newInstruction = new DECInstruction(0xDE);
            break;
        case (0x49):
            newInstruction = new EORInstruction(0x49);
            break;
        case (0x45):
            newInstruction = new EORInstruction(0x45);
            break;
        case (0x55):
            newInstruction = new EORInstruction(0x55);
            break;
        case (0x4D):
            newInstruction = new EORInstruction(0x4D);
            break;
        case (0x5D):
            newInstruction = new EORInstruction(0x5D);
            break;
        case (0x59):
            newInstruction = new EORInstruction(0x59);
            break;
        case (0x41):
            newInstruction = new EORInstruction(0x41);
            break;
        case (0x51):
            newInstruction = new EORInstruction(0x51);
            break;
        case (0x18):
            newInstruction = new CLCInstruction(0x18);
            break;
        case (0x38):
            newInstruction = new SECInstruction(0x38);
            break;
        case (0x58):
            newInstruction = new CLIInstruction(0x58);
            break;
        case (0x78):
            newInstruction = new SEIInstruction(0x78);
            break;
        case (0xB8):
            newInstruction = new CLVInstruction(0xB8);
            break;
        case (0xD8):
            newInstruction = new CLDInstruction(0xD8);
            break;
        case (0xF8):
            newInstruction = new SEDInstruction(0xF8);
            break;
        case (0xE6):
            newInstruction = new INCInstruction(0xE6);
            break;
        case (0xF6):
            newInstruction = new INCInstruction(0xF6);
            break;
        case (0xEE):
            newInstruction = new INCInstruction(0xEE);
            break;
        case (0xFE):
            newInstruction = new INCInstruction(0xFE);
            break; /*
        case (0x4C):
            newInstruction = new JMPInstruction(0x4C);
            break;
        case (0x6C):
            newInstruction = new JMPInstruction(0x6C);
            break;
        case (0x20):
            newInstruction = new JSRInstruction(0x20);
            break; */
        case (0xA9):
            newInstruction = new LDAInstruction(0xA9);
            break;
        case (0xA5):
            newInstruction = new LDAInstruction(0xA5);
            break;
        case (0xB5):
            newInstruction = new LDAInstruction(0xB5);
            break;
        case (0xAD):
            newInstruction = new LDAInstruction(0xAD);
            break;
        case (0xBD):
            newInstruction = new LDAInstruction(0xBD);
            break;
        case (0xB9):
            newInstruction = new LDAInstruction(0xB9);
            break;
        case (0xA1):
            newInstruction = new LDAInstruction(0xA1);
            break;
        case (0xB1):
            newInstruction = new LDAInstruction(0xB1);
            break;
        case (0xA2):
            newInstruction = new LDXInstruction(0xA2);
            break;
        case (0xA6):
            newInstruction = new LDXInstruction(0xA6);
            break;
        case (0xB6):
            newInstruction = new LDXInstruction(0xB6);
            break;
        case (0xAE):
            newInstruction = new LDXInstruction(0xAE);
            break;
        case (0xBE):
            newInstruction = new LDXInstruction(0xBE);
            break;
        case (0xA0):
            newInstruction = new LDYInstruction(0xA0);
            break;
        case (0xA4):
            newInstruction = new LDYInstruction(0xA4);
            break;
        case (0xB4):
            newInstruction = new LDYInstruction(0xB4);
            break;
        case (0xAC):
            newInstruction = new LDYInstruction(0xAC);
            break;
        case (0xBC):
            newInstruction = new LDYInstruction(0xBC);
            break;
        case (0x4A):
            newInstruction = new LSRInstruction(0x4A);
            break;
        case (0x46):
            newInstruction = new LSRInstruction(0x46);
            break;
        case (0x56):
            newInstruction = new LSRInstruction(0x56);
            break;
        case (0x4E):
            newInstruction = new LSRInstruction(0x4E);
            break;
        case (0x5E):
            newInstruction = new LSRInstruction(0x5E);
            break;
        case (0x09):
            newInstruction = new ORAInstruction(0x09);
            break;
        case (0x05):
            newInstruction = new ORAInstruction(0x05);
            break;
        case (0x15):
            newInstruction = new ORAInstruction(0x15);
            break;
        case (0x0D):
            newInstruction = new ORAInstruction(0x0D);
            break;
        case (0x1D):
            newInstruction = new ORAInstruction(0x1D);
            break;
        case (0x19):
            newInstruction = new ORAInstruction(0x19);
            break;
        case (0x01):
            newInstruction = new ORAInstruction(0x01);
            break;
        case (0x11):
            newInstruction = new ORAInstruction(0x11);
            break;
        case (0xAA):
            newInstruction = new TAXInstruction(0xAA);
            break;
        case (0x8A):
            newInstruction = new TXAInstruction(0x8A);
            break;
        case (0xCA):
            newInstruction = new DEXInstruction(0xCA);
            break;
        case (0xE8):
            newInstruction = new INXInstruction(0xE8);
            break;
        case (0xA8):
            newInstruction = new TAYInstruction(0xA8);
            break;
        case (0x98):
            newInstruction = new TYAInstruction(0x98);
            break;
        case (0x88):
            newInstruction = new DEYInstruction(0x88);
            break;
        case (0xC8):
            newInstruction = new INYInstruction(0xC8);
            break;
        case (0x2A):
            newInstruction = new ROLInstruction(0x2A);
            break;
        case (0x26):
            newInstruction = new ROLInstruction(0x26);
            break;
        case (0x36):
            newInstruction = new ROLInstruction(0x36);
            break;
        case (0x2E):
            newInstruction = new ROLInstruction(0x2E);
            break;
        case (0x3E):
            newInstruction = new ROLInstruction(0x3E);
            break;
        case (0x6A):
            newInstruction = new RORInstruction(0x6A);
            break;
        case (0x66):
            newInstruction = new RORInstruction(0x66);
            break;
        case (0x76):
            newInstruction = new RORInstruction(0x76);
            break;
        case (0x6E):
            newInstruction = new RORInstruction(0x6E);
            break;
        case (0x7E):
            newInstruction = new RORInstruction(0x7E);
            break;
        case (0xE9):
            newInstruction = new SBCInstruction(0xE9);
            break;
        case (0xE5):
            newInstruction = new SBCInstruction(0xE5);
            break;
        case (0xF5):
            newInstruction = new SBCInstruction(0xF5);
            break;
        case (0xED):
            newInstruction = new SBCInstruction(0xED);
            break;
        case (0xFD):
            newInstruction = new SBCInstruction(0xFD);
            break;
        case (0xF9):
            newInstruction = new SBCInstruction(0xF9);
            break;
        case (0xE1):
            newInstruction = new SBCInstruction(0xE1);
            break;
        case (0xF1):
            newInstruction = new SBCInstruction(0xF1);
            break;
        case (0x85):
            newInstruction = new STAInstruction(0x85);
            break;
        case (0x95):
            newInstruction = new STAInstruction(0x95);
            break;
        case (0x8D):
            newInstruction = new STAInstruction(0x8D);
            break;
        case (0x9D):
            newInstruction = new STAInstruction(0x9D);
            break;
        case (0x99):
            newInstruction = new STAInstruction(0x99);
            break;
        case (0x81):
            newInstruction = new STAInstruction(0x81);
            break;
        case (0x91):
            newInstruction = new STAInstruction(0x91);
            break;
        case (0x9A):
            newInstruction = new TXSInstruction(0x9A);
            break;
        case (0xBA):
            newInstruction = new TSXInstruction(0xBA);
            break; /*
        case (0x48):
            newInstruction = new PHAInstruction(0x48);
            break;
        case (0x68):
            newInstruction = new PLAInstruction(0x68);
            break;
        case (0x08):
            newInstruction = new PHPInstruction(0x08);
            break;
        case (0x28):
            newInstruction = new PLPInstruction(0x28);
            break; */
        case (0x86):
            newInstruction = new STXInstruction(0x86);
            break;
        case (0x96):
            newInstruction = new STXInstruction(0x96);
            break;
        case (0x8E):
            newInstruction = new STXInstruction(0x8E);
            break;
        case (0x84):
            newInstruction = new STYInstruction(0x84);
            break;
        case (0x94):
            newInstruction = new STYInstruction(0x94);
            break;
        case (0x8C):
            newInstruction = new STYInstruction(0x8C);
            break;
        default:
            break;
    }
		
		return newInstruction;
	}
}
