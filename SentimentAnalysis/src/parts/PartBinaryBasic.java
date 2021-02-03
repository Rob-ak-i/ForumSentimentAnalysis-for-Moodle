package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import common.CommonData;
import common.EntityEditor_Helper;

public abstract class PartBinaryBasic extends PartBasic{
	public double amperagefrom;
	public boolean isCurrentKnown=false;
	public double voltageDifference;
	
	private boolean coordinatesExtraWidthChanged=true;
	private static int additionalWidth=30;public void additionalWidthIncrease() {additionalWidth++;coordinatesExtraWidthChanged=true;}public void additionalWidthDecrease() {if(additionalWidth>0)additionalWidth--;coordinatesExtraWidthChanged=true;}public int additionalWidthGet() {return additionalWidth;}
	
	
	public static String[] partsList = {
			common.Lang.InnerTable.Misc.part_0, 
			common.Lang.InnerTable.Misc.part_1,
			common.Lang.InnerTable.Misc.part_2
			};
	public static final Class[] classesList = {
			PartBinaryLine.class, 
			PartBinaryRelation.class,
			PartBinaryWords.class
			};
	public static int classesListGetIndexOf(String str) {
		for(int i=0;i<classesList.length;++i)
			if(str.compareTo(classesList[i].getSimpleName())==0)return i;
		return -1;
	}
	public static int SIZE_WIDTH_STANDART=30;
	public static int SIZE_HEIGHT_STANDART=30;
	
	public static final int DIRECTION_BACKWARD=-1;
	public static final int DIRECTION_BIDIRECTIONAL=0;
	public static final int DIRECTION_FORWARD=1;
	
	//protected static Image img;
	
	protected int direction;
	protected PartNode nodeFrom, nodeTo;
	public PartNode getNodeFrom() {return nodeFrom;}public PartNode getNodeTo() {return nodeTo;}
	
	protected double dx,dy,len;
	protected int x0,y0,x1,y1;public void setPos(int newX, int newY) {super.setPos(newX, newY);computeCoordinates();}
	public PartBinaryBasic(int x, int y, int w, int h, PartNode nodeFrom, PartNode nodeTo) {
		super(x, y, w, h);
		//this.img=img;
		
		this.nodeFrom=nodeFrom;
		nodeFrom.addPart();
		this.nodeTo=nodeTo;
		nodeTo.addPart();
		computeCoordinates();
	}
	/**���������� ��� ������������� � ����������� �����*/
	public void computeCoordinates() {
		int x0=nodeFrom.x,y0=nodeFrom.y,x1=nodeTo.x,y1=nodeTo.y;
		//x=((x0+x1)>>>1);y=((y0+y1)>>>1);
		dx=x1-x0;dy=y1-y0;
		len = Math.sqrt(dx*dx+dy*dy);
		dx=dx*((double)width)/len;
		dy=dy*((double)width)/len;

		this.x0=(int) Math.round(x-dx*0.5);
		this.y0=(int) Math.round(y-dy*0.5);
		this.x1=(int) Math.round(x+dx*0.5);
		this.y1=(int) Math.round(y+dy*0.5);
		
		ax=this.x0;ay=this.y0;
		bx=this.x1-ax;by=this.y1-ay;
		cx=(-by*((double)height))/(((double)width)*2.);
		cy=(bx*((double)height))/(((double)width)*2.);
		
		coordinatesExtraWidthChanged=true;
		computeCoordinatesOfExtraWidthDots();
	}
	public void changeDirection() {
		direction*=-1;
		int temp;temp=x0;x0=x1;x1=temp;temp=y0;y0=y1;y1=temp;
		PartNode nodeTemp;nodeTemp=nodeTo;nodeTo=nodeFrom;nodeFrom=nodeTemp;
		computeCoordinates();
	}
	public void drawWithColor(Color color) {
		g.setColor(color);
		/*
		int medx=((x0+x1)>>>1),medy=((y0+y1)>>>1);
		g.drawRect(medx-xr, medy-xr, medx+xr, medy+yr);
		*/
		//draw wires to nodes
		if(additionalWidth>3) {
			if(coordinatesExtraWidthChanged)computeCoordinatesOfExtraWidthDots();
			g.drawLine(ewx0, ewy0, nodeFrom.x, nodeFrom.y);
			g.drawLine(ewx1, ewy1, nodeTo.x, nodeTo.y);
			g.drawLine(x0, y0, ewx0, ewy0);
			g.drawLine(x1, y1, ewx1, ewy1);
			
		}else {
			g.drawLine(x0, y0, nodeFrom.x, nodeFrom.y);
			g.drawLine(x1, y1, nodeTo.x, nodeTo.y);
		}
	}
	

	public String getCaption() {
		return "0";		
	}
	//public void setCaption(String s) { }
	private int ewx0=0,ewy0=0,ewx1=0,ewy1=0;
	private void computeCoordinatesOfExtraWidthDots() {
		if(!coordinatesExtraWidthChanged)return; 
		coordinatesExtraWidthChanged=false; drawWithColor(EntityEditor_Helper.colorBackground);
		
		ewx0=(int) Math.round(x-dx*(double)(additionalWidth)/width);
		ewy0=(int) Math.round(y-dy*(double)(additionalWidth)/width);
		ewx1=(int) Math.round(x+dx*(double)(additionalWidth)/width);
		ewy1=(int) Math.round(y+dy*(double)(additionalWidth)/width);
		
	}
	
	static double arrowLength=10, arrow1length=4;
	public void drawCurrent(Color color, int side, double currentCaption) {
		if(additionalWidth>arrowLength)drawCurrent1(color,side,currentCaption);
		//older version
		else drawCurrent2(color,side,currentCaption);
	}
	/**from xend,yend*/
	private void drawCurrent1(Color color, int side, double currentCaption) {
		int a0x,a0y,a1x,a1y,a2x,a2y;
		double ex,ey;int e0x,e0y;
		ex=dx*arrowLength/width;ey=dy*arrowLength/width;e0x=x1;e0y=y1;
		if(side*((int)Math.signum(currentCaption))==-1) {
			ex=-ex;ey=-ey;
			e0x=x0;e0y=y0;
		}
		ex=
		a0x=(int) (ex);a0y=(int)(ey);
		a1x=(int) (
				ex*(1.-arrow1length/arrowLength)
				+(cx*2.*arrow1length/height)
				);
		a1y=(int)(
				ey*(1.-arrow1length/arrowLength)
				+(cy*2.*arrow1length/height)
				);
		a2x=(int) (
				ex*(1.-arrow1length/arrowLength)
				-(cx*2.*arrow1length/height)
				);
		a2y=(int)(
				ey*(1.-arrow1length/arrowLength)
				-(cy*2.*arrow1length/height)
				);
		a0x+=e0x;a1x+=e0x;a2x+=e0x;a0y+=e0y;a1y+=e0y;a2y+=e0y;
		
		
		g.setColor(color);
		//draw arrow
		g.drawLine(e0x, e0y, a0x, a0y);
		g.drawLine(a1x, a1y, a0x, a0y);
		g.drawLine(a2x, a2y, a0x, a0y);
	}
	/**from ewx,ewy*/
	private void drawCurrent2(Color color, int side, double currentCaption) {
		int a0x,a0y,a1x,a1y,a2x,a2y;
		double ex,ey;int e0x,e0y;
		ex=ewx1-x1;ey=ewy1-y1;e0x=ewx1;e0y=ewy1;
		if(side*((int)Math.signum(currentCaption))==-1) {
			ex=ewx0-x0;ey=ewy0-y0;e0x=ewx0;e0y=ewy0;
		}
		double l=Math.sqrt(ex*ex+ey*ey);ex/=l;ey/=l;
		a0x=(int) (ex*arrowLength);a0y=(int)(ey*arrowLength);
		a1x=(int) (
				ex*(arrowLength-arrow1length)
				-(ey*arrow1length)
				);
		a1y=(int)(
				ey*(arrowLength-arrow1length)
				+(ex*arrow1length)
				);
		a2x=(int) (
				ex*(arrowLength-arrow1length)
				+(ey*arrow1length)
				);
		a2y=(int)(
				ey*(arrowLength-arrow1length)
				-(ex*arrow1length)
				);
		a0x+=e0x;a1x+=e0x;a2x+=e0x;a0y+=e0y;a1y+=e0y;a2y+=e0y;
		
		
		g.setColor(color);
		//draw arrow
		g.drawLine(e0x, e0y, a0x, a0y);
		g.drawLine(a1x, a1y, a0x, a0y);
		g.drawLine(a2x, a2y, a0x, a0y);
	}
	public String printVoltageDifference() {
		String s="";
		
		return s;
	}
}
