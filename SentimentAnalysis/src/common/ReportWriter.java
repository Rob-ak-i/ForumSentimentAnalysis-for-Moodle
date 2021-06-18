package common;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import datascience.HeatMap;
import imageprocessing.Blur;
import imageprocessing.IntMap;
import languageprocessing.Message;
import languageprocessing.Sequence;
import parts.PartBasic;
import parts.PartBinaryBasic;
import parts.PartBinaryLine;
import parts.PartNode;
import parts.SchematicManager;
import util.Colors;
import util.DataTable;
import util.DotDouble;
import util.Lists;

public class ReportWriter {
	int frequenciesCount;
	int[] frequenciesCaptions;
	private static PrintWriter output;
	private static String returnFileLocation(String fn) {return Settings.reportSubDirectory+Settings.linkDelimiter+fn;}
	private static DataTable forum;
	private static MessageBank msgs;
	private static EntityTextManager txtmgr;
	private static Sequence sequencesRoot;
	/**список всех идентификаторов пользователей форума*/
	private static ArrayList<Integer> users=null;
	///**рёбра связей (ориентированный граф)*/
	//private static ArrayList<Point> directConnections=null;
	/**массив связей*/
	private static int[][] relationsArray;
	///**рёбра связей (простой граф)*/
	//private static ArrayList<Point> connections=null;
	/**словарь имён пользователей форума по их идентификаторам в системе ЭО*/
	private static HashMap<Integer, String> userNames;
	
	private static int usersCount;
	//"userIDList/Integer;      0
	//userNameList/String;      1
	//postIDList/Integer;       2
	//parentPostIDList/Integer; 3
	//postTimeList/String;      4
	//messageList/String"       5
	
	public static void writeHTMForumPage(PrintWriter out) {
		//CommonData.renderer.stop();
		// блок инициализации переменных
		Message message;txtmgr=CommonData.textManager;
		if(CommonData.sequenceManager.size()<=0) {out.println("Ошибка, для отчёта нужен полный анализ форума (попробуйте посмотреть в основном меню.)");System.out.println("Error: first of all please make text analysis and sequence analysis.");return;}
		
		users = new ArrayList<Integer>();
		
		forum = CommonData.dataManager.getManagedElement(0);
		msgs = CommonData.textManager.getManagedElement(0);
		sequencesRoot = CommonData.sequenceManager.getManagedElement(0);
		
		out.println("<html>");
		out.println("<meta charset="+'\"'+"utf-8"+'\"'+"/>");
		out.println("<head><title>"+"Отчёт по форуму"+"</title></head>");
		out.println("<body>");
		out.println("<br><h3>Форум: "+forum.name+"</h3><br>");
		/**внешние ссылки(на сообщения вне форума)*/
		int externalLinks=0;
		/**число фактов взаимного общения*/
		int tetAtetCount=0;
		/**число рёбер общения*/
		int edgesCount=0;
		/*получаем массив уникальных userIDs*/
		Lists.getUniqueArrayList(TextForum.getUserIDList(forum), users);
		usersCount=users.size();
		{DataTable forNames = forum.copy();//forNames.removeColumn(5);forNames.removeColumn(4);forNames.removeColumn(3);forNames.removeColumn(2);
			userNames = new HashMap<Integer, String>();for(int i=0;i<forNames.nRows();++i)userNames.put((Integer)forNames.dataColumns.get(0).get(i), (String) forNames.dataColumns.get(1).get(i));
			int minID,maxID;int minIDIndex, maxIDIndex;
			relationsArray = new int[usersCount][usersCount];
			//HashMap<Integer,Integer>relations = new HashMap<Integer,Integer>();
			for(int i=0;i<forNames.nRows();++i) {
				minID=TextForum.getPostID(forNames, i);
				maxID=TextForum.getParentPostID(forNames, i);
				if(minID<0||maxID<0)continue;
				//if(minID==maxID)continue;
				//if(minID>maxID) {
				//	minID=maxID;maxID=TextForum.getPostID(forNames, i);
				//}
				minIDIndex=users.indexOf(minID);
				maxIDIndex=users.indexOf(maxID);
				if(minIDIndex<0||maxIDIndex<0) {externalLinks++;continue;}
				relationsArray[minIDIndex][maxIDIndex]+=1;
			}
			//connections = new ArrayList<Point>();
			forNames.clear();}
		for(int i=0;i<relationsArray.length;++i)
			for(int j=0;j<i;++j) {
				if(relationsArray[i][j]>0&&relationsArray[j][i]>0)
					tetAtetCount++;
				if(relationsArray[i][j]>0||relationsArray[j][i]>0)
					edgesCount++;
			}
		int nMsgs=forum.nRows();
		write("общее число сообщений: ");writeln(nMsgs);
		write("число внешних ссылок на письма: ");write(externalLinks);write("; (");write((double)externalLinks/nMsgs);writeln(")");
		
		write("общее число связей между участниками: ");write(edgesCount);write("; (");write((double)edgesCount/nMsgs);writeln(")");
		write("число взаимоотношений (двусторонних связей): ");write(tetAtetCount);write("; (");write((double)tetAtetCount/nMsgs);writeln(")");
		
		double sentimentCount=0,nowSentiment=0;
		int[] userSentencesCount = new int[usersCount];HashMap<Integer,Integer> usersSentences=new HashMap<Integer,Integer>();
		int[] userWordsCount = new int[usersCount];HashMap<Integer,Integer> usersWords=new HashMap<Integer,Integer>();
		int[] userSentimentsCount = new int[usersCount];HashMap<Integer,Double> usersSentiment=new HashMap<Integer,Double>();
		int wordsCount = 0;int sentencesCount=0;int lemmaIndex;
		int userID;
		for(int graphemeIndex=0;graphemeIndex<msgs.textMessages.size();++graphemeIndex) {
			message=msgs.textMessages.get(graphemeIndex);
			userID=message.userID;
			usersSentiment.put(userID, 0.);
			usersWords.put(userID, 0);
			usersSentences.put(userID, 0);
			while(message!=null) {
				usersSentences.put(userID, usersSentences.get(userID)+1);
				for(int i=0;i<message.words.size();++i) {
					lemmaIndex=message.getLemmaIndex(i);
					wordsCount++;
					usersWords.put(userID, usersWords.get(userID)+1);
					nowSentiment=message.getSentimentData(i).detSummarizedInfo();
					sentimentCount+=nowSentiment;
					usersSentiment.put(userID, usersSentiment.get(userID)+nowSentiment);
				}
				sentencesCount+=1;
				message=message.nextPartOfMessage;
			}
		}

		write("тональность текста на форуме:");write(sentimentCount);write("; (");write((double)sentimentCount/nMsgs);writeln(")");
		write("общее число предложений:");write(sentencesCount);write("; (");write((double)sentencesCount/nMsgs);writeln(")");
		write("общее число слов:");write(wordsCount);write("; (");write((double)wordsCount/nMsgs);writeln(")");
		
		writeln("график плотностей точек (длина текста пользователя, тональность пользователя)");
		ArrayList<DotDouble> usersInPlot = new ArrayList<DotDouble>();

		for(int userID1:usersSentiment.keySet()) {
			
			usersInPlot.add(new DotDouble(usersSentiment.get(userID1),usersWords.get(userID1)));
		}
		int[][] plotmap = HeatMap.buildFromElements(usersInPlot, 500, 500);
		makeImage(plotmap, "usersplot1.bmp");
		
		
		out.println("</body>");
		out.print("</html>");
		/*блок очистки переменных*/
		users.clear();userNames.clear();
		//connections.clear();
		relationsArray=null;
		//CommonData.renderer.start();
	}
	public static void writeHTMUserPage(PrintWriter out) throws IOException {
		users = new ArrayList<Integer>();
		
		forum = CommonData.dataManager.getManagedElement(0);
		msgs = CommonData.textManager.getManagedElement(0);
		sequencesRoot = CommonData.sequenceManager.getManagedElement(0);

		users = new ArrayList<Integer>();
		/*получаем массив уникальных userIDs*/
		Lists.getUniqueArrayList(TextForum.getUserIDList(forum), users);
		usersCount=users.size();
		
		String input;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in) );
		System.out.flush();
		System.out.println("Выбрана опция создания отчёта по пользователю.");
		System.out.println("Выберите индекс для отображения конкретного пользователя из списка:");
		for(int i=0;i<usersCount;++i)
			System.out.println("Пользователь с userID:"+Integer.toString(users.get(i)));
		input = reader.readLine();
		if(input==null||input.length()==0) {System.out.println("Отчёт не выполнен.");return;}
		int selectedUserID = Integer.valueOf(input);
		if(users.indexOf(selectedUserID)==-1){System.out.println("Отчёт не выполнен: невозможно найти пользователя с userID="+Integer.toString(selectedUserID)+"в данном форуме "+forum.name);return;}
		Message message;txtmgr=CommonData.textManager;
		if(CommonData.sequenceManager.size()<=0) {out.println("Ошибка, для отчёта нужен полный анализ форума (попробуйте посмотреть в основном меню.)");System.out.println("Error: first of all please make text analysis and sequence analysis.");return;}
		
		
		out.println("<html>");
		out.println("<meta charset="+'\"'+"utf-8"+'\"'+"/>");
		out.println("<head><title>"+"Отчёт по пользователю"+"</title></head>");
		out.println("<body>");
		out.println("<br><h3>Отчёт по пользователю (userID="+Integer.toString(selectedUserID)+") из форума "+forum.name+"</h3><br>");
		/**внешние ссылки(на сообщения вне форума)*/
		int externalLinks=0;
		/**число фактов взаимного общения*/
		int tetAtetCount=0;
		/**число рёбер общения*/
		int edgesCount=0;
		{DataTable forNames = forum.copy();
			userNames = new HashMap<Integer, String>();for(int i=0;i<forNames.nRows();++i)userNames.put((Integer)forNames.dataColumns.get(0).get(i), (String) forNames.dataColumns.get(1).get(i));
			int minID,maxID;int minIDIndex, maxIDIndex;
			relationsArray = new int[usersCount][usersCount];
			for(int i=0;i<forNames.nRows();++i) {
				minID=TextForum.getPostID(forNames, i);
				maxID=TextForum.getParentPostID(forNames, i);
				if(minID<0||maxID<0)continue;
				minIDIndex=users.indexOf(minID);
				maxIDIndex=users.indexOf(maxID);
				if(minIDIndex<0||maxIDIndex<0) {externalLinks++;continue;}
				relationsArray[minIDIndex][maxIDIndex]+=1;
			}
			//connections = new ArrayList<Point>();
			forNames.clear();}
		for(int i=0;i<relationsArray.length;++i)
			for(int j=0;j<i;++j) {
				if(relationsArray[i][j]>0&&relationsArray[j][i]>0)
					tetAtetCount++;
				if(relationsArray[i][j]>0||relationsArray[j][i]>0)
					edgesCount++;
			}
		int nMsgs=forum.nRows();
		double sentimentCount=0,nowSentiment=0;
		int[] userSentencesCount = new int[usersCount];HashMap<Integer,Integer> usersSentences=new HashMap<Integer,Integer>();
		int[] userWordsCount = new int[usersCount];HashMap<Integer,Integer> usersWords=new HashMap<Integer,Integer>();
		int[] userSentimentsCount = new int[usersCount];HashMap<Integer,Double> usersSentiment=new HashMap<Integer,Double>();
		int wordsCount = 0;int sentencesCount=0;int lemmaIndex;
		int userID;
		for(int graphemeIndex=0;graphemeIndex<msgs.textMessages.size();++graphemeIndex) {
			message=msgs.textMessages.get(graphemeIndex);
			userID=message.userID;
			usersSentiment.put(userID, 0.);
			usersWords.put(userID, 0);
			usersSentences.put(userID, 0);
			while(message!=null) {
				usersSentences.put(userID, usersSentences.get(userID)+1);
				for(int i=0;i<message.words.size();++i) {
					lemmaIndex=message.getLemmaIndex(i);
					wordsCount++;
					usersWords.put(userID, usersWords.get(userID)+1);
					nowSentiment=message.getSentimentData(i).detSummarizedInfo();
					sentimentCount+=nowSentiment;
					usersSentiment.put(userID, usersSentiment.get(userID)+nowSentiment);
				}
				sentencesCount+=1;
				message=message.nextPartOfMessage;
			}
		}
		writeln("<b>СВОДКИ ПО ФОРУМУ:</b>");
		write("общее число сообщений: ");writeln(nMsgs);
		write("число ссылок на корневое сообщение: ");write(externalLinks);write("; (");write((double)externalLinks/nMsgs);writeln(")");
		
		write("общее число связей между участниками: ");write(edgesCount);write("; (");write((double)edgesCount/nMsgs);writeln(")");
		write("число взаимоотношений (двусторонних связей): ");write(tetAtetCount);write("; (");write((double)tetAtetCount/nMsgs);writeln(")");
		

		writeln("<b>СВОДКИ ПО ПОЛЬЗОВАТЕЛЮ:</b>");
		write("тональность текста на форуме:");writeln(sentimentCount);
		write("тональность текста пользователя:");writeln(usersSentiment.get(selectedUserID));
		write("тональность текста пользователя/число слов пользователя:");writeln((double)usersSentiment.get(selectedUserID)/usersWords.get(selectedUserID));
		write("тональность текста пользователя/число сообщений пользователя:");writeln((double)usersSentiment.get(selectedUserID)/usersSentences.get(selectedUserID));
		write("число предложений пользователя:");writeln(usersSentences.get(selectedUserID));
		write("число слов пользователя:");writeln(usersWords.get(selectedUserID));
		printUserRating((double)usersSentences.get(selectedUserID),usersSentences.values(),"рейтинг пользователя по числу предложений:");
		printUserRating((double)usersWords.get(selectedUserID),usersWords.values(),"рейтинг пользователя по числу слов:");
		printUserRating2((double)usersSentiment.get(selectedUserID),usersSentiment.values(),"рейтинг пользователя по тональности:");
		ArrayList<Double>divides = new ArrayList<Double>();
		for(int userID1:usersSentiment.keySet())
			divides.add( ((double)usersSentiment.get(userID1)/usersWords.get(userID1)));
		double userSentimentOnWord = usersSentiment.get(selectedUserID)/usersWords.get(selectedUserID);
		printUserRating2(userSentimentOnWord,divides,"рейтинг пользователя по сводке тональность/слово :");
		
		
		out.println("</body>");
		out.print("</html>");
		/*блок очистки переменных*/
		users.clear();userNames.clear();
		//connections.clear();
		relationsArray=null;
		//CommonData.renderer.start();
	}
	private static double printUserRating2(double userWeight, Collection<Double> otherWeights, String caption) {
		Double value=1.;
		ArrayList<Double> abstractRating = new ArrayList<Double>();
		abstractRating.addAll(otherWeights);Collections.sort(abstractRating);
		write(caption);
		int len=otherWeights.size();
		for(int i=0;i<len;++i) {
			if(userWeight<=abstractRating.get(i)) {
				value=(double)i/len;
				break;
			}
		}
		writeln(value);
		return 1.;
	}
	private static double printUserRating(double userWeight, Collection<Integer> otherWeights, String caption) {
		Double value=1.;
		ArrayList<Integer> abstractRating = new ArrayList<Integer>();
		abstractRating.addAll(otherWeights);Collections.sort(abstractRating);
		write(caption);
		int len=otherWeights.size();
		for(int i=0;i<len;++i) {
			if(userWeight<=abstractRating.get(i)) {
				value=(double)i/len;
				break;
			}
		}
		writeln(value);
		return 1.;
	}
	private static void makeImage(int[][] data, String fileName) {
		String imageFileName = Settings.reportSubDirectory + Settings.linkDelimiter + fileName;
		FileIO.drawBitMap(imageFileName, data);
		output.println("<img src="+'"'+imageFileName+'"'+" alt="+'"'+common.Lang.InnerTable.Report.reportPart1PrintImageTextName+'"'+">");
		output.println("<br>");
	}
	private static void printImage(String imgName) {
		output.println("<img src="+'"'+imgName+'"'+" alt="+'"'+common.Lang.InnerTable.Report.reportPart1PrintImageTextName+'"'+">");
		output.println("<br>");
	}
	private static void write(Object txt) {output.println(txt.toString());}
	private static void writeln(Object txt) {output.println(txt.toString());output.println("<br>");}
	private static void println(PrintWriter out,Object txt) {
		out.println(txt.toString());
		out.println("<br>");
	}
	
	public static void writeResult(String fileName, boolean isAllForumReport) {
		//File f=new File(fileName);
		//String dir=f.getParent();
		new File(Settings.reportSubDirectory).mkdirs();
	    //Determine file
	    File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {throw new RuntimeException(e);}
	    PrintWriter out = null;
		try {
			out = new PrintWriter(file.getAbsoluteFile());
		}catch (FileNotFoundException e){e.printStackTrace();}
	    try {output=out;
	    	if(isAllForumReport)
	    		writeHTMForumPage(out);
	    	else
	    		writeHTMUserPage(out);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	    	out.close();
	    }
	}

}
