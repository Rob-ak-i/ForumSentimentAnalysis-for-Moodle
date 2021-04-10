package util;

import java.util.ArrayList;
import java.util.List;

import languageprocessing.Sequence;

public class IntList {
	public int[] data;
	private int count;
	private int length;
	public int size() {return count;}
	public void clear() {
		count=0;length=0;
		data=null;
	}
	public String toText(ArrayList<String> wordsArray){
		StringBuilder result=new StringBuilder();
		for(int i=0;i<count;++i) {
			result.append(wordsArray.get(data[i]));
			result.append(' ');
		}
		return result.toString();
	}
	public ArrayList<Integer> toArrayList(){ArrayList<Integer> result= new ArrayList<Integer>();for(int i=0;i<count;++i)result.add(data[i]);return result;}
	public int[] subList(int indexBegin, int indexEnd) {
		int count1=indexEnd-indexBegin;
		int[] resultData=new int[count1];int j=0;
		for(int i=indexBegin;i<indexEnd;++i) {
			try{
			resultData[j]=data[i];
			}catch (Exception e) {
				System.out.println(i);
				System.out.println(i);
				System.out.println(count1);
				System.out.println(indexBegin);
				System.out.println(indexEnd);
				System.out.println(count);
				System.out.println(data[i]);
				}
			++j;
		}
		return resultData;
	}
	public IntList subIntList(int indexBegin, int indexEnd) {
		int[] resultData = subList(indexBegin, indexEnd);
		IntList result =new IntList(resultData,false);
		return result;
	}
	public int indexOf(int number) {
		return indexOf(number, 0);
	}
	public int indexOf(int number, int indexFrom) {
		if(indexFrom>count)return -1;
		for(int i=indexFrom;i<count;++i) {
			if(data[i]==number) {
				return i;
			}
		}
		return -1;
	}
	public int lastIndexOf(int number) {
		return lastIndexOf(number, count);
	}
	public int lastIndexOf(int number, int indexFrom) {
		if(indexFrom>count)return -1;
		for(int i=indexFrom-1;i>=0;--i) {
			if(data[i]==number) {
				return i;
			}
		}
		return -1;
	}
	public int indexOf(IntList subSequence) {return indexOf(subSequence,0);}
	public int indexOf(IntList subSequence, int indexFrom) {
		int firstFindElement=subSequence.data[0],count1=subSequence.size();
		int occurrenceIndex=-1;boolean occurenceOk;
		int iMax=count-count1+1;if(indexFrom>iMax)return -1;
		for(int i=indexFrom;i<iMax;++i) {
			if(data[i]==firstFindElement) {
				occurenceOk=true;
				for(int j=1;j<count1;++j) {
					if(data[i+j]!=subSequence.data[j]) {
						occurenceOk=false;break;}}
				if(occurenceOk) {occurrenceIndex=i;return i;}
			}
		}
		return -1;
	}
	public int lastIndexOf(IntList subSequence, int indexFrom) {
		int firstFindElement=subSequence.data[0],count1=subSequence.size();
		int occurrenceIndex=-1;boolean occurenceOk;
		if(indexFrom>count-count1)indexFrom=count-count1;
		for(int i=indexFrom;i>=0;--i) {
			if(data[i]==firstFindElement) {
				occurenceOk=true;
				for(int j=1;j<count1;++j) {
					if(data[i+j]!=subSequence.data[j]) {
						occurenceOk=false;break;}}
				if(occurenceOk) {occurrenceIndex=i;return i;}
			}
		}
		return -1;
	}
	public int get(int index) {return data[index];}
	public void add(int element) {
		if(count>=length) extend();
		data[count]=element;
		count++;
	}
	public IntList(int initialCapacity) {
		data=new int[initialCapacity];count=0;length=initialCapacity;
	}
	public IntList(int[] data, boolean copyData) {
		if(copyData)
			this.data=data.clone();
		else
			this.data=data;
		count=data.length;length=count;
	}
	public IntList() {
		data=new int[4];count=0;length=4;
	}
	public IntList(List<Integer> componentsToAdd) {
		count=componentsToAdd.size();length=count;data=new int[count];
		for(int i=0;i<count;++i)data[i]=componentsToAdd.get(i);
	}
	public IntList(IntList componentsToAdd) {
		count=componentsToAdd.size();length=count;data=new int[count];
		for(int i=0;i<count;++i)data[i]=componentsToAdd.data[i];
	}
	public void removeSubList(int indexBegin, int indexEnd) {
		int excludeCount=indexEnd-indexBegin;
		int newCount=count-excludeCount;
		int j=0;
		int[] newData=new int[newCount];
		for(int i=0;i<indexBegin;++i) {
			newData[j]=data[i];
			++j;
		}
		for(int i=indexEnd;i<count;++i) {
			newData[j]=data[i];
			++j;
		}
		data=null;
		data=newData;
		length=newCount;
		count=newCount;
	}
	private void narrow() {narrow(getNewNarrowedSize(length),false);}
	private void narrow(int newLength, boolean destroyData) {
		if(destroyData)count=newLength;else
			if(newLength<count)newLength=count;
		int[] newData = new int[newLength];
		for(int i=0;i<count;++i)
			newData[i]=data[i];
		data=null;
		data=newData;
		length=newLength;
	}
	public void ensureCapacity() {
		if(length==count)return;
		int[] newData = new int[count];
		for(int i=0;i<count;++i)
			newData[i]=data[i];
		data=null;
		data=newData;
		length=count;
	}
	private void extend() {
		int newLength=getNewExtendedSize(length);
		int[] newData = new int[newLength];
		for(int i=0;i<count;++i)
			newData[i]=data[i];
		data=null;
		data=newData;
		length=newLength;
	}
	private static int getNewExtendedSize(int size) {
		if(size==0)return 4;
		if(size>100000)return size*5/4;
		if(size>1000)return size*3/2;
		if(size<100)return size*5;
		return size*2;
	}
	private static int getNewNarrowedSize(int size) {
		if(size<4)return 0;
		if(size>100000)return size*4/5;
		if(size>1000)return size*2/3;
		return size/2;
	}
	private int capacity() {
		return length;
	}
	@Override
	/**used for search in ArrayList subsequence*/
	public boolean equals(Object a) {
		if(a.getClass().equals(IntList.class)) {
			int[] otherData=((IntList)a).data;
			if(otherData.length!=data.length)return false;
			for(int i=0;i<count;++i)if(otherData[i]!=data[i])return false;
			return true;
		}
		return false;
	}

	public int findInArray(List<IntList> a) {return findInArray(a,0);}
	public int findInArray(List<IntList> a, int indexFrom) {
		int firstFindElement=data[0],count1=a.size();int[] dataIn;
		//int occurrenceIndex=-1;
		boolean occurenceOk;
		for(int i=indexFrom;i<count1;++i) {
			if(a.get(i).data[0]==firstFindElement) {
				if(a.get(i).size()==count)occurenceOk=true;else continue;
				dataIn=a.get(i).data;
				for(int j=1;j<count;++j) {
					if(data[j]!=dataIn[j]) {
						occurenceOk=false;break;}}
				if(occurenceOk) {
					//occurrenceIndex=i;
					return i;
				}
			}
		}
		return -1;
	}

	public int findInArray(ArrayList<Sequence> a) {return findInArray(a,0);}
	public int findInArray(ArrayList<Sequence> a, int indexFrom) {
		int firstFindElement=data[0],count1=a.size();int[] dataIn;
		//int occurrenceIndex=-1;
		boolean occurenceOk;
		for(int i=indexFrom;i<count1;++i) {
			if(a.get(i).data[0]==firstFindElement) {
				if(a.get(i).size()==count)occurenceOk=true;else continue;
				dataIn=a.get(i).data;
				for(int j=1;j<count;++j) {
					if(data[j]!=dataIn[j]) {
						occurenceOk=false;break;}}
				if(occurenceOk) {
					//occurrenceIndex=i;
					return i;
				}
			}
		}
		return -1;
	}
}
