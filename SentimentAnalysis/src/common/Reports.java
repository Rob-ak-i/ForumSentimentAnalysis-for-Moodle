package common;

import java.util.ArrayList;

import parts.SchematicManager;
import util.DataTable;
import util.Lists;

public class Reports {
	//---------------------------------behavioral troubles---------------------------------------------------------------------------
		public static void makeRGZ() {
			
			
		}
		//TODO garbage
		/**marked for garbage*/
		public static void createReport() {
			SchematicManager.reindexAllObjects();
			SchematicManager.computeSuperNodesAndWays();
			// TO DO WIP
				//SchematicManager.computePartsForAllNodes();
			ReportWriter.writeResult(common.Lang.InnerTable.Report.reportBasicName);
		}
		public static void makeResult1() {
			CommonData.frame_tableSelector.prepareToSaveReport(1);
		}public static void makeResult1_inner(DataTable selectedTable) {
			ArrayList<String> result=new ArrayList<String>();
			result.add("Список пользователей, отсортированный по числу сообщений");
			ArrayList<Integer> users = new ArrayList<Integer>();
			Lists.getUniqueArrayList(TextForum.getUserIDList(selectedTable), users);
			//DataTable table=new DataTable("userName/String;userMsgCount/Integer");
			DataTable table=selectedTable;
			//"userIDList/Integer;
			//userNameList/String;
			//postIDList/Integer;
			//parentPostIDList/Integer;
			//postTimeList/String;
			//messageList/String"
			table.removeColumn(5);
			table.removeColumn(4);
			table.removeColumn(3);
			ArrayList<Object> ones = new ArrayList<Object>();
			for(int i=0;i<table.nRows();++i) {ones.add(1);}
			table.fillColumn(2, ones, 0);
			int[] tableSortOrder={0};
			boolean[] sortOrderForward= {true};
			table.sortRows(tableSortOrder,sortOrderForward);
			int[] columnsChangeBehavior = {0,0,1};
			table.NarrowByUnique(0, columnsChangeBehavior);
			table.sortRowsByColumnIndexes("-2;-0");
			int[] columnsPrintOrder = {2,0,1};
			table.saveDataToFile(CommonData.reportsSubDir+"report1.txt", columnsPrintOrder, false);
		}
		public static void makeResult2() {
			System.out.println("WIP");
		}
		public static void makeNLP() {
			CommonData.frame_tableSelector.prepareToSaveReport(3);
		}
}
