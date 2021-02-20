package common;


public class MatrixHelper {
	
	


	public static int searchNearestNeighbor(int x, int y, int maxRadius, boolean includeSelf, int[][] dotsMap, /*int[][]  dotsMapShared, */int forbiddenCaption) {
		/**x-axis coordinates by radius=1 from dot*/
		final int[] vecSearchDevX = { 1, 0,-1, 0, 1,-1,-1, 1};
		/**y-axis coordinates by radius=1 from dot*/
		final int[] vecSearchDevY = { 0, 1, 0,-1, 1, 1,-1,-1};
		
		int width=dotsMap.length;
		int height=dotsMap[0].length;
		
		int nowX, nowY;
		int incrX=0, incrY=0;
		int direction=0, directionPrev=4;
		
		if(includeSelf) {
			if (
					(dotsMap[x][y]!=forbiddenCaption)//if dot exists and
					//&&(dotsMapShared[x][y]==0)//it is not already scanned
			) 
			{
				//dotsMapShared[x][y]+=1;//then mark it as scanned and return its index
				return dotsMap[x][y];
			}
		}
		for (int i=0;i<vecSearchDevX.length;++i) {
			nowX=x+vecSearchDevX[i];
			nowY=y+vecSearchDevY[i];
			if((nowX>=0)&&(nowY>=0)&&(nowX<width)&&(nowY<height))
			{
				if(
						(dotsMap[nowX][nowY]!=forbiddenCaption)//if dot exists
						//&&(dotsMapShared.data[nowX][nowY]==0)//and it is not already scanned
						)
				{
					//dotsMapShared.data[nowX][nowY]+=1;//then mark it as scanned and return its index
					return dotsMap[nowX][nowY];
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
				if((nowX>=0)&&(nowY>=0)&&(nowX<width)&&(nowY<height))
				{
					if(
							(dotsMap[nowX][nowY]!=forbiddenCaption)//if dot exists
							//&&(dotsMapShared[nowX][nowY]==0)//and it is not already scanned
							)
					{
						//dotsMapShared[nowX][nowY]+=1;//then mark it as scanned and return its index
						return dotsMap[nowX][nowY];
					}
				}
				nowX+=incrX;
				nowY+=incrY;
			}
		}
		return -1;
	}
	/**it is used for collision check*/
	public static int checkZoneExtended(int[][]mat, int x0,int y0, int x1, int y1, int radius, int excludingCaption) {
		x0-=radius;
		y0-=radius;
		x1+=radius;
		y1+=radius;
		return checkZone(mat, x0, y0, x1, y1, excludingCaption);
	}
	/**it is used for collision check*/
	public static int checkZone(int[][]mat, int x0,int y0, int x1, int y1, int excludingCaption) {
		int w=mat.length;
		int h=mat[0].length;
		if(x0<0)x0=0;
		if(y0<0)y0=0;
		if(x1>=w)x1=w-1;
		if(y1>=h)y1=h-1;
		for(int i=x0;i<x1;++i)
			for(int j=y0;j<y1;++j)
				if(mat[i][j]!=excludingCaption)return mat[i][j];
		return excludingCaption;
	}
	/**it is used for filling zone(deleting/adding part)*/
	public static void fillZone(int[][]mat, int x0,int y0, int x1, int y1, int caption) {
		int w=mat.length;
		int h=mat[0].length;
		if(x0<0)x0=0;
		if(y0<0)y0=0;
		if(x1>=w)x1=w-1;
		if(y1>=h)y1=h-1;
		for(int i=x0;i<x1;++i)
			for(int j=y0;j<y1;++j)
				mat[i][j]=caption;
	}

	public static void drawLine(int[][]mat, int x0,int y0, int x1, int y1, int caption) {
		int w=mat.length;
		int h=mat[0].length;
		int errx=0,erry=0,dx=Math.abs(x1-x0),dy=Math.abs(y1-y0),delta=Math.max(dx, dy),incx=(int)Math.signum(x1-x0),incy=(int)Math.signum(y1-y0);
		int x=x0,y=y0;
		mat[x][y]=caption;
		while(x!=x1||y!=y1){
			errx+=dx;
			erry+=dy;
			if(errx>=delta){
				x+=incx;
				errx-=delta;
				mat[x][y]=caption;
			}
			if(erry>=delta){
				y+=incy;
				erry-=delta;
				mat[x][y]=caption;
			}
		}
		mat[x1][y1]=caption;
	}
	
	public static void clear(int[][]mat) {fill(mat,-1);}
	//------------------LL------------------
	public static void fill(int[][] mat, int caption) {
		int w=mat.length;
		int h=mat[0].length;
		for(int i=0;i<w;++i)
			for(int j=0;j<h;++j) {
				mat[i][j]=caption;
			}
	}
	public static void resize(int[][] mat,int width, int height){
		if(mat!=null)mat=null;
		mat = new int[width][height];
	}
}
