package languageprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.CommonData;
import common.MessageBank;
import common.Settings;
import util.DataTable;
import util.IntList;

public class LowLevelTextProcessor {

	public static MessageBank extractMessagesFromDataTable(DataTable messages) {
		MessageBank result = new MessageBank();
		Indexes.extractIndexes(messages.colNames, messages.colClasses);
		if(Indexes.messageText==-1) {System.out.println("DataTable '"+messages.name+"' doesnot have column contained 'message' substring");return null;}
		if(!Indexes.allFieldsFound) {System.out.println("DataTable '"+messages.name+"' hasn't all fields(userid,postid,parentpostid,time,message), but it has messageText field at "+Integer.toString(Indexes.messageText)+", trying to make MessageBank" );}
		int count=messages.nRows();
		Message message=null;
		for(int row=0;row<count;++row) {
			message=new Message();
			message.words = extractOnlyRussianWordsAndSignsWithoutCapitalsFromHTML((String) messages.getField(row, Indexes.messageText));
			message.userID=(int) messages.getField(row, Indexes.userID);
			message.postID=(int) messages.getField(row, Indexes.postID);
			message.parentPostID=(int) messages.getField(row, Indexes.parentPostID);
			message.postTime=(int) messages.getField(row, Indexes.parentPostID);
			result.textMessages.add(message);
		}
		result.storageLevel=MessageBank.storageLevel_Vanilla;
		return result;
	}
	
	private static class Indexes {
		public static int userID=0;
		public static int postID=0;
		public static int parentPostID=0;
		public static int postTime=0;
		public static int messageText=0;
		public static boolean allFieldsFound=false;
		public static void extractIndexes(ArrayList<String> colNames, ArrayList<Class> colClasses) {
			int nCols=colNames.size();
			String colName;int foundFields=0;
			userID=-1;postID=-1;parentPostID=-1;messageText=-1;postTime=-1;
			for(int i=0;i<nCols;++i) {
				colName=colNames.get(i).toLowerCase();
				if(colName.indexOf("userid")!=-1) {userID=i;foundFields++;}
				if(colName.indexOf("postid")!=-1 && colName.indexOf("parent")==-1) {postID=i;foundFields++;}
				if(colName.indexOf("parentpostid")!=-1) {parentPostID=i;foundFields++;}
				if(colName.indexOf("time")!=-1) {postTime=i;foundFields++;}
				if(colName.indexOf("message")!=-1) {if(messageText==-1)foundFields++;messageText=i;}
				if(colName.indexOf("text")!=-1) {if(messageText==-1)messageText=i;foundFields++;}
			}
			System.out.print("foundFields:");
			System.out.println(foundFields);
			if(nCols==1&&messageText==-1&&foundFields==0&&colClasses.get(0).equals(String.class))messageText=0;
			allFieldsFound=(foundFields==5);
		}
	}
	

	private static final String signsTestVersion=".,;:\'\"&?!#";
	public static IntList extractOnlyRussianWordsAndSignsWithoutCapitalsFromHTML(String text) {
		IntList result = new IntList();//new ArrayList<Integer>();
		char c;
		boolean isRussian=false;
		StringBuilder sb=new StringBuilder();
		int wordIndex=0;int iResult,textLength=text.length();
		for(int i=0;i<textLength;++i) {
			c=text.charAt(i);
			isRussian=(c>='А'&&c<='Я'||c>='а'&&c<='я'||c>='0'&&c<='9');
			if(isRussian) {
				sb.append(c);
			}else {
				
				if(c=='<') {iResult=tryToReadTag(text,i);if(iResult!=-1) {i+=iResult;continue;}}
				if(sb.length()>0) {result.add(CommonData.textManager.addWord(sb.toString().toLowerCase()));sb=null;sb=new StringBuilder();}
				if(signsTestVersion.indexOf(c)!=-1)result.add(CommonData.textManager.addWord(String.valueOf(c)));
			}
		}
		return result;
	}
	private static String[] knownTags = {
			"se",
			"pm",
			"a",
			"n"
			};
	private static int tryToReadTag(String text, int pos) {
		char c;int j=0;boolean tagIsClosed=false;int firstIndex=-1,index=-1;int textLength=text.length();
		for(int i=pos+1;i<textLength;++i) {
			c=text.charAt(i);
			if(c=='/'&&j==0) {tagIsClosed=true;continue;}
			if(j>=knownTags.length) {if(c=='>')return j+1+(tagIsClosed?1:0); else return -1;}
			if(j==0)firstIndex=knownTags[j].indexOf(c);else index=knownTags[j].indexOf(c);
			if(firstIndex==-1&&j==0||(j>0&&firstIndex!=index&&c!='>'))return -1;
			if(c=='>')return j+1+(tagIsClosed?1:0);
			j++;
		}
		return j+(tagIsClosed?1:0);
	}
	/**----------------------------------------------------------------------------------------------------------*/
	/**updates lemmas, wordTags[, sentiments]*/
	public static void appendKnowledgeBase(List<String> newWords, String fileName) {
		saveNewWordsArrayToInputtxt(newWords,fileName);
		runPyMorphy2(Settings.appPath+Settings.linkDelimiter+"data", "script.py");
		System.out.println("pyMorphy2 analyzer done!");
		//textManagerCommonWork(try to know new words)
		
		//TODO working on OpenCorporaTag interpretation
		appendAnalyzedWords(Settings.appPath+Settings.linkDelimiter+"saves"+Settings.linkDelimiter+"words_tag.txt", Settings.appPath+Settings.linkDelimiter+"saves"+Settings.linkDelimiter+"words_normalForm.txt");
		
	}
	private static void runPyMorphy2(String scriptFilePath, String scriptFileName) {
		//String homeDirectory = System.getProperty("user.home");
		Process process=null;int exitCode=0;
		boolean isWindows = System.getProperty("os.name").contains("Windows");
		try {
			if (isWindows) {
				process = Runtime.getRuntime().exec(String.format("cmd.exe python3 %s", scriptFilePath+Settings.linkDelimiter+scriptFileName));
			    //process = Runtime.getRuntime().exec(String.format("cmd.exe /c dir %s", homeDirectory));
			} else {
				ProcessBuilder pb = new ProcessBuilder(
						new String[] {
								"bash",
								"-c",
								"python3"
								//, 
								+" "+
								scriptFilePath+Settings.linkDelimiter+scriptFileName
						}
						);
				pb.redirectErrorStream(true);
				process=pb.start();
			}
			long time0=System.nanoTime();
			//while(process.isAlive()) {}
			process.waitFor();
			BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null)System.out.println(line);
			long time1=System.nanoTime();
			long dt=time1-time0;dt=dt/1000000l;
			System.out.println("python script finished for time "+Integer.toString((int)dt)+" ms");
			
			
			//process.destroy();
			//process = Runtime.getRuntime().exec(
			//		"python3 "+scriptFilePath+Settings.linkDelimiter+scriptFileName
				//new String[] {
				//		"python3", scriptFilePath+Settings.linkDelimiter+scriptFileName
				//}
			//	);
			//process = Runtime.getRuntime().exec("sh -c python3 "+scriptFilePath+Settings.linkDelimiter+scriptFileName);
			
			//process.getInputStream().
			//StreamGobbler streamGobbler = 
			//  new StreamGobbler(process.getInputStream(), System.out::println);
			//Executors.newSingleThreadExecutor().submit(streamGobbler);
			//exitCode = process.waitFor();
			//assert exitCode == 0;
		}catch(Exception e) {System.out.println(e);}
	}
	private static void saveNewWordsArrayToInputtxt(List<String> newWords, String fileName) {
		PrintWriter out=null;
		try {
			out = new PrintWriter(new File(fileName));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(out==null)return;
		
		for(int i=0;i<newWords.size();++i) {
			out.println(newWords.get(i));
		}
		out.close();
	}
	
	private static void appendAnalyzedWords(String tagsFileName, String lemmesFileName) {
		Scanner inTags=null,inLemmes=null;
		try {
			inTags = new Scanner(new File(tagsFileName));
			inLemmes = new Scanner(new File(lemmesFileName));
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(inTags==null||inLemmes==null)return;
		String lemmaStr,tagStr;
		while(inTags.hasNext()) {
			lemmaStr=inLemmes.nextLine();
			tagStr=inTags.nextLine();
			CommonData.textManager.addLemmaAndTag(lemmaStr, tagStr);
		}
		inLemmes.close();
		inTags.close();
	}
}
