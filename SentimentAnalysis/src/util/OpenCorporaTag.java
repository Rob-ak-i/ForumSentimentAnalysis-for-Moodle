package util;

import java.util.ArrayList;

public class OpenCorporaTag {
	private static final String[] tagDecoder_POS_raw = {"NOUN","ADJF","ADJS","COMP","VERB","INFN","PRTF","PRTS","GRND","NUMR","ADVB","NPRO","PRED","PREP","CONJ","PRCL","INTJ","LATN","PNCT","NUMB","INTG","REAL","ROMN","UNKN"};
	public static final int POS_NOUN=0;
	public static final int POS_ADJF=1;
	public static final int POS_ADJS=2;
	public static final int POS_COMP=3;
	public static final int POS_VERB=4;
	public static final int POS_INFN=5;
	public static final int POS_PRTF=6;
	public static final int POS_PRTS=7;
	public static final int POS_GRND=8;
	public static final int POS_NUMR=9;
	public static final int POS_ADVB=10;
	public static final int POS_NPRO=11;
	public static final int POS_PRED=12;
	public static final int POS_PREP=13;
	public static final int POS_CONJ=14;
	public static final int POS_PRCL=15;
	public static final int POS_INTJ=16;
	
	public static final int POS_LATN=17;
	public static final int POS_PNCT=18;
	public static final int POS_NUMB=19;
	public static final int POS_INTG=20;
	public static final int POS_REAL=21;
	public static final int POS_ROMN=22;
	public static final int POS_UNKN=23;
	
	private static final String[] tagDecoder_Case_raw = {"NOMN","GENT","DATV","ACCS","ABLT","LOCT","VOCT","GEN2","ACC2","LOC2","IMPF"};
	public static final int Case_NOMN=0;
	public static final int Case_GENT=1;
	public static final int Case_DATV=2;
	public static final int Case_ACCS=3;
	public static final int Case_ABLT=4;
	public static final int Case_LOCT=5;
	public static final int Case_VOCT=6;
	public static final int Case_GEN2=7;
	public static final int Case_ACC2=8;
	public static final int Case_LOC2=9;
	/**for verbs, etc...*/
	public static final int Case_IMPF=10;

	private static final String[] tagDecoder_plur_raw = {"SING","PLUR"};
	public static final int plur_SING=0;
	public static final int plur_PLUR=1;

	private static final String[] tagDecoder_gender_raw = {"MASC","FEMN","NEUT","3PER"};
	public static final int gender_MASC=0;
	public static final int gender_FEMN=1;
	public static final int gender_NEUT=2;
	/**third person - gender unknown*/
	public static final int gender_3PER=3;
		
	public int POS;
	public int Case;
	public int plur;
	public int gender;
	public int lemmaIndex;
	private void decodeTag_POS(String str) {
		int index=POS_UNKN;
		for(int i=0;i<tagDecoder_POS_raw.length;++i)
			if(str.equalsIgnoreCase(tagDecoder_POS_raw[i])) {index=i;break;}
	}
	private void decodeTag_Case(String str) {
		int index=Case_NOMN;
		for(int i=0;i<tagDecoder_Case_raw.length;++i)
			if(str.equalsIgnoreCase(tagDecoder_Case_raw[i])) {index=i;break;}
	}
	private void decodeTag_plur(String str) {
		int index=plur_SING;
		for(int i=0;i<tagDecoder_plur_raw.length;++i)
			if(str.equalsIgnoreCase(tagDecoder_plur_raw[i])) {index=i;break;}
	}
	private void decodeTag_gender(String str) {
		int index=gender_MASC;
		for(int i=0;i<tagDecoder_gender_raw.length;++i)
			if(str.equalsIgnoreCase(tagDecoder_gender_raw[i])) {index=i;break;}
	}
	private void decodeTag(String tag) {
		int commaIndex0,commaIndex1;
		commaIndex0 = tag.indexOf(',');
		if(commaIndex0==-1) {
			decodeTag_POS(tag);
			return;
		}
		String tag_POS = tag.substring(0,commaIndex0);
	}
	public OpenCorporaTag(String tag, int lemmaIndex) {
		decodeTag(tag);
		this.lemmaIndex=lemmaIndex;
	}
	
}
