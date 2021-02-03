package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import common.CommonData;
import common.EntityEditor_Helper;

public class PartBinaryLine extends PartBinaryBasic {
	
	public String messageText=null;
	public double resistance;
	
	public PartBinaryLine(int x, int y, PartNode nodeFrom, PartNode nodeTo) {
		super(x, y, PartBinaryBasic.SIZE_WIDTH_STANDART, (PartBinaryBasic.SIZE_HEIGHT_STANDART), nodeFrom, nodeTo);
	}
	public void computeCoordinates() {
		super.computeCoordinates();
	}

	public void drawWithColor(Color color) {

		color=(Color.BLACK);
		if(amperagefrom>=2)color=(Color.BLUE);
		if(amperagefrom>=4)color=(Color.RED);
		if(amperagefrom>=6)color=(Color.MAGENTA);
		g.drawLine((x0+x1)>>1, (y0+y1)>>1, nodeFrom.x, nodeFrom.y);
		if(voltageDifference>=2)color=(Color.BLUE);
		if(voltageDifference>=4)color=(Color.RED);
		if(voltageDifference>=6)color=(Color.MAGENTA);
		g.drawLine((x0+x1)>>1, (y0+y1)>>1, nodeTo.x, nodeTo.y);
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
