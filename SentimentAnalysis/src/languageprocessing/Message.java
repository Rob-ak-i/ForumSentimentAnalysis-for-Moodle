package languageprocessing;

import java.util.ArrayList;

import common.CommonData;
import util.IntList;
import util.ManagedObject;

public class Message implements ManagedObject{
	public int userID=-1;
	public int postID=-1;
	public int parentPostID=-1;
	public int postTime=-1;
	/**for graphemes*/
	public Message nextPartOfMessage=null;
	
	public IntList words=null;
	public IntList lemmes=null;
	public int[] edgesChilds=null;
	public int[] edgesParents=null;
	public int root=-1;

	/**includes dot and semicolon*/
	private static ArrayList<Integer> delimitersIndexes=new ArrayList<Integer>();
	public static void delimitersIndexesFind(String delimiters) {
		int index;String nowDelimiter;
		for(int i=0;i<delimiters.length();++i) {
			nowDelimiter=delimiters.substring(i,i+1);
			index=CommonData.textManager.textMonomials.indexOf(nowDelimiter);
			if(index!=-1&&!delimitersIndexes.contains(index))delimitersIndexes.add(index);
		}
	}
	//public ArrayList<Integer>lemmes=null;
	public int getLemmaIndex(int wordIndex) {
		int index = words.get(wordIndex);
		//if(storageLevel==0)return -1;
		return CommonData.textManager.textMonomialsToLemmes.get(index);
	}
	//public ArrayList<Integer>lemmes=null;
	public SentimentData getSentimentData(int wordIndex) {
		int index = words.get(wordIndex);
		//if(storageLevel==0)return -1;
		return CommonData.textManager.textMonomialstoSentiment.get(index);
	}
	public void addLemmes() {
		if(lemmes!=null)return;
		int nWords=words.size();
		lemmes=new IntList(nWords);//new ArrayList<Integer>(nWords);
		for(int i=0;i<nWords;++i) {
			lemmes.add(CommonData.textManager.textMonomialsToLemmes.get(words.get(i)));
		}
	}
	public void spliceToGraphemes(){
		ArrayList<Message> result=null;Message message=null;
		//if(storageLevel==0)return;//return null;
		int nowWordIndex=0;int latchBegin=-1,latchEnd=-1;//String lemma=null;OpenCorporaTag tag=null;int normWordsCount=0;int prevOccurrence=0;int latchEnd=-1;
		/**splice graphemes*/
		int iMax=words.size()-1;String WIPDEBUGSTRING=words.toText(CommonData.textManager.textMonomials);int j;
		for(int i=iMax;i>=0;--i) {
			try {
			nowWordIndex=words.get(i);
			}catch(Exception e) {
				System.out.println(words.data.length);
				System.out.println(words.size());
				System.out.println(iMax);
				System.out.println(i);
				System.out.println(latchBegin);
				System.out.println(latchEnd);
				System.out.println(result.size());
				System.out.println(WIPDEBUGSTRING);
				System.out.println(words.toText(CommonData.textManager.textMonomials));
				for(int k=0;k<result.size();++k)
					System.out.println(result.get(k).words.toText(CommonData.textManager.textMonomials));
				System.out.println(words.get(i));
				
			}
			if(delimitersIndexes.contains(nowWordIndex)) {
				latchBegin=i;j=-1;
				for(j=i;j>=0;--j) {
					if(!delimitersIndexes.contains(words.get(j))) {
						latchEnd=j+1;break;
					}
				}
				if(j<=0)break;
				if(latchBegin==iMax) {latchBegin=-1;i=latchEnd;continue;}
				if(result==null)result=new ArrayList<Message>();
				message=new Message();
				result.add(message);
				message.words=words.subIntList(latchBegin+1, words.size());//message.words=new ArrayList<Integer>(words.subList(latchBegin+1, words.size()));
				words.removeSubList(latchBegin+1, words.size());//words=new ArrayList<Integer>(words.subList(0, latchBegin));//words.subList(latchBegin, words.size()).clear();
				if(lemmes!=null) {
					message.lemmes=lemmes.subIntList(latchBegin+1, lemmes.size());//message.lemmes=new ArrayList<Integer>(lemmes.subList(latchBegin, lemmes.size()));
					lemmes.removeSubList(latchBegin+1, lemmes.size());//lemmes.subList(latchBegin, lemmes.size()).clear();
				}
				i=latchEnd;
			}
		}if(result!=null) {
			nextPartOfMessage = result.get(0);
			for(int i=0;i<result.size()-1;++i)result.get(i).nextPartOfMessage=result.get(i+1);
			//result.get(0).previousPartOfMessage=null;
			for(int i=0;i<result.size();++i) {
				result.get(i).parentPostID=parentPostID;
				result.get(i).postID=postID;
				result.get(i).userID=userID;
				result.get(i).postTime=postTime;
			}
			/**this result nothing to set*/
			result.clear();
			result=null;
		}
		return ;//result;
	}
	@Override
	public void clear() {
		if(lemmes!=null) {lemmes.clear();lemmes=null;}
		if(words!=null) {words.clear();words=null;}
		if(nextPartOfMessage!=null) {nextPartOfMessage.clear();nextPartOfMessage=null;}
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "postID="+Integer.toString(postID);
	}
	@Override
	public int getMeasurableParameter() {
		// TODO Auto-generated method stub
		if(words==null)return -1;
		return words.size();
	}
}
