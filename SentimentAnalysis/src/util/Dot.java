package util;

public class Dot {
	public int x=0;
	public int y=0;
	public Dot(int x, int y){
		this.x=x;
		this.y=y;
	}
	public static Dot getDot(int x, int y){
		return new Dot(x,y);
	}
	public static Dot getDot(double x, double y){
		return new Dot((int)x,(int)y);
	}
}
