package common;

import java.util.ArrayList;

import parts.SchematicManager;
import util.DataTable;
import util.Lists;

public class Reports {
	//---------------------------------behavioral troubles---------------------------------------------------------------------------
		public static void makeRGZ() {
			
			
		}
		public static void createReport() {
			SchematicManager.reindexAllObjects();
			SchematicManager.computeSuperNodesAndWays();
			//TODO WIP
				SchematicManager.computePartsForAllNodes();
			ReportWriter.writeResult(common.Lang.InnerTable.Report.reportBasicName);
		}
		public static void makeResult1() {
			ArrayList<String> result=new ArrayList<String>();
			result.add("Список пользователей, отсортированный по числу сообщений");
			ArrayList<Integer> users = new ArrayList<Integer>();
			Lists.getUniqueArrayList(CommonData.discuss.userIDList, users);
			//DataTable table=new DataTable("userName/String;userMsgCount/Integer");
			DataTable table=CommonData.discuss.toDataTable();
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
		public static void makeResult3() {
			DataTable table=CommonData.discuss.toDataTable();
			int[] columnsSortOrder = {0,1,2,3,4,5};
			table.saveDataToFile(CommonData.reportsSubDir+"report3.txt", columnsSortOrder, true);
		}
}
