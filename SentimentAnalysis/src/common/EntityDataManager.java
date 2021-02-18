package common;

import java.util.ArrayList;

import gui.JComboBoxExt;
import util.DataTable;

public class EntityDataManager {
	private ArrayList<JComboBoxExt> controlledComboBoxes;
	public void addControlledComboBox(JComboBoxExt controlledComboBox) {this.controlledComboBoxes.add(controlledComboBox);}
	

	public int tablesSize() {return tables.size();}//=new ArrayList<DataTable>();
	private ArrayList<DataTable> tables;//=new ArrayList<DataTable>();
	public ArrayList<String> identifiers;
	
	public EntityDataManager() {
		tables=new ArrayList<DataTable>();
		identifiers= new ArrayList<String>();
		controlledComboBoxes=new ArrayList<JComboBoxExt>();
	}
	/**try to add table with identifier==table.name*/
	public void addTable(DataTable table) {
		addTable(table,table.name);
	}
	public void addTable(DataTable table, String identifier) {
		int index = identifiers.indexOf(identifier);
		if(index!=-1) {System.out.println("DataTable with identifier '"+identifier+"' already exists; try to add datatable with another identifier.");return;}
		tables.add(table);
		identifiers.add(identifier);
		for(int i=0;i<controlledComboBoxes.size();++i)controlledComboBoxes.get(i).addItem(identifier);
	}
	public void removeTable(String identifier) {
		int index=identifiers.indexOf(identifier);
		if(index==-1) {System.out.println("DataTable with identifier '"+identifier+"' not exists, nothing to remove.");return;}
		tables.get(index).clear();
		tables.remove(index);
		identifiers.remove(index);
		for(int i=0;i<controlledComboBoxes.size();++i)controlledComboBoxes.get(i).removeItemAt(index);
	}
	public DataTable getTable(String identifier) {
		int index=identifiers.indexOf(identifier);
		if(index==-1) {System.out.println("DataTable with identifier '"+identifier+"' not exists, nothing to get.");return null;}
		return tables.get(index);
	}
	
	public DataTable getTable(int index) {
		return tables.get(index);
	}
	
}
