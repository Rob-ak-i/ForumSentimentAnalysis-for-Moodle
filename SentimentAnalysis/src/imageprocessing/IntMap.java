package imageprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class IntMap{
	public int [][]data;
	public static final int MODE_RGB=0;
	public static final int MODE_GRAY=1;
	public static final int MODE_HSV=2;
	public static final int MODE_BOOLEAN=3;
	public static final int MODE_HSL=4;
	public static final int MODE_HSLHalf=5;
	public int mode;//0 - RGB, 1 - luminance(0..255), 2 - HSV, 3- boolean, 4 - HSL, 5 - HSLHalf
	private int height,width;
	public IntMap(int width, int height) {
		data = new int[width][height];
		this.width=width;
		this.height=height;
		mode = 0;
	}private IntMap(int width, int height, int[][] data) {this.data=data;this.width=width;this.height=height;}
	public static IntMap getMap(int[][]data) {if(data==null)return null;
		return new IntMap(data.length, data[0].length,data);
	}
	public IntMap clone() {
		IntMap result=new IntMap(width,height);
		result.mode=this.mode;
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j)
				result.data[i][j]=this.data[i][j];
		return result;
	}
	public int[] toArray() {
		int[] result= new int [height*width];
		int k=0;
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j) {
				result[k]=data[i][j];
				++k;
			}
		return result;
	}
	public void toHSV() {
		if(mode==2)return;
		if(mode==0) {
			for(int i=0;i<width;++i)
				for(int j=0;j<height;++j) {
					data[i][j]=HSV.fromRGB(data[i][j]);
				}
		}
		if(mode==1) {
			//����� ������ �� ���������, �� � ��� ��������
		}
		mode=2;
	}
	public void toHSL() {
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j) {
				data[i][j]=HSL.fromRGB(data[i][j]);
				//data[i][j]=data[i][j]+(data[i][j]<<8)+(data[i][j]<<16);
			}
		mode=4;
	}
	public void toHSLHalf() {
		if(mode!=4)return;
		int hue;
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j) {
				hue=data[i][j]>>>16;
				if(hue>180)hue=360-hue;
				data[i][j]=(data[i][j]&0xFFFF)|(hue<<16);
			}
		mode=5;
	}
	public void toRGB() {
		if(mode==0)return;
			for(int i=0;i<width;++i)
				for(int j=0;j<height;++j) {
					switch(mode) {
					case 2:
						data[i][j]=RGB.fromHSV(data[i][j]);
						break;
					case 3:
						data[i][j]=(data[i][j]<<16)+(data[i][j]<<8)+(data[i][j]);
					case 4:
						data[i][j]=HSL.toRGB(data[i][j]);
						break;
					}
				}
		mode=0;
	}
	public void toGray() {
		if(mode==1)return;
		for (int i=0;i<width;++i)
			for (int j=0;j<height;++j) {
				switch (mode) {
				case 0:
					data[i][j]=RGB.luminanceFancy(data[i][j]);
					break;
				case 2:
				case 4:
				case 5:
					data[i][j]&=0xFF;
					break;
				}
			}
		mode=1;
	}
	public void toBoolean(int trigger) {
		for (int i=0;i<width;++i)
			for (int j=0;j<height;++j)
				if(Math.abs(data[i][j])>=trigger)
					data[i][j]=1;
				else 
					data[i][j]=0;
		mode=3;
	}
	public void clear() {
		fill(0);
	}
	public void normalize(int maxCaption,int minCaption) {
		int pix=0,max=-1, min=-1;
		for (int i=1;i<width-1;++i)
			for (int j=1;j<height-1;++j) {
				pix=data[i][j];
				if(min==-1||min>pix)min=pix;
				if(max==-1||max>pix)max=pix;
			}
		for (int i=1;i<width-1;++i)
			for (int j=1;j<height-1;++j) {
				pix=data[i][j];
				data[i][j]=getScaledSigmoid(pix,max,min,maxCaption,minCaption);
			}
		
	}
	private int getScaledSigmoid(int value, int inputmax,int inputmin,int outputmax,int outputmin) {
		double input = (double)(value - inputmin)/(double)(inputmax-inputmin);
		double ret = getSigmoid(input);
		return (int)(ret*((double)(outputmax-outputmin)))+outputmin;
		
	}
	/**x in range(0,1), result in same range*/
	private double getSigmoid(double x) {
		return Math.sin((x-0.5)*Math.PI);
	}
	public IntMap differentiate() {
		IntMap result=new  IntMap(width,height);
		result.mode=1;
		int equation=0;
		int caption=0;
		for (int i=1;i<width-1;++i)
			for (int j=1;j<height-1;++j) {
				equation=(data[i-1][j]+data[i+1][j]+data[i][j-1]+data[i][j+1]);
				equation>>>=2;
				if((equation==data[i][j])||(data[i][j]>128))
					caption=0;
				else
					caption=255;
				result.data[i][j]=caption;
			}
		return result;
	}
	public IntMap differentiateHSV(int sMin, int sMax, int valueMin,int valueMax, int byY) {
		IntMap result=new IntMap(width,height);
		result.mode=1;
		int equation=0;
		int caption=0;
		int[] pixel0;
		int[] pixel1;
		for (int i=1;i<width-1;++i)
			for (int j=1;j<height-1;++j) {
				if(byY==1)
					pixel1=RGB.unpack(data[i][j-1]);
				else
					pixel1=RGB.unpack(data[i-1][j]);
				pixel0=RGB.unpack(data[i][j]);
				equation=(pixel0[0]<<8)+pixel0[1];
				equation-=(pixel1[0]<<8)+pixel1[1];
				equation=Math.abs(equation);
				if(equation>180)equation-=180;
				equation*=pixel1[2];//��� ������� �������������, ��� ������ ������ � ���, ��� ����� �����
									//�� ����� ����������� ��� ������ ������� ���� if(pixel1[2]<sMin)equation=0;
				caption=(int)((double)equation*0.01);//��������� ������� �������� �� � �����, � � ���������
				result.data[i][j]=caption;
				pixel0=null;
				pixel1=null;
			}
		return result;
	}
	public void invertBool() {
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j)
				data[i][j]=1-data[i][j];
	}
	public void substituteBooleanMask(int maskValue) {
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j)
				data[i][j]&=maskValue;
	}
	public void fill(int value) {
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j)
				data[i][j]=value;
	}
	public void put(util.Dot loc, int value) {
		this.data[loc.x][loc.y] = value;
	}public int get(util.Dot loc) {return data[loc.x][loc.y];}
	public int height() {return height;}
	public int width() {return width;}
	public void free() {
		data = null;
	}
	public void drawBitMap(String fileName)
	{
		drawBitMap(fileName, this.data, null, 1);
	}
	/*���� ����� data, null, 1; ���� - null, datad, int*/
	public static void drawBitMap(String fileName, int[][] data, double[][] datad, double multiplier)
	{
		int width=data.length;
		int height = data[0].length;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;++i) {
			for(int j=0;j<height;++j) {
				if(datad!=null) {
					datad[i][j]*=multiplier;
					img.setRGB(i, j, (int)datad[i][j]);
				}else
					img.setRGB(i, j, data[i][j]);
			}
				//out.print(data[i][j]);
			//out.println();
		}
		try {
			ImageIO.write(img, "BMP", new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		img.flush();
		img=null;
	}
	public void readBitMap(String fileName)
	{
		free();
		BufferedImage img = null;// = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				//out.print(data[i][j]);
			//out.println();
		try {
			img=ImageIO.read(new File(fileName));
			width=img.getWidth();
			height=img.getHeight();
			data = new int[width][height];
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(img==null)return;
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j)
				data[i][j]=img.getRGB(i, j)&0xFFFFFF;
		//for(int i=0;i<height;++i)
		//	img.getRGB(0, i, width, 1, data[i], 0, 1);
		img.flush();
		img=null;
	}
	public static int threshold(IntMap src, int threshold) {
		int width=src.width();
		int height=src.height();
		int maxCap=0;//TODO debug 
		for(int i=0;i<width;++i)
			for(int j=0;j<height;++j) {
				
				//TODO ����� ���������, ����� ������ ��� �������
				if(src.data[i][j]>=maxCap)
					maxCap=src.data[i][j];
				
				if (src.data[i][j]>=threshold)
					src.data[i][j]=255;
				else
					src.data[i][j]=0;
			}
		return maxCap;
	}
	public int get(int x, int y) {
		return data[x][y];
	}
	public int getProtected(int x, int y) {
		if((x<0)||(y<0)||(x>=width)||(y>=height))return -1;//-1 if ARGB
		return data[x][y];
	}
	public void put(int x, int y, int color) {
		data[x][y]=color;
	}
	public int rows() {
		return height;
	}
	public int cols() {
		return width;
	}
	
	/**lengthType
	 * 0 - Closest Axis
	 * 1 - Minkovsky
	 * 2 - Real
	 * 
	 * 3 - Sum*/
	public static IntMap getModule(IntMap a, IntMap b, int lengthType) {
		int w=a.width();
		int h=a.height();
		IntMap result = new IntMap(w,h);
		for(int x=0;x<w;++x) {
			for(int y=0;y<h;++y) {
				if(lengthType==0)
					result.data[x][y]=Math.max(Math.abs(a.data[x][y]), Math.abs(b.data[x][y]));
				if(lengthType==1)
					result.data[x][y]=Math.abs(a.data[x][y])+Math.abs(b.data[x][y]);
				if(lengthType==2)
					result.data[x][y]=(int)Math.sqrt(a.data[x][y]*a.data[x][y]+b.data[x][y]*b.data[x][y]);
				if(lengthType==3)
					result.data[x][y]=a.data[x][y]+b.data[x][y];
			}
		}
		return result;
	}
	/**logicOperation:
	 * 0 - OR
	 * 1 - AND
	 * 2 - NOR
	 * 3 - NAND
	 * 
	 * */
	public static IntMap getLogicOperation(IntMap a, IntMap b, int logicOperation) {
		int w=Math.min(a.width(), b.width());
		int h=Math.min(a.height(), b.height());
		IntMap result = new IntMap(w,h);
		for(int x=0;x<w;++x) {
			for(int y=0;y<h;++y) {
				if((logicOperation&1)==0)
					result.data[x][y]=a.data[x][y]|b.data[x][y];
				else
					result.data[x][y]=a.data[x][y]&b.data[x][y];
				if(logicOperation>1)
					result.data[x][y]=-result.data[x][y];
			}
		}
		return result;
	}

}