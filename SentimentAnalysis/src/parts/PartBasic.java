package parts;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import common.CommonData;
import common.EntityEditor_Helper;
import common.MatrixHelper;

public abstract class PartBasic {
	public static int SIZE_WIDTH_STANDART;
	public static int SIZE_HEIGHT_STANDART;
	//public static SchematicManager schematicManager = new SchematicManager();
	public int index;
	public int selected=0;//1 - selected//2 -sub//3-sub2 etc..
	public boolean isMainCaptionKnown=false;
	
	
	public static Graphics g = EntityEditor_Helper.graphics;
	
	
	protected int x,y;public int getPosX() {return x;}public int getPosY() {return y;}public void setPos(int newX, int newY) {x=newX;y=newY;}
	protected int xr,yr,width,height;
	protected double ax,ay,bx,by,cx,cy;
	//public int partIndex;
	public String partName;
	public PartBasic(int x, int y, int w, int h) {
		this.x=x;
		this.y=y;
		width=w;xr=(w>>>1);
		height=h;yr=(h>>>1);
		//partIndex=0;
		partName="";
	}
	public void draw() {
		drawWithColor(EntityEditor_Helper.getColor(selected));
		if(EntityEditor_Helper.isShowPartNamesAlways==0)drawName(EntityEditor_Helper.getTextColor(selected));
	}
	/*
	public void drawSelected() {
		drawWithColor(EntityEditor_Helper.colorSelected);
		if(EntityEditor_Helper.isShowPartNamesAlways==0)drawName(EntityEditor_Helper.colorSelected);
	}
	*/
	public void drawOnMap(int[][] map, int caption) {
		MatrixHelper.fillZone(map, x-xr, y-yr, x+xr, y+yr, caption);
	}
	public void drawWithColor(Color color) {
		g.setColor(color);
		g.fillOval(x-xr, y-yr, x+xr, y+yr);
	}
	public void drawName(Color colorText) {
		g.setColor(colorText);
		double sx=cx,sy=cy;if(sy>0) {sx=-sx;sy=-sy;}
		sx*=1.5;sy*=1.5;
		g.drawString(partName, x+(int)sx, y+(int)sy);
	}

	public String getCaption() {
		return "0";		
	}
	public abstract void setCaption(String value);
	public void setCaption(double value) {
		setCaption(Double.toString(value));
	}
	
	public String getAdditionalData() {
		return null;		
	}
	public void setAdditionalData(String value) {
				
	}
}
