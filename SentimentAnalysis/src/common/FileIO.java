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
	public static TextForum readHTMPage(String fileName) {
		File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {throw new RuntimeException(e);}
		//Paths f=new Paths();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.length());
        TextForum result = loadValues(sb);
        result.name=fileName.substring(fileName.lastIndexOf(Settings.linkDelimiter));
        return result;
		
	}
	private static TextForum loadValues(StringBuilder text) {
		
		String firstPostBeginKey = "<article";
		String firstPostEndKey = "\"replies-container\">"; //"<div data-region=";
		String postsBeginKey = "<article";
		String postsEndKey = "</article>";
		String titleBeginKey="<title>";
		String titleEndKey="</title>";
		
		ArrayList<String> postsRawData = new ArrayList<String>();
		String firstPostRawData = "";
		String title="";
		int valueBegin=0, valueEnd=0;String nowStr=null;
		try {
			
			valueBegin=text.indexOf(postsBeginKey);
			valueEnd=text.indexOf(postsEndKey);
			title=text.substring(valueBegin+postsBeginKey.length(),valueEnd);
			
			valueBegin=text.indexOf(firstPostBeginKey);
			valueEnd=text.indexOf(firstPostEndKey, valueBegin);
			firstPostRawData=text.substring(valueBegin, valueEnd);
			int nowMatcherPosition = valueEnd;
			while (true) {
				valueBegin=text.indexOf(postsBeginKey,nowMatcherPosition);
				nowMatcherPosition = valueBegin;
				valueEnd=text.indexOf(postsEndKey, nowMatcherPosition);
				nowMatcherPosition = valueEnd;
				nowStr=text.substring(valueBegin, valueEnd);
				postsRawData.add(nowStr);
			}
		}catch (IndexOutOfBoundsException e) {
			System.out.println(valueBegin);
			System.out.println(valueEnd);
		}
		
		System.out.println("1st post len = ".concat(Integer.toString(firstPostRawData.length())));
		System.out.println("posts count = ".concat(Integer.toString(postsRawData.size())));
		TextForum result=new TextForum();
		result.title=title;
		result.add(firstPostRawData);
		for(int i=0;i<postsRawData.size();++i) {
			result.add(postsRawData.get(i));
		}
		return result;
		
	}
	//TODO HTMParserTest
	public static void main(String args[]) {
		readHTMPage("/home/user1/Документы/2___Desert/prac3(htm parser)/discuss1linear.html");
	}
}
