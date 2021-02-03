package parts;

import java.awt.Graphics;
import java.util.ArrayList;

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
	
	//TODO Rendering scene 26.01.2021
	public static void drawScene(Graphics g, int camposx, int camposy, int radiusx, int radiusy)
	{
		PartBasic element;
		int x,y,matx,maty;
		for(int i=0;i<elements.size();++i) {
			element=elements.get(i);
			x=element.x;
			y=element.y;
			matx=x-camposx+radiusx;
			maty=y-camposy+radiusy;
			if(matx>0 && matx<2*radiusx && maty>0 && maty<2*radiusy) {
				element.draw();
			}
		}
			
			
	}	/**returns element number, or else -1*/
	public static int addExistingNode(PartNode node) {
		nodes.add(node);
		elements.add(node);
		int elementIndex=elements.size()-1;
		int nodeIndex=nodes.size()-1;
		node.index=nodeIndex;
		if(drawModelOnPanel)node.draw();
		if(drawModelOnMatrix) {
			node.drawOnMap(elementsMatrix, elementIndex);
			node.drawOnMap(nodesMatrix, nodeIndex);
		}
		return elementIndex;
	}
	/**returns element number, or else -1*/
	public static int addExistingPart(PartBinaryBasic part) {

		parts.add(part);
		elements.add(part);
		int elementIndex=elements.size()-1;
		int partIndex=parts.size()-1;
		if(drawModelOnPanel)part.draw();
		if(drawModelOnMatrix) {
			part.drawOnMap(elementsMatrix, elementIndex);
			part.drawOnMap(partsMatrix, partIndex);
		}
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
	public static void computePartsForAllNodes() {
		for(int i=0;i<nodes.size();++i)
			nodes.get(i).getPartsForNode(parts);
	}
}
