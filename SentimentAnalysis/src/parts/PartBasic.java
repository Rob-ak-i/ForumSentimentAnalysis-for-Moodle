package parts;

import util.Colors;
import util.GenericObject;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

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
	protected String name;
	protected HashMap<String,String> properties;
	public static double screenWidthHalf=400;
	public static double screenHeightHalf=300;
	public PartBasic(int x, int y, int w, int h) {
		this.x=x;
		this.y=y;
		width=w;xr=(w>>>1);
		height=h;yr=(h>>>1);
		//partIndex=0;
		name="";
		properties=null;//
	}
	public void free() {
		name=null;
		properties.clear();
		properties=null;
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
		g.drawString(name, x+(int)4, y+(int)2);
	}
	public String getName() {return name;}
	public void setName(String value) {name=value;}
	public void setProperty(String fieldName, String fieldCaption) {
		if(properties==null)properties=new HashMap<String,String>();
		properties.put(fieldName, fieldCaption);
	}
	public String getProperty(String fieldName) {
		return properties.get(fieldName);
	}
	public String getProperties() {
		if(properties==null)return "";
		return properties.toString();		
	}
	public void clearProperties() {
		if(properties!=null)
			properties.clear();		
	}
	/**value format:"key:value;key:value;...;key:value;"*/
	public void setProperties(String value) {
		if(properties==null)properties=new HashMap<String, String>();
		if(value.charAt(value.length()-1)=='}')value=value.substring(0, value.length()-1);
		if(value.charAt(0)=='{')value=value.substring(1, value.length());
		if(value.charAt(value.length()-1)!=';')value=value.concat(";");
		int l=value.length();
		char c;
		StringBuilder sb=new StringBuilder();
		String k=null,v=null;boolean isFieldNameRead=true;
		for(int i=0;i<l;++i) {
			c=value.charAt(i);
			switch(c) {
			case ':':case '=':
				k=(sb.toString());
				sb=new StringBuilder();
				isFieldNameRead=false;
				break;
			case ';':case ',':
				v=sb.toString();
				properties.put(k, v);
				sb=new StringBuilder();
				isFieldNameRead=true;
				break;
			default:
				if(isFieldNameRead&&c==' ')break;//имя переменной не должно содержать пробелов
				sb.append(c);
			}
		}
	}
}
