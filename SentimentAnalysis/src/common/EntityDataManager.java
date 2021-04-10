package common;

import java.util.ArrayList;
import java.util.HashMap;

import gui.JComboBoxExt;
import util.DataTable;
import util.EntityAbstractManager;

public class EntityDataManager extends EntityAbstractManager<DataTable> implements ObjectProcessor{
	/* DataTable manager */
	
	
	
	private ArrayList<DataTable> tables;//=new ArrayList<DataTable>();
	public ArrayList<String> identifiers;
	
	public EntityDataManager() {
		super();
	}
	public void addManagedElement(DataTable table) {
		addManagedElement(table,table.name);
	}
	public Class<DataTable> getManagedObjectClass() {
		// TODO Auto-generated method stub
		return DataTable.class;
	}
	public boolean loadDataTable(String fileName, String key) {
		DataTable table = DataTable.readFromFile(CommonData.fileName);
		if(table==null)return false;
		addManagedElement(table, key);
		return true;
	}
	public boolean saveDataTable(String fileName, String key) {
		DataTable table=getManagedElement(key);
		if(table==null)return false;
		table.saveDataToFile(fileName, true);
		return true;
	}
	@Override
	public boolean process(String methodName, HashMap<String, Object> parameters) {
		boolean result=true;
		int nCatches = 0;
		if(methodName.equals("loadDataTable")) {
			result=loadDataTable((String)parameters.get("fileName"), (String)parameters.get("key"));
			nCatches++;
		}
		if(methodName.equals("saveDataTable")) {
			result=loadDataTable((String)parameters.get("fileName"), (String)parameters.get("key"));
			nCatches++;
		}
		parameters.clear();
		parameters=null;
		if(nCatches!=1)return false;
		return result;
	}
	
}
