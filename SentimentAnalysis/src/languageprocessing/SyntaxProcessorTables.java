package languageprocessing;

import java.util.ArrayList;

import util.IntList;

public class SyntaxProcessorTables {
	protected static final int multicatchDisabled = 0;
	protected static final int multicatchIfEnumeration = 1;
	protected static final int multicatchAllwaysEnabled = 2;
	

	private static final int enumerationStateDisabled = 0;
	private static final int enumerationStatePOSDetected = 1;
	private static final int enumerationStateConjunction = 2;
	private static final int enumerationStateConfirmed = 3;
	private static int enumerationState=0;
	private static int enumerationLastFunctionCallIndex=0;
	private static int lastPOS = -1;
	private static int lastPOSIndex = -1;
	/**для правильной работы необходимо вызывать ещё enumerationCheck на каждой итерации*/
	protected static boolean isEnumeration() {return enumerationState>enumerationStateDisabled;}
	/**постоянно определяет состояние перечисления в тексте*/
	protected static void enumerationCheck(int callIndex, int nowPOS) {
		int callRadius = callIndex-enumerationLastFunctionCallIndex;if(callRadius<0) {callRadius=0;enumerationState=enumerationStateDisabled;}
		enumerationLastFunctionCallIndex=callIndex;
		switch(enumerationState) {
		case enumerationStateDisabled:
			/**enumeration may be beginning*/
			lastPOS=nowPOS;
			lastPOSIndex=callIndex;
			enumerationState=enumerationStatePOSDetected;
			closeAllEnumerativeMulticatches(true, nowPOS);
			break;
		case enumerationStatePOSDetected:
			/**enumeration may be in progress*/
			if(nowPOS==OpenCorporaTag.tag_POS_CONJ||nowPOS==-1) {
				enumerationState=enumerationStateConjunction;
				return;
			}
			closeAllEnumerativeMulticatches(true, nowPOS);
			lastPOS=nowPOS;
			lastPOSIndex=callIndex;
			//enumerationState=enumerationStateDisabled;
			break;
		case enumerationStateConjunction:
			if(nowPOS==lastPOS) {
				enumerationState=enumerationStateConfirmed;
				return;
			}
			break;
		case enumerationStateConfirmed:
			if(callRadius>1) {closeAllEnumerativeMulticatches(false, nowPOS);enumerationState=enumerationStateDisabled;return;}
			if(nowPOS==lastPOS||(nowPOS==OpenCorporaTag.tag_POS_CONJ||nowPOS==-1))
				return;
			else {
				closeAllEnumerativeMulticatches(false, nowPOS);
				enumerationState=enumerationStateDisabled;
			}
			break;
		}
	}
	/**удаляются все сокеты с зависимыми от перечисления портами*/
	private static void closeAllEnumerativeMulticatches(boolean ifConnectionExists, int excludedPOS) {
		for(int i=0;i<multicatchParentSocket.length;++i) {
			if(i==excludedPOS)continue;
			removeSocket(i
					,(multicatchParentSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||childsExist[i])
					,(multicatchChildSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||parentsExist[i])
					//,(multicatchChildSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||childsExist[i])
					//,(multicatchParentSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||parentsExist[i])
					);
		}
	}
	/**удаляются все сокеты с зависимыми от перечисления портами*/
	private static void closeEnumerativeMulticatch(boolean ifConnectionExists, int POSIndex) {
		removeSocket(POSIndex
				,(multicatchParentSocket[POSIndex]==multicatchIfEnumeration)&&(!ifConnectionExists||childsExist[POSIndex])
				,(multicatchChildSocket[POSIndex]==multicatchIfEnumeration)&&(!ifConnectionExists||parentsExist[POSIndex])
				//,(multicatchChildSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||childsExist[i])
				//,(multicatchParentSocket[i]==multicatchIfEnumeration)&&(!ifConnectionExists||parentsExist[i])
				);
	}
	
	protected static int[] multicatchParentSocket = {
			/**"NOUN"*/multicatchIfEnumeration
			/**"ADJF"*/,multicatchIfEnumeration
			/**"ADJS"*/,multicatchIfEnumeration
			/**"COMP"*/,multicatchDisabled
			/**"VERB"*/,multicatchAllwaysEnabled
			/**"INFN"*/,multicatchDisabled
			/**"PRTF"*/,multicatchDisabled
			/**"PRTS"*/,multicatchDisabled
			/**"GRND"*/,multicatchDisabled
			/**"NUMR"*/,multicatchDisabled
			/**"ADVB"*/,multicatchDisabled
			/**"NPRO"*/,multicatchDisabled
			/**"PRED"*/,multicatchDisabled
			/**"PREP"*/,multicatchDisabled
			/**"CONJ"*/,multicatchDisabled
			/**"PRCL"*/,multicatchDisabled//multicatchIfEnumeration
			/**"INTJ"*/,multicatchDisabled
			/**"LATN"*/,multicatchDisabled
			/**"PNCT"*/,multicatchDisabled
			/**"NUMB"*/,multicatchDisabled
			/**"INTG"*/,multicatchDisabled
			/**"REAL"*/,multicatchDisabled
			/**"ROMN"*/,multicatchDisabled
			/**"UNKN"*/,multicatchDisabled
	};
	protected static int[] multicatchChildSocket = {
			/**"NOUN"*/multicatchIfEnumeration
			/**"ADJF"*/,multicatchDisabled//пока отключу, т.к.
			/**"ADJS"*/,multicatchDisabled//редко где бывает
			/**"COMP"*/,multicatchDisabled
			/**"VERB"*/,multicatchIfEnumeration
			/**"INFN"*/,multicatchDisabled
			/**"PRTF"*/,multicatchDisabled
			/**"PRTS"*/,multicatchDisabled
			/**"GRND"*/,multicatchDisabled
			/**"NUMR"*/,multicatchDisabled
			/**"ADVB"*/,multicatchDisabled
			/**"NPRO"*/,multicatchDisabled
			/**"PRED"*/,multicatchDisabled
			/**"PREP"*/,multicatchDisabled
			/**"CONJ"*/,multicatchDisabled
			/**"PRCL"*/,multicatchDisabled
			/**"INTJ"*/,multicatchDisabled
			/**"LATN"*/,multicatchDisabled
			/**"PNCT"*/,multicatchDisabled
			/**"NUMB"*/,multicatchDisabled
			/**"INTG"*/,multicatchDisabled
			/**"REAL"*/,multicatchDisabled
			/**"ROMN"*/,multicatchDisabled
			/**"UNKN"*/,multicatchDisabled
	};
	protected static char[][] connectionParentConditions = {
			//		  сущ прл кпл кмп глг инф прч кпч дпр чсл нар мст пдк пдл сюз чст мжд лат пкт нум цел вещ рим нзв
			/**сущ*/{ '~','F','F',' ','A',' ','F','F','!',' ','!',' ',' ','A','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**прл*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','!',' ',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кпл*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','!',' ',' ','!','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кмп*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**глг*/{ 'P',' ',' ',' ',' ','!',' ',' ',' ',' ','!','P',' ','!','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**инф*/{ 'P',' ',' ',' ',' ',' ',' ',' ',' ',' ','!','P',' ',' ','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**прч*/{ 'P',' ',' ',' ',' ',' ',' ',' ',' ',' ','!','P',' ','!','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кпч*/{ 'P',' ',' ',' ',' ',' ',' ',' ',' ',' ','!','P',' ','!','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**дпр*/{ 'P',' ',' ',' ',' ','!',' ',' ',' ',' ','!','P',' ','!','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**чсл*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**нар*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**мст*/{ 'A','F','F',' ','A',' ','F','F','!',' ',' ',' ',' ','A','.','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**пдк*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**пдл*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','!',' ',' ',' ',' ',' ',' ',' ',' '},
			/**сюз*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**чст*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**мжд*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ЛАТ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ПКТ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**НУМ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ЦЕЛ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ВЕЩ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**РИМ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**НЗВ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}
			//x - no connection, o - connection excluded, . - set, ; - set without same flexibility, f - same flexibility, A - active, P - passive
	};
	protected static char[][] connectionChildConditions = {
			//		  сущ прл кпл кмп глг инф прч кпч дпр чсл нар мст пдк пдл сюз чст мжд лат пкт нум цел вещ рим нзв
			/**сущ*/{ '~',' ',' ',' ','P','P','P','P','P',' ',' ',' ',' ','P','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**прл*/{ 'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кпл*/{ 'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кмп*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**глг*/{ 'A',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','A',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**инф*/{ ' ',' ',' ',' ','!',' ','!','!','!',' ',' ',' ',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**прч*/{ 'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**кпч*/{ 'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**дпр*/{ 'F',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','F',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**чсл*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**нар*/{ ' ','!','!',' ','!','!','!','!','!',' ',' ',' ',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**мст*/{ ' ','F','F',' ','P','P','P','P','P',' ',' ',' ',' ','P','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**пдк*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**пдл*/{ ' ',' ',' ',' ','!','!','!','!','!',' ',' ','A',' ',' ','.',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**сюз*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**чст*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**мжд*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ЛАТ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ПКТ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**НУМ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ЦЕЛ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**ВЕЩ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**РИМ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
			/**НЗВ*/{ ' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}
			//"" - no connection, "o" - connection excluded, "." - set, ";" - set without same flexibility, "f" - same flexibility, "A" - active, "P" - passive
		};
	private static boolean[] childsExist = new boolean[25];
	private static boolean[] parentsExist = new boolean[25]; 

	protected static void makeConnection(int childPosition, int parentPosition,boolean shift) {
		if(shift) {int temp=childPosition;childPosition=parentPosition;parentPosition=temp;}
		if(root==-1)root=parentPosition;
		else if(root==childPosition)root=parentPosition;
		edgesChilds.add(childPosition);//childs[parentPosition]=childPosition;
		edgesParents.add(parentPosition);//parents[childPosition]=parentPosition;
		
		OpenCorporaTag tag=tags[childPosition];
		parentsExist[tag.tag_POS]=true;
		
		tag=tags[parentPosition];
		childsExist[tag.tag_POS]=true;
	}

	protected static void removeSocket(OpenCorporaTag tag, boolean parentPort, boolean childPort) {
		int POSIndex=tag.tag_POS;
		if(POSIndex==OpenCorporaTag.tag_POS_NOUN&&tag.tag_case!=OpenCorporaTag.tag_case_nomn)POSIndex=24;
		removeSocket(POSIndex, parentPort, childPort);
	}
	protected static void removeSocket(int POSIndex, boolean parentPort, boolean childPort) {
		int i;
		if(parentPort&&unlinkedWordsParents[POSIndex]!=-1) {
			unlinkedWordsParents[POSIndex]=-1;
			unlinkedWordsParentsLength--;
			i=unlinkedWordsParentsClass.indexOf(POSIndex);
			if(i!=-1)unlinkedWordsParentsClass.remove(i);
		}
		if(childPort&&unlinkedWordsChilds[POSIndex]!=-1) {
			unlinkedWordsChilds[POSIndex]=-1;
			unlinkedWordsChildsLength--;
			i=unlinkedWordsChildsClass.indexOf(POSIndex);
			if(i!=-1)unlinkedWordsChildsClass.remove(i);
		}
	}
	protected static void addSocket(int tagIndex, boolean parentPort, boolean childPort) {
		int POSIndex=tags[tagIndex].tag_POS;if(POSIndex==-1)return;
		if(POSIndex==OpenCorporaTag.tag_POS_NOUN&&tags[tagIndex].tag_voice==OpenCorporaTag.tag_voice_pssv)POSIndex=24;
		int i;
		if(parentPort) {
			if(unlinkedWordsParents[POSIndex]==-1)
				unlinkedWordsParentsLength++;
			unlinkedWordsParents[POSIndex]=tagIndex;
			i=unlinkedWordsParentsClass.indexOf(POSIndex);
			if(i!=-1)unlinkedWordsParentsClass.remove(i);
			unlinkedWordsParentsClass.add(POSIndex);
			childsExist[POSIndex]=false;
		}
		if(childPort) {
			if(unlinkedWordsChilds[POSIndex]==-1)
				unlinkedWordsChildsLength++;
			unlinkedWordsChilds[POSIndex]=tagIndex;
			i=unlinkedWordsChildsClass.indexOf(POSIndex);
			if(i!=-1)unlinkedWordsChildsClass.remove(i);
			unlinkedWordsChildsClass.add(POSIndex);
			parentsExist[POSIndex]=false;
		}
	}
	protected static int root = -1;
	protected static IntList edgesParents = null;
	protected static IntList edgesChilds = null;
	protected static OpenCorporaTag[] tags=null;
	protected static int[] unlinkedWordsChilds = new int[25];//for noun saves active and passive forms
	protected static int[] unlinkedWordsParents = new int[25];//for noun saves active and passive forms
	protected static int unlinkedWordsChildsLength = 0;
	protected static int unlinkedWordsParentsLength = 0;
	protected static ArrayList<Integer> unlinkedWordsChildsClass = new ArrayList<Integer>(25);//links to OCT.POS
	protected static ArrayList<Integer> unlinkedWordsParentsClass = new ArrayList<Integer>(25);//links to OCT.POS
}
