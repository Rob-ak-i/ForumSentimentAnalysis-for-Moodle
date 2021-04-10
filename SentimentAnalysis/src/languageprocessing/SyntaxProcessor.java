package languageprocessing;

import java.util.ArrayList;

import common.CommonData;
import util.IntList;

public class SyntaxProcessor extends SyntaxProcessorTables {
	//private static int[] lastPOSwords = new int[OpenCorporaTag.tagDecoder_POS_raw.length];
	//private static ArrayList<OpenCorporaTag> unlinkedNouns;
	public static void processMessage(Message msg) {
		{
			for(int i=0;i<25;++i) {
				unlinkedWordsChilds[i]=-1;
				unlinkedWordsParents[i]=-1;
			}
			unlinkedWordsChildsLength=0;
			unlinkedWordsParentsLength=0;
			unlinkedWordsChildsClass.clear();
			unlinkedWordsParentsClass.clear();
			
		}
		int n=msg.words.size();
		boolean[] checked = new boolean[n];
		tags = new OpenCorporaTag[n];
		edgesParents = new IntList(n);
		edgesChilds = new IntList(n);
		root=-1;
		int wordIndex=0;
		OpenCorporaTag tag=null;
		for(int i=0;i<n;++i) {
			wordIndex=msg.words.get(i);
			tags[i]=CommonData.textManager.textMonomialsToTags.get(wordIndex);
			tag=tags[i];
			if(tag.tag_POS==-1) {if(CommonData.textManager.textMonomials.get(wordIndex).contains(","))enumerationCheck(i,-1);continue;}
			enumerationCheck(i,tag.tag_POS);
			catchWordInCatchArray(i);
		}
		edgesParents.ensureCapacity();
		edgesChilds.ensureCapacity();
		msg.edgesParents = edgesParents.data;
		msg.edgesChilds = edgesChilds.data;
		msg.root=root;
	}
	private static boolean compareTags(int catchedMessageElement, boolean catchedTagParentSocket, boolean catchedTagChildSocket, int matchedTagIndex, boolean matchedTagParentSocket, boolean matchedTagChildSocket, char operation) {
		OpenCorporaTag matchedTag = tags[matchedTagIndex];
		OpenCorporaTag catchedTag = tags[catchedMessageElement];
		
		switch(operation) {
		case'A':
			if((catchedTag.tag_POS==OpenCorporaTag.tag_POS_VERB)&&(matchedTag.tag_POS==OpenCorporaTag.tag_POS_NOUN)) {
				if(edgesChilds.indexOf(matchedTagIndex)!=-1)return false;
			}
		case'P':
			boolean isActive = operation=='A';
			OpenCorporaTag noun=catchedTag;
			if(matchedTag.tag_POS==OpenCorporaTag.tag_POS_NOUN)noun=matchedTag;
			return (noun.tag_voice==OpenCorporaTag.tag_voice_actv)==isActive;
		case'F':
			if(catchedTag.tag_number!=matchedTag.tag_number) {if((catchedTag.tag_number!=-1)&&(matchedTag.tag_number!=-1))return false;}
			if(catchedTag.tag_gender!=matchedTag.tag_gender) {if((catchedTag.tag_gender!=-1)&&(matchedTag.tag_gender!=-1)&&(catchedTag.tag_gender<2)&&(matchedTag.tag_gender<2))return false;}
			if(catchedTag.tag_person!=matchedTag.tag_person) {if((catchedTag.tag_person!=-1)&&(matchedTag.tag_person!=-1))return false;}
			if(catchedTag.tag_case!=matchedTag.tag_case) {if((catchedTag.tag_case!=-1)&&(matchedTag.tag_case!=-1))return false;}
			return true;
		case'!':
			return true;
		}
		return false;
	}
	private static void catchWordInCatchArray(int catchedMessageElement) {char operation;
		OpenCorporaTag catchedTag=tags[catchedMessageElement];
		int POSIndex=catchedTag.tag_POS;
		OpenCorporaTag openedTag;int openedTagIndex;
		if(catchedTag.tag_POS==OpenCorporaTag.tag_POS_NOUN&&catchedTag.tag_voice==OpenCorporaTag.tag_voice_pssv)
			POSIndex = 24;
		ArrayList<Integer>unlinkedWordsClass=null;
		char[][]connectionConditions=null;
		int[]unlinkedWords=null;
		int[] multicatchSocket = null;
		boolean parentSocketOpened=true, childSocketOpened=true;
		int nowUnlinkedWordClass=-1;
		boolean parentPort=false,childPort=false;
		for(int nowCondition=0;nowCondition<2;++nowCondition){
			if(nowCondition==0) {//попытка нового слова стать ребёнком для прошлого (newWordBecomeChild)
				parentPort=false;childPort=true;
				unlinkedWordsClass=unlinkedWordsParentsClass;
				connectionConditions=connectionChildConditions;
				unlinkedWords=unlinkedWordsParents;
				multicatchSocket=multicatchChildSocket;
				childSocketOpened = true;
			}else {//попытка нового слова стать родителем для прошлого (newWordBecomeParent)
				parentPort=true;childPort=false;
				unlinkedWordsClass=unlinkedWordsChildsClass;
				connectionConditions=connectionParentConditions;
				unlinkedWords=unlinkedWordsChilds;
				multicatchSocket=multicatchParentSocket;
				parentSocketOpened = true;
			}
			for(int i=unlinkedWordsClass.size()-1;i>=0;--i) {
				nowUnlinkedWordClass=unlinkedWordsClass.get(i);
				/**смотрим текущую операцию по текущему слову и нераспределённому*/
				operation=connectionConditions[POSIndex%24][nowUnlinkedWordClass];
				/**если соединение должно быть, то */
				if(operation!=' ') {
					openedTagIndex=unlinkedWords[nowUnlinkedWordClass];//unlinkedWords[POSIndex];
					openedTag=tags[openedTagIndex];
					if(compareTags(catchedMessageElement, parentSocketOpened, childSocketOpened, openedTagIndex, nowCondition==1, nowCondition==0, operation)) {
						if(operation=='.') {
							/**WIF*/ //groupConnection(parameters);
							removeSocket(openedTag,!parentPort,!childPort);
						}else {
							makeConnection(catchedMessageElement,openedTagIndex,parentPort);
							if(multicatchSocket[openedTag.tag_POS]==multicatchDisabled)
								removeSocket(openedTag, !parentPort, !childPort);
							//addSocket(catchedMessageElement, nowCondition==0, parentPort);
							
							if(multicatchSocket[catchedTag.tag_POS]==multicatchDisabled) {
								if(childPort)
									childSocketOpened=false;
								else
									parentSocketOpened=false;
								break;
							}
						}
					}
				}
			}
		}
		addSocket(catchedMessageElement, parentSocketOpened, childSocketOpened);
	}
}
