package util;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import common.CommonData;
import common.Settings;

public class LanguageProcessor {
	private static void runPyMorphy2(String scriptFilePath, String scriptFileName) {
		//String homeDirectory = System.getProperty("user.home");
		Process process=null;int exitCode=0;
		boolean isWindows = System.getProperty("os.name").contains("Windows");
		try {
			if (isWindows) {
				process = Runtime.getRuntime().exec(String.format("cmd.exe python3 %s", scriptFilePath+Settings.linkDelimiter+scriptFileName));
			    //process = Runtime.getRuntime().exec(String.format("cmd.exe /c dir %s", homeDirectory));
			} else {
				process = Runtime.getRuntime().exec(String.format("sh python3 %s", scriptFilePath+Settings.linkDelimiter+scriptFileName));
			    //process = Runtime.getRuntime().exec(String.format("sh -c ls %s", homeDirectory));
			}
			//process.getInputStream().
			//StreamGobbler streamGobbler = 
			//  new StreamGobbler(process.getInputStream(), System.out::println);
			//Executors.newSingleThreadExecutor().submit(streamGobbler);
			exitCode = process.waitFor();
			//assert exitCode == 0;
		}catch(Exception e) {System.out.println(e);}
	}
	
	private static final String signsTestVersion=".,;:\'\"&?!#";
	public static ArrayList<Integer> extractOnlyRussianWordsAndSignsWithoutCapitalsFromHTML(String text) {
		ArrayList<Integer>result = new ArrayList<Integer>();
		char c;
		boolean isRussian=false;
		StringBuilder sb=new StringBuilder();
		int wordIndex=0;
		for(int i=0;i<text.length();++i) {
			c=text.charAt(i);
			isRussian=(c>='А'&&c<='Я'||c>='а'&&c>='я');
			if(isRussian) {
				sb.append(c);
			}else {
				if(sb.length()>0) {result.add(CommonData.textManager.addWord(sb.toString().toLowerCase()));sb=null;sb=new StringBuilder();}
				if(signsTestVersion.indexOf(c)!=-1)result.add(CommonData.textManager.addWord(String.valueOf(c)));
			}
		}
		return result;
	}
	private static class Indexes {
		public static int userID=0;
		public static int postID=0;
		public static int parentPostID=0;
		public static int postTime=0;
		public static int messageText=0;
		public static boolean allFieldsFound=false;
		public static void extractIndexes(ArrayList<String> colNames) {
			int nCols=colNames.size();
			String colName;int foundFields=0;
			userID=-1;postID=-1;parentPostID=-1;messageText=-1;postTime=-1;
			for(int i=0;i<nCols;++i) {
				colName=colNames.get(i).toLowerCase();
				if(colName.contains("userid")) {userID=i;foundFields++;}
				if(colName.contains("postid")) {postID=i;foundFields++;}
				if(colName.contains("parentpostid")) {parentPostID=i;foundFields++;}
				if(colName.contains("time")) {postTime=i;foundFields++;}
				if(colName.contains("message")) {messageText=i;foundFields++;}
			}
			allFieldsFound=(foundFields==5);
		}
	}
	public static void extractMessagesFromDataTable(DataTable messages) {
		Indexes.extractIndexes(messages.colNames);
		if(Indexes.messageText==-1) {System.out.println("DataTable '"+messages.name+"' doesnot have column contained 'message' substring");return;}
		if(!Indexes.allFieldsFound) {System.out.println("DataTable '"+messages.name+"' haven't all fields(userid,postid,parentpostid,time,message)");return;}
		int count=messages.nRows();
		Message message=null;
		for(int row=0;row<count;++row) {
			message=new Message();
			message.words = extractOnlyRussianWordsAndSignsWithoutCapitalsFromHTML((String) messages.getField(row, Indexes.messageText));
			message.userID=(int) messages.getField(row, Indexes.userID);
			message.postID=(int) messages.getField(row, Indexes.postID);
			message.parentPostID=(int) messages.getField(row, Indexes.parentPostID);
			message.postTime=(int) messages.getField(row, Indexes.parentPostID);
			message.storageLevel=1;
		}
	}
	private static void saveArrayToInputtxt(String fileName) {
		PrintWriter out=null;
		try {
			out = new PrintWriter(new File(fileName));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(out==null)return;
		
		for(int i=0;i<CommonData.textManager.textMonomials.size();++i) {
			out.println(CommonData.textManager.textMonomials.get(i));
		}
		out.close();
	}
	private static void importTags(String tagsFileName, String lemmesFileName) {
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
	public static void doAll(DataTable messages) {
		extractMessagesFromDataTable(messages);
		saveArrayToInputtxt(Settings.appPath+"saves"+Settings.linkDelimiter+"words.txt");
		runPyMorphy2(Settings.appPath+Settings.linkDelimiter+"data", "script.py");
		System.out.println("pyMorphy2 analyzer done!");
		//TODO working on OpenCorporaTag interpretation
		importTags(Settings.appPath+"saves"+Settings.linkDelimiter+"words_tag.txt", Settings.appPath+"external"+Settings.linkDelimiter+"words_normalForm.txt");
		//unifyTagsByLemmes(); - was made automatically by textManager
		
		upgradeMessageObjects();
		collectWordPatterns();
		getGraphemesFromMessages();
		resolveAnaphoras();
		getActantPatternsFromGraphemes();
	}
}