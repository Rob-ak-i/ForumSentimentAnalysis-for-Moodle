package gui;

import javax.swing.JComboBox;

public class JComboBoxExt extends JComboBox<String> {
	/**random*/
	private static final long serialVersionUID = 520710680942711020L;
	public String getCaption() {
		return (String) this.selectedItemReminder;
	}

}
