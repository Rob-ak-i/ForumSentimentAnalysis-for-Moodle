package gui;

import javax.swing.JComboBox;

public class JComboBoxExt extends JComboBox<String> {
	private static final long serialVersionUID = 520710680942711020L;/**random*/
	public String getCaption() {
		return (String) this.selectedItemReminder;
	}

}
