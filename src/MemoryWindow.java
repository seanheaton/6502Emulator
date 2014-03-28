/**
 * MemoryWindow.java
 *
 * Creates window to display the emulated memory.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class MemoryWindow extends JFrame
{
	public static JTextPane textPaneMemory;

	public MemoryWindow() 
	{
		setSize(320, 320);
		setVisible(false);
		setTitle("Memory");
	    setLocationRelativeTo(null);
	    
	    JPanel panelMemory = new JPanel();
	    getContentPane().add(panelMemory, BorderLayout.CENTER);
	    GridBagLayout gbl_panelMemory = new GridBagLayout();
	    gbl_panelMemory.columnWidths = new int[]{0, 0};
	    gbl_panelMemory.rowHeights = new int[]{0, 0};
	    gbl_panelMemory.columnWeights = new double[]{1.0, Double.MIN_VALUE};
	    gbl_panelMemory.rowWeights = new double[]{1.0, Double.MIN_VALUE};
	    panelMemory.setLayout(gbl_panelMemory);
	    
	    textPaneMemory = new JTextPane();
	    textPaneMemory.setEditable(false);
	    textPaneMemory.setFont(UIManager.getFont("TextArea.font"));
	    GridBagConstraints gbc_textPaneMemory = new GridBagConstraints();
	    gbc_textPaneMemory.fill = GridBagConstraints.BOTH;
	    gbc_textPaneMemory.gridx = 0;
	    gbc_textPaneMemory.gridy = 0;
	    panelMemory.add(textPaneMemory, gbc_textPaneMemory);
	}
}
