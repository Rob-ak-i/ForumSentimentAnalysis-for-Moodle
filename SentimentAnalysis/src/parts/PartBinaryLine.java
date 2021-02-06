package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import common.CommonData;
import common.MatrixHelper;

public class PartBinaryLine extends PartBinaryBasic {
	
	public String messageText=null;
	public double resistance;
	
	public PartBinaryLine(int x, int y, PartNode nodeFrom, PartNode nodeTo) {
		super(x, y, PartBinaryBasic.SIZE_WIDTH_STANDART, (PartBinaryBasic.SIZE_HEIGHT_STANDART), nodeFrom, nodeTo);
	}
	public void computeCoordinates() {
		super.computeCoordinates();
	}

	public void drawWithColor(Color color, double camposx,double camposy,double scalex, double scaley) {

		int x0=(int)(((double)nodeFrom.x-camposx)*scalex+screenWidthHalf);
		int y0=(int)(((double)nodeFrom.y-camposy)*scaley+screenHeightHalf);
		int x1=(int)(((double)nodeTo.x-camposx)*scalex+screenWidthHalf);
		int y1=(int)(((double)nodeTo.y-camposy)*scaley+screenHeightHalf);
		int x=(int)(((double)this.x-camposx)*scalex+screenWidthHalf);
		int y=(int)(((double)this.y-camposy)*scaley+screenHeightHalf);
		
		color=(Color.BLACK);
		if(amperagefrom>=2)color=(Color.BLUE);
		if(amperagefrom>=4)color=(Color.RED);
		if(amperagefrom>=6)color=(Color.MAGENTA);
		g.setColor(color);
		g.drawLine((x0+x1)>>>1, (y0+y1)>>>1, x0,y0);
		if(voltageDifference>=2)color=(Color.BLUE);
		if(voltageDifference>=4)color=(Color.RED);
		if(voltageDifference>=6)color=(Color.MAGENTA);
		g.setColor(color);
		g.drawLine((x0+x1)>>>1, (y0+y1)>>>1, x1,y1);
	}
	
	public String getCaption() {
		return Double.toString(amperagefrom);
	}
	public void setCaption(double value) {
		resistance=value;
	}
	@Override
	public void setCaption(String value) {
		// TODO Auto-generated method stub
		resistance=0.5;
	}
	

}
