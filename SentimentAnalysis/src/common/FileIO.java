package common;
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
}
