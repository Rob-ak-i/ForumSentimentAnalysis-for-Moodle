import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.SwingUtilities;

import common.CommonData;
import common.Lang;

public class SentimentAnalyser {

	public static void main(String[] args) {
		common.Settings.initialize();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GUI();
			}
		});
	}
}
