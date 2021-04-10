package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import common.CommonData;

public class PartBinaryRelation extends PartBinaryBasic {
	
	private int d0x,d0y,d1x,d1y,d2x,d2y,d3x,d3y,d4x,d4y,d5x,d5y;
	
	public double resistance;
	
	public PartBinaryRelation(int x, int y, PartNode nodeFrom, PartNode nodeTo) {
		super(x, y, PartBinaryBasic.SIZE_WIDTH_STANDART, (PartBinaryBasic.SIZE_HEIGHT_STANDART), nodeFrom, nodeTo);
	}
	public void computeCoordinates() {
		super.computeCoordinates();
		d0x=(int)(ax+(bx/6.)+cx/3.);
		d0y=(int)(ay+(by/6.)+cy/3.);

		d1x=(int)(ax+(bx*5./6.)+cx/3.);
		d1y=(int)(ay+(by*5./6.)+cy/3.);

		d2x=(int)(ax+(bx/6.)-cx/3.);
		d2y=(int)(ay+(by/6.)-cy/3.);

		d3x=(int)(ax+(bx*5./6.)-cx/3.);
		d3y=(int)(ay+(by*5./6.)-cy/3.);

		d4x=(int)(ax+(bx/6.));
		d4y=(int)(ay+(by/6.));
		
		d5x=(int)(ax+(bx*5./6.));
		d5y=(int)(ay+(by*5./6.));
	}
/*
	public void drawWithColor(Color color) {
		super.drawWithColor(color);
		//g.setColor(color);-||-
		g.drawLine(x0, y0, d4x, d4y);
		g.drawLine(x1, y1, d5x, d5y);
		g.drawLine(d0x, d0y, d2x, d2y);
		g.drawLine(d1x, d1y, d3x, d3y);
		g.drawLine(d0x, d0y, d1x, d1y);
		g.drawLine(d2x, d2y, d3x, d3y);
	}
	*/
	

}
