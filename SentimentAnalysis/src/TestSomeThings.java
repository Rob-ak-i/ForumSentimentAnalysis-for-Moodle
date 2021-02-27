import java.util.ArrayList;

import languageprocessing.Sequence;
import util.Time;

public class TestSomeThings {

	private static String[] knownTags = {
			"se",
			"pm",
			"a",
			"n"
			};
	private static int tryToReadTag(String text, int pos) {
		char c;int j=0;boolean tagIsClosed=false;int firstIndex=-1,index=-1;int textLength=text.length();
		for(int i=pos+1;i<textLength;++i) {
			c=text.charAt(i);
			if(c=='/'&&j==0) {tagIsClosed=true;continue;}
			if(j>=knownTags.length) {if(c=='>')return j+1+(tagIsClosed?1:0); else return -1;}
			if(j==0)firstIndex=knownTags[j].indexOf(c);else index=knownTags[j].indexOf(c);
			if(firstIndex==-1&&j==0||(j>0&&firstIndex!=index&&c!='>'))return -1;
			if(c=='>')return j+1+(tagIsClosed?1:0);
			j++;
		}
		return j+(tagIsClosed?1:0);
	}
	public static void testTextExtractor() {
		String text="Посмотрев в ту часть города, мо<em>жно было разглядеть<span> самую (<-:) примечател</span>ьную триумфальную арку.";
		char c;int iCopy,iResult;StringBuilder newString=new StringBuilder();
		for(int i=0;i<text.length();++i) {
			c=text.charAt(i);
			if(c=='<') {iCopy=i;iResult=tryToReadTag(text,i);if(iResult!=-1) {i+=iResult;continue;}}
			newString.append(c);
		}
		System.out.println(newString.toString());
	}
	public static void main(String[] args) {
		testTextExtractor();
		//DataTable 
		testtime("2012-12-31T23:59:59");
	}
	public static void testtime(String time) {
		System.out.println(time);
		int inttime =Time.fromString(time);
		System.out.println(inttime);
		String strtime=Time.fromIntegerWithTimeZone(inttime);
		System.out.println(strtime);
		/**testing ability to search extended elements by parent elements*/
		ArrayList<Integer>a=new ArrayList<Integer>();for(int i=0;i<10;++i)a.add(i);a.subList(5, a.size()-1).clear();
		ArrayList<Integer>b=new ArrayList<Integer>();b.add(2);b.add(3);
		System.out.println("a: "+a);
		System.out.println("b: "+b);
		Sequence s=new Sequence(a),s1=new Sequence(b);ArrayList<Sequence>epoch = new ArrayList<Sequence>();epoch.add(s);epoch.add(s1);
		int index=epoch.indexOf(b);
		System.out.println(index);
		System.out.println(a.containsAll(b));
	}

}
