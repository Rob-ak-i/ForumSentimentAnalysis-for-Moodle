package gui;

import javax.swing.JFrame;

public class GUIElementViewer extends JFrame {
	/**was set by random seed*/
	private static final long serialVersionUID = -3713450632567079274L;
	public GUIElementViewer() {
		super(common.Lang.InnerTable.GUI.GUIElementViewerName);
		this.setSize(300, 500);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(false);
		this.setFocusable(true);
		this.setBounds(100,100,800,600);
	}
	
	
}
