package imageprocessing;

public class XYZ{
	public double x;
	public double y;
	public double z;
	public static float[][] arrR2X = 
		{ 
			{ 0.49F, 0.31F, 0.2F}, 
			{ 0.17697F, 0.8124F, 0.01063F},
			{ 0.00F, 0.01F, 0.99F} 
		};
	public XYZ()
	{
		
	}
	
	public void read(RGB rgb)
	{
		x=rgb.r*arrR2X[1][1]+rgb.g*arrR2X[1][2]+rgb.b*arrR2X[1][3];
		y=rgb.r*arrR2X[2][1]+rgb.g*arrR2X[2][2]+rgb.b*arrR2X[2][3];
		z=rgb.r*arrR2X[3][1]+rgb.g*arrR2X[3][2]+rgb.b*arrR2X[3][3];
	}
	
}