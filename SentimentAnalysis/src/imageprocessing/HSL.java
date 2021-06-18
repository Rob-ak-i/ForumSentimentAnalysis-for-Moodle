package imageprocessing;

public class HSL {
	public static double eps=0.00001;
	public int h;
	public int s;
	public int v;
	
	public static int fromRGB(int ARGB) {
		int flagSide=0;
		double r=((double)((ARGB&0x00FF0000)>>>16))*0.003921568;
		double g=((double)((ARGB&0x0000FF00)>>>8))*0.003921568;
		double b=((double)(ARGB&0x000000FF))*0.003921568;
		double h;
		double s;
		double l;
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
		if(delta<=eps) {
			h=(int)((double)(cmax*255));
			s=0;
			l=0;
		}
		else {
			h=60;
			switch(flagSide){
			case 1:
				h*=((g-b)/delta);
				if(g<b)
					h+=360;
				break;
			case 2:
				h*=((b-r)/delta);
				h+=120;
				break;
			case 3:
				h*=((r-g)/delta);
				h+=240;
				break;
			}
			if(h<0)h+=360;
			if(h>360)h-=360;
		}
		l=(cmax+cmin)*0.5;
		if(l<=0.5) {
			s=(delta)*0.5/l;
		}else {
			s=delta/(2-2*l);
		}
		int hue=(int)h;
		int sat=(int)((double)s*255);
		int lgt=(int)((double)l*255);
		return (hue<<16)+(sat<<8)+(lgt);
	}
	public static int toRGB(int HHSV) {
		int hue=HHSV>>>16;
		int sat=((HHSV&0x0000FF00)>>>8);
		int lgt=HHSV&0x000000FF;
		double h=((double)hue)*0.002777778;
		double s=((double)sat)*0.003921568;
		double l=((double)lgt)*0.003921568;
		double q=0;
		if(l<0.5)
			q=l*(1.0+s);
		else
			q=l+s-(l*s);
		double p=2.0*l-q;
		double[] t = new double[3];
		t[0]=h+0.333333333;
		t[1]=h;
		t[2]=h-0.333333333;
		double[] rgb= new double[3];
		for(int i=0;i<3;++i) {
			if(t[i]<0)t[i]+=1.0;else {
				if(t[i]>1)t[i]-=1.0;
			}
			if(t[i]<0.166666667)
				rgb[i]=p+((q-p)*6.0*t[i]);
			else {
				if(t[i]<0.5)
					rgb[i]=q;
				else {
					if(t[i]<0.666666667)
						rgb[i]=p+((q-p)*(0.666666667-t[i])*6.0);
					else
						rgb[i]=p;
				}
			}
		}
		t=null;
		int r=(int)((double)rgb[0]*255);
		int g=(int)((double)rgb[1]*255);
		int b=(int)((double)rgb[2]*255);
		rgb=null;
		return (r<<16)+(g<<8)+(b);
	}
}
