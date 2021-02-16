package util;

public class DotDouble {
	public double x=0;
	public double y=0;
	public DotDouble(double x, double y){
		this.x=x;
		this.y=y;
	}
	public static DotDouble getDotDouble(int x, int y){
		return new DotDouble(x,y);
	}
	public static DotDouble getDotDouble(double x, double y){
		return new DotDouble(x,y);
	}
}
