package common;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import util.Colors;
import util.DataTable;

public class FileIO {
	public static void drawBitMap(String fileName, int[][] data)
	{
		int width=data.length;
		int height = data[0].length;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<width;++i) {
			for(int j=0;j<height;++j)
				img.setRGB(i, j, data[i][j]);
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
	public static void drawImage(String fileName, BufferedImage img)
	{
		try {
			ImageIO.write(img, "BMP", new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Rectangle cropImage(BufferedImage img) {
		int w=CommonData.WIDTH,h=CommonData.HEIGHT;
		int colorBG=Colors.colorBackground.getRGB(),colorUp=0,colorDown=0;
		int centerx=w/2,centery=h/2;
		boolean topDone=false,bottomDone=false,leftDone=false,rightDone=false;int bottom=centery+1,top=centery,left=centerx,right=centerx+1;
		for(int i=0;i<w;++i) {
			for(int j=0;j<h;++j) {
				if(!leftDone) {colorUp = img.getRGB(i,j);if(colorUp!=colorBG) {leftDone=true;left=i;if(rightDone)break;}}
				if(!rightDone) {colorDown = img.getRGB(w-1-i,j);if(colorDown!=colorBG) {rightDone=true;right=w-1-i;if(leftDone)break;}}
			}
			if(leftDone&&rightDone)break;
		}
		for(int j=0;j<h;++j) {
			for(int i=0;i<w;++i) {
				if(!topDone) {colorUp = img.getRGB(i,j);if(colorUp!=colorBG) {topDone=true;top=j;if(bottomDone)break;}}
				if(!bottomDone) {colorDown = img.getRGB(i,h-1-j);if(colorDown!=colorBG) {bottomDone=true;bottom=h-1-j;if(topDone)break;}}
			}
			if(topDone&&bottomDone)break;
		}
		Rectangle result = new Rectangle();
		result.setBounds(left, top, right-left, bottom-top);
		return result;
	}
}
