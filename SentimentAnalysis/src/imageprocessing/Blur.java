package imageprocessing;

import imageprocessing.IntMap;
import imageprocessing.RGB;

public class Blur {

	public static IntMap blur(IntMap src) {
		int width=src.cols();
		int height=src.rows();
		IntMap result=new IntMap(width, height);
		int wMax=width-2; 
		int hMax=height-2;
		for (int i=2;i<wMax;++i)
			for (int j=2;j<hMax;++j) {
				if(src.mode==1)
					result.data[i][j]=computeGaussian(src, i, j);
				else
					result.data[i][j]=computeGaussianColored(src, i, j);
			}
		for (int i=0;i<width;++i) {
			for(int j=0;j<2;++j) {
				result.data[i][j]=src.data[i][j];
				result.data[i][height-j-1]=src.data[i][height-j-1];
			}
		}
		for (int i=0;i<2;++i) {
			for(int j=2;j<hMax;++j) {
				result.data[i][j]=src.data[i][j];
				result.data[width-i-1][j]=src.data[width-i-1][j];
			}
		}
		
		
		return result;
	}
	private static int computeGaussian(IntMap src, int x, int y) {
		int maxRadius=4;//� �������� ����� ���������� 4 ������� �����, � � ����� ����� ������ 4 ������������ �����
		
		int nowX, nowY;
		int incrX=0, incrY=0;
		int direction=0, directionPrev=4;
		int pixelCost=4;
		int pixelSeries=0;
		int pixel=0;
		pixelSeries=src.data[x][y];//��������� ������� ������ �����������
		pixelSeries<<=pixelCost;//�������� �� �� ��������� �������
		pixel+=pixelSeries;//���������� � ���������� �������, ������� ����� ������� ����� �����������(������� �� 100)
		for (int r=2;r<=maxRadius;++r) {
			nowX=x+r-1;
			nowY=y;
			pixelSeries=0;
			pixelCost--;
			for(int k=0;k<4*(r-1);++k) {
				direction=k/(r-1);
				if(directionPrev!=direction) {
					if(direction>1)incrX=1; else incrX=-1;
					if((direction==0)||(direction==3))incrY=1; else incrY=-1;
					directionPrev=direction;
				}
				if(maxRadius<4)
					pixelSeries+=src.data[nowX][nowY];
				else {
					if(k%3>0)//����������, ���� k=0,3,6,9
						pixelSeries+=src.data[nowX][nowY];
				}
				nowX+=incrX;
				nowY+=incrY;
			}
			pixelSeries<<=pixelCost;
			pixel+=pixelSeries;
		}
		{//����� ������ (���������=1)
			pixel+=src.data[x-2][y-2];
			pixel+=src.data[x-2][y+2];
			pixel+=src.data[x+2][y-2];
			pixel+=src.data[x+2][y+2];
		}
		return pixel/100;
	}
	private static void sumArrays(int[]a, int[]b) {
		for(int i=0;i<4;++i)
			a[i]+=b[i];
	}
	private static int computeGaussianColored(IntMap src, int x, int y) {
		int maxRadius=4;//� �������� ����� ���������� 4 ������� �����, � � ����� ����� ������ 4 ������������ �����
		int[] data;
		int nowX, nowY;
		int incrX=0, incrY=0;
		int direction=0, directionPrev=4;
		int pixelCost=4;
		int[] pixelSeries=new int[4];
		int[] pixel=new int[4];
		int[] temp=RGB.unpack(src.data[x][y]);
		for(int i=0;i<4;++i)pixelSeries[i]+=temp[i];
		temp=null;
		for(int i=0;i<4;++i)pixelSeries[i]<<=pixelCost;
		for(int i=0;i<4;++i)pixel[i]+=pixelSeries[i];
		for (int r=2;r<=maxRadius;++r) {
			nowX=x+r-1;
			nowY=y;
			for(int i=0;i<4;++i)pixelSeries[i]=0;
			pixelCost--;
			for(int k=0;k<4*(r-1);++k) {
				direction=k/(r-1);
				if(directionPrev!=direction) {
					if(direction>1)incrX=1; else incrX=-1;
					if((direction==0)||(direction==3))incrY=1; else incrY=-1;
					directionPrev=direction;
				}
				if(maxRadius<4) {
					temp=RGB.unpack(src.data[nowX][nowY]);
					for(int i=0;i<4;++i)pixelSeries[i]+=temp[i];
					temp=null;
				}else {
					if(k%3>0) {
						temp=RGB.unpack(src.data[nowX][nowY]);
						for(int i=0;i<4;++i)pixelSeries[i]+=temp[i];
						temp=null;
					}
				}
				nowX+=incrX;
				nowY+=incrY;
			}
			for(int i=0;i<4;++i)pixelSeries[i]<<=pixelCost;
			for(int i=0;i<4;++i)pixel[i]+=pixelSeries[i];
		}
		{//����� ������ (���������=1)

			temp=RGB.unpack(src.data[x-2][y-2]);
			for(int i=0;i<4;++i)pixel[i]+=temp[i];
			temp=null;
			temp=RGB.unpack(src.data[x-2][y+2]);
			for(int i=0;i<4;++i)pixel[i]+=temp[i];
			temp=null;
			temp=RGB.unpack(src.data[x+2][y-2]);
			for(int i=0;i<4;++i)pixel[i]+=temp[i];
			temp=null;
			temp=RGB.unpack(src.data[x+2][y+2]);
			for(int i=0;i<4;++i)pixel[i]+=temp[i];
			temp=null;
		}
		
		for(int i=0;i<4;++i)pixel[i]/=100;
		int result=RGB.pack(pixel);
		pixelSeries=null;
		pixel=null;
		return result;
	}
}
