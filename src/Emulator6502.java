/**
 * Emulator6502.java
 *
 * Creates graphical dialog for control of a 6502 processor emulator.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.io.*;
import java.util.*;

public class Emulator6502 extends JFrame
{
	/**
	 * Not sure why this is needed, but apparently it is 
	 */
	private static final long serialVersionUID = 1L;
	
	public JFileChooser fileChooser;
	public StatusBar statusBar;
	public JSlider sliderExecutionDelay;
	public int sliderExecutionDelayValue;
	public MemoryWindow memoryWindow;
	public TraceLogWindow traceLogWindow;
	public static JTextPane textPaneConsole;

	
	// Get the size of the screen
	public Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Determine the location of the window
	public int w;
	public int h;
	public int x;
	public int y;

	// Assembler    
	private final Assembler assembler;
	
    // Simulation related variables
    private final Bus    bus;
    private final Processor    processor;
    private final Memory mem;
    
    // Program to be executed
    private Program prog;
    
	
	// Constants used by the simulation
    private static final int BUS_START = 0x0000;
    private static final int BUS_END    = 0xffff;

    // 32K of RAM from $0000 - $7FFF
    private static final int MEM_START = 0x0000;
    private static final int MEM_SIZE = 0x8000;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
	    SwingUtilities.invokeLater(new Runnable()
	    {
	        public void run()
	        {
	            Emulator6502 ex = null;
				try {
					ex = new Emulator6502();
				} catch (OutOfRangeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            ex.setVisible(true);
	        }
	    });
	}
	
	/**
	 * Create the application window.
	 * @throws OutOfRangeException 
	 */
	public Emulator6502() throws OutOfRangeException
	{
	    initialize();
	    
	    // instantiate hardware
        bus = new Bus(BUS_START, BUS_END);
        processor = Processor.getInstance();
        processor.setBus(bus);
        mem = new Memory(MEM_START, MEM_SIZE, false);
        bus.addProcessor(processor);
        bus.addMemDevice(mem);
	    
	    // instantiate assembler
	    assembler = new Assembler(bus); 
	    assembler.view = this;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public final void initialize()
	{
	
		setTitle("6502 Processor Emulator");
	    setSize(450, 395);
	    w = this.getSize().width;
	    h = this.getSize().height;
	    x = (dim.width-w)/2;
	    y = (dim.height-h)/2;
	
	    setLocationRelativeTo(null);
	    setLocation(x, y);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	    JMenuBar menuBar = new JMenuBar();
	    setJMenuBar(menuBar);
	
	    JMenu mnFile = new JMenu("File");
	    mnFile.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(mnFile);

		fileChooser = new JFileChooser();

		JMenuItem mntmOpenFile = new JMenuItem("Open File...");
		mntmOpenFile.setMnemonic(KeyEvent.VK_O);
		mntmOpenFile.setEnabled(true);
		mntmOpenFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmOpenFileAction();
			}
		});
		mnFile.add(mntmOpenFile);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mntmRun.setMnemonic(KeyEvent.VK_R);
		mntmRun.setEnabled(true);
		mntmRun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmRunAction();
			}
		});
		mnFile.add(mntmRun);
		
		JMenuItem mntmReset = new JMenuItem("Reset");
		mntmReset.setMnemonic(KeyEvent.VK_E);
		mntmReset.setEnabled(true);
		mntmReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmResetAction();
			}
		});
		mnFile.add(mntmReset);
		
	    JMenuItem mntmExit = new JMenuItem("Exit");
	    mntmExit.setMnemonic(KeyEvent.VK_X);
		mntmExit.setEnabled(true);
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmExitAction();
			}
		});
		mnFile.add(mntmExit);
	
		JMenu mnView = new JMenu("View");
		mnView.setMnemonic(KeyEvent.VK_V);
		menuBar.add(mnView);
		
		JMenuItem mntmMemory = new JMenuItem("Memory");
		mntmMemory.setMnemonic(KeyEvent.VK_M);
		mntmMemory.setEnabled(true);
		mntmMemory.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmMemoryAction();
			}
		});
		mnView.add(mntmMemory);
		
		JMenuItem mntmTraceLog = new JMenuItem("Trace Log");
		mntmTraceLog.setMnemonic(KeyEvent.VK_T);
		mntmTraceLog.setEnabled(true);
		mntmTraceLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmTraceLogAction();
			}
		});
		mnView.add(mntmTraceLog);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(mnHelp);
		
		JMenuItem mntmAboutEmulator = new JMenuItem("About 6502 Emulator");
		mntmAboutEmulator.setMnemonic(KeyEvent.VK_A);
		mntmAboutEmulator.setEnabled(true);
		mntmAboutEmulator.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				mntmAboutEmulatorAction();
			}
		});
		mnHelp.add(mntmAboutEmulator);
	
		statusBar = new StatusBar();
		statusBar.setFont(new Font("Tahoma", Font.PLAIN, 11));
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	
		JPanel panelMain = new JPanel();
		getContentPane().add(panelMain, BorderLayout.CENTER);
		GridBagLayout gbl_panelMain = new GridBagLayout();
		gbl_panelMain.columnWidths = new int[]{0, 0};
		gbl_panelMain.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelMain.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelMain.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelMain.setLayout(gbl_panelMain);
	
		JPanel panelRegistersHeading = new JPanel();
		GridBagConstraints gbc_panelRegistersHeading = new GridBagConstraints();
		gbc_panelRegistersHeading.insets = new Insets(0, 5, 5, 5);
		gbc_panelRegistersHeading.fill = GridBagConstraints.BOTH;
		gbc_panelRegistersHeading.gridx = 0;
		gbc_panelRegistersHeading.gridy = 0;
		panelMain.add(panelRegistersHeading, gbc_panelRegistersHeading);
		GridBagLayout gbl_panelRegistersHeading = new GridBagLayout();
		gbl_panelRegistersHeading.columnWidths = new int[]{0, 0};
		gbl_panelRegistersHeading.rowHeights = new int[]{0, 0};
		gbl_panelRegistersHeading.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelRegistersHeading.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelRegistersHeading.setLayout(gbl_panelRegistersHeading);
		
		JLabel labelRegisters = new JLabel("Registers:");
		labelRegisters.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_labelRegisters = new GridBagConstraints();
		gbc_labelRegisters.gridx = 0;
		gbc_labelRegisters.gridy = 0;
		panelRegistersHeading.add(labelRegisters, gbc_labelRegisters);
		
		JPanel panelRegisters = new JPanel();
		GridBagConstraints gbc_panelRegisters = new GridBagConstraints();
		gbc_panelRegisters.insets = new Insets(0, 5, 0, 5);
		gbc_panelRegisters.fill = GridBagConstraints.BOTH;
		gbc_panelRegisters.gridx = 0;
		gbc_panelRegisters.gridy = 1;
		panelMain.add(panelRegisters, gbc_panelRegisters);
		GridBagLayout gbl_panelRegisters = new GridBagLayout();
		gbl_panelRegisters.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelRegisters.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelRegisters.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelRegisters.rowWeights = new double[]{0.0, 0.0, 0.0, 4.9E-324, Double.MIN_VALUE};
		panelRegisters.setLayout(gbl_panelRegisters);
		
		JLabel labelRegNegative = new JLabel("Negative:");
		labelRegNegative.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegNegative = new GridBagConstraints();
		gbc_labelRegNegative.anchor = GridBagConstraints.WEST;
		gbc_labelRegNegative.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegNegative.gridx = 0;
		gbc_labelRegNegative.gridy = 0;
		panelRegisters.add(labelRegNegative, gbc_labelRegNegative);
		
		JLabel displayRegNegative = new JLabel("0");
		displayRegNegative.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegNegative = new GridBagConstraints();
		gbc_displayRegNegative.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegNegative.gridx = 1;
		gbc_displayRegNegative.gridy = 0;
		panelRegisters.add(displayRegNegative, gbc_displayRegNegative);
		
		JLabel labelRegOverflow = new JLabel("Overflow:");
		labelRegOverflow.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegOverflow = new GridBagConstraints();
		gbc_labelRegOverflow.anchor = GridBagConstraints.WEST;
		gbc_labelRegOverflow.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegOverflow.gridx = 0;
		gbc_labelRegOverflow.gridy = 1;
		panelRegisters.add(labelRegOverflow, gbc_labelRegOverflow);
		
		JLabel displayRegOverflow = new JLabel("0");
		displayRegOverflow.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegOverflow = new GridBagConstraints();
		gbc_displayRegOverflow.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegOverflow.gridx = 1;
		gbc_displayRegOverflow.gridy = 1;
		panelRegisters.add(displayRegOverflow, gbc_displayRegOverflow);
		
		JLabel labelRegIgnored = new JLabel("Ignored:");
		labelRegIgnored.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegIgnored = new GridBagConstraints();
		gbc_labelRegIgnored.anchor = GridBagConstraints.WEST;
		gbc_labelRegIgnored.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegIgnored.gridx = 0;
		gbc_labelRegIgnored.gridy = 2;
		panelRegisters.add(labelRegIgnored, gbc_labelRegIgnored);
		
		JLabel displayRegIgnored = new JLabel("0");
		displayRegIgnored.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegIgnored = new GridBagConstraints();
		gbc_displayRegIgnored.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegIgnored.gridx = 1;
		gbc_displayRegIgnored.gridy = 2;
		panelRegisters.add(displayRegIgnored, gbc_displayRegIgnored);
		
		JLabel labelRegBreak = new JLabel("Break:");
		labelRegBreak.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegBreak = new GridBagConstraints();
		gbc_labelRegBreak.anchor = GridBagConstraints.WEST;
		gbc_labelRegBreak.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegBreak.gridx = 2;
		gbc_labelRegBreak.gridy = 0;
		panelRegisters.add(labelRegBreak, gbc_labelRegBreak);
		
		JLabel displayRegBreak = new JLabel("0");
		displayRegBreak.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegBreak = new GridBagConstraints();
		gbc_displayRegBreak.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegBreak.gridx = 3;
		gbc_displayRegBreak.gridy = 0;
		panelRegisters.add(displayRegBreak, gbc_displayRegBreak);
		
		JLabel labelRegDecimal = new JLabel("Decimal:");
		labelRegDecimal.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegDecimal = new GridBagConstraints();
		gbc_labelRegDecimal.anchor = GridBagConstraints.WEST;
		gbc_labelRegDecimal.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegDecimal.gridx = 2;
		gbc_labelRegDecimal.gridy = 1;
		panelRegisters.add(labelRegDecimal, gbc_labelRegDecimal);
		
		JLabel displayRegDecimal = new JLabel("0");
		displayRegDecimal.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegDecimal = new GridBagConstraints();
		gbc_displayRegDecimal.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegDecimal.gridx = 3;
		gbc_displayRegDecimal.gridy = 1;
		panelRegisters.add(displayRegDecimal, gbc_displayRegDecimal);
		
		JLabel labelRegInterrupt = new JLabel("Interrupt:");
		labelRegInterrupt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegInterrupt = new GridBagConstraints();
		gbc_labelRegInterrupt.anchor = GridBagConstraints.WEST;
		gbc_labelRegInterrupt.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegInterrupt.gridx = 2;
		gbc_labelRegInterrupt.gridy = 2;
		panelRegisters.add(labelRegInterrupt, gbc_labelRegInterrupt);
		
		JLabel displayRegInterrupt = new JLabel("0");
		displayRegInterrupt.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegInterrupt = new GridBagConstraints();
		gbc_displayRegInterrupt.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegInterrupt.gridx = 3;
		gbc_displayRegInterrupt.gridy = 2;
		panelRegisters.add(displayRegInterrupt, gbc_displayRegInterrupt);
		
		JLabel labelRegZero = new JLabel("Zero:");
		labelRegZero.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegZero = new GridBagConstraints();
		gbc_labelRegZero.anchor = GridBagConstraints.WEST;
		gbc_labelRegZero.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegZero.gridx = 4;
		gbc_labelRegZero.gridy = 0;
		panelRegisters.add(labelRegZero, gbc_labelRegZero);
		
		JLabel displayRegZero = new JLabel("0");
		displayRegZero.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegZero = new GridBagConstraints();
		gbc_displayRegZero.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegZero.gridx = 5;
		gbc_displayRegZero.gridy = 0;
		panelRegisters.add(displayRegZero, gbc_displayRegZero);
		
		JLabel labelRegCarry = new JLabel("Carry:");
		labelRegCarry.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegCarry = new GridBagConstraints();
		gbc_labelRegCarry.anchor = GridBagConstraints.WEST;
		gbc_labelRegCarry.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegCarry.gridx = 4;
		gbc_labelRegCarry.gridy = 1;
		panelRegisters.add(labelRegCarry, gbc_labelRegCarry);
		
		JLabel displayRegCarry = new JLabel("0");
		displayRegCarry.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegCarry = new GridBagConstraints();
		gbc_displayRegCarry.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegCarry.gridx = 5;
		gbc_displayRegCarry.gridy = 1;
		panelRegisters.add(displayRegCarry, gbc_displayRegCarry);
		
		JLabel labelRegCounter = new JLabel("Program Counter:");
		labelRegCounter.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegCounter = new GridBagConstraints();
		gbc_labelRegCounter.anchor = GridBagConstraints.WEST;
		gbc_labelRegCounter.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegCounter.gridx = 6;
		gbc_labelRegCounter.gridy = 0;
		panelRegisters.add(labelRegCounter, gbc_labelRegCounter);
		
		JLabel displayRegCounter = new JLabel("0");
		displayRegCounter.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegCounter = new GridBagConstraints();
		gbc_displayRegCounter.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegCounter.gridx = 7;
		gbc_displayRegCounter.gridy = 0;
		panelRegisters.add(displayRegCounter, gbc_displayRegCounter);
		
		JLabel labelRegAccumulator = new JLabel("Accumulator:");
		labelRegAccumulator.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegAccumulator = new GridBagConstraints();
		gbc_labelRegAccumulator.anchor = GridBagConstraints.WEST;
		gbc_labelRegAccumulator.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegAccumulator.gridx = 6;
		gbc_labelRegAccumulator.gridy = 1;
		panelRegisters.add(labelRegAccumulator, gbc_labelRegAccumulator);
		
		JLabel displayRegAccumulator = new JLabel("0");
		displayRegAccumulator.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegAccumulator = new GridBagConstraints();
		gbc_displayRegAccumulator.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegAccumulator.gridx = 7;
		gbc_displayRegAccumulator.gridy = 1;
		panelRegisters.add(displayRegAccumulator, gbc_displayRegAccumulator);
		
		JLabel labelRegStack = new JLabel("Stack Pointer:");
		labelRegStack.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegStack = new GridBagConstraints();
		gbc_labelRegStack.anchor = GridBagConstraints.WEST;
		gbc_labelRegStack.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegStack.gridx = 6;
		gbc_labelRegStack.gridy = 2;
		panelRegisters.add(labelRegStack, gbc_labelRegStack);
		
		JLabel displayRegStack = new JLabel("0");
		displayRegStack.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegStack = new GridBagConstraints();
		gbc_displayRegStack.insets = new Insets(0, 0, 5, 5);
		gbc_displayRegStack.gridx = 7;
		gbc_displayRegStack.gridy = 2;
		panelRegisters.add(displayRegStack, gbc_displayRegStack);
		
		JLabel labelRegX = new JLabel("X:");
		labelRegX.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegX = new GridBagConstraints();
		gbc_labelRegX.anchor = GridBagConstraints.WEST;
		gbc_labelRegX.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegX.gridx = 8;
		gbc_labelRegX.gridy = 0;
		panelRegisters.add(labelRegX, gbc_labelRegX);
		
		JLabel displayRegX = new JLabel("0");
		displayRegX.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegX = new GridBagConstraints();
		gbc_displayRegX.insets = new Insets(0, 0, 5, 0);
		gbc_displayRegX.gridx = 9;
		gbc_displayRegX.gridy = 0;
		panelRegisters.add(displayRegX, gbc_displayRegX);
		
		JLabel labelRegY = new JLabel("Y:");
		labelRegY.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelRegY = new GridBagConstraints();
		gbc_labelRegY.anchor = GridBagConstraints.WEST;
		gbc_labelRegY.insets = new Insets(0, 5, 5, 5);
		gbc_labelRegY.gridx = 8;
		gbc_labelRegY.gridy = 1;
		panelRegisters.add(labelRegY, gbc_labelRegY);
		
		JLabel displayRegY = new JLabel("0");
		displayRegY.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displayRegY = new GridBagConstraints();
		gbc_displayRegY.insets = new Insets(0, 0, 5, 0);
		gbc_displayRegY.gridx = 9;
		gbc_displayRegY.gridy = 1;
		panelRegisters.add(displayRegY, gbc_displayRegY);
		
		JLabel displaySpacer01 = new JLabel("        ");
		displaySpacer01.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displaySpacer01 = new GridBagConstraints();
		gbc_displaySpacer01.insets = new Insets(0, 0, 0, 5);
		gbc_displaySpacer01.gridx = 1;
		gbc_displaySpacer01.gridy = 3;
		panelRegisters.add(displaySpacer01, gbc_displaySpacer01);
		
		JLabel displaySpacer02 = new JLabel("        ");
		displaySpacer02.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displaySpacer02 = new GridBagConstraints();
		gbc_displaySpacer02.insets = new Insets(0, 0, 0, 5);
		gbc_displaySpacer02.gridx = 3;
		gbc_displaySpacer02.gridy = 3;
		panelRegisters.add(displaySpacer02, gbc_displaySpacer02);
		
		JLabel displaySpacer03 = new JLabel("        ");
		displaySpacer03.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displaySpacer03 = new GridBagConstraints();
		gbc_displaySpacer03.insets = new Insets(0, 0, 0, 5);
		gbc_displaySpacer03.gridx = 5;
		gbc_displaySpacer03.gridy = 3;
		panelRegisters.add(displaySpacer03, gbc_displaySpacer03);
		
		JLabel displaySpacer04 = new JLabel("        ");
		displaySpacer04.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displaySpacer04 = new GridBagConstraints();
		gbc_displaySpacer04.insets = new Insets(0, 0, 0, 5);
		gbc_displaySpacer04.gridx = 7;
		gbc_displaySpacer04.gridy = 3;
		panelRegisters.add(displaySpacer04, gbc_displaySpacer04);
		
		JLabel displaySpacer05 = new JLabel("        ");
		displaySpacer05.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_displaySpacer05 = new GridBagConstraints();
		gbc_displaySpacer05.gridx = 9;
		gbc_displaySpacer05.gridy = 3;
		panelRegisters.add(displaySpacer05, gbc_displaySpacer05);
		
		JPanel panelButtons = new JPanel();
		GridBagConstraints gbc_panelButtons = new GridBagConstraints();
		gbc_panelButtons.insets = new Insets(0, 5, 0, 5);
		gbc_panelButtons.fill = GridBagConstraints.BOTH;
		gbc_panelButtons.gridx = 0;
		gbc_panelButtons.gridy = 2;
		panelMain.add(panelButtons, gbc_panelButtons);
		GridBagLayout gbl_panelButtons = new GridBagLayout();
		gbl_panelButtons.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelButtons.rowHeights = new int[]{0, 0};
		gbl_panelButtons.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelButtons.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelButtons.setLayout(gbl_panelButtons);
		
		JButton buttonRun = new JButton("Run");
		buttonRun.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_buttonRun = new GridBagConstraints();
		gbc_buttonRun.insets = new Insets(0, 0, 0, 5);
		gbc_buttonRun.gridx = 0;
		gbc_buttonRun.gridy = 0;
		buttonRun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				buttonRunAction();
			}
		});
		panelButtons.add(buttonRun, gbc_buttonRun);
		
		JButton buttonBreak = new JButton("Break");
		buttonBreak.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_buttonBreak = new GridBagConstraints();
		gbc_buttonBreak.insets = new Insets(0, 0, 0, 5);
		gbc_buttonBreak.gridx = 1;
		gbc_buttonBreak.gridy = 0;
		buttonBreak.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				buttonBreakAction();
			}
		});
		panelButtons.add(buttonBreak, gbc_buttonBreak);
		
		JButton buttonStep = new JButton("Step");
		buttonStep.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_buttonStep = new GridBagConstraints();
		gbc_buttonStep.insets = new Insets(0, 0, 0, 5);
		gbc_buttonStep.gridx = 2;
		gbc_buttonStep.gridy = 0;
		buttonStep.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				buttonStepAction();
			}
		});
		panelButtons.add(buttonStep, gbc_buttonStep);
		
		JPanel panelExecutionSpeed = new JPanel();
		GridBagConstraints gbc_panelExecutionSpeed = new GridBagConstraints();
		gbc_panelExecutionSpeed.insets = new Insets(0, 0, 0, 5);
		gbc_panelExecutionSpeed.fill = GridBagConstraints.BOTH;
		gbc_panelExecutionSpeed.gridx = 3;
		gbc_panelExecutionSpeed.gridy = 0;
		panelButtons.add(panelExecutionSpeed, gbc_panelExecutionSpeed);
		GridBagLayout gbl_panelExecutionSpeed = new GridBagLayout();
		gbl_panelExecutionSpeed.columnWidths = new int[]{0, 0};
		gbl_panelExecutionSpeed.rowHeights = new int[]{0, 0, 0};
		gbl_panelExecutionSpeed.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelExecutionSpeed.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelExecutionSpeed.setLayout(gbl_panelExecutionSpeed);
	
		JLabel labelExecutionSpeed = new JLabel("Execution Speed:");
		labelExecutionSpeed.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_labelExecutionSpeed = new GridBagConstraints();
		gbc_labelExecutionSpeed.insets = new Insets(0, 5, 0, 0);
		gbc_labelExecutionSpeed.anchor = GridBagConstraints.WEST;
		gbc_labelExecutionSpeed.gridx = 0;
		gbc_labelExecutionSpeed.gridy = 0;
		panelExecutionSpeed.add(labelExecutionSpeed, gbc_labelExecutionSpeed);
	
		sliderExecutionDelay = new JSlider();
		sliderExecutionDelay.setMajorTickSpacing(10);
		sliderExecutionDelay.setSnapToTicks(true);
		sliderExecutionDelay.setToolTipText("Moving the slider to the left makes the instructions execute more slowly. Moving the slider to the right makes the instructions execute more rapidly.");
		GridBagConstraints gbc_sliderExecutionSpeed = new GridBagConstraints();
		gbc_sliderExecutionSpeed.insets = new Insets(5, 0, 0, 0);
		gbc_sliderExecutionSpeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderExecutionSpeed.gridx = 0;
		gbc_sliderExecutionSpeed.gridy = 1;
		sliderExecutionDelay.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				sliderExecutionDelayAction();
			}
		});
		panelExecutionSpeed.add(sliderExecutionDelay, gbc_sliderExecutionSpeed);
	
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 10 ), new JLabel("Slow") );
		labelTable.put( new Integer( 90 ), new JLabel("Fast") );
		sliderExecutionDelay.setLabelTable( labelTable );
		sliderExecutionDelay.setPaintLabels(true);
	
		JButton buttonReset = new JButton("Reset");
		buttonReset.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GridBagConstraints gbc_buttonReset = new GridBagConstraints();
		gbc_buttonReset.gridx = 4;
		gbc_buttonReset.gridy = 0;
		buttonReset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				buttonResetAction();
			}
		});
		panelButtons.add(buttonReset, gbc_buttonReset);
		
		JPanel panelConsoleHeading = new JPanel();
		GridBagConstraints gbc_panelConsoleHeading = new GridBagConstraints();
		gbc_panelConsoleHeading.insets = new Insets(0, 5, 5, 5);
		gbc_panelConsoleHeading.fill = GridBagConstraints.BOTH;
		gbc_panelConsoleHeading.gridx = 0;
		gbc_panelConsoleHeading.gridy = 3;
		panelMain.add(panelConsoleHeading, gbc_panelConsoleHeading);
		GridBagLayout gbl_panelConsoleHeading = new GridBagLayout();
		gbl_panelConsoleHeading.columnWidths = new int[]{0, 0};
		gbl_panelConsoleHeading.rowHeights = new int[]{0, 0};
		gbl_panelConsoleHeading.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelConsoleHeading.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelConsoleHeading.setLayout(gbl_panelConsoleHeading);
		
		JLabel labelConsole = new JLabel("Console:");
		labelConsole.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_labelConsole = new GridBagConstraints();
		gbc_labelConsole.gridx = 0;
		gbc_labelConsole.gridy = 0;
		panelConsoleHeading.add(labelConsole, gbc_labelConsole);
		
		JPanel panelConsole = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 4;
		panelMain.add(panelConsole, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelConsole.setLayout(gbl_panel_5);
		
		textPaneConsole = new JTextPane();
		textPaneConsole.setFont(UIManager.getFont("TextArea.font"));
		GridBagConstraints gbc_textPaneConsole = new GridBagConstraints();
		gbc_textPaneConsole.fill = GridBagConstraints.BOTH;
		gbc_textPaneConsole.gridx = 0;
		gbc_textPaneConsole.gridy = 0;
		panelConsole.add(textPaneConsole, gbc_textPaneConsole);
	
	    memoryWindow = new MemoryWindow();
	    traceLogWindow = new TraceLogWindow();
	}
	
	public void mntmOpenFileAction()
	{
		statusBar.setMessage("Open File");
		
		int retval = fileChooser.showOpenDialog(this);

		if (retval == JFileChooser.APPROVE_OPTION) {
		    // The user selected a file, get it, use it.
		    File file = fileChooser.getSelectedFile();
		
		    // Update user interface.
			statusBar.setMessage("Opening " + file.getName());

			try {
				this.assembler.OpenFile(file);
				this.assembler.Assemble();
				prog = new Program(this.assembler.getOrigin());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void mntmRunAction() 
	{
		statusBar.setMessage("Run");

		prog.executeProgram(this.sliderExecutionDelayValue);
	}
	
	public void mntmResetAction()
	{
		statusBar.setMessage("Reset");

		// Set the counter back to the first instruction
		prog.resetCounter();
		
		// XXX reset windows
	}
	
	public void mntmExitAction()
	{
		System.exit(0);
	}
	
	public void mntmMemoryAction()
	{
		memoryWindow.setLocation(x+w, y);

		if (memoryWindow.isVisible()) 
		{
			memoryWindow.setVisible(false);
	    	statusBar.setMessage("Hiding memory window");
	    	//MemoryWindow.textPaneMemory.setText("Hiding memory window");
		}
		else 
		{
			memoryWindow.setVisible(true);
	    	statusBar.setMessage("Showing memory window");
	    	//MemoryWindow.textPaneMemory.setText("Showing memory window");
		}
	}
	
	public void mntmTraceLogAction()
	{
		traceLogWindow.setLocation(x-320, y);

		if (traceLogWindow.isVisible()) 
		{
			traceLogWindow.setVisible(false);
	    	statusBar.setMessage("Hiding trace log window");
	    	//TraceLogWindow.textPaneTraceLog.setText("Hiding trace log window");
		}
		else 
		{
			traceLogWindow.setVisible(true);
	    	statusBar.setMessage("Showing trace log window");
	    	//TraceLogWindow.textPaneTraceLog.setText("Showing trace log window");
		}
	}
	
	public void mntmAboutEmulatorAction()
	{
		statusBar.setMessage("About 6502 Emulator");

		// XXX do work
	}
	
	public void buttonRunAction()
	{
		// use the menu item action
		mntmRunAction();
	}
	
	public void buttonBreakAction()
	{
		statusBar.setMessage("Break");

		// Cancel program execution
		prog.cancel();
	}
	
	public void buttonStepAction()
	{
		statusBar.setMessage("Step");

		prog.executeProgramStep();
	}
	
	public void buttonResetAction()
	{
		// use the menu item action
		mntmResetAction();
	}
	
	public void sliderExecutionDelayAction()
	{
		int rawDelayValue = sliderExecutionDelay.getValue();
		int delayValue = 100 - rawDelayValue;

		if (delayValue > sliderExecutionDelayValue)
		{
			statusBar.setMessage("Increasing execution delay to " + delayValue);
		}
		else 
		{
			statusBar.setMessage("Decreasing execution delay to " + delayValue);
		}

		sliderExecutionDelayValue = delayValue;

		// XXX do work
	}
}