
package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TextForum {
	public ArrayList<Integer> userIDList;
	public ArrayList<String> userNameList;
	public ArrayList<Integer> postIDList;
	public ArrayList<Integer> parentPostIDList;
	public ArrayList<String> postTimeList;
	public ArrayList<String> messageList;
	public String name="",title="";
	public int rootIndex;
	public util.DataTable toDataTable() {
		util.DataTable result = new util.DataTable(
				"userIDList:Integer;userNameList:String;postIDList:Integer;parentPostIDList:Integer;postTimeList:String;messageList:String");
		result.fillColumn(0, userIDList, 0);
		result.fillColumn(1, userNameList, 0);
		result.fillColumn(2, postIDList, 0);
		result.fillColumn(3, parentPostIDList, 0);
		result.fillColumn(4, postTimeList, 0);
		result.fillColumn(5, messageList, 0);
		result.name=name;
		result.title=title;
		return result;
	}
	public TextForum() {
		rootIndex=-1;
		userIDList = new ArrayList<Integer>();
		postIDList = new ArrayList<Integer>();
		parentPostIDList = new ArrayList<Integer>();
		messageList = new ArrayList<String>();
		userNameList = new ArrayList<String>();
		postTimeList = new ArrayList<String>();
	}
	public void add(String HTMLFragment) {
        dataextractor.fromHTMLFragment(HTMLFragment);
        postTimeList.add(dataextractor.postTime);
        userNameList.add(dataextractor.userName);
        messageList.add(dataextractor.messageText);
        postIDList.add(dataextractor.postID);
        parentPostIDList.add(dataextractor.parentPostID);
        userIDList.add(dataextractor.userID);
        if(dataextractor.isRootMessage)rootIndex=messageList.size()-1;
	}
	private static class dataextractor {
		public static boolean isRootMessage;
		public static int postID;
		public static int parentPostID;
		public static String postTime;
		public static String messageText;
		public static int userID;
		public static String userName;
		public static String rootDetectorKey ="data-timestamp=\"";
		public static String postIDBeginKey_root ="id=\"post-header-";
		public static String postIDEndKey_root ="-";
		public static String postTimeBeginKey_root="data-timestamp=\"";
		public static String postIDBeginKey ="id=\"p";
		public static String postIDEndKey ="\"";
		public static String userIDBeginKey ="rateduserid\"";
		public static String userIDEndKey ="\"";
		public static String messageTextBeginKeyPrefix = "id=\"post-content-";
		public static String messageTextBeginKeySuffix = "\"";
		public static String postTimeBeginKey = "datetime=\"";
		public static String postTimeEndKey = "\"";
		public static String userNameBeginKey = "\">";
		public static String userNameEndKey = "</a>";
		private static final String digits="0123456789";
		public static void fromHTMLFragment(String htmfrag) {
			char c ;
			int i;
			StringBuilder sb=null;
		/** part 1 - extracting postID */
			int postIDBegin = htmfrag.indexOf(postIDBeginKey),postIDEnd=0;
			
			boolean isRootMsg = htmfrag.indexOf(rootDetectorKey)!=-1;//digits.indexOf(htmfrag.charAt(postIDBegin+postIDBeginKey.length()))==-1;
			if(isRootMsg) {
				postIDBegin = htmfrag.indexOf(postIDBeginKey_root);
				postIDEnd = htmfrag.indexOf(postIDEndKey_root,postIDBegin+(postIDBeginKey_root.length()));
				postID=Integer.valueOf(htmfrag.substring(postIDBegin+postIDBeginKey_root.length(), postIDEnd));
			}else {
				postIDEnd = htmfrag.indexOf(postIDEndKey,postIDBegin+(postIDBeginKey.length()));
				postID=Integer.valueOf(htmfrag.substring(postIDBegin+postIDBeginKey.length(), postIDEnd));
			}
			
			
			
		/** part 2 - extracting userID */
			i = htmfrag.indexOf(userIDBeginKey)+userIDBeginKey.length();
			while(digits.indexOf(htmfrag.charAt(i))==-1)++i;
			sb=new StringBuilder();
			c=htmfrag.charAt(i);
			while (digits.indexOf(c)!=-1){
				sb.append(c);
				i++;
				c=htmfrag.charAt(i);
			}
			userID=Integer.valueOf(sb.toString());
			//sb=new StringBuilder();
		/** part 3 - extracting textData */
			String tmpstr=messageTextBeginKeyPrefix.concat(Integer.toString(postID)).concat(messageTextBeginKeySuffix);
			i = htmfrag.indexOf(tmpstr);
			commonTagData.readTagDataOnly(htmfrag, i+tmpstr.length()+1, true);
			messageText = commonTagData.data;
		/** part 4 - extracting parentPostID */
			int postIDRefer = htmfrag.indexOf("#p");
			int parentPostIDRefer = htmfrag.indexOf("#p", postIDRefer+2);
			if(postIDRefer==parentPostIDRefer || parentPostIDRefer==-1)
				parentPostID=-1;
			else {
				sb=new StringBuilder();
				i=parentPostIDRefer+2;
				c=htmfrag.charAt(i);
				while(digits.indexOf(c)!=-1) {
					sb.append(c);
					i+=1;
					c=htmfrag.charAt(i);
				}
				parentPostID=Integer.valueOf(sb.toString());
			}
		/** part 5 - extracting postTime*/
			int postTimeBegin,postTimeEnd;
			if(isRootMsg) {
				postTimeBegin = htmfrag.indexOf(postTimeBeginKey_root);
				postTimeEnd = htmfrag.indexOf(postTimeEndKey,postTimeBegin+postTimeBeginKey_root.length());
				postTime=htmfrag.substring(postTimeBegin+postTimeBeginKey_root.length(), postTimeEnd);
			}else {
				postTimeBegin = htmfrag.indexOf(postTimeBeginKey);
				postTimeEnd = htmfrag.indexOf(postTimeEndKey,postTimeBegin+postTimeBeginKey.length());
				postTime=htmfrag.substring(postTimeBegin+postTimeBeginKey.length(), postTimeEnd);
			}

			if(isRootMsg) {
				int time=Integer.valueOf(postTime);
				int yearsCount=1970+time/31536000;
				double dextradouble = 0.25D*(double)(yearsCount-1969);
				int dextra = (yearsCount-1969)/4;
				int[] monthsLength=monthsLen;
				if((dextradouble-(double)dextra)==0)monthsLength=monthsLenExtra;
				int d = time/86400;
				int dd = d-dextra;
				int dayOfYear = dd%365;
				int dayOfMonth=dayOfYear;
				int month=1;
				while(dayOfMonth>monthsLength[month]) {
					dayOfMonth-=monthsLength[month];
					month++;
					if(month==13) {
						dayOfMonth=1;
						yearsCount++;
						break;
					}
				}
				int s=time-(d*86400);
				int hour = s/3600;
				int minute=s-(hour*3600);
				int secund=minute;
				minute=minute/60;
				secund=secund-minute*60;
				//2017-01-30T17:10:21+07:00
				String mon=Integer.toString(month);
				if(mon.length()==1)mon='0'+mon;
				String dm=Integer.toString(dayOfMonth);
				if(dm.length()==1)dm='0'+dm;
				String hr=Integer.toString(hour);
				if(hr.length()==1)hr='0'+hr;
				String min=Integer.toString(minute);
				if(min.length()==1)min='0'+min;
				String sec=Integer.toString(secund);
				if(sec.length()==1)sec='0'+sec;
				postTime=
						Integer.toString(yearsCount)
						+'-'
						+mon
						+'-'
						+dm
						+'T'
						+hr
						+':'
						+min
						+':'
						+sec
						;
			}
		/** part 6 - extracting userName*/
			if(isRootMsg) {
				userName="root";
			}else {
				int userNameBegin = htmfrag.lastIndexOf(userNameBeginKey,postTimeBegin);
				int userNameEnd = htmfrag.indexOf(userNameEndKey,userNameBegin+userNameBeginKey.length());
				userName=htmfrag.substring(userNameBegin+userNameBeginKey.length(), userNameEnd);
			}
			
			isRootMessage=isRootMsg;
		}
		private static int[] monthsLen= {31,28,31,30,31,30,31,31,30,31,30,31};
		private static int[] monthsLenExtra= {31,29,31,30,31,30,31,31,30,31,30,31};
	}
	private static class commonTagData{
		public static String name;
		public static String data;
		public static ArrayList<String> parameters=new ArrayList<String>(0);
		public static ArrayList<String> captions=new ArrayList<String>(0);
		public static final String delimiters="= \n<>\'\"";
		public static void readTagDataOnly(String htm,int indexDataStart, boolean beginningInAttributeMode) {
			int i=indexDataStart ,len=htm.length();
			data="";
			char c=htm.charAt(i);
			if(c=='/')return;
			StringBuilder sbName = new StringBuilder();
			int counter=0;
			boolean readCaptionMode=false;
			boolean inAttributeMode=beginningInAttributeMode;
			int tagsHierarchyLevel=0;
			while(i<len) {
				c=htm.charAt(i);
				i+=1;
				/**read data section*/
				
				if(inAttributeMode==true &&(c=='\'' || c=='\"'))
					readCaptionMode=!readCaptionMode;
				if(!readCaptionMode) {
					if(c=='<') {
						inAttributeMode=true;
						if(htm.charAt(i)=='/')
							tagsHierarchyLevel-=1;
						else {
							if(htm.charAt(i)=='b' && htm.charAt(i+1)=='r' && (htm.charAt(i+2)=='>' || htm.charAt(i+2)==' ')) {
								tagsHierarchyLevel=tagsHierarchyLevel;
							}else
								tagsHierarchyLevel+=1;
						}
					}else if (c=='>') {
						inAttributeMode=false;
						if(beginningInAttributeMode==true) {
							beginningInAttributeMode=false;
							continue;
						}
					}
				}
				if(tagsHierarchyLevel==-1) {
					data=sbName.toString();
					return;
				}
				if(!beginningInAttributeMode)
					sbName.append(c);
			}
		}
		public static void read(String htm, int indexLeftBreak, int indexRightBreak, boolean readData, boolean beginningInAttributeMode) {
			int i=indexLeftBreak ,len=htm.length();
			parameters.clear();
			captions.clear();
			data="";
			name="";
			char c=htm.charAt(i);
			if(c!='<')i-=1;
			if(c=='/')return;
			StringBuilder sbName = new StringBuilder();
			int counter=0;
			boolean readCaptionMode=false;
			boolean inAttributeMode=beginningInAttributeMode;
			int tagsHierarchyLevel=0;
			boolean delimiterAlreadyRead=true;
			while((i<indexRightBreak && !readData)||(readData && i<len)) {
				i+=1;
				c=htm.charAt(i);
				/**read data section*/
				if(counter==-1) {
					if(inAttributeMode==true &&(c=='\'' || c=='\"'))
						readCaptionMode=!readCaptionMode;
					if(!readCaptionMode) {
						if(c=='<') {
							inAttributeMode=true;
							if(htm.charAt(i+1)=='/')
								tagsHierarchyLevel-=1;
							else
								tagsHierarchyLevel+=1;
						}else if (c=='>')inAttributeMode=false;
					}
					sbName.append(c);
					if(tagsHierarchyLevel==-1) {
						data=sbName.toString();
						return;
					}
					continue;
				}
				/**read attributes section*/
				if(delimiters.indexOf(c)==-1) {
					sbName.append(c);
					delimiterAlreadyRead=false;
				} else {
					if(c=='\'' || c=='\"')readCaptionMode=!readCaptionMode;
					else if(readCaptionMode)
						{sbName.append(c);}
					if(readCaptionMode)continue;
					if(delimiterAlreadyRead)continue;
					delimiterAlreadyRead=true;
					
					switch(counter) {
					case 0:
						name=sbName.toString();
						break;
					default:
						if(counter%2==1) {
							parameters.add(sbName.toString());
						}else {
							captions.add(sbName.toString());
						}
					}
					sbName=new StringBuilder();
					counter++;
					if(c=='>') {
						if(!readData)break;
						readCaptionMode=false;
						counter=-1;
					}
				}
			}
			
		}
	}
	public static void readTag(String tagname, String htm, int indexfrom) {
		int len=htm.length();
		int i=indexfrom;
		char c;
		boolean descriptorMode=false;
		boolean descriptorBeginState=true;
		while(i<len) {
			c=htm.charAt(i);
			if(c=='<') {descriptorMode=true;
				if(htm.charAt(i+1)=='/')descriptorBeginState=false;
				
			}else {
				if(c=='>') {descriptorMode=false;
				
				}
			}
			i+=1;
		}
	}
}
