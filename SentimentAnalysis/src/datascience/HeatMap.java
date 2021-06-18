package datascience;

import java.util.List;
import java.util.Map;

import imageprocessing.Blur;
import imageprocessing.Ellipse;
import imageprocessing.IntMap;
import util.DotDouble;

public class HeatMap {
	
	
	public static int[][] buildFromElements(List<DotDouble> elements, int w, int h){
		double[][] elementsMap=new double [w][h];
		double xmin=-1,ymin=-1,xmax=-1,ymax=-1, x,y,cx,cy,sizex,sizey;
		double xmean=0,ymean=0; double xdisp=0,ydisp=0,xsqrsum=0,ysqrsum=0, n=elements.size();
		DotDouble dot;
		for(int i=0;i<elements.size();++i) {
			dot = elements.get(i);
			x=dot.x;
			y=dot.y;
			xmean+=x;
			ymean+=y;
			if(i==0||xmin>x)xmin=x;
			if(i==0||ymin>y)ymin=y;
			if(i==0||xmax<x)xmax=x;
			if(i==0||ymax<y)ymax=y;
		}
		cx=(xmin+xmax)/2;
		cy=(ymin+ymax)/2;
		sizex=xmax-xmin;
		sizey=ymax-ymin;

		for(int i=0;i<elements.size();++i) {
			dot = elements.get(i);
			x=dot.x;
			y=dot.y;
			xsqrsum=Math.pow(x-xmean, 2);
			ysqrsum=Math.pow(y-ymean, 2);
		}
		xdisp = Math.sqrt(xsqrsum)/(n-1);
		ydisp = Math.sqrt(ysqrsum)/(n-1);
		
		
		
		
		int[][] result = new int[w][h];
		IntMap resultMap = IntMap.getMap(result);
		resultMap.fill(0xffffff);
		int a,b;
		for(int i=0;i<n;++i) {
			dot = elements.get(i);
			x=dot.x;
			y=dot.y;
			a=1+(int)((x-xmin)*(double)(w-3)/sizex);
			b=1+(int)((y-ymin)*(double)(h-3)/sizey);
			//result[a][b]+=1;
			Ellipse.V_BRcirc(a, b, 9, 0x7f7f4a, resultMap,true);
		}
		return resultMap.data;
	}
}
