package imageprocessing;



public class Ellipse{
	public int contourlen;
	public double x;
	public double y;
	public double radius1;
	public double radius2;
	public double angle;
	
	public Box getBox() {
		Box result = new Box();
		result.x0=(int)(x-radius1);
		result.y0=(int)(y-radius1);
		result.x1=(int)(x+radius1);
		result.y1=(int)(y+radius1);
		return result;
	}
	public void draw(IntMap dest, int color, boolean zalivka) {
		if(x<0 || x>dest.width() || y<0 || y>dest.height() || radius1>(dest.height()+dest.width()))return;
		V_BRcirc ((int)x, (int)y, (int)radius1, color, dest, zalivka);
	}
	public static void V_BRcirc (int X1, int Y1, int R, int color, IntMap dest, boolean zalivka)
	{  
	 // R - ������, X1, Y1 - ���������� ������
	   int x = 0;
	   int y = R;
	   int delta = 1 - 2 * R;
	   int error = 0;
	   while (y >= 0) {
		   if(!zalivka) {
		       drawpixel(X1 + x, Y1 + y, color, dest);
		       drawpixel(X1 + x, Y1 - y, color, dest);
		       drawpixel(X1 - x, Y1 + y, color, dest);
		       drawpixel(X1 - x, Y1 - y, color, dest);
		   }else
		       for(int i=-y;i<y;++i) {
			       drawpixel(X1 + x, Y1 +i , color, dest);
			       drawpixel(X1 - x, Y1 + i, color, dest);
		       }
	       error = 2 * (delta + y) - 1;
	       if ((delta < 0) && (error <= 0)) {
	           delta += 2 * ++x + 1;
	           continue;
	       }
	       error = 2 * (delta - x) - 1;
	       if ((delta > 0) && (error > 0)) {
	           delta += 1 - 2 * --y;
	           continue;
	       }
	       x++;
	       delta += 2 * (x - y);
	       y--;
	   }
	}
	public static void line(int x0, int y0, int x1, int y1, int color, IntMap dest) {
		int deltax = (x1 - x0);
		int deltay = (y1 - y0);
		int errorX = 0;
		int errorY = 0;
		int incrX = 1; if(deltax<0) {incrX=-1;deltax=-deltax;}if(deltax==0)incrX=0;
		int incrY = 1; if(deltay<0) {incrY=-1;deltay=-deltay;}if(deltay==0)incrY=0;
		int deltaerr; if(deltax>deltay)deltaerr=deltax; else deltaerr=deltay;
		int curX=x0;
		int curY=y0;
		drawpixel(curX,curY, color, dest);
		while((curX != x1) || (curY != y1)) {
			errorX+=deltax;
			errorY+=deltay;
			if(errorX >= deltaerr)  {
				errorX-=deltaerr;
				curX+=incrX;
			}
			if(errorY >= deltaerr)  {
				errorY-=deltaerr;
				curY+=incrY;
			}
			drawpixel(curX,curY, color, dest);
		}
	}
	private static void drawpixel (int x, int y, int color, IntMap dest)
	{
		if((x<dest.cols())&&(x>0)&&(y<dest.rows())&&(y>0))
			dest.data[x][y]= color;
	}  
}