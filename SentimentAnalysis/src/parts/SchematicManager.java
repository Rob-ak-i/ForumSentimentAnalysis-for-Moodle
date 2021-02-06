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
	//TODO Rendering scene 26.01.2021
	public static void drawLogicMap(double wl,double wr,double wu,double wd, double cx, double cy, double sx, double sy){
		//System.out.println("Drawing logic level");
		MatrixHelper.clear(elementsMatrix);
		MatrixHelper.clear(nodesMatrix);
		MatrixHelper.clear(partsMatrix);
		PartBasic element;
		int x,y,x0,y0,x1,y1;
		boolean isElementInNotInScreen;
		for(int i=0;i<elements.size();++i){
			element=elements.get(i);
			x=element.x;
			y=element.y;
			isElementInNotInScreen=false;
			if(x<wl||x>=wr||y<wd||y>=wu)isElementInNotInScreen=true;
			if(isElementInNotInScreen)continue;
			if(!element.getClass().equals(PartNode.class)){
				x0=((PartBinaryBasic)element).nodeFrom.x;
				y0=((PartBinaryBasic)element).nodeFrom.y;
				x1=((PartBinaryBasic)element).nodeTo.x;
				y1=((PartBinaryBasic)element).nodeTo.y;
				if(x0<wl||x0>=wr||y0<wd||y0>=wu)isElementInNotInScreen=true;
				if(x1<wl||x1>=wr||y1<wd||y1>=wu)isElementInNotInScreen=true;
			}
			if(isElementInNotInScreen)continue;
			element.drawOnMap(elementsMatrix, i, cx, cy, sx, sy);
		}
		PartNode node;
		for(int i=0;i<nodes.size();++i){
			node=nodes.get(i);
			x=node.x;
			y=node.y;
			isElementInNotInScreen=false;
			if(x<wl||x>=wr||y<wd||y>=wu)isElementInNotInScreen=true;
			if(isElementInNotInScreen)continue;
			node.drawOnMap(nodesMatrix, i, cx, cy, sx, sy);
		}
		PartBinaryBasic part;
		for(int i=0;i<parts.size();++i){
			part=parts.get(i);
			x=part.x;
			y=part.y;
			isElementInNotInScreen=false;
			if(x<wl||x>=wr||y<wd||y>=wu)isElementInNotInScreen=true;
			if(isElementInNotInScreen)continue;
			part.drawOnMap(partsMatrix, i, cx, cy, sx, sy);
		}
	}
	public static void drawScene(Graphics g, double wl,double wr,double wu,double wd, double cx, double cy, double sx, double sy)
	{
		g.setColor(Color.black);
		g.drawRect((int)wl+3, (int)wu+3,(int)(wr-wl-3), (int)(wd-wu-3));
		g.setColor(Color.RED);
		Dot d0=EntityRenderer.getImageDotFromMathCoords(wl+3, wu+3),d1=EntityRenderer.getImageDotFromMathCoords(wr-3,wd-3);
		g.drawRect(d0.x,d0.y,d1.x-d0.x,d1.y-d0.y);
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
	

	public static void computeSuperNodesAndWays() {
		/**part 0/2 - concatinating nodes to exclude lines(resistors with zero-resistance)*/
		
		/**Track criterise:
		 * 		parallelization - supernode always have two or more attached tracks
		 * 		single current
		 * 		different potentials at beginnings
		 * TRACK(WAY) TRIGGERS
		 * 		supernode - because may have start to multitracks
		 * now we have less amount of unchecked parts
		 * 
		 * generator start 
		 * 
		 * */
		
		
		/**part 1/2 - detecting supernodes(edges>=3)*/
		int count=nodes.size();
		int[] candidates = new int[count];
		for(int i=0;i<parts.size();++i) {
			candidates[parts.get(i).nodeFrom.index]++;
			candidates[parts.get(i).nodeTo.index]++;
		}
		/**part 2/2 - computing ways(tracks)*/
		boolean[]partsUsed = new boolean[parts.size()];
		int supernodesCount=0, supernodeDir=0,tmpresult;
		PartBinaryBasic part=null;
		ArrayList<PartBinaryBasic> freeParts = new ArrayList<PartBinaryBasic>();
		
		for(int i=0;i<parts.size();++i) {
			if(partsUsed[i]==true)continue;
			supernodesCount=0;
			part=parts.get(i);
			switch(supernodesCount) {
			case 0:
				freeParts.add(part);
				break;
			}
		}
		boolean trigger;
		for(int i=0;i<freeParts.size();++i) {
			part=freeParts.get(i);
			if(partsUsed[part.index])continue;
			trigger=false;
		}
		freeParts.clear();
		
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
