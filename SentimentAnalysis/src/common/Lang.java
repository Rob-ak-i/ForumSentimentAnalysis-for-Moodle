package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lang {
	public static ArrayList<String> values=null;
	public static ArrayList<String> identifiers=null;
	public static void loadTables() {
		Scanner in=null;
		try {
			in = new Scanner(new File(CommonData.dataSubDir+"lang.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace(); return;}
		String data = in.toString();
		loadValues(data);
	}
	/*
	public static String getField(String identifier) {
		try {
			int i=0,index=-1, len=identifiers.size();
			while (i<len) {
				//identifier.equals();
				i++;
			}
		}catch(Exception e) {
			return "MISSING:"+identifier;
		}
		
	}
	*/
	private static void loadValues(String text) {
		Pattern names = Pattern.compile(".+[^\\r\\n]");
		Matcher namesMatcher = names.matcher(text);
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> valueList = new ArrayList<String>();
		int valueBegin=0, valueEnd=0;String nowStr=null;
		while (namesMatcher.find()) {
			nowStr=text.substring(namesMatcher.start(), namesMatcher.end());
			valueBegin=nowStr.indexOf(' ')+1;
			valueEnd=nowStr.length();
			valueList.add(nowStr.substring(valueBegin, valueEnd));
			nameList.add(nowStr.substring(0, valueBegin-1));
		}
		for(int i=0;i<Math.min(nameList.size(),valueList.size());++i)
			System.out.println(nameList.get(i)+"\n\""+valueList.get(i)+"\"");
	}
	public static void LoadInnerTable(String filename) {
		Scanner in=null;
		try {
			in = new Scanner(new File(filename));
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(in==null)return;
		String line=null,classname="",fieldname="";
		classname=Lang.class.getName();
		int brake0pos,brake1pos, equalspos, spacepos;
		@SuppressWarnings("rawtypes")
		Class nowClass=Lang.class;
		try {
			while(true) {
				line = in.nextLine();
				int i=0;
				for(;(line.charAt(i)=='	' || line.charAt(i)==' ');++i);if(i>0)line=line.substring(i, line.length());
				brake0pos=line.indexOf('{');
				brake1pos=line.indexOf('}');
				spacepos=line.indexOf(' ');
				equalspos=line.indexOf('=');

				if(equalspos!=-1) {
					int strbrake0pos, strbrake1pos;
					strbrake0pos=line.indexOf('\"');
					strbrake1pos=line.lastIndexOf('\"');
					int x1=line.indexOf(' ');
					if((x1==-1)||(x1>equalspos))x1=equalspos;
					fieldname = line.substring(0, x1);
					String caption = line.substring(strbrake0pos+1, strbrake1pos);
					try {
						nowClass.getField(fieldname).set(nowClass, caption);
					}catch (Exception e) {
						System.out.println(InnerTable.Console.notValidLangClassNameFieldName+" Class:"+classname+"; Field:"+fieldname);
					}
					continue;
				}
				if(brake0pos!=-1) {
					int x1 = spacepos!=-1? spacepos : brake0pos;
					classname=classname + (classname.length()>0? "$" : "") + line.substring(0,x1);
					try {
						nowClass = Class.forName(classname);
					}catch(Exception e) {
						System.out.println(InnerTable.Console.notValidLangClassNameFieldName+" Class:"+classname);
					}
					continue;
				}
				if(brake1pos!=-1) {
					int dotpos=classname.lastIndexOf('$');
					if(dotpos==-1) {
						classname=Lang.class.getName();
						nowClass = Lang.class;
					}else {
						classname=classname.substring(0,dotpos);
						try {
							nowClass = Class.forName(classname);
						}catch(Exception e) {
							System.out.println(InnerTable.Console.notValidLangClassNameFieldName+" Class:"+classname);
						}
					}
					continue;
				}
				
			}
		}catch(Exception e) {
		}
		in.close();
	}
	private static class printer{
		static PrintWriter out;
		private static int tabulations=0;
		public static String tabulationChars="";
		public static void print(String str) {
			if(tabulations>0)out.println(tabulationChars+str);
			else out.println(str);
		}
		public static void printBegin(String str) {
			out.println(tabulationChars+str+" {");
			incTab();
		}
		public static void printEnd() {
			decTab();
			out.println(tabulationChars+"}");
		}
		public static void incTab() {tabulations+=1;tabulationChars=tabulationChars+"    ";}
		public static void decTab() {if(tabulations<=0)return;tabulations-=1;tabulationChars=tabulationChars.substring(0, tabulationChars.length()-4);}
	}
	@SuppressWarnings("rawtypes")
	public static void SaveInnerTable(String filename) throws IllegalArgumentException, IllegalAccessException {
		PrintWriter out=null;
		try {
			out = new PrintWriter(new File(filename));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(out==null)return;
		printer.out=out;
		//-------text start---------
		printer.printBegin(InnerTable.class.getSimpleName());
		Class[] innerTableClasses = InnerTable.class.getClasses();
		Field[] fields=null;String tmp=null;
		for(int i=0;i<innerTableClasses.length;++i) {
			fields = innerTableClasses[i].getFields();
			printer.printBegin(innerTableClasses[i].getSimpleName());
			for(int j=0;j<fields.length;++j) {
				printer.print(fields[j].getName()+" = "+'\"'+fields[j].get(tmp)+'\"');
			}
			printer.printEnd();
		}
		printer.printEnd();
		//-------text finish--------
		out.close();
	}
	
	public static class InnerTable{
		public static class Tooltip{
			public static String arrowbutton = "observe mode";
			public static String selectbutton = "select mode";
			public static String buttonBrushAddNode = "add node mode";
			public static String buttonBrushAddPart = "add connection mode";
			public static String buttonApplyName = "give new name to selected";
			public static String partsListComboBox = "change connection type";
			public static String buttonApplyCaption = "apply new caption to selected";
			public static String buttonSwapPart = "swap connection direction";
			public static String buttonMovePart = "move element mode";
			public static String buttonRemovePart = "remove selected parts";
		}
		public static class Item{
			public static String windowSequenceSelectorName = "Sequence selector";
			public static String windowBankSelectorName = "MessageBank selector";
			public static String windowTableSelectorName = "Table selector";
			public static String windowReportSelectorName = "Report selector";
			public static String window2Name = "Table editor";
			public static String windowName = "Forum browser";
			public static String itemFileMenuName = "File";
			public static String itemParametersMenuName = "Setup";
			public static String itemCommonMenuName = "Common";
			public static String itemEditorMenuName = "Editor";
			public static String itemComputationsMenuName = "Computations";
			public static String itemTextMenuName = "ShowPartNames";
			public static String itemTextMenuSwitchedName = "HidePartNames";
			public static String selectorModeMenuName = "SelectorMode: Elements";
			public static String selectorModeMenuSwitchedName = "SelectorMode: Nodes";
			public static String selectorModeMenuSwitched2Name = "SelectorMode: Parts";
			public static String itemNameEditFantomCaptionName = "Part Name";
			public static String itemCaptionEditFantomCaptionName = "Part Basic Value";
			public static String itemNameEditFantomMultipleCaptionName = "Set Names to many objects";
			public static String itemCaptionEditFantomMultipleCaptionName = "Set Value to many objects";
		}
		public static class Action{
			public static String buttonAcceptActionName = "Yes";
			public static String buttonDeclineActionName = "No";
			public static String buttonAbortActionName = "Cancel";
			public static String loadCircuitActionName = "Load DataTable or schematic";
			public static String saveCircuitActionName = "Save DataTable or schematic";
			public static String exportReportActionName = "Export report";
			public static String saveCircuitActionErroredName = "Error while trying to save";
			public static String showTextMenuActionName = "ShowPartNamesMenu";
			public static String selectorModeMenuActionName = "SelectorMode";
			public static String printForumMenuActionName = "print forum on editor";
			public static String printSequenceMenuActionName = "print NGramms Sequence tree on editor";
			public static String printSyntaxTestMenuActionName = "print syntax tree of messages on editor";
			public static String analyzeMessagesMenuActionName = "analyze MessagesBank from DataTable";
			public static String analyzeLemmaPatternsMenuActionName = "analyze all NGramms from MessageBank(MessageBank)";
			public static String showTableEditorMenuActionName = "show table editor GUI";
			public static String showTextMenuActionOptionName = "ShowPartNamesMenuOption";
			public static String showTextMenuActionDescriptionName = "Show PartNames on Circuit?";
			public static String computationsMenuAction0Name = "LocalizeTracksAndNodes";
			public static String computationsMenuAction1Name = "PrintMsgCount";
			public static String computationsMenuAction2Name = "PrintDicts";
			
			public static String fileNotFoundName = "E404";
			public static String WIPName = "WorkInProgress";
			public static String WbFName = "WillBeInFuture";
			public static String MbFName = "MayBeInFuture";
		}
		public static class Report{
			public static String reportPart1HeadTitleName = "Report for solution of CGP 1";
			public static String reportPart1Head1Name = "Schematic:";
			public static String reportPart1Text1 = "Net List:";
			public static String reportPart1Text2 = "Net #";
			public static String reportPart1Text3 = "Current equations by first Kirchghoff laws";
			public static String reportPart1PrintImageTextName = "Alternate text";
			public static String reportBasicName = "Report.htm";
		}
		public static class Dialog{
			public static String notValidClassName = "Error! Object with selected class could not be created.";
			public static String SchemeStorageSaveResultText1 = "Number of nodes in schematic";
			public static String SchemeStorageSaveResultText2 = "Number of parts in schematic";
			public static String SchemeStorageSaveResultText3 = "List of nodes";
			public static String SchemeStorageSaveResultText4 = "_ X Y Name";
			public static String SchemeStorageSaveResultText5 = "List of parts";
			public static String SchemeStorageSaveResultText6 = "Type";
			public static String SchemeStorageSaveResultText7 = "Node0_ Node1_ Caption  X Y Name";
			public static String SchemeStorageSaveResultText8 = "Additional data";
			public static String GUIHelper_nameedit_defaultCaption="Part name";
			public static String GUIHelper_captionedit_defaultCaption="part value";
		}
		public static class GUI{
			public static String GUIElementViewerName = "Element viewer";
			
			public static String AbstractSelectorLablePerformAction = "processing..."; 
			
			public static String TableSelectorLableUseForScript= "Select identifier name for making script with element";
			public static String TableSelectorLableSave = "Select identifier name for saving element";
			public static String TableSelectorLableLoad = "Press unique identifier name for loading element"; 
			public static String TableSelectorLablePrint = "Select identifier name for element which will be printed";
			public static String TableSelectorLableUpgradeToDataBank = "Select identifier name for table which contains All Messages for language processing";
			public static String TableSelectorLableUpgradeToSequence= "select MessagesBank for making sequences";
			
		}
		public static class Console{
			public static String partsSchematicManager_ComputeWays_Start = "START: lost connections exist at region: "; 
			public static String partsSchematicManager_ComputeWays_Finish = "FINISH: lost connections exist at region: ";
			public static String partsSchematicManager_ComputeWays_Onesupernode = "unpredictable situation. Nextly Recursion will could to been.";
			public static String notValidLangClassNameFieldName = "error, wrong class/field in language loading!";
			public static String in_test="work quality not checked yet!";
		}
		public static class Misc{
			public static String dataTableToMessageBankAppendix = "MessageBankFromDataTable:";
			public static String messageBankToPhrasesTreeAppendix = "PhrasesTreeFromMessageBank:";
			
			public static String part_0 = "Line";
			public static String part_1 = "Relation";
			public static String part_2 = "Words";

			public static String table_tutorial = "press '=' to next table, press '-' to previous table, pres Esc to exit";
			public static String table_appendMode = "appendToPrintedGraphEnabled";
		}
	}
}