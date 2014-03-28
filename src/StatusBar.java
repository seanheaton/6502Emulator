/**
 * StatusBar.java
 *
 * Extends JLabel to create a status bar at the bottom of an application window
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class StatusBar extends JLabel
{
	/** 
	 * Creates a new instance of StatusBar 
	 */
	public StatusBar()
	{
		super();
		super.setPreferredSize(new Dimension(100, 16));
		
		// initialize border styles
		// 
		Border blackline, raisedetched, loweredetched, raisedbevel, loweredbevel, empty;
		blackline = BorderFactory.createLineBorder(Color.black);
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		empty = BorderFactory.createEmptyBorder();
	
		super.setBorder(loweredbevel);
	
		setMessage("Ready");
	}
	
	/**
	 * Updates StatusBar text 
	 */
	public void setMessage(String message)
	{
		setText(" " + message);
	}
}