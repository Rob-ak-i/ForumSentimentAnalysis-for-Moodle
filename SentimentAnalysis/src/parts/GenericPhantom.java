package parts;

import java.awt.Color;
import java.awt.Graphics;

public class GenericPhantom {
	public static int phantomType=0;
	public static final int phantomType_FORUM=1;
	public static final int phantomType_DISABLED=0;
	public static String name="";
	public static String caption="";
	public static int xi=0;
	public static int yi=0;
	public static void flush(){
		name="";
		caption="";
		phantomType=phantomType_DISABLED;
	}
	public static void draw(Graphics g){
		g.setColor(Color.gray);
		switch(phantomType){
		case phantomType_FORUM:
			g.drawLine(xi-30, yi-30, xi+30, yi+30);
			g.drawOval(xi-15, yi-15, 30, 30);
			g.drawString(name, xi+3, yi-6);
			g.drawString(caption, xi+3, yi+6);
			break;
		}
		
	}
}
