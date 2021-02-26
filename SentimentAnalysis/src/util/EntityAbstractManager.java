package util;

import java.lang.ref.Cleaner.Cleanable;
import java.util.ArrayList;

import javax.lang.model.util.Elements;

import gui.JComboBoxExt;

public abstract class EntityAbstractManager<E extends ManagedObject> {
	protected ArrayList <E> managedElements;
	protected ArrayList <String> identifiers;
	protected ArrayList <JComboBoxExt> controlledComboBoxes;
	
	public EntityAbstractManager() {
		managedElements=new ArrayList<E>();
		identifiers= new ArrayList<String>();
		controlledComboBoxes=new ArrayList<JComboBoxExt>();
	}
	public abstract Class<?> getManagedObjectClass();
	public void addControlledComboBox(JComboBoxExt controlledComboBox) {this.controlledComboBoxes.add(controlledComboBox);}
	public int getManagedElementsCount() {return managedElements.size();}//=new ArrayList<DataTable>();
	/**try to add table with identifier==table.name*/

	public void addManagedElement(E element, String identifier) {
		int index = identifiers.indexOf(identifier);
		if(index!=-1) {System.out.println("Element with identifier '"+identifier+"' already exists; try to add this element with another identifier.");return;}
		managedElements.add(element);
		identifiers.add(identifier);
		for(int i=0;i<controlledComboBoxes.size();++i)controlledComboBoxes.get(i).addItem(identifier);
	}
	public void removeManagedElement(String identifier) {
		int index=identifiers.indexOf(identifier);
		if(index==-1) {System.out.println("Element with identifier '"+identifier+"' not exists, nothing to remove.");return;}
		managedElements.get(index).clear();
		managedElements.remove(index);
		identifiers.remove(index);
		for(int i=0;i<controlledComboBoxes.size();++i)controlledComboBoxes.get(i).removeItemAt(index);
	}
	public E getManagedElement(String identifier) {
		int index=identifiers.indexOf(identifier);
		if(index==-1) {System.out.println("Element with identifier '"+identifier+"' not exists, nothing to get.");return null;}
		return managedElements.get(index);
	}
	public int getManagedElementIndex(String identifier) {
		int index=identifiers.indexOf(identifier);
		if(index==-1) {System.out.println("Element with identifier '"+identifier+"' not exists, nothing to get.");return -1;}
		return index;
	}
	public String getManagedElementIdentifier(int index) {
		return identifiers.get(index);
	}
	public E getManagedElement(int index) {
		return managedElements.get(index);
	}
}
