/**
 * TraceLogWindow.java
 *
 * Creates window to display the trace log.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class TraceLogWindow extends JFrame
{
	public static JTextPane textPaneTraceLog;

	public TraceLogWindow() 
	{
		setSize(320, 320);
		setVisible(false);
		setTitle("Trace Log");
	    setLocationRelativeTo(null);
	    
	    JPanel panelTraceLog = new JPanel();
	    getContentPane().add(panelTraceLog, BorderLayout.CENTER);
	    GridBagLayout gbl_panelTraceLog = new GridBagLayout();
	    gbl_panelTraceLog.columnWidths = new int[]{0, 0};
	    gbl_panelTraceLog.rowHeights = new int[]{0, 0};
	    gbl_panelTraceLog.columnWeights = new double[]{1.0, Double.MIN_VALUE};
	    gbl_panelTraceLog.rowWeights = new double[]{1.0, Double.MIN_VALUE};
	    panelTraceLog.setLayout(gbl_panelTraceLog);
	    
	    textPaneTraceLog = new JTextPane();
	    textPaneTraceLog.setEditable(false);
	    textPaneTraceLog.setFont(UIManager.getFont("TextArea.font"));
	    GridBagConstraints gbc_textPaneTraceLog = new GridBagConstraints();
	    gbc_textPaneTraceLog.fill = GridBagConstraints.BOTH;
	    gbc_textPaneTraceLog.gridx = 0;
	    gbc_textPaneTraceLog.gridy = 0;
	    panelTraceLog.add(textPaneTraceLog, gbc_textPaneTraceLog);
	}
}
