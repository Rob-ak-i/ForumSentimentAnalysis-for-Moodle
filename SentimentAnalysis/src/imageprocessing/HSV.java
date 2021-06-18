package imageprocessing;


public class HSV{
	public double h;
	public double s;
	public double v;
	public static double eps = 0.1F;
	public HSV()
	{
		
	}
	public static int fromRGB(int color) {
		int[] data=RGB.unpack(color);
		RGB a = new RGB();
		a.r=data[1];
		a.g=data[2];
		a.b=data[3];
		HSV b=new HSV();
		b.read(a);
		data[0]=(int)b.h >>>8;
		data[1]=(int)b.h & 0xFF;
		data[2]=(int)(b.s*100.0) & 0xFF;
		data[3]=(int)(b.v*100.0) & 0xFF;
		int result=RGB.pack(data);
		data=null;
		a=null;
		b=null;
		return result;
	}
	//H=0..360
	//S=0..100
	//V=0..1
	public void read(RGB rgb)
	{
		int flagSide=0;
		double r=rgb.r / 255;
		double g=rgb.g / 255;
		double b=rgb.b / 255; 
		double cmax, cmin;
		if(r>g){
			if(r>b) {
				cmax=r;
				flagSide=1;
			}else {
				cmax=b;
				flagSide=3;
			}
		}else{
			if(g>b) {
				cmax=g;
				flagSide=2;
			}else {
				cmax=b;
				flagSide=3;
			}
		}
		if(r<g){
			if(r<b)
				cmin=r;
			else
				cmin=b;
		}else{
			if(g<b)
				cmin=g;
			else
				cmin=b;
		}
		double delta=cmax-cmin;
		if(delta<=eps)
			h=0;
		else {
			h=60;
			switch(flagSide){
			case 1:
				h*=((g-b)/delta)%6;
				break;
			case 2:
				h*=((b-r)/delta)+2;
				break;
			case 3:
				h*=((r-g)/delta)+4;
				break;
			}
			if(h<0)h+=360;
			if(h>360)h-=360;
		}
		if(cmax<=eps)
			s=0;
		else
			s=delta/cmax;
		v=cmax;
	}
	
}