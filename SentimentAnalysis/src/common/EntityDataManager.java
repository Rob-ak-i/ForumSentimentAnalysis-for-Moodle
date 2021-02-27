package common;

import java.util.ArrayList;

import gui.JComboBoxExt;
import util.DataTable;
import util.EntityAbstractManager;

public class EntityDataManager extends EntityAbstractManager<DataTable>{
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
	
}
