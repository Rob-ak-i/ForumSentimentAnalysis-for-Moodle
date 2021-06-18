package imageprocessing;


public class Box{
	public int x0,y0,x1,y1;
	public void limit(int minx, int miny, int maxx, int maxy) {
		if(x0<minx)x0=minx;
		if(y0<miny)y0=miny;
		if(x1>maxx)x1=maxx;
		if(y1>maxy)y1=maxy;
	}
	public void draw(int color, IntMap dest) {
		LineSearch.drawLine(x0, y0, x0, y1, color, dest);
		LineSearch.drawLine(x0, y0, x1, y0, color, dest);
		LineSearch.drawLine(x0, y1, x1, y1, color, dest);
		LineSearch.drawLine(x1, y0, x1, y1, color, dest);
	}
	public void drawDiag(int color, IntMap dest) {
		LineSearch.drawLine(x0, y0, x1, y1, color, dest);
	}
}