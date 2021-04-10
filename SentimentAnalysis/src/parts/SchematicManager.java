package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import util.Dot;
import common.CommonData;
import common.EntityRenderer;
import common.MatrixHelper;

public class SchematicManager {
	public static ArrayList<PartNode> nodes = new ArrayList<PartNode>();
	public static ArrayList<PartBinaryBasic> parts = new ArrayList<PartBinaryBasic>();
	public static ArrayList<PartBasic> elements = new ArrayList<PartBasic>();
	
	public static int[][] elementsMatrix = new int[common.CommonData.WIDTH][common.CommonData.HEIGHT];
	public static int[][] partsMatrix = new int[common.CommonData.WIDTH][common.CommonData.HEIGHT];
	public static int[][] nodesMatrix = new int[common.CommonData.WIDTH][common.CommonData.HEIGHT];
	public static boolean drawModelOnPanel = true;
	public static boolean drawModelOnMatrix = true;
	
	private static boolean isSuperNode(PartNode node) {return false;}
	public static void resize(){
		MatrixHelper.resize(elementsMatrix, CommonData.WIDTH, CommonData.HEIGHT);
		MatrixHelper.resize(nodesMatrix, CommonData.WIDTH, CommonData.HEIGHT);
		MatrixHelper.resize(partsMatrix, CommonData.WIDTH, CommonData.HEIGHT);
		PartBasic.screenWidthHalf=(double)((CommonData.WIDTH>>>1));
		PartBasic.screenHeightHalf=(double)((CommonData.HEIGHT>>>1));
	}
	public static void drawLogicMap(double wl,double wr,double wu,double wd, double cx, double cy, double sx, double sy){
		
		MatrixHelper.clear(elementsMatrix);
		MatrixHelper.clear(nodesMatrix);
		MatrixHelper.clear(partsMatrix);
		PartBasic element;
		int x,y,x0,y0,x1,y1;
		boolean isElementOutOfScreen;
		for(int i=0;i<elements.size();++i){
			element=elements.get(i);
			x=element.x;
			y=element.y;
			isElementOutOfScreen=false;
			if(x<=wl||x>=wr||y>=wd||y<=wu)isElementOutOfScreen=true;
			if(isElementOutOfScreen)continue;
			if(!element.getClass().equals(PartNode.class)){
				x0=((PartBinaryBasic)element).nodeFrom.x;
				y0=((PartBinaryBasic)element).nodeFrom.y;
				x1=((PartBinaryBasic)element).nodeTo.x;
				y1=((PartBinaryBasic)element).nodeTo.y;
				if(x0<=wl||x0>=wr||y0>=wd||y0<=wu)isElementOutOfScreen=true;
				if(x1<=wl||x1>=wr||y1>=wd||y1<=wu)isElementOutOfScreen=true;
			}
			if(isElementOutOfScreen)continue;
			element.drawOnMap(elementsMatrix, i, cx, cy, sx, sy);
		}
		PartNode node;
		for(int i=0;i<nodes.size();++i){
			node=nodes.get(i);
			x=node.x;
			y=node.y;
			isElementOutOfScreen=false;
			if(x<=wl||x>=wr||y>=wd||y<=wu)isElementOutOfScreen=true;
			if(isElementOutOfScreen)continue;
			node.drawOnMap(nodesMatrix, i, cx, cy, sx, sy);
		}
		PartBinaryBasic part;
		for(int i=0;i<parts.size();++i){
			part=parts.get(i);
			x=part.x;
			y=part.y;
			isElementOutOfScreen=false;
			if(x<=wl||x>=wr||y>=wd||y<=wu)isElementOutOfScreen=true;
			if(isElementOutOfScreen)continue;
			x0=part.nodeFrom.x;
			y0=part.nodeFrom.y;
			x1=part.nodeTo.x;
			y1=part.nodeTo.y;
			if(x0<=wl||x0>=wr||y0>=wd||y0<=wu)isElementOutOfScreen=true;
			if(x1<=wl||x1>=wr||y1>=wd||y1<=wu)isElementOutOfScreen=true;
			if(isElementOutOfScreen)continue;
			part.drawOnMap(partsMatrix, i, cx, cy, sx, sy);
		}
	}
	public static void drawScene(Graphics g, double wl,double wr,double wu,double wd, double cx, double cy, double sx, double sy)
	{
		g.setColor(Color.black);
		//g.drawRect((int)wl+3, (int)wu+3,(int)(wr-wl-3), (int)(wd-wu-3));
		//g.setColor(Color.RED);
		//Dot d0=EntityRenderer.getImageDotFromMathCoords(wl+3, wu+3),d1=EntityRenderer.getImageDotFromMathCoords(wr-3,wd-3);
		//g.drawRect(d0.x,d0.y,d1.x-d0.x,d1.y-d0.y);
		PartBasic.g=g;
		PartBasic element;
		int x,y,x0,y0,x1,y1;
		boolean isElementOutOfScreen;
		for(int i=0;i<elements.size();++i) {
			element=elements.get(i);
			x=element.x;
			y=element.y;
			isElementOutOfScreen=false;
			if(x<wl||x>=wr||y>=wd||y<=wu)isElementOutOfScreen=true;
			if(isElementOutOfScreen)continue;
			if(!element.getClass().equals(PartNode.class)){
				x0=((PartBinaryBasic)element).nodeFrom.x;
				y0=((PartBinaryBasic)element).nodeFrom.y;
				x1=((PartBinaryBasic)element).nodeTo.x;
				y1=((PartBinaryBasic)element).nodeTo.y;
				if(x0<wl||x0>=wr||y0>=wd||y0<wu)isElementOutOfScreen=true;
				if(x1<wl||x1>=wr||y1>=wd||y1<wu)isElementOutOfScreen=true;
			}
			if(isElementOutOfScreen)continue;
			element.draw(cx, cy, sx, sy);
		}
			
			
	}	/**returns element number, or else -1*/
	public static int addExistingNode(PartNode node) {
		nodes.add(node);
		elements.add(node);
		int elementIndex=elements.size()-1;
		int nodeIndex=nodes.size()-1;
		node.index=nodeIndex;
		/*
		if(drawModelOnPanel)node.draw();
		if(drawModelOnMatrix) {
			node.drawOnMap(elementsMatrix, elementIndex);
			node.drawOnMap(nodesMatrix, nodeIndex);
		}
		*/
		return elementIndex;
	}
	/**returns element number, or else -1*/
	public static int addExistingPart(PartBinaryBasic part) {

		parts.add(part);
		elements.add(part);
		int elementIndex=elements.size()-1;
		int partIndex=parts.size()-1;
		//if(drawModelOnPanel)part.draw(); - elements will be drawn only in renderer
		/*
		if(drawModelOnMatrix) {
			part.drawOnMap(elementsMatrix, elementIndex);
			part.drawOnMap(partsMatrix, partIndex);
		}
		*/
		return elementIndex;
	}
	public static void reindexAllObjects() {
		for(int i=0;i<nodes.size();++i)
			nodes.get(i).index=i;
		for(int i=0;i<parts.size();++i)
			parts.get(i).index=i;
	}
	public static ArrayList<PartBinaryBasic> getPartsForNode(PartNode node){
		ArrayList<PartBinaryBasic> result = new ArrayList<PartBinaryBasic>();
		PartBinaryBasic part;
		for(int i=0;i<parts.size();++i) {
			part = parts.get(i);
			if(part.nodeFrom.equals(node))result.add(part);
			if(part.nodeTo.equals(node))result.add(part);
		}
		return result;
	}
	/*
	public static void computePartsForAllNodes() {
		for(int i=0;i<nodes.size();++i)
			nodes.get(i).getPartsForNode(parts);
	}
	*/
}
