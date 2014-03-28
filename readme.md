Project Authors

Graham Tapscott
Sean Heaton
Brandon Blackmoor
David Dadanlar


NOTE: This is a work in progress. It is not yet a fully functional emulator



Summary

The goal of this project was to create an emulator for the MOS Technology 6502 microprocessor. This entailed creation of virtual microprocessor components to mimic those of the 6502 chip, as well as a Graphic User Interface (GUI) by which a user can compile and execute assembly code written for the 6502, as well as control some aspects of the processor's functionality.

Status

26 March 2014

All major component classes have been constructed, as well as all basic 6502 instructions. However, some additional functionality is lacking in the Processor class.

General Design

Structurally, the emulator project is divided into four major components: GUI, Assembly, Execution, and Processor Abstraction. The GUI contains those objects responsible for the user interface and presentation. The Assembly section is comprised of an Assembler object, which controls the process of compiling assembly code into the processor's memory, as well as Parser and InstructionLookup “helper” objects. Execution contains the objects that represent an assembly Program and all of the Instructions that might be found in 6502 assembly code. Finally, the Processor Abstraction layer represents the major components of a 6502 microprocesser, such as the Processor, Memory, Bus, and Processor State (i.e., the registers).

The GUI

The GUI is composed of three main components, the main control window, the memory window, and the trace log window. The control window contains all of the components needed to load and run a program, as well as a console and display of the register values. The memory window represents the contents of the processor's memory, and the trace log window displays the last few instructions completed.

These components have been implemented using the main Emulator6502 class, which has as its members the objects needed for the control window. Furthermore, the Processor, Bus, Memory, and Program components are “wired” to Emulator6502. Additionally, the Emulator6502 class has MemoryWIndow and TraceLogWindow members, which serve the functions of those other two GUI components.

Assembly

While the Assembler is the focal point of this component, the Parser object is a major “work-horse” in the process of assembling code into memory. The Parser contains methods for parsing a line of assembly code into the components of an instruction that can then be stored in the processor's memory. The Assembler makes use of the Parser via its public assemble() method, which in turn calls upon the private, helper functions, FirstPass() and SecondPass(). The Assembler method is related to the Emulator6502 class by composition.

Execution

Once the program code has been assembled into memory, the Execution section takes care of retrieving and executing its instructions. As such, it contains a Program class, which in turn has as a member a List of all Instructions in the program retrieved from memory. In order to represent the 6502 instruction set, an abstract Instruction class was created with all of the functionality common to the various processor instructions. In turn, a large set of specific instruction classes (e.g., LDAInstruction) was created, all of which extend the abstract Instruction class. An InstructionFactory utility object is responsible for creating appropriate, specific Instruction objects based on the opcodes found in memory.

The Execution section is connected to the Processor Abstraction section through ownership of references to the Processor and Bus objects.

Processor Abstraction

In this component, we find representations of the major components of a 6502 microprocesser. The singleton Processor object is the focus here, and it contains a ProcessorState object, which is responsible for representing the status of the processor registers. Also found here are the Memory and system Bus, which reads and writes bytes to and from the Memory.

Development Environment:

JDK 1.7 Eclipse (Kepler)

User System Requirements: JRE 1.6 or higher
