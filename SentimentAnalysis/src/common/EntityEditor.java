package common;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import parts.PartBasic;
import parts.PartBinaryBasic;
import parts.PartBinaryLine;
import parts.PartBinaryRelation;
import parts.PartBinaryWords;
import parts.PartNode;
import parts.SchematicManager;
import util.DataTable;
import util.Lists;



public class EntityEditor {
	private int[][] matrix, matrixOfNodeIndexes,matrixOfPartIndexes;
	private int w,h;
	public int frequenciesCount;
	public int[] frequenciesCaptions;
	
	/**this is used only for placing parts via graphic editor*/
	public ArrayList<PartNode> nodesContainer;
	/**it is used excludingly for computations, and like alternative to nodelist*/
	public ArrayList<PartBinaryBasic> partsContainer;
	
	public ArrayList<PartBasic> elementsContainer;
	
	public EntityEditor(int width, int height) {
		nodesContainer = SchematicManager.nodes;//new ArrayList<PartNode>();
		partsContainer = SchematicManager.parts;//new ArrayList<PartBinaryBasic>();
		elementsContainer = SchematicManager.elements;//new ArrayList<PartBasic>();
		w=width;h=height;
		matrix=SchematicManager.elementsMatrix;//matrix=new int[w][h];
		matrixOfNodeIndexes=SchematicManager.nodesMatrix;
		matrixOfPartIndexes=SchematicManager.partsMatrix;

		MatrixHelper.clear(matrix);
		MatrixHelper.clear(matrixOfNodeIndexes);
		MatrixHelper.clear(matrixOfPartIndexes);
		//matrixOfNodeIndexes= new int[w][h];MatrixHelper.clear(matrixOfNodeIndexes);
	}
	public void printForum() {
		//TODO five-nine work
		int x=10,y=10, ystep=20;
		int index;String tmpstr;
		int postsCount = CommonData.discuss.messageList.size();
		ArrayList<Integer> uniqueUserIDs=new ArrayList<Integer>();
		Lists.getUniqueArrayList(CommonData.discuss.userIDList,uniqueUserIDs);
		int discussUsersCount=uniqueUserIDs.size();
		double radius=250,cx=300, cy=300, nowX, nowY;double angle=0,deltaAngle=Math.PI*2./(discussUsersCount+1);
		for(int i=0;i<discussUsersCount;++i) {
			index=CommonData.discuss.userIDList.indexOf(uniqueUserIDs.get(i));
			tmpstr=CommonData.discuss.userNameList.get(index);
			nowX=radius*Math.cos(angle)+cx;
			nowY=radius*Math.sin(angle)+cy;
			angle+=deltaAngle;
			index=addNode((int)nowX,(int) nowY);
			elementsContainer.get(index).partName=tmpstr;
		}
		int postID,parentPostID,postUserID,parentPostUserID,node0Index,node1Index;
		int[][] relationsMatrix = new int[discussUsersCount][discussUsersCount];
		for(int i=0;i<postsCount;++i) {
			postID=CommonData.discuss.postIDList.get(i);
			parentPostID=CommonData.discuss.parentPostIDList.get(i);
			if(parentPostID==-1)continue;
			postUserID=CommonData.discuss.userIDList.get(i);
			index=CommonData.discuss.postIDList.indexOf(parentPostID);
			parentPostUserID=CommonData.discuss.userIDList.get(index);
			node0Index=uniqueUserIDs.indexOf(postUserID);
			node1Index=uniqueUserIDs.indexOf(parentPostUserID);
			relationsMatrix[node0Index][node1Index]+=1;
		}
		PartNode n0,n1;
		for(int i=0;i<discussUsersCount;++i)
			for(int j=0;j<i;++j) {
				if(relationsMatrix[i][j]==0 && relationsMatrix[j][i]==0)continue;
				n0=nodesContainer.get(i);
				n1=nodesContainer.get(j);
				nowX=(n0.getPosX()+n1.getPosX());nowX=nowX*0.5;
				nowY=(n0.getPosY()+n1.getPosY());nowY=nowY*0.5;
				index=addPart((int)nowX,(int) nowY, n0, n1, 0);
				int authorID=uniqueUserIDs.get(i);
				int passionID=uniqueUserIDs.get(j);
				//CommonData.discuss.userIDList.
				//((PartBinaryLine)(elementsContainer.get(index))).messageText=;
				((PartBinaryBasic)(elementsContainer.get(index))).amperagefrom=relationsMatrix[i][j];
				((PartBinaryBasic)(elementsContainer.get(index))).voltageDifference=relationsMatrix[j][i];
			}
		System.out.println("5/9");
		System.out.println("discussUsersCount=".concat(Integer.toString(discussUsersCount)));
		
		
	}
	
	//---------------------------------graphical troubles----------------------------------------------------------------------------
	public void drawElementOnMatrix(PartBasic element, boolean erase) {
		int captionElement=-1, captionPart=-1;
		boolean elementIsNode=element.getClass().getSimpleName().matches(PartNode.class.getSimpleName());
		if(!erase) {
			captionElement=elementsContainer.indexOf(element);
			if(elementIsNode)	captionPart=nodesContainer.indexOf((PartNode)element);
			else				captionPart=partsContainer.indexOf((PartBinaryBasic)element);
		}
		element.drawOnMap(matrix, captionElement);
		if(elementIsNode) 	element.drawOnMap(matrixOfNodeIndexes, captionPart);
		else 				element.drawOnMap(matrixOfPartIndexes, captionPart);
	}

	public void redrawAllOnGraph() {
		Graphics g = EntityEditor_Helper.graphics;
		g.setColor(EntityEditor_Helper.colorBackground);
		g.fillRect(0, 0, CommonData.WIDTH, CommonData.HEIGHT);
		for(int i=0;i<elementsContainer.size();++i) {
			elementsContainer.get(i).draw();
		}
		EntityEditor_Helper.panel.repaint();
	}

	public void redrawAllOnMatrixes() {
		MatrixHelper.clear(matrix);
		MatrixHelper.clear(matrixOfNodeIndexes);
		MatrixHelper.clear(matrixOfPartIndexes);
		//MatrixHelper.clear(matrixOfNodeIndexes);
		for(int i=0;i<elementsContainer.size();++i) {
			drawElementOnMatrix(elementsContainer.get(i),false);
		}
		EntityEditor_Helper.panel.repaint();
	}
	
	
	public int addNode(int xPad, int yPad) {
		if(partsCollider(xPad,yPad, 5))return -1;
		PartNode node = new PartNode(xPad, yPad);
		return SchematicManager.addExistingNode(node);
	}
	public int addPart(int x, int y, PartNode node0, PartNode node1, int partClassIndex) {
		//if(partsCollider(xPad,yPad))return false;
		//nowClass=PartBinaryBasic.classesList[partIndex];
		PartBinaryBasic part=addPart_constructPart(x, y, node0, node1, partClassIndex);
		
		
		return SchematicManager.addExistingPart(part);
	}
	/**
	 * redrawVisualLayer: panel will be redrawn *
	 * redrawPhysicalLayer: physical array-panel will be redrawn *
	 * reIndexPhysicalLayerLabels: sends to all parts and nodes its own indexes in lists */
	public void removeElement(PartBasic element, boolean redrawVisualLayer, boolean redrawPhysicalLayer, boolean reIndexPhysicalLayerLabels) {
		
		/**if it is node*/
		if(element.getClass().getSimpleName().matches(PartNode.class.getSimpleName())) {
			removeNode((PartNode) element, reIndexPhysicalLayerLabels);
		/**if it is part*/
		}else
			removePart(element, reIndexPhysicalLayerLabels);
		if(redrawPhysicalLayer)
			redrawAllOnMatrixes();
		if(redrawVisualLayer)
			redrawAllOnGraph();
		System.out.println("Done: SchemeStorage.removeElement( element = "+element.getClass().getName()+", redrawVisualLayer = "+redrawVisualLayer+", redrawPhysicalLayer = "+redrawPhysicalLayer+")");
	}
	public void removeNode(PartNode node, boolean reIndexPhysicalLayerLabels) {
		ArrayList<PartBinaryBasic> parts = 
				//getPartsFromNode(node);
				CommonData.scheme.partsContainer;
		PartBinaryBasic nowPart = null;
		for(int i=0;i<parts.size();++i) {
			nowPart=parts.get(i);
			if(nowPart.getNodeFrom().equals(node) || nowPart.getNodeTo().equals(node))
				removePart(parts.get(i), false);
		}
		removePart(node, reIndexPhysicalLayerLabels);
	}
	public void removePart(PartBasic part, boolean reIndexPhysicalLayerLabels) {
		drawElementOnMatrix(part, true);
		int nodeIndex=-1, elementIndex=-1, partIndex=-1;
		if(part.getClass().getSimpleName().matches(PartNode.class.getSimpleName()))
			nodeIndex=nodesContainer.indexOf(part);
		else
			partIndex=partsContainer.indexOf(part);
		elementIndex=elementsContainer.indexOf(part);
		
		
		/**deleting element from lists*/
		if(nodeIndex>=0)nodesContainer.remove(nodeIndex);
		if(partIndex>=0)partsContainer.remove(partIndex);
		if(elementIndex>=0)elementsContainer.remove(elementIndex);
		/**reIndexAllPartsAndNodes*/
		if(reIndexPhysicalLayerLabels)
			SchematicManager.reindexAllObjects();
	}
	
	//---------------------------------computational troubles----------------------------------------------------------------------------
	public ArrayList<PartBinaryBasic> getPartsFromNode(PartNode node){
		//int count=(int)node.getCaption();
		//if(count==0)return null;
		ArrayList<PartBinaryBasic>  result = new ArrayList<PartBinaryBasic>();
		for(int i=0;i<partsContainer.size();++i) {
			if(
					(partsContainer.get(i).getNodeFrom()==node)
					||(partsContainer.get(i).getNodeTo()==node)
					)
				result.add(partsContainer.get(i));
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//---------------------------------lowlevel computational troubles---------------------------------------------------------------------------
	/*
	public int whatNodeIndexIsHere(int x, int y) {
		return matrixOfNodeIndexes[x][y];
	}
	public PartNode whatNodeIsHere(int x, int y) {
		return nodesContainer.get(whatNodeIndexIsHere(x,y));
	}
	public int whatNodeIndexInZone(int x, int y, int scanRadius) {
		return MatrixHelper.searchNearestNeighbor(x, y, scanRadius, true, matrixOfNodeIndexes, -1);
	}
	public PartNode whatNodeInZone(int x, int y, int scanRadius) {
		int index=whatNodeIndexInZone(x,y, scanRadius);
		if(index<0)return null;
		return nodesContainer.get(index);
	}
	*/
	

	public int whatPartBinaryIndexIsHere(int x, int y) {
		return matrixOfPartIndexes[x][y];
	}
	public PartBinaryBasic whatPartBinaryIsHere(int x, int y) {
		PartBinaryBasic result=null;
		try {result=partsContainer.get(whatElementIndexIsHere(x,y));}catch(Exception e) {result=null;}
		return result;
	}
	public int whatNodeIndexIsHere(int x, int y) {
		return matrixOfNodeIndexes[x][y];
	}
	public PartNode whatNodeIsHere(int x, int y) {
		PartNode result=null;
		try {result=nodesContainer.get(whatElementIndexIsHere(x,y));}catch(Exception e) {result=null;}
		return result;
	}
	public int whatElementIndexIsHere(int x, int y) {
		return matrix[x][y];
	}
	public PartBasic whatElementIsHere(int x, int y) {
		PartBasic result=null;
		try {result=elementsContainer.get(whatElementIndexIsHere(x,y));}catch(Exception e) {result=null;}
		return result;
	}
	public PartBasic whatIsHere(int x, int y) {
		PartBasic result=null;
		if(CommonData.selectorMode==CommonData.selectorMode_ELEMENT)return whatElementIsHere(x,y);
		if(CommonData.selectorMode==CommonData.selectorMode_NODE)return whatNodeIsHere(x,y);
		if(CommonData.selectorMode==CommonData.selectorMode_PART)return whatPartBinaryIsHere(x,y);
		return result;
	}
	int[][] selectedMatrix(){
		if(CommonData.selectorMode==CommonData.selectorMode_ELEMENT)return matrix;
		if(CommonData.selectorMode==CommonData.selectorMode_NODE)return matrixOfNodeIndexes;
		if(CommonData.selectorMode==CommonData.selectorMode_PART)return matrixOfPartIndexes;
		return null;
	}
	ArrayList selectedContainer(){
		if(CommonData.selectorMode==CommonData.selectorMode_ELEMENT)return elementsContainer;
		if(CommonData.selectorMode==CommonData.selectorMode_NODE)return nodesContainer;
		if(CommonData.selectorMode==CommonData.selectorMode_PART)return partsContainer;
		return null;
	}
	public int whatElementIndexInZone(int x, int y, int scanRadius) {
		return MatrixHelper.searchNearestNeighbor(x, y, scanRadius, true, selectedMatrix(), -1);
	}
	public PartBasic whatElementInZone(int x, int y, int scanRadius) {
		int index=whatElementIndexInZone(x,y, scanRadius);
		if(index<0)return null;
		return (PartBasic)selectedContainer().get(index);
	}
	

	public boolean partsCollider(int xPad, int yPad, int objectRadius) {
		
		
		return false;
	}
	//@SuppressWarnings("unchecked")
	private static PartBinaryBasic addPart_constructPart(int x, int y, PartNode node0, PartNode node1, int partClassIndex) {
		/*
		try {
			(PartBinaryBasic.classesList[partIndex]).getConstructor(PartBinaryBasic.class).newInstance(part)
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.out.println(common.Lang.InnerTable.Dialog.notValidClassName);
			e.printStackTrace();
		}
		*/
		switch (partClassIndex) {
		case 0: return new PartBinaryLine(x,y,node0,node1);
		case 1: return new PartBinaryRelation(x,y,node0,node1);
		case 2: return new PartBinaryWords(x,y,node0,node1);
		}
		//TODO WIP
		try {
			return (PartBinaryBasic) PartBinaryBasic.classesList[partClassIndex].getDeclaredConstructor().newInstance(x,y,node0,node1);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();} 
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
		return null;
	}
	//---------------------------------------------------------------------------------------
	/**elements not prepared*/
	public void saveResult(String fileName) {
	    File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {throw new RuntimeException(e);}
	    PrintWriter out = null;
		try {
			out = new PrintWriter(file.getAbsoluteFile());
		}catch (FileNotFoundException e){e.printStackTrace();}
	    try {
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText1);
			out.println(nodesContainer.size());
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText2);
			out.println(partsContainer.size());
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText3);
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText4);
			PartNode nodetemp;String additionalData=null;
			for(int i=0;i<nodesContainer.size();++i) {
				nodetemp=nodesContainer.get(i);
				out.print(Integer.toString(i)
						+" "
						+Integer.toString(nodetemp.getPosX())
						+" "
						+Integer.toString(nodetemp.getPosY())
						+" "
						+nodetemp.partName
						);
				
				out.println();
			}

			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText5);
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText6);
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText7);
			out.println(common.Lang.InnerTable.Dialog.SchemeStorageSaveResultText8);
			PartBasic parttemp;PartNode node0=null, node1=null;
			for(int i=0;i<partsContainer.size();++i) {
				parttemp=partsContainer.get(i);
				node0=((PartBinaryBasic)parttemp).getNodeFrom();
				node1=((PartBinaryBasic)parttemp).getNodeTo();
				out.println(parttemp.getClass().getSimpleName());
				out.print(
						Integer.toString(nodesContainer.indexOf(node0))
						+" "
						+Integer.toString(nodesContainer.indexOf(node1))
						+" "
						+parttemp.getCaption()
						+" "
						+Integer.toString(parttemp.getPosX())
						+" "
						+Integer.toString(parttemp.getPosY())
						);
				out.println(
						" "
						+parttemp.partName);
				additionalData=parttemp.getAdditionalData();
				if(additionalData==null)	out.println();
				else						out.println(additionalData);
			}
			
	    }finally {
	    	out.close();
	    }
	}
	
	/**elements not prepared */
	public void loadResult(String fileName) {
	    File file = new File(fileName);
	    Scanner in = null;
		try {in = new Scanner(file);} catch (Exception e) {e.printStackTrace();}
	    try {
	    	String dummy=null;int dummyint=0;
	    	dummy=in.nextLine();//("nodes number in schematic");
			int nodesCount=in.nextInt();dummy=in.nextLine();//out.println(nodesContainer.size());
			dummy=in.nextLine();//out.println("number of parts in schematic");
			int partsCount=in.nextInt();dummy=in.nextLine();//out.println(partsContainer.size());
			dummy=in.nextLine();//out.println("nodelist");
			dummy=in.nextLine();//out.println("Number X Y Name");
			PartNode nodetemp;int tmpx,tmpy;String tmpstr, tmpstrclassname;String additionalData=null;
			for(int i=0;i<nodesCount;++i) {
				//nodetemp=nodesContainer.get(i);
				//out.print(
				dummyint=in.nextInt();//	Integer.toString(i)
					//+" "
				tmpx=in.nextInt();//	+Integer.toString(nodetemp.getPosX())
					//+" "
				tmpy=in.nextInt();//	+Integer.toString(nodetemp.getPosY())
					//+" "
				tmpstr=in.nextLine();//	+nodetemp.partName
					//);
				
				//in.nextLine(); - commented because previous string scanned not by pattern, but to divider//out.println();
				nodetemp = new PartNode(tmpx, tmpy);
				nodetemp.partName=tmpstr;
				SchematicManager.addExistingNode(nodetemp);
			}

			dummy=in.nextLine();//out.println("partlist");
			dummy=in.nextLine();//out.println("type");
			dummy=in.nextLine();//out.println("#Node0 #Node1 Caption  X Y Name");
			dummy=in.nextLine();//out.println("Additional data");
			PartBinaryBasic parttemp;PartNode node0=null, node1=null;int node0index,node1index;double caption;
			for(int i=0;i<partsCount;++i) {
				//parttemp=partsContainer.get(i);
				//node0=((PartBinaryBasic)parttemp).getNodeFrom();
				//node1=((PartBinaryBasic)parttemp).getNodeTo();
				//out.print(
				tmpstrclassname=in.nextLine();//parttemp.getClass().getName()
						//+" "
				node0index=in.nextInt();//+Integer.toString(nodesContainer.indexOf(node0))
						//+" "
				node1index=in.nextInt();//+Integer.toString(nodesContainer.indexOf(node1))
						//+" "
				tmpstr=in.next();caption=Double.parseDouble(tmpstr);//+parttemp.getCaption()
						//+" "
				tmpx=in.nextInt();//+parttemp.getPosX()
						//+" "
				tmpy=in.nextInt();//+parttemp.getPosY()
						//+" "
				tmpstr=in.nextLine();// +parttemp.partName
						//);
				additionalData=in.nextLine();// +parttemp.partName
				//in.nextLine(); -||-   //out.println();
				node0=nodesContainer.get(node0index);node1=nodesContainer.get(node1index);
				parttemp = addPart_constructPart(tmpx, tmpy, node0, node1,   PartBinaryBasic.classesListGetIndexOf(tmpstrclassname) );
				parttemp.partName=tmpstr;
				parttemp.setCaption(caption);
				parttemp.setAdditionalData(additionalData);
				SchematicManager.addExistingPart(parttemp);
			}
		}finally {
			in.close();
		}
	}
	public void loadDuscuss(String fileName) {
		// TODO Discuss loading processor
		TextForum discuss = FileIO.readHTMPage(fileName);
		CommonData.discuss=discuss;
	}
}
