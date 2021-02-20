package util;

import java.awt.Color;

public class Colors {

	/**Color*/
	public static Color colorMain=Color.BLACK;
	public static Color colorSelected=Color.red;
	public static Color colorSelectedSub=Color.blue;
	public static Color colorSelectedSub1=Color.yellow;
	public static Color getColor(int index) {
		switch(index) {
		case 0:	return colorMain;
		case 1:	return colorSelected;
		case 2:	return colorSelectedSub;
		case 3:	return colorSelectedSub1;
		}
		return Color.gray;
	};
	public static Color getTextColor(int index) {
		switch(index) {
		case 0:	return Color.gray;
		case 1:	return Color.magenta;
		case 2:	return Color.cyan;
		case 3:	return Color.orange;
		}
		return Color.gray;
	};

	public static Color colorBackground=Color.white;
	public static Color colorText=Color.BLACK;
	private static java.awt.Color[] colors = {Color.red,Color.orange,Color.yellow,Color.green,Color.cyan,Color.blue,Color.magenta};
	public static Color colorRandomPallete() {return colors[(int)(Math.random()*(double)colors.length)];	}
	public static Color colorRandom() {return new Color((int)(Math.random()*16777215.));	}
	public static Color color(int rgb) {return new Color((int)(rgb));	}
}
