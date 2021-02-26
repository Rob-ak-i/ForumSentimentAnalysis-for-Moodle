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

import languageprocessing.Sequence;
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
	//private int w,h;
	public int frequenciesCount;
	public int[] frequenciesCaptions;
	
	/**this is used only for placing parts via graphic editor*/
	public ArrayList<PartNode> nodesContainer;
	/**it is used excludingly for computations, and like alternative to nodelist*/
	public ArrayList<PartBinaryBasic> partsContainer;
	
	public ArrayList<PartBasic> elementsContainer;
	
	public DataTable metaData;
	public static final String metaData_userIDUniqueListIdentifier="userIDUniqueList";
	
	public EntityEditor(int width, int height) {
		//TextForum temp = new TextForum();
		metaData=new DataTable();//temp.toDataTable();temp=null;
		metaData.addColumn(metaData_userIDUniqueListIdentifier, "Integer");
		
		nodesContainer = SchematicManager.nodes;//new ArrayList<PartNode>();
		partsContainer = SchematicManager.parts;//new ArrayList<PartBinaryBasic>();
		elementsContainer = SchematicManager.elements;//new ArrayList<PartBasic>();
		//w=width;h=height;
		matrix=SchematicManager.elementsMatrix;//matrix=new int[w][h];
		matrixOfNodeIndexes=SchematicManager.nodesMatrix;
		matrixOfPartIndexes=SchematicManager.partsMatrix;

		MatrixHelper.clear(matrix);
		MatrixHelper.clear(matrixOfNodeIndexes);
		MatrixHelper.clear(matrixOfPartIndexes);
		//matrixOfNodeIndexes= new int[w][h];MatrixHelper.clear(matrixOfNodeIndexes);
	}
	/**prints discuss which contains list of messages with info about sender and recipient.
	 * appendMode - if true then will be added only new members, relations will be corrected; else - will be added all members and connections*/
	public void printDiscuss(int cx, int cy, DataTable discuss, boolean isAppendMode) {
		if(isAppendMode) {System.out.println("Adding with append mode is not completed.");return;}
		//TODO appendMode

		int initialNodesContainerSize=nodesContainer.size();
		
		int x=10,y=10, ystep=20;
		int index;String tmpstr;
		int postsCount = discuss.nRows();
		ArrayList<Integer> uniqueUserIDs=new ArrayList<Integer>();
		Lists.getUniqueArrayList(TextForum.getUserIDList(discuss),uniqueUserIDs);
		int discussUsersCount=uniqueUserIDs.size();
		double radius=250, nowX, nowY;double angle=0,deltaAngle=Math.PI*2./(discussUsersCount+1);
		for(int i=0;i<discussUsersCount;++i) {
			index=TextForum.getUserIDList(discuss).indexOf(uniqueUserIDs.get(i));
			tmpstr=TextForum.getUserName(discuss,index);
			nowX=radius*Math.cos(angle)+cx;
			nowY=radius*Math.sin(angle)+cy;
			angle+=deltaAngle;
			index=addNode((int)nowX,(int) nowY);
			elementsContainer.get(index).partName=tmpstr;
		}
		int postID,parentPostID,postUserID,parentPostUserID,node0Index,node1Index;
		int[][] relationsMatrix = new int[discussUsersCount][discussUsersCount];
		for(int i=0;i<postsCount;++i) {
			postID=TextForum.getPostID(discuss,i);//CommonData.discuss.postIDList.get(i);
			parentPostID=TextForum.getParentPostID(discuss,i);//CommonData.discuss.parentPostIDList.get(i);
			if(parentPostID==-1)continue;
			postUserID=TextForum.getUserID(discuss,i);//CommonData.discuss.userIDList.get(i);
			index=TextForum.getPostIDList(discuss).indexOf(parentPostID);//CommonData.discuss.postIDList.indexOf(parentPostID);
			parentPostUserID=TextForum.getUserID(discuss,index);//CommonData.discuss.userIDList.get(index);
			node0Index=uniqueUserIDs.indexOf(postUserID);
			node1Index=uniqueUserIDs.indexOf(parentPostUserID);
			relationsMatrix[node0Index][node1Index]+=1;
		}
		PartNode n0,n1;
		for(int i=0;i<discussUsersCount;++i)
			for(int j=0;j<i;++j) {
				if(relationsMatrix[i][j]==0 && relationsMatrix[j][i]==0)continue;
				n0=nodesContainer.get(i+initialNodesContainerSize);
				n1=nodesContainer.get(j+initialNodesContainerSize);
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
	}
	public static int stepX=10,stepY=-10;
	public void printSequenceTree(int cx, int cy, Sequence rootSequence) {
		//int treeHeight=rootSequence.get(0);
		int width=rootSequence.getNumberOfLeaves();
		printSequenceSubTree(cx,cy+stepY,width*stepX,rootSequence);
	}
	private int printSequenceSubTree(int x, int y, int subTreeWidth, Sequence rootNode) {
		int nowX=x+subTreeWidth/2;
		int nowY=y;
		int rootNodeIndex=this.addNode(nowX, nowY);
		if(rootNode.childs==null)return rootNodeIndex;
		int nowNodeIndex;Sequence nowNode;int nowWidth;
		for(int i=0;i<rootNode.childs.size();++i) {
			nowNode = rootNode.childs.get(i);
			nowWidth=nowNode.getNumberOfLeaves();
			nowNodeIndex = printSequenceSubTree(x,y+stepY,nowWidth*stepX,nowNode);
			x+=nowWidth*stepX;
			/**make connection between rootNode and nowNode*/
			this.addPart(0, 0, nodesContainer.get(rootNodeIndex), nodesContainer.get(nowNodeIndex), PartBinaryBasic.classesListGetIndexOf(PartBinaryLine.class.getSimpleName()));
		}
		return rootNodeIndex;
	}
	
	//---------------------------------graphical troubles----------------------------------------------------------------------------
	public void drawElementOnMatrix(PartBasic element, boolean erase, int camposx, int camposy, int radiusx, int radiusy) {
		int captionElement=-1, captionPart=-1;
		boolean elementIsNode=element.getClass().getSimpleName().matches(PartNode.class.getSimpleName());
		if(!erase) {
			captionElement=elementsContainer.indexOf(element);
			if(elementIsNode)	captionPart=nodesContainer.indexOf((PartNode)element);
			else				captionPart=partsContainer.indexOf((PartBinaryBasic)element);
		}
		element.drawOnMap(matrix, captionElement,camposx, camposy, radiusx, radiusy);
		if(elementIsNode) 	element.drawOnMap(matrixOfNodeIndexes, captionPart,camposx, camposy, radiusx, radiusy);
		else 				element.drawOnMap(matrixOfPartIndexes, captionPart,camposx, camposy, radiusx, radiusy);
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
	/**WARNING ArrayList elements will be cleared*/
	public void removeElements(ArrayList<PartBasic> elements, boolean reIndexPhysicalLayerLabels) {
		
		/**if it is node*/
		int additionalRemovings=0;
		PartBasic element=null;
		for(int i=0;i<elements.size();++i) {
			element=elements.get(i);
			if(element.getClass().getSimpleName().matches(PartNode.class.getSimpleName())) {
				//removeNode((PartNode) element, reIndexPhysicalLayerLabels);
			/**if it is part*/
			}else {
				removePart(element, false);
				elements.set(i, null);
			}
		}
		for(int i=0;i<elements.size();++i) {
			element=elements.get(i);
			if(element==null)continue;
			removeNode((PartNode)element, false);
		}
		if(reIndexPhysicalLayerLabels)
			SchematicManager.reindexAllObjects();
		System.out.println("Done: SchemeStorage.removeElements, with count of"+Integer.toString(elements.size()));
		elements.clear();
	}
	/**
	 * redrawVisualLayer: panel will be redrawn *
	 * redrawPhysicalLayer: physical array-panel will be redrawn *
	 * reIndexPhysicalLayerLabels: sends to all parts and nodes its own indexes in lists */
	public void removeElement(PartBasic element, boolean reIndexPhysicalLayerLabels) {
		
		/**if it is node*/
		if(element.getClass().getSimpleName().matches(PartNode.class.getSimpleName())) {
			removeNode((PartNode) element, reIndexPhysicalLayerLabels);
		/**if it is part*/
		}else
			removePart(element, reIndexPhysicalLayerLabels);
		if(reIndexPhysicalLayerLabels)
			SchematicManager.reindexAllObjects();
		System.out.println("Done: SchemeStorage.removeElement( element = "+element.getClass().getName()+")");
	}
	private void removeNode(PartNode node, boolean reIndexPhysicalLayerLabels) {
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
	private void removePart(PartBasic part, boolean reIndexPhysicalLayerLabels) {
		//drawElementOnMatrix(part, true);
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
	public ArrayList<PartBasic> whatElementsInZone(int x0, int y0, int x1, int y1) {
		ArrayList<PartBasic> result = new ArrayList<PartBasic>();
		ArrayList<Integer> partsIndexes = new ArrayList<Integer>();
		int[][] selectedMatrix=selectedMatrix();
		int index=0;
		if(x1<x0) {index=x0;x0=x1;x1=index;}
		if(y1<y0) {index=y0;y0=y1;y1=index;}
		for(int x=x0;x<=x1;++x) {
			for(int y=y0;y<=y1;++y) {
				index=selectedMatrix[x][y];
				if(index==-1)continue;
				if(partsIndexes.contains(index))continue;
				partsIndexes.add(index);
			}
		}
		for(int i=0;i<partsIndexes.size();++i) {
			result.add((PartBasic) (selectedContainer().get(partsIndexes.get(i))));
		}
		partsIndexes.clear();
		return result;
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
		DataTable discuss = TextForum.readHTMPage(fileName);
		CommonData.dataManager.addManagedElement(discuss);
		//CommonData.discuss=discuss;
	}
}
