package parts;

import javax.imageio.ImageIO;

import util.Colors;
import util.Dot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import common.CommonData;
import common.EntityRenderer;
import common.MatrixHelper;

public abstract class PartBasic {
	public static int SIZE_WIDTH_STANDART;
	public static int SIZE_HEIGHT_STANDART;
	//public static SchematicManager schematicManager = new SchematicManager();
	public int index;
	public int selected=0;//1 - selected//2 -sub//3-sub2 etc..
	public boolean isMainCaptionKnown=false;
	
	
	public static Graphics g = null;//CommonData.graphics;
	
	
	protected int x,y;public int getPosX() {return x;}public int getPosY() {return y;}public void setPos(int newX, int newY) {x=newX;y=newY;}
	protected int xr,yr,width,height;
	protected double ax,ay,bx,by,cx,cy;
	//public int partIndex;
	public String partName;
	public static double screenWidthHalf=400;
	public static double screenHeightHalf=300;
	public PartBasic(int x, int y, int w, int h) {
		this.x=x;
		this.y=y;
		width=w;xr=(w>>>1);
		height=h;yr=(h>>>1);
		//partIndex=0;
		partName="";
	}
	/*
	public void draw() {
		drawWithColor(EntityEditor_Helper.getColor(selected),x,y,CommonData.WIDTH,CommonData.HEIGHT);
		if(EntityEditor_Helper.isShowPartNamesAlways==0)drawName(EntityEditor_Helper.getTextColor(selected));
	}
	*/
	public void draw(double camposx,double camposy,double scalex, double scaley) {
		drawWithColor(Colors.getColor(selected),camposx,camposy,scalex,scaley);
		if(EntityRenderer.isShowPartNamesAlways==0)drawName(Colors.getTextColor(selected),camposx,camposy,scalex,scaley);
	}
	/*
	public void drawSelected() {
		drawWithColor(EntityEditor_Helper.colorSelected);
		if(EntityEditor_Helper.isShowPartNamesAlways==0)drawName(EntityEditor_Helper.colorSelected);
	}
	*/
	public void drawOnMap(int[][] map, int caption, double camposx,double camposy,double scalex, double scaley) {
		//Dot scrPos = EntityRenderer.getImageDotFromMathCoords(x, y); - not corectly work during to asynchronous updates of campos
		int x=(int)(((double)this.x-camposx)*scalex+screenWidthHalf);
		int y=(int)(((double)this.y-camposy)*scaley+screenHeightHalf);
		MatrixHelper.fillZone(map, (x-xr), (y-yr), (x+xr), (y+yr), caption);
	}
	public void drawWithColor(Color color, double camposx,double camposy,double scalex, double scaley) {
		g.setColor(color);
		int x=(int)(((double)this.x-camposx)*scalex+screenWidthHalf);
		int y=(int)(((double)this.y-camposy)*scaley+screenHeightHalf);
		g.fillOval(x-xr, y-yr, x+xr, y+yr);
	}
	public void drawName(Color colorText, double camposx,double camposy,double scalex, double scaley) {
		g.setColor(colorText);
		double sx=cx,sy=cy;if(sy>0) {sx=-sx;sy=-sy;}
		sx*=1.5;sy*=1.5;

		int x=(int)(((double)this.x-camposx)*scalex+screenWidthHalf);
		int y=(int)(((double)this.y-camposy)*scaley+screenHeightHalf);
		g.drawString(partName, x+(int)4, y+(int)2);
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
