package parts;

import java.awt.Color;
import java.awt.Graphics;

public class GenericPhantomRectangle {
	public static int x0=0;
	public static int y0=0;
	public static int x1=0;
	public static int y1=0;
	public static void flush(){
	}
	public static void draw(Graphics g){
		g.setColor(Color.gray);
		g.drawLine(x0,y0,x1,y0);
		g.drawLine(x0,y1,x1,y1);
		g.drawLine(x0,y0,x0,y1);
		g.drawLine(x1,y0,x1,y1);
	}

}
