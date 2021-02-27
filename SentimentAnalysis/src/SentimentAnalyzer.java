import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.SwingUtilities;

import common.CommonData;
import common.Lang;
import gui.GUI;

public class SentimentAnalyzer {

	public static void main(String[] args) {
		common.Settings.initialize();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI();
			}
		});
	}
}
