package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import common.CommonData;
import common.EntityRenderer;

public class PartNode extends PartBasic{
	public static int PART_DOT_SIZE=5;
	public static boolean drawRecurse=false;
	private int elementsCount=0;
	private static boolean recurseLock=false;
	//public ArrayList<PartBinaryBasic>parts=null;
	//public ArrayList<Integer> partsOrientations=null;
	public static int partsOrientations_FROMME=-1;
	public static int partsOrientations_TOME=1;
	public PartNode(int x, int y) {
		super(x, y, PartNode.PART_DOT_SIZE, PartNode.PART_DOT_SIZE);
	}
	/*
	public void destroy() {
		parts.clear();
	}
	*/
	public void drawWithColor(Color color,double camposx, double camposy, double scalex, double scaley) {
		g.setColor(color);

		int x=(int)(((double)this.x-camposx)*scalex+screenWidthHalf);
		int y=(int)(((double)this.y-camposy)*scaley+screenHeightHalf);
		g.fillOval(x-xr, y-yr, width, height);
		super.drawName(color, camposx, camposy, scalex, scaley);
		if(recurseLock)return;
		recurseLock=true;
		if(drawRecurse==true) {
			int x0,y0,x1,y1;boolean isElementOutOfScreen;
			PartBinaryBasic part;
			for(int i=0;i<CommonData.scheme.partsContainer.size();++i){
				part=CommonData.scheme.partsContainer.get(i);
				isElementOutOfScreen=false;
				if(part.nodeFrom==this){
					x1=part.nodeTo.x;
					y1=part.nodeTo.y;
					if(x1<EntityRenderer.windowLeft||x1>=EntityRenderer.windowRight||y1<EntityRenderer.windowDown||y1>=EntityRenderer.windowUp)isElementOutOfScreen=true;
				}else{
					if(part.nodeTo==this){
						x0=part.nodeFrom.x;
						y0=part.nodeFrom.y;
						if(x0<EntityRenderer.windowLeft||x0>=EntityRenderer.windowRight||y0<EntityRenderer.windowDown||y0>=EntityRenderer.windowUp)isElementOutOfScreen=true;		
					}else continue;
				}
				if(isElementOutOfScreen)continue;
				part.drawWithColor(
						Color.pink//(color==EntityEditor_Helper.colorMain) ? EntityEditor_Helper.colorMain : EntityEditor_Helper.colorRandom()
						, camposx, camposy,scalex, scaley
						);
			}
		}
		recurseLock=false;
	}
	public String getCaption() {
		return Integer.toString(elementsCount);		
	}
	@Override
	public void setCaption(String value) {
		// TODO Auto-generated method stub
		System.out.println(common.Lang.InnerTable.Console.partsPartNode_SetCaption);
		
	}	
	public void setCaption(double value) {
		System.out.println(common.Lang.InnerTable.Console.partsPartNode_SetCaption);
		//elementsCount=(int) value;
	}
	public void addPart() {
		elementsCount+=1;
	}
	public void removePart() {
		elementsCount-=1;
	}
	
	public ArrayList<PartBinaryBasic> getPartsForNode(){
		ArrayList<PartBinaryBasic>parts=CommonData.scheme.partsContainer;
		ArrayList<Integer> partsOrientations = new ArrayList<Integer>();
		ArrayList<PartBinaryBasic> result = new ArrayList<PartBinaryBasic>();
		PartBinaryBasic part;
		for(int i=0;i<parts.size();++i) {
			part = parts.get(i);
			if(part.nodeFrom.equals(this)) {result.add(part);partsOrientations.add((partsOrientations_FROMME));}
			if(part.nodeTo.equals(this)) {result.add(part);partsOrientations.add((partsOrientations_TOME));}
		}
		return result;
	}

}
