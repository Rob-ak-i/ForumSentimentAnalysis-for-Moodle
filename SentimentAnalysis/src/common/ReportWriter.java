package common;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import parts.PartBasic;
import parts.PartBinaryBasic;
import parts.PartBinaryLine;
import parts.PartNode;
import parts.SchematicManager;
import util.Colors;

public class ReportWriter {
	int frequenciesCount;
	int[] frequenciesCaptions;
	private static String returnFileLocation(String fn) {return Settings.reportSubDirectory+Settings.linkDelimiter+fn;}
	public static void writeHTMPage(PrintWriter out) {
		CommonData.renderer.stop();
		out.println("<html>");
		out.println("<meta charset="+'\"'+"utf-8"+'\"'+"/>");
		out.println("<head><title>"+common.Lang.InnerTable.Report.reportPart1HeadTitleName+"</title></head>");
		out.println("<body>");

		out.println("<br><h2>Список пользователей:/h2><br>");
		
		ArrayList<PartNode> users=CommonData.scheme.nodesContainer;
		ArrayList<PartBinaryBasic> messages=CommonData.scheme.partsContainer;
		for(int i=0;i<users.size();++i)
			out.println(users.get(i).getName()+"<br>");
		out.println("<br><br><br><h2>Список сообщений</h2><br>");
		for(int i=0;i<messages.size();++i) {
			out.println("<br>Отправитель: <b>"+messages.get(i).getNodeFrom().getName()+"</b>");
			out.println("<br>Получатель: <b>"+messages.get(i).getNodeTo().getName()+"</b>");
			//out.println("<br><b>Текст</b>: <br>"+((PartBinaryLine)(messages.get(i)).partName));
		}
		//writeScheme(out);
		
		out.println("</body>");
		out.print("</html>");
		CommonData.renderer.start();
	}
	private static void printImage(PrintWriter out,String imgName) {
		out.println("<img src="+'"'+imgName+'"'+" alt="+'"'+common.Lang.InnerTable.Report.reportPart1PrintImageTextName+'"'+">");
		out.println("<br>");
	}
	private static void println(PrintWriter out,String txt) {
		out.println(txt);
		out.println("<br>");
	}
	private static void writeScheme(PrintWriter out) {
		int w=CommonData.WIDTH,h=CommonData.HEIGHT;
		BufferedImage img=new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB),subimg=null;
		PartBasic.g=img.getGraphics();Rectangle rect=null;
		img.getGraphics().setColor(Colors.colorBackground);
		img.getGraphics().fillRect(0, 0, w, h);
		rect=cropImage(img);
		subimg=img.getSubimage(rect.x, rect.y, rect.width, rect.height);
		FileIO.drawImage(returnFileLocation("schematic"+".bmp"), subimg);
		//PartBasic.g=EntityEditor_Helper.graphics;
	}
	private static void writeAllWays(PrintWriter out) {
		int w=CommonData.WIDTH,h=CommonData.HEIGHT;
		BufferedImage img=new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB),subimg=null;
		PartBasic.g=img.getGraphics();Rectangle rect=null;
		//PartBasic.g=EntityEditor_Helper.graphics;
	}
	public static Rectangle cropImage(BufferedImage img) {
		int w=CommonData.WIDTH,h=CommonData.HEIGHT;
		//int[] data=new int[w*h];
		//img.getRGB(0, 0, w, h, data, 0, 1);
		//img.getRaster().getPixels(0, 0, w-1, h-1, data);
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
	
	public static void writeResult(String fileName) {
		//File f=new File(fileName);
		//String dir=f.getParent();
		new File(Settings.reportSubDirectory).mkdirs();
	    //Determine file
	    File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {throw new RuntimeException(e);}
	    PrintWriter out = null;
		try {
			out = new PrintWriter(file.getAbsoluteFile());
		}catch (FileNotFoundException e){e.printStackTrace();}
	    try {
	    	writeHTMPage(out);
	    }finally {
	    	out.close();
	    }
	}

}
