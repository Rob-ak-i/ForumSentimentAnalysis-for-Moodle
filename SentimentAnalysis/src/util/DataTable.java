package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class DataTable {
	//TODO -----------DATATABLE-ENGINEERING-PART----
	public ArrayList<ArrayList> fields;
	public int nRows() {return fields.size();};
	public int nColumns() {return colNames.size();};
	public String name, title;
	public ArrayList<String> colNames;
	//public ArrayList<String> colTypes;
	public ArrayList<Class> colClasses;
	
	public Object getField(int row, int col) {
		return (fields.get(row).get(col));
	}
	private void readColumns(String columns) {
		if(columns.charAt(columns.length()-1)!=';')columns=columns.concat(";");
		int l=columns.length();
		char c;
		StringBuilder sb=new StringBuilder();
		String str;
		fields = new ArrayList<ArrayList>();
		for(int i=0;i<l;++i) {
			c=columns.charAt(i);
			switch(c) {
			case ':':
				colNames.add(sb.toString());
				sb=new StringBuilder();
				break;
			case ';':
				str=sb.toString();

				if(str.equalsIgnoreCase("string")) {
					colClasses.add(String.class);
				}else if(str.equalsIgnoreCase("integer")) {
					colClasses.add(Integer.class);
				}else if(str.equalsIgnoreCase("double")) {
					colClasses.add(Double.class);
				}colClasses.add(GenericObject.class);
				sb=new StringBuilder();
				break;
			default:
				sb.append(c);
			}
		}
	}
	/**input format : "COLNAME:COLTYPE;..."
	 * supported types: Integer,String,Double,Object  */
	public DataTable(String columns) {
		name="";title="";
		colNames = new ArrayList<String>();
		colClasses= new ArrayList<Class>();
		readColumns(columns);
	}
	private DataTable() {
		colNames = new ArrayList<String>();
		colClasses= new ArrayList<Class>();
	}
	private String packString(String str) {
		str=str.replace("\n", "\\n");
		str=str.replace("\"", "\\\"");
		str=str.replace("\\", "\\\\");
		return '\"'+str+'\"';
	}
	private String unpackString(String str) {
		int l=str.length();
		str=str.substring(1,l-1);
		str=str.replace("\\n", "\n");
		str=str.replace("\\\"", "\"");
		str=str.replace("\\\\", "\\");
		return str;
		
	}
	//TODO -----------DATATABLE-IO-PART----
	public void saveDataToFile(String fileName, int[]columnsPositionOrder, boolean includeHeader) {
		File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {return; }//throw new RuntimeException(e);}
		PrintWriter out=null; try {out = new PrintWriter(file);} catch (FileNotFoundException e) {e.printStackTrace(); return;}
		StringBuilder line;
		ArrayList<Object> row;Object cell;
		if(includeHeader) {
			out.print('\"');
			for(int j=0;j<nColumns();++j) {
				out.print(colNames.get(columnsPositionOrder[j])+":"+(colClasses.get(columnsPositionOrder[j]).getSimpleName())+";");
			}
			out.print('\"');
			out.println();
		}
		for(int i=0;i<nRows();++i) {
			line=new StringBuilder();
			row=fields.get(i);
			for(int j=0;j<nColumns();++j) {
				cell=row.get(columnsPositionOrder[j]);
				if(colClasses.get(columnsPositionOrder[j]).equals(String.class)) {
					String str=(String)cell;
					line.append(packString(str));
				}else
					line.append(cell.toString());
				line.append(' ');
			}
			out.println(line.toString());
		}
		out.close();
	}
	//TODO -----------DATATABLE-IO-PART----
	public static DataTable readFromFile(String fileName) {
		DataTable result = new DataTable();
		result.readDataFromFile(fileName);
		return result;
	}
	public void readDataFromFile(String fileName) {
		readDataFromFile(fileName, true, true);
	}
	public void readDataFromFile(String fileName, boolean includeHeader) {
		readDataFromFile(fileName, includeHeader, false);
	}
	private void readDataFromFile(String fileName, boolean includeHeader, boolean isIncludeHeaderMissing) {
		File file = new File(fileName);try {if(!file.exists())file.createNewFile();} catch(java.io.IOException e) {return; }//throw new RuntimeException(e);}
		Scanner in=null; try {in = new Scanner(file);} catch (FileNotFoundException e) {e.printStackTrace(); return;}
		StringBuilder line;String str;
		ArrayList<Object> row;Object cell;
		char c;
		int nowIndex;
		while(in.hasNext()) {
			str=in.nextLine();
			if(includeHeader){
				includeHeader=false;
				if(isIncludeHeaderMissing) {
					if(this.columnFormatDetector(str)) {
						//if header doesnot exist but header autodecoded successful
					}else {
						//if is header are in file but user doesnot says about it
						readColumns(str);
						continue;
					}
				}else {
					//if user said about header exists and we are print this; 
					readColumns(str);
					continue;
				}
			}
			row=new ArrayList();
			nowIndex=0;
			for(int col=0;col<nColumns();++col) {
				//class definition may be if will be needed
				//if(str.charAt(nowIndex)=='\"')class1=String.class;
				nowIndex=addDataToRowFromString(row,col,str,nowIndex);
			}
			fields.add(row);
		}
		in.close();
	}
	private int addDataToRowFromString(ArrayList row, int columnIndex, String str, int beginIndex) {
		String datastring=null;
		int endIndex;
		Class classOfObject=colClasses.get(columnIndex);
		if(classOfObject.equals(String.class)) {
			datastring=readString(str,beginIndex);
			endIndex=datastring.length()+beginIndex;
			row.add(datastring);
			return endIndex;
		}
		if(classOfObject.equals(Integer.class)) {
			datastring=readInteger(str,beginIndex);
			endIndex=datastring.length()+beginIndex;
			row.add(Integer.valueOf(datastring));
			return endIndex;
		}
		if(classOfObject.equals(Double.class)) {
			datastring=readString(str,beginIndex);
			endIndex=datastring.length()+beginIndex;
			row.add(Double.valueOf(datastring));
			return endIndex;
		}
		datastring=readObject(str,beginIndex);
		endIndex=datastring.length()+beginIndex;
		row.add(new GenericObject(datastring));
		return endIndex;
	}
	private String readString(String str, int beginIndex) {
		int l=str.length();
		if(str.charAt(beginIndex)==' ')beginIndex++;
		int index=beginIndex;
		if(str.charAt(index)=='\"')index++;
		while(index<l) {
			index=str.indexOf("\"", index);
			if(index==-1)return null;
			if(str.charAt(index-1)!='\\')return unpackString(str.substring(beginIndex,index));
			index++;
		}
		return null;
	}
	private static final String digits="0123456789";
	private static final String digitsAndDots="0123456789.,";
	private String readDouble(String str, int beginIndex) {
		int l=str.length();
		if(str.charAt(beginIndex)==' ')beginIndex++;
		int index=beginIndex;
		char c;
		boolean pointAlreadyAdded=false;
		int digit=digitsAndDots.indexOf(str.charAt(index));
		while(digit!=-1 && index<l) {
			if(digit>9) {
				if(pointAlreadyAdded)return str.substring(beginIndex, index);
				pointAlreadyAdded=true;
			}
			index++;
			digit=digitsAndDots.indexOf(str.charAt(index));
		}
		return str.substring(beginIndex, index);
	}
	private String readInteger(String str, int beginIndex) {
		int l=str.length();
		if(str.charAt(beginIndex)==' ')beginIndex++;
		int index=beginIndex;
		char c;
		int digit=digits.indexOf(str.charAt(index));
		while(digit!=-1 && index<l) {
			index++;
			digit=digitsAndDots.indexOf(str.charAt(index));
		}
		return str.substring(beginIndex, index);
	}
	private static final String hexDigits="0123456789abcdefABCDEF";
	private String readObject(String str, int beginIndex) {
		int l=str.length();
		if(str.charAt(beginIndex)==' ')beginIndex++;
		if(str.charAt(beginIndex)=='#')beginIndex++;
		int index=beginIndex;
		int digit=digitsAndDots.indexOf(str.charAt(index));
		while(digit!=-1 && index<l) {
			index++;
			digit=digitsAndDots.indexOf(str.charAt(index));
		}
		return str.substring(beginIndex, index);
	}
	private Object getInstance(Class class1) {
		if(class1.equals(String.class))return (Object)(new String());
		if(class1.equals(Double.class))return (Object)(0.D);
		if(class1.equals(Integer.class))return (Object)(0);
		try {
			return class1.getConstructor(null).newInstance(null);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();}
		return null;
	}
	public void setValueOfString(int row, int col,String caption) {
		fields.get(row).set(col,caption);
	}
	public void setValueOfInteger(int row, int col,Integer caption) {
		fields.get(row).set(col,caption);
	}
	public void setValueOfDouble(int row, int col,Double caption) {
		fields.get(row).set(col,caption);
	}
	public void setValue(int row, int col,Object caption) {
		Class class1 = colClasses.get(col);
		if(class1.equals(String.class)) {fields.get(row).set(col,(String)caption);return;}
		if(class1.equals(Double.class)) {fields.get(row).set(col,(Double)caption);return;}
		if(class1.equals(Integer.class)) {fields.get(row).set(col,(Integer)caption);return;}
		fields.get(row).set(col,(Object)caption);
		
	}
	private void createRows(int rowsCount) {
		ArrayList newRow;Object newCell = null;
		Class class1;
		int colsCount=nColumns();
		for(int i=0;i<rowsCount;++i) {
			newRow = new ArrayList();
			for(int j=0;j<colsCount;++j) {
				class1=colClasses.get(j);
				if(class1.equals(String.class)) {newRow.add("");continue;}
				if(class1.equals(Double.class)) {newRow.add(0.D);continue;}
				if(class1.equals(Integer.class)) {newRow.add(0);continue;}
				try {newRow.add(class1.getConstructor(null).newInstance(null));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
			fields.add(newRow);
		}
	}
	public void fillColumn(int colIndex, ArrayList in,int fillFrom) {
		if(fillFrom+in.size()>nRows())createRows(fillFrom+in.size()-nRows());
		for(int i=0;i<in.size();++i) {
			//((Object)(fields.get(i+fillFrom).get(colIndex))) = in.get(i);
			fields.get(i+fillFrom).set(colIndex, in.get(i));
		}
	}/**before using this sort array by key value 
	behavior is:
	0 - erase other captions
	1 - sum(concatenation)*/
	public void NarrowByUnique(int keyColumn,int[]columnsChangeBehavior) {
		Object nowKeyValue=null,oldKeyValue=null;int lastUniqueRow=0;
		for(int i=0;i<nRows();++i) {
			if(nowKeyValue!=null) {oldKeyValue=nowKeyValue;}
			nowKeyValue=fields.get(i).get(keyColumn);
			if(oldKeyValue==null)continue;
			if(oldKeyValue.equals(nowKeyValue)) {
				for(int j=0;j<nColumns();++j) {
					if(j==keyColumn) {fields.get(i).set(j, null);continue;}
					if(columnsChangeBehavior[j]==1) {
						if(colClasses.get(j).equals(String.class)) {
							setValueOfString(lastUniqueRow,j,
									((String)(fields.get(lastUniqueRow).get(j)))
									+
									((String)(fields.get(i).get(j)))
									);
							continue;
						}
						if(colClasses.get(j).equals(Integer.class)) {
							setValueOfInteger(lastUniqueRow,j,
											((Integer)(fields.get(lastUniqueRow).get(j)))
											+
											((Integer)(fields.get(i).get(j)))
									);
							continue;
						} 
						if(colClasses.get(j).equals(Double.class)) {
							setValueOfDouble(lastUniqueRow,j,
									((Double)(fields.get(lastUniqueRow).get(j)))
									+
									((Double)(fields.get(i).get(j)))
							);
							continue;
						}
						
					}
				}
				fields.get(i).clear();
			}else
				lastUniqueRow=i;
		}
		
		for(int i=0;i<fields.size();) {
			if(fields.get(i).isEmpty())fields.remove(i);
			else ++i;
		}
	}
	public void removeColumn(int colName) {
		for(int i=0;i<nRows();++i) {
			fields.get(i).remove(colName);
		}
		colClasses.remove(colName);
		colNames.remove(colName);
	}
	private boolean columnFormatDetector(String example) {
		char c;
		int index=0,l=example.length();
		String dataString;
		//colClasses=new ArrayList<Class>();
		//colNames=new ArrayList<String>();
		while(index<l) {
			c=example.charAt(index);
			switch(c) {
			case'\"':
				dataString=this.readString(example, index);
				index+=dataString.length();
				if(dataString.length()==l && dataString.indexOf(':')!=0 && dataString.indexOf(';')!=0) {return false;
				}
				colClasses.add(String.class);
				break;
			case'#':
				dataString=this.readObject(example, index);
				index+=dataString.length();
				colClasses.add(GenericObject.class);
				break;
			case' ':
				index++;
				continue;
			default:
				dataString=this.readDouble(example, index);
				index+=dataString.length();
				if(dataString.indexOf('.')==-1)
					colClasses.add(Integer.class);
				else
					colClasses.add(Double.class);
			}
			colNames.add("column"+Integer.toString(colNames.size())+"of"+colClasses.get(colClasses.size()).getSimpleName());
			
		}
		return true;
	}
	/**input of this: (1,0,2)(false,false,true)
	 * means that sort will be in 3 rules: first priority has second column that sorts by reverse order,
	 * second priority sort will be reverse and by first column, 
	 * and third priority has third column that sorts directly.*/
	public void sortRows(int[] columnsSortPriority, boolean[] dirsForward) {
		if((columnsSortPriority.length==0) || (columnsSortPriority.length>nColumns()))return;
		Collections.sort(fields,new SortBy(columnsSortPriority.length, columnsSortPriority, dirsForward));
	}
	//TODO public void sortColumns(int[] columnsPositionOrder) {WIP;}
	/**INPUT LIKE THIS: ("-colName1;+colName2;-colName3")
	 *  - this input causes procedure will make sort by priority 1 fifth column reverseDirection, etc...*/
	public void sortRowsByColumnNames(String sortRules) {sortRows(sortRules, true);}
	/**INPUT LIKE THIS: ("-5;+2;-4")
	 *  - this input causes procedure will make sort by priority 1 fifth column reverseDirection, etc...*/
	public void sortRowsByColumnIndexes(String sortRules) {sortRows(sortRules, false);}
	/**INPUT LIKE THIS: ("-colName1;+colName2;-colName3",true)
	 * OR: ("-5;+2;-4",false)
	 *  - this input causes procedure will make sort by priority 1 fifth column reverseDirection, etc...*/
	public void sortRows(String sortRules,boolean isNamesinRules) {
		if(sortRules.charAt(sortRules.length()-1)!=';')sortRules=sortRules.concat(";");
		int l=sortRules.length();
		int[] colsOrder=null;
		boolean[] dirsForward=null;
		char c;int i=0;
		ArrayList<Integer>columnLinks = new ArrayList<Integer>();
		StringBuilder sortDirectionsAtLinks = new StringBuilder();
		StringBuilder sb=new StringBuilder();
		while(i<l) {
			c=sortRules.charAt(i);
			switch(c) {
			case '-': case '+':
				sortDirectionsAtLinks.append(c);
				break;
			case ';':
				String str=sb.toString();
				sb=new StringBuilder();
				if(isNamesinRules) {
					int index=colNames.indexOf(str);
					if (index==-1)return;
					columnLinks.add(index);
				}else {
					int index=Integer.valueOf(str);
					if(index>=nColumns())return;
					columnLinks.add(index);
				}
				if(sortDirectionsAtLinks.length()<columnLinks.size())
					sortDirectionsAtLinks.append('+');
				break;
			default:
				sb.append(c);
			}
			++i;
		}
		if(columnLinks.size()<=0 || columnLinks.size()>this.nColumns())return;
		colsOrder=new int[columnLinks.size()];
		dirsForward=new boolean[columnLinks.size()];
		for(i=0;i<columnLinks.size();++i) {
			colsOrder[i]=columnLinks.get(i);
			dirsForward[i]=sortDirectionsAtLinks.charAt(i)=='+';
		}
		sortRows(colsOrder,dirsForward);
	}
	static class SortBy implements Comparator<Object>{
		//public static Class[] colTypes=null;
		public int sortOrdersNumber;
		public int[] sortColumnsOrder;
		public boolean[] sortForward;
		public SortBy(int sortOrdersNumber, int[] sortColumnsOrder, boolean[]sortForward) {
			super();
			this.sortOrdersNumber=sortOrdersNumber;
			this.sortColumnsOrder=sortColumnsOrder;
			this.sortForward=sortForward;
		}
		@Override
		public int compare(Object a, Object b) {
			int result=0;
			//switch(colTypes)
			result=((Comparable)(((ArrayList)(a)).get(sortColumnsOrder[0]))).compareTo(((ArrayList)(b)).get(sortColumnsOrder[0]));
			if(result==0 && sortOrdersNumber>1)
				return compare((ArrayList)a,(ArrayList)b,1);
			
			if(!sortForward[0])
				return -result;
			return result;
		}
		private int compare(ArrayList a, ArrayList b, int sortOrderNumber) {
			int result=0;
			result=((Comparable)(a.get(sortColumnsOrder[sortOrderNumber]))).compareTo(b.get(sortColumnsOrder[sortOrderNumber]));
			if(result==0 && sortOrderNumber<sortOrdersNumber)
				return compare(a,b,sortOrderNumber+1);
			if(!sortForward[0])
				return -result;
			return result;
		}
	}
}
