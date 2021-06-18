package imageprocessing;

public class RGB{
	public double r;
	public double g;
	public double b;
	public static float[][] arrX2R = 
		{ 
			{ 0.41847F, -0.15866F, -0.082835F}, 
			{ -0.091169F, 0.25243F, 0.015708F},
			{ 0.00092090F, -0.0025498F, 0.17860F} 
		};

	public static double eps = 0.1F;
	public RGB()
	{
		
	}
	public static int fromHSV(int colorHSV) {
		RGB a = new RGB();
		HSV b = new HSV();
		b.h = colorHSV>>>16;
		b.s = (colorHSV>>>8)&0xFF;
		b.v = colorHSV&0xFF;
		a.read(b);
		int[] data=new int[4];
		data[0]=0;
		data[1]=(int)a.r;
		data[2]=(int)a.g;
		data[3]=(int)a.b;
		int result = RGB.pack(data);
		data=null;
		a=null;
		b=null;
		return result;
		
	}
	public static int fromMatData(double[] data) {
		int result;
		int []dataInt=new int[data.length];
		for(int i=0;i<data.length;++i)dataInt[i]=(int)data[i];
		result = packArgbFromRgba(dataInt);
		dataInt=null;
		return result;
	}
	public static int fromMatData8UC3(byte[] data) {
		int result;
		int []dataInt=new int[data.length];
		for(int i=0;i<data.length;++i)dataInt[i]=(int)data[i];
		result = packArgbFromRgba(dataInt);
		dataInt=null;
		return result;
	}
	public static double[] toMatData(int ARGB) {
		double []data=new double[4];
		int[] dataInt = unpack(ARGB);
		data[0]=dataInt[1];
		data[1]=dataInt[2];
		data[2]=dataInt[3];
		data[3]=dataInt[0];
		return data;
	}
	public static byte[] toMatData8UC(int ARGB, int channels) {
		byte []data=new byte[channels];
		int[] dataInt = unpack(ARGB);
		if(channels==3) {
			data[2]=(byte) dataInt[3];
			data[1]=(byte) dataInt[2];
			data[0]=(byte) dataInt[1];
		}
		if(channels==1)
			data[0]=(byte) dataInt[3];
		//data[3]=dataInt[0];
		dataInt=null;
		return data;
	}
	public static int[] unpack(int ARGB)
	{
		int[] data=new int[4];
		data[0]=ARGB >>> 24;
		data[1]=ARGB << 8 >>> 24;
		data[2]=ARGB << 16 >>> 24;
		data[3]=ARGB << 24 >>> 24;
		return data;
	}
	public static int luminance(int colorARGB) {
		//ARGB = FFFFFFFF & RGB
		//Y = (R+R+R+B+G+G+G+G)>>3
		colorARGB&=16777215;
		int r=colorARGB>>>16;//int r=colorARGB>>>15;//r=R*2+B>>7
		r*=3;//r=r&0x1FE+r>>>1;//r=3*R
		
		int g=colorARGB & 0xFF00;//g=G<<8
		g>>>=6;//не на 8, потому что нужно ещё умножить на 4 (g=G<<2)
		int rgb=colorARGB & 0xFF + r+g;//rgb=B+3*R+4*G
		rgb>>>=3;
		return rgb;
	}
	public static int luminanceBetter(int colorARGB) {
		//ARGB = FFFFFFFF & RGB
		//Y = (R+R+B+G+G+G)/6
		colorARGB&=16777215;
		int r=colorARGB>>>15;//int r=colorARGB>>>15;//r=R*2+B>>7
		r=r&0x1FE;
		
		int g=colorARGB & 0xFF00;//g=G<<8
		g>>>=8;
		g*=3;
		int rgb=colorARGB & 0xFF + r+g;//rgb=B+3*R+4*G
		rgb/=6;
		return rgb;
	}
	public static int luminanceFancy(int colorARGB) {
		//ARGB = FFFFFFFF & RGB
		//Y = (R+R+B+G+G+G)/6
		colorARGB&=16777215;
		int r=colorARGB>>>16;
		int g=colorARGB & 0xFF00;
		g>>>=8;
		int b=colorARGB & 0xFF;
		double y=0.299*r + 0.587*g + 0.114*b;
		return (int)y;
	}
	public static int pack(int[] dataARGB)
	{
		int ARGB=0;
		ARGB = (dataARGB[0] << 24)
			 + (dataARGB[1] << 16)
			 + (dataARGB[2] << 8)
			 + (dataARGB[3]);
		return ARGB;
	}
	public static int getRandom() {
		return (int) (Math.random()*16777215);
	}
	public static int packArgbFromRgba(int[] dataRGBA)
	{
		int ARGB=0;
		int len=dataRGBA.length;
		if(len==1)return dataRGBA[0];
		if(len>4)return RGB.getRandom();
		ARGB = //(dataRGBA[3] << 24)
			 + (dataRGBA[0] << 16)				
			 + (dataRGBA[1] << 8)
			 + (dataRGBA[2]);
		if(len==4)ARGB+=dataRGBA[3] << 24;
		return ARGB;
	}
	public static int greyValue(double coefficient) {
		int caption;
		caption=(int)(coefficient);
		caption+=(int)(coefficient)<<8;
		caption+=(int)(coefficient)<<16;
		return caption;
	}
	public static double getDelta4Channels(int point1, int point2) {
		if((point1==-1)||(point2==-1))return 0;
		int[]data1=unpack(point1);
		int[]data2=unpack(point2);
		double result=0;
		for (int i=0;i<4;++i)
			result +=(data1[i]-data2[i]) 
					* (data1[i]-data2[i]);
		data1=null;
		data2=null;
		return Math.sqrt(result);
	}
	public static double getDiagonal(double a, double b) {
		double result=0;
			result =(a)*(a)+(b)*(b);
		return Math.sqrt(result);
	}
	public void read(XYZ xyz)
	{
		r=xyz.x*arrX2R[1][1]+xyz.y*arrX2R[1][2]+xyz.z*arrX2R[1][3];
		g=xyz.x*arrX2R[2][1]+xyz.y*arrX2R[2][2]+xyz.z*arrX2R[2][3];
		b=xyz.x*arrX2R[3][1]+xyz.y*arrX2R[3][2]+xyz.z*arrX2R[3][3];
	}
	public void read(HSV hsv)
	{
		double rr=0,gg=0,bb=0;
		double c,x,m,hh;
		int segment;
		hh=hsv.h/60;
		segment= (int)Math.floor(hh) %6;
		c=hsv.v*hsv.s;
		x=c*(1-Math.abs(hh%2 - 1));
		m=hsv.v-c;
		switch(segment) {
		case 0:
			rr=c;
			gg=x;
			bb=0;
			break;
		case 1:
			rr=x;
			gg=c;
			bb=0;
			break;
		case 2:
			rr=0;
			gg=c;
			bb=x;
			break;
		case 3:
			rr=0;
			gg=x;
			bb=c;
			break;
		case 4:
			rr=x;
			gg=0;
			bb=c;
			break;
		case 5:
			rr=c;
			gg=0;
			bb=x;
			break;
		}
		r=(rr+m)*255;
		g=(gg+m)*255;
		b=(bb+m)*255;		
	}
	
}