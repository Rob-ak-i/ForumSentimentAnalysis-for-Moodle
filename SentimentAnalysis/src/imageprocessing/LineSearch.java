package imageprocessing;

import java.util.ArrayList;

import util.Dot;
import util.DotDouble;

public class LineSearch {
	private static int[] vecSearchDevX = { 1, 0,-1, 0, 1,-1,-1, 1};
	private static int[] vecSearchDevY = { 0, 1, 0,-1, 1, 1,-1,-1};
	
	public static int vecLengthUpTrigger = 64;//��� �����������
	public static int vecLengthDownTrigger = 36;
	//public static int vecLengthUpTrigger = 400;//��� ������������� ���� ������ 5 �� 5
	//public static int vecLengthDownTrigger = 50;
	
	//public static int neighborsMaxRadius = 5;
	
	public static int initialSearchRadius = 5;
	public static int neighborsOnLineSearchRadius = 3;
	
	//private int[][][]lineArray=null;
	
	public int width, height;
	public ArrayList<Dot> dotsStorage;
	public ArrayList<Circuit> circuits;
	
	//������ ������� ����� �� �����, ������ �������+1, ����� �������� ��� �� �������� ��������(���������� ������)
	IntMap dotsMap;
	
	//����������� ������ ��� �������������� ������� �� ������ ������
	IntMap dotsMapShared;

	public ArrayList<Ellipse> ellipseArray;
	public ArrayList<Box> lineArray;
	public IntMap ellipseMap;
	
	private int[][][] specialArray;
	
	public static ArrayList<Circuit> filterCircuits(ArrayList<Circuit> circuits){
		//������ ���������, ������� ���������, ������� ������, � �.�.
		//TODO ������ ���� �������� �� new2
		ArrayList<Circuit> result = new ArrayList<Circuit>();
		Circuit tmpcrc=null;
		int medLen=0, maxLen=0, nowLen0;
		for(int i=0;i<circuits.size();++i) {
			nowLen0=circuits.get(i).dotLinkList.size();
			if(maxLen<nowLen0)maxLen=nowLen0;
			medLen+=nowLen0;
		}
		medLen/=circuits.size();
		medLen=(medLen+maxLen)/2;
		int nowLen=0;
		for(int i=0;i<circuits.size();++i) {
			nowLen=circuits.get(i).dotLinkList.size();
			if((2*nowLen>medLen)&&(nowLen<2*medLen))
				result.add(circuits.get(i));
			else
				circuits.get(i).free();
		}
		circuits.clear();
		return result;
	}
	public void drawCircuits(String fileName) {
		IntMap tempOut = new IntMap(width, height);
		int color;
		int pointIndex;
		Circuit nowCircuit;
		int k=0;
		for(int i=0;i<circuits.size();++i) {
			k=(int) (Math.random()*16777216);
			color=k;//color=(i+1)+(i+1)*256+(i+1)*65536;
			nowCircuit=circuits.get(i);
			//nowCircuit.draw(tempOut, color);
				for(int j=0;j<nowCircuit.dotLinkList.size();++j) {
					pointIndex=(int)nowCircuit.dotLinkList.get(j);
					tempOut.put(dotsStorage.get(pointIndex).x, dotsStorage.get(pointIndex).y, color);
				}
				
		}
		if(fileName==null)fileName="C:\\Zen\\finalCircuits.bmp";
		tempOut.drawBitMap(fileName);
	}
	//TODO ��������
	private void linearizeCircuits(int scales) {
		ArrayList<Dot> dotsStorageNew = new ArrayList<Dot>();
		//������ ������� ����� �� �����, ������ �������+1, ����� �������� ��� �� �������� ��������(���������� ������)
		IntMap dotsMapNew=new IntMap(width, height);dotsMapNew.fill(-1);//dotsStorageNew.add(null);
		Circuit nowCircuit;
		int tempX=0, tempY=0;
		ArrayList<Integer> nowCircuitDotsNew;
		boolean flagContinueOnZero=false;
		boolean flagLast=false;
		boolean flagStart=false;
		for (int i =0 ; i<circuits.size();++i) {
			nowCircuit=circuits.get(i);
			nowCircuitDotsNew=new ArrayList<Integer>();
			flagContinueOnZero=false;
			flagLast=false;
			flagStart=false;
			tempX=0;
			tempY=0;
			if(nowCircuit.dotLinkList.size()>scales)
				for (int j =0 ; j<nowCircuit.dotLinkList.size();++j) {
					tempX+=dotsStorage.get(nowCircuit.dotLinkList.get(j)).x;
					tempY+=dotsStorage.get(nowCircuit.dotLinkList.get(j)).y;
					if(flagContinueOnZero) {
						if (j%scales==0){
							if(flagStart) {
								tempX/=scales;
								tempY/=scales;
							}else {
								tempX/=scales+1;
								tempY/=scales+1;
								flagStart=true;
							}
							flagLast=true;
						}else {
							if(j==nowCircuit.dotLinkList.size()-1) {
								tempX/=j%scales;
								tempY/=j%scales;
								flagLast=true;
							} 
						}
						if(flagLast) {
							flagLast=false;
							dotsStorageNew.add(new Dot(tempX,tempY));
							nowCircuitDotsNew.add(dotsStorageNew.size()-1);
							
							dotsMapNew.data[tempX][tempY]=dotsStorageNew.size()-1;
							tempX=0;
							tempY=0;
						}
					}else
						flagContinueOnZero=true;
				}
			nowCircuit.dotLinkList.clear();
			nowCircuit.dotLinkList=null;
			nowCircuit.dotLinkList=nowCircuitDotsNew;
		}
		dotsMap.clear();
		dotsMap=null;
		dotsMap=dotsMapNew;
		//for(int i=0;i<dotsStorage.size();++i) dotsStorage.get(i).clear();
		dotsStorage.clear();
		dotsStorage=null;
		dotsStorage=dotsStorageNew;
	}
	public void computeCirclesAndDraw(String fileName) {
		ArrayList<Ellipse>ellipses=new ArrayList<Ellipse>(20);
		ellipseMap = new IntMap(width, height);ellipseMap.fill(-1);
		Ellipse ellipseTemp = null;
		IntMap circlesMap = new IntMap(width, height);
		IntMap contoursMap = new IntMap(width, height);
		int k, color, pointIndex;Circuit nowCircuit;
		for (int i =0 ; i<circuits.size();++i) {
			ellipseTemp=circuits.get(i).analyze(dotsStorage);
			if(ellipseTemp!=null) {
				if(ellipseTemp.x>0 && ellipseTemp.x<width && ellipseTemp.y>0 && ellipseTemp.y<height) {
					ellipseTemp.draw(circlesMap, 0x00FF00);
					ellipses.add(ellipseTemp);
					ellipseMap.data[(int)ellipseTemp.x][(int)ellipseTemp.y]=ellipses.size()-1;
					k=(int) (Math.random()*16777216);
					color=k;//color=(i+1)+(i+1)*256+(i+1)*65536;
					nowCircuit=circuits.get(i);
					//nowCircuit.draw(tempOut, color);
						for(int j=0;j<nowCircuit.dotLinkList.size();++j) {
							pointIndex=(int)nowCircuit.dotLinkList.get(j);
							contoursMap.put(dotsStorage.get(pointIndex).x, dotsStorage.get(pointIndex).y, color);
						}
				}else{
					ellipseTemp=null;
				}
			}
		}
		if (fileName==null)fileName="C:\\Zen\\finalCircles22.bmp";
		circlesMap.drawBitMap(fileName.concat("circles.bmp"));
		circlesMap.clear();
		circlesMap=null;
		contoursMap.drawBitMap(fileName.concat("contoursFinalTEST.bmp"));
		contoursMap.clear();
		contoursMap=null;
		if(ellipseArray!=null) {
			ellipseArray.clear();
			ellipseArray = null;
		}
		ellipseArray = ellipses;
	}
	public void free() {
		dotsMap.free();
		dotsMap=null;
		dotsMapShared.free();
		dotsMapShared=null;
		dotsStorage.clear();
		dotsStorage=null;
		for (int i=0;i<circuits.size();++i)
			circuits.get(i).free();
		circuits.clear();
	}
	/*
	public void getAllLines() {
		
		boolean[][] dotsDone=new boolean[width][height];
		int delta;
		int startPosX,startPosY;
		int maxWidth=width-neighborsMaxRadius+1;
		int maxHeight=height-neighborsMaxRadius+1;
		for(int x=neighborsMaxRadius-1;x<(maxWidth);++x) {
			for(int y=neighborsMaxRadius-1;y<(maxHeight);++y) {
				if(((allDots.get(x, y) & 0xFFFFFF)!=16777215))continue;
			
				startPosX=x;
				startPosY=y-neighborsMaxRadius+1;
				for (int i=0;i<neighborsMaxRadius*2;++i) {
					delta=i%2;
					for(int k=0;k<neighborsMaxRadius+delta-1;++k)
						if((allDots.get(startPosX, startPosY)& 0xFFFFFF)==16777215)
							makeLine(x,y, startPosX+k, startPosY+k);//� ��� ��� ������ ��������� ����� - ��������� � � ��������
					if(delta==0)startPosX++;
					else startPosY++;
				 
				}
			}
		}
		//dotsDone=null;
	}
	*/


	//��� ���������� ������ ����� ������=5, ��� ����������� - 3 (�������� ������ - �� ������� � �������� ����������� (���.18 ������ �������) )
	public static int searchNearestNeighborNew(int x, int y, int maxRadius, boolean includeSelf, IntMap dotsMap, IntMap dotsMapShared) {
		int nowX, nowY;
		int incrX=0, incrY=0;
		int direction=0, directionPrev=4;
		if(includeSelf) {
			if ((dotsMap.data[x][y]>=0)&&//���� ����� ���� �
			(dotsMapShared.data[x][y]==0)) {//��� �� �����������
				dotsMapShared.data[x][y]+=1;//�� �������� �, ��� ������������� � ������� � ������
				return dotsMap.data[x][y];
			}
		}
		for (int i=0;i<vecSearchDevX.length;++i) {
			nowX=x+vecSearchDevX[i];
			nowY=y+vecSearchDevY[i];
			if((nowX>=0)&&(nowY>=0)&&(nowX<dotsMap.width())&&(nowY<dotsMap.height()))
			{
				if((dotsMap.data[nowX][nowY]>=0)&&//���� ����� ���� �
						(dotsMapShared.data[nowX][nowY]==0))//��� �� �����������
				{
					dotsMapShared.data[nowX][nowY]+=1;//�� �������� �, ��� ������������� � ������� � ������
					return dotsMap.data[nowX][nowY];
				}
			}
		}
		for (int r=3;r<=maxRadius;++r) {
			nowX=x+r-1;
			nowY=y;
			for(int k=0;k<4*(r-1);++k) {
				direction=k/(r-1);
				if(directionPrev!=direction) {
					if(direction>1)incrX=1; else incrX=-1;
					if((direction==0)||(direction==3))incrY=1; else incrY=-1;
					directionPrev=direction;
				}
				if((nowX>=0)&&(nowY>=0)&&(nowX<dotsMap.width())&&(nowY<dotsMap.height()))
				{
					if((dotsMap.data[nowX][nowY]>=0)&&//���� ����� ���� �
							(dotsMapShared.data[nowX][nowY]==0))//��� �� �����������
					{
						dotsMapShared.data[nowX][nowY]+=1;//�� �������� �, ��� ������������� � ������� � ������
						return dotsMap.data[nowX][nowY];
					}
				}
				nowX+=incrX;
				nowY+=incrY;
			}
		}
		return -1;
	}
	/**circlieDirection = unknown, line, clock-right, anticlock-left
	 *                    0        1     2            3*/
	/**circlieDirectionNew = left, right, line		, unknown
	 *                    0        1     2            3*/

	
	private static final int[] incrSideLeftDirection = {0,2,-2,0,1,-1,2,-2,3,-3,4,-4,0,1,-1,1,-1,3,-3,0,0,0,0};
	private static final int[] incrSideRightDirection = {0,-2,2,0,-1,1,-2,2,-3,3,-4,4,0,-1,1,-1,1,-3,3,0,0,0,0};
	private static final int[][] incrSide = {null, null, incrSideRightDirection, incrSideLeftDirection};
	private static final int[] incrRadius = {1,2,2,2,3,3,4,4,3,3,1,1,3,4,4,5,5,5,5,4,5,5,5};
	private static final int searchDotsArrayCount=23;

	private static final double[] incrBySideX = { 1, 0.66666, 0.5, 0.33333, 0,-0.33333,-0.5,-0.66666,-1,-0.66666,-0.5,-0.33333, 0, 0.33333, 0.5, 0.66666};
	private static final double[] incrBySideY = { 0, 0.33333, 0.5, 0.66666, 1, 0.66666, 0.5, 0.33333, 0,-0.33333,-0.5,-0.66666,-1,-0.66666,-0.5,-0.33333};
	private static final double[] mulByRadius = {1,2,2,2,3,3,4,4,3,3,1,1,3,4,4,5,5,5,5,4,5,5,5};
	/**�������� ������ �� ���.40*/
	private static int searchNearestDotForCircle(int x, int y, int prevSide, int circleDirection, IntMap dotsMap, IntMap dotsMapShared) {
		if (circleDirection<2)circleDirection+=2;
		int tmpX,tmpY;
		int w=dotsMap.width(), h=dotsMap.height();
		int nowSide;
		for(int i=0;i<searchDotsArrayCount;++i) {
			nowSide=prevSide+incrSide[circleDirection][i];			if(nowSide>15)nowSide-=16;if(nowSide<0)nowSide+=16;
			tmpX=x+(int)(incrBySideX[nowSide]*mulByRadius[i]);
			tmpY=y+(int)(incrBySideY[nowSide]*mulByRadius[i]);
			if((tmpX<0)||(tmpY<0)||(tmpX>=w)||(tmpY>=h))continue;
			if ((dotsMap.data[tmpX][tmpY]>=0)&&//���� ����� ���� �
			(dotsMapShared.data[tmpX][tmpY]==0)) {//��� �� �����������
				dotsMapShared.data[tmpX][tmpY]+=1;//�� �������� �, ��� ������������� � ������� � ������
				return dotsMap.data[tmpX][tmpY];
			}
		}
		return -1;
	}
	private static int tailMaxLen = 10;
	private static int sidesCount = 4;
	/**[prevDir], [nowDir]*/
	private static final int[][] getDirection =
		{
				{1,3,3,3,3,3,3,3,4,2,2,2,2,2,2,2},
				{2,1,3,3,3,3,3,3,3,4,2,2,2,2,2,2},
				{2,2,1,3,3,3,3,3,3,3,4,2,2,2,2,2},
				{2,2,2,1,3,3,3,3,3,3,3,4,2,2,2,2},
				{2,2,2,2,1,3,3,3,3,3,3,3,4,2,2,2},
				{2,2,2,2,2,1,3,3,3,3,3,3,3,4,2,2},
				{2,2,2,2,2,2,1,3,3,3,3,3,3,3,4,2},
				{2,2,2,2,2,2,2,1,3,3,3,3,3,3,3,4},
				{4,2,2,2,2,2,2,2,1,3,3,3,3,3,3,3},
				{3,4,2,2,2,2,2,2,2,1,3,3,3,3,3,3},
				{3,3,4,2,2,2,2,2,2,2,1,3,3,3,3,3},
				{3,3,3,4,2,2,2,2,2,2,2,1,3,3,3,3},
				{3,3,3,3,4,2,2,2,2,2,2,2,1,3,3,3},
				{3,3,3,3,3,4,2,2,2,2,2,2,2,1,3,3},
				{3,3,3,3,3,3,4,2,2,2,2,2,2,2,1,3},
				{3,3,3,3,3,3,3,4,2,2,2,2,2,2,2,1},
				
		};
	//**unknown, line, clock-right, anticlock-left*/
	private static int getCircleDirection(int normalDirection){
		switch (normalDirection) {
		case -1: return 3;
		case 0: return 1;
		case 1: return 2;
		}
		return 0;
	}
	//**-1 ������, 0 �����, 1 �������*/
	private static final int[] getNormalDirection = {0, 0, 1, -1};
	//**�� ���� - ��� getDirection, ��������������� ����� getNormalDirection, �� � ������������*/
	private static final int[][] getRighterMove =
		{
				{ 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1},
				{ 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2},
				{ 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3},
				{ 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4},
				{ 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5},
				{ 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7, 6},
				{ 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0, 7},
				{ 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7,-0},
				{-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6,-7},
				{-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5,-6},
				{-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4,-5},
				{-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3,-4},
				{-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2,-3},
				{-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1,-2},
				{-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0,-1},
				{-1,-2,-3,-4,-5,-6,-7,-0, 7, 6, 5, 4, 3, 2, 1, 0},
				
		};
	private static final double[][] getRadius15by15 =
		{
				{ 9.899495, 9.219544, 8.602325, 8.062258, 7.615773, 7.280110, 7.071068, 7.000000, 7.071068, 7.280110, 7.615773, 8.062258, 8.602325, 9.219544, 9.899495},
				{ 9.219544, 8.485281, 7.810250, 7.211103, 6.708204, 6.324555, 6.082763, 6.000000, 6.082763, 6.324555, 6.708204, 7.211103, 7.810250, 8.485281, 9.219544},
				{ 8.602325, 7.810250, 7.071068, 6.403124, 5.830952, 5.385165, 5.099020, 5.000000, 5.099020, 5.385165, 5.830952, 6.403124, 7.071068, 7.810250, 8.602325},
				{ 8.062258, 7.211103, 6.403124, 5.656854, 5.000000, 4.472136, 4.123106, 4.000000, 4.123106, 4.472136, 5.000000, 5.656854, 6.403124, 7.211103, 8.062258},
				{ 7.615773, 6.708204, 5.830952, 5.000000, 4.242641, 3.605551, 3.162278, 3.000000, 3.162278, 3.605551, 4.242641, 5.000000, 5.830952, 6.708204, 7.615773},
				{ 7.280110, 6.324555, 5.385165, 4.472136, 3.605551, 2.828427, 2.236068, 2.000000, 2.236068, 2.828427, 3.605551, 4.472136, 5.385165, 6.324555, 7.280110},
				{ 7.071068, 6.082763, 5.099020, 4.123106, 3.162278, 2.236068, 1.414214, 1.000000, 1.414214, 2.236068, 3.162278, 4.123106, 5.099020, 6.082763, 7.071068},
				{ 7.000000, 6.000000, 5.000000, 4.000000, 3.000000, 2.000000, 1.000000, 0.000000, 1.000000, 2.000000, 3.000000, 4.000000, 5.000000, 6.000000, 7.000000},
				{ 7.071068, 6.082763, 5.099020, 4.123106, 3.162278, 2.236068, 1.414214, 1.000000, 1.414214, 2.236068, 3.162278, 4.123106, 5.099020, 6.082763, 7.071068},
				{ 7.280110, 6.324555, 5.385165, 4.472136, 3.605551, 2.828427, 2.236068, 2.000000, 2.236068, 2.828427, 3.605551, 4.472136, 5.385165, 6.324555, 7.280110},
				{ 7.615773, 6.708204, 5.830952, 5.000000, 4.242641, 3.605551, 3.162278, 3.000000, 3.162278, 3.605551, 4.242641, 5.000000, 5.830952, 6.708204, 7.615773},
				{ 8.062258, 7.211103, 6.403124, 5.656854, 5.000000, 4.472136, 4.123106, 4.000000, 4.123106, 4.472136, 5.000000, 5.656854, 6.403124, 7.211103, 8.062258},
				{ 8.602325, 7.810250, 7.071068, 6.403124, 5.830952, 5.385165, 5.099020, 5.000000, 5.099020, 5.385165, 5.830952, 6.403124, 7.071068, 7.810250, 8.602325},
				{ 9.219544, 8.485281, 7.810250, 7.211103, 6.708204, 6.324555, 6.082763, 6.000000, 6.082763, 6.324555, 6.708204, 7.211103, 7.810250, 8.485281, 9.219544},
				{ 9.899495, 9.219544, 8.602325, 8.062258, 7.615773, 7.280110, 7.071068, 7.000000, 7.071068, 7.280110, 7.615773, 8.062258, 8.602325, 9.219544, 9.899495}
				};

	private static final int[][] sideIncrArray = {
			{//left
				0,1,-1,2,-2,3,-3,4,-4
			},
			{//right
				0,-1,1,-2,2,-3,3,-4,4
			}
	};
	private int [] searchNeighborByPiority(int x, int y, int side, int direction) {
		int counter=0;
		int tempSide;
		int[] result = null;
		for(int r=1;r<5;++r) {
			for(int incrCell=0;incrCell<r*2+1;++incrCell) {
				counter++;
				if(counter>16)break;
				tempSide=side+sideIncrArray[3-direction][incrCell];
				result=specialArray[tempSide][r];
				if(result.length>=1)counter=20;
			}
		}
		return result;
	}
	private static double lineTrigger = 3;
	private static double radiusMaxDeviation = 8;
	private static double radiusByDirectionsMaxDeviation = 15;
	//TODO dbg 1day
	public int starts=0;
	public int cyclesIntro=0;
	public int cyclesIntro2=0;
	public int cyclesTailLong=0;
	
	
	private static int signum(double arg) {
		if(arg>0)return 1;
		if(arg<0)return 0;
		return 0;
	}
	private void makeCircuitNew(int x, int y) {
		dotsMapShared.data[x][y]+=1;
		int pointIndex=dotsMap.data[x][y];//searchNearestNeighborNew(x, y, initialSearchRadius, true);
		//if(pointIndex <=0)return;
		//int nowDX=0, nowDY=0;
		int nowX=x;//dotsStorage.get(pointIndex).x,
		int nowY=y;//=dotsStorage.get(pointIndex).y;
		Circuit newCircuit = new Circuit();
		circuits.add(newCircuit);
		//if((dotsMap.data[x][y]!=0)&&
		//(dotsMapShared.data[nowX][nowY]==0)) newCircuit.dotLinkList.add(dotsMap.data[x][y]);
		//if(true){
		//	nowDX=nowX-x;//dotsStorage.get(dotsMap.data[x][y]).x-x;
		//	nowDY=nowY-y;//dotsStorage.get(dotsMap.data[x][y]).y-y;
		//}
		while(true) {
			//dotsMapShared.data[nowX][nowY]+=1;
			newCircuit.dotLinkList.add(pointIndex);//(dots.get(pointIndex));

			//nowX+=nowDX;
			//nowY+=nowDY;
			if((nowX<0)||(nowY<0)||(nowX>=width)||(nowY>=height))return;
			pointIndex=searchNearestNeighborNew(nowX, nowY, neighborsOnLineSearchRadius, false, dotsMap, dotsMapShared);
			if(pointIndex==-1)return;
			nowX=dotsStorage.get(pointIndex).x;//nowDX+=dotsStorage.get(pointIndex).x-nowX;// - ��� ����� � ���������
			nowY=dotsStorage.get(pointIndex).y;//nowDY+=dotsStorage.get(pointIndex).y-nowY;// - ��� ����� � ���������
			//nowDX=(int)Math.floor((double)(dotsStorage.get(pointIndex).x-nowX + nowDX)/2);// - ����������
			//nowDY=(int)Math.floor((double)(dotsStorage.get(pointIndex).y-nowY + nowDY)/2);
			//if(nowDX==0 && nowDY==0)return;
		}
	}
	//��� ���������� ������ ����� ������=5, ��� ����������� - 3 (�������� ������ - �� ������� � �������� ����������� (���.18 ������ �������) )
		private int searchNearestNeighbor(int srcIndex, int maxRadius) {
			int nowX, nowY;
			int incrX=0, incrY=0;
			int direction=0, directionPrev=4;
			int pointX=dotsStorage.get(srcIndex).x;
			int pointY=dotsStorage.get(srcIndex).y;
			for (int r=2;r<=maxRadius;++r) {
				nowX=pointX+r-1;
				nowY=pointY;
				for(int k=0;k<4*(r-1);++k) {
					direction=k/(r-1);
					if(directionPrev!=direction) {
						if(direction>1)incrX=1; else incrX=-1;
						if((direction==0)||(direction==3))incrY=1; else incrY=-1;
						directionPrev=direction;
					}
					if((nowX>=0)&&(nowY>=0)&&(nowX<width)&&(nowY<height))
					{
						if((dotsMap.data[nowX][nowY]>=0)&&//���� ����� ���� �
								(dotsMapShared.data[nowX][nowY]==0))//��� �� �����������
						{
							dotsMapShared.data[nowX][nowY]+=1;//�� �������� �, ��� ������������� � ������� � ������
							return dotsMap.data[nowX][nowY];
						}
					}
					nowX+=incrX;
					nowY+=incrY;
				}
			}
			return -1;
		}
		
		private void makeCircuit(int x, int y) {
			dotsMapShared.data[x][y]+=1;
			int pointIndex=searchNearestNeighbor(dotsMap.data[x][y], initialSearchRadius);
			if(pointIndex ==-1)return;
			int nowDX=0, nowDY=0;
			int nowX=x,nowY=y;
			Circuit newCircuit = new Circuit();
			circuits.add(newCircuit);
			while(true) {
				//dotsMapShared.data[nowX][nowY]+=1;
				newCircuit.dotLinkList.add(pointIndex);//(dots.get(pointIndex));
				nowDX=dotsStorage.get(pointIndex).x-nowX;
				nowDY=dotsStorage.get(pointIndex).y-nowY;
				nowX+=nowDX;
				nowY+=nowDY;
				pointIndex=searchNearestNeighbor(dotsMap.data[nowX][nowY], neighborsOnLineSearchRadius);
				if(pointIndex<0)return;
				if((nowX<0)||(nowY<0)||(nowX>width)||(nowY>height))return;
			}
		}
	//��� ��������� �����
	public static void drawCircle(int x,int y, int r, int color, IntMap img) {
		Ellipse.V_BRcirc(x, y, r, color, img);
	}
	public static void drawLine(int x0, int y0, int x1, int y1, int color, IntMap dest) {
		Ellipse.line(x0, y0, x1, y1, color, dest);
	}

}
class Circuit{
	public ArrayList<Integer> dotLinkList;
	public Circuit() {
		dotLinkList = new ArrayList<Integer>();
	}
	public void free() {
		dotLinkList.clear();
		dotLinkList=null;
	}
	private static int dotsCount=5;
	public Ellipse analyze(ArrayList<Dot> dotsStorage) {
		int count=dotLinkList.size();
		if(count<20)return null;//40
		int tempX=0,tempY=0;
		Ellipse result = new Ellipse();
		result.contourlen=count;
		Dot nowDot;
		
		DotDouble []points=new DotDouble[dotsCount];//0 - A, 1 - B, 2 - M, 3 - X
		int j=0;
		int roughCount=2;
		//���������� �������� ����������� ��������� ������� � ������ � ����� � ���������� 29.05.18
		for(int k=0;k<dotsCount;++k) {
			tempX=0;
			tempY=0;
			for(int i=0;i<roughCount;++i) {//4 - ����������� �����
				if(k==0)j=i;
				if(k==1)j=count-i-1;
				if(k==2)j=(count>>>1) - (roughCount>>>1) + i;//�� �����
				if(k==3)j=(i+(count>>>1) - (roughCount>>>1) + i)>>>1;
				if(k==4)j=(count-i-1+(count>>>1) - (roughCount>>>1) + i)>>>1;
				nowDot=dotsStorage.get(dotLinkList.get(j));
				tempX+=nowDot.x;
				tempY+=nowDot.y;
			}
			points[k]=new DotDouble(0,0);
			points[k].x=(double)tempX/roughCount;
			points[k].y=(double)tempY/roughCount;
		}
		double x0=points[0].x;
		double y0=points[0].y;
		double x1=points[1].x;
		double y1=points[1].y;
		double x2=points[2].x;
		double y2=points[2].y;
		double vectorMGx=(x0+x1-(x2*2))*0.5;
		double vectorMGy=(y0+y1-(y2*2))*0.5;
		double g=Math.sqrt(vectorMGx*vectorMGx+vectorMGy*vectorMGy);
		if(g==0) {
			result=null;
			points=null;
			return null;
		}
		double hordeLengthInSqr=(x1-x0)*(x1-x0)+(y1-y0)*(y1-y0);
		double radius = ((hordeLengthInSqr)/(4*g)+g)*0.5;
		double x=x2+vectorMGx*radius/g;
		double y=y2+vectorMGy*radius/g;
		

		result.radius1=radius;
		result.x=x;
		result.y=y;
		
		boolean trig=true;
		double tmpg;
		for(int i=0;i<0;++i) {
			vectorMGx=(points[i].x+x2-(points[3+i].x*2))*0.5;
			vectorMGy=(points[i].y+y2-(points[3+i].y*2))*0.5;
			tmpg=Math.sqrt(vectorMGx*vectorMGx+vectorMGy*vectorMGy);
			if((tmpg<=g*0.5)||(tmpg>=g*2))trig=false;
		}
		if(!trig)
			result=null;
		points=null;
		
		return result;
	}
}



class primitivesContainer{
	public Ellipse ellipse;
	public Box line;
}