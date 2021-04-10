package languageprocessing;

import java.util.ArrayList;

public class OpenCorporaTag {
	public static final String[] tagDecoder_POS_raw = {"None","NOUN","ADJF","ADJS","COMP","VERB","INFN","PRTF","PRTS","GRND","NUMR","ADVB","NPRO","PRED","PREP","CONJ","PRCL","INTJ","LATN","PNCT","NUMB","intg","real","ROMN","UNKN"};
	public static final String[] tagDecoder_POS_raw_ru = {"None","СУЩ","ПРИЛ","КР_ПРИЛ","КОМП","ГЛ","ИНФ","ПРИЧ","КР_ПРИЧ","ДЕЕПР","ЧИСЛ","Н","МС","ПРЕДК","ПР","СОЮЗ","ЧАСТ","МЕЖД","ЛАТ","ЗПР","ЧИСЛО","цел","вещ","РИМ","НЕИЗВ"};
	/**имя существительное (бутявка)*/
	public static final int tag_POS_NOUN=0;
	/**имя прилагательное (полное) (бутявкий)*/
	public static final int tag_POS_ADJF=1;
	/**имя прилагательное (краткое) (плох, бутявок) путается с существительным в родительном падеже*/
	public static final int tag_POS_ADJS=2;
	/**компаратив (лучше, умнее, хливче)*/
	public static final int tag_POS_COMP=3;
	/** глагол (личная форма) (говорю, говорит)*/
	public static final int tag_POS_VERB=4;
	/** глагол (инфинитив) (говорить, написать)*/
	public static final int tag_POS_INFN=5;
	/** причастие (полное) (прочитавший, прочитанная)*/
	public static final int tag_POS_PRTF=6;
	/** причастие (краткое) (прочитаны)*/
	public static final int tag_POS_PRTS=7;
	/** деепричастие (прочитав, рассказывая)*/
	public static final int tag_POS_GRND=8;
	/** числительное (три, восемь)*/
	public static final int tag_POS_NUMR=9;
	/** наречие (круто)*/
	public static final int tag_POS_ADVB=10;
	/** местоимение-существительное (они)*/
	public static final int tag_POS_NPRO=11;
	/** предикатив (некогда); кастрированный глагол; флективен*/
	public static final int tag_POS_PRED=12;
	/** предлог (на); зависимость далее*/
	public static final int tag_POS_PREP=13;
	/** союз (и); зависимость далее*/
	public static final int tag_POS_CONJ=14;
	/** частица (бы, же, ли, не); нефлективна*/
	public static final int tag_POS_PRCL=15;
	/** междометие (ой)*/
	public static final int tag_POS_INTJ=16;

	/** слово на латинских символах*/
	public static final int tag_POS_LATN=17;
	/** пунктуация*/
	public static final int tag_POS_PNCT=18;
	/** общее число*/
	public static final int tag_POS_NUMB=19;
	/** целое число*/
	public static final int tag_POS_INTG=20;
	/** вещественное число*/
	public static final int tag_POS_REAL=21;
	/** римские цифры*/
	public static final int tag_POS_ROMN=22;
	/** часть речи неизвестна*/
	public static final int tag_POS_UNKN=23;
	/**одушевлённость*/
	public static final String[] tagDecoder_animacy_raw = {"None","anim", "inan"};
	public static final String[] tagDecoder_animacy_raw_ru = {"None","од", "неод"};
	/**одушевлённый*/
	public static final int tag_animacy_anim=0;
	/**неодушевлённый*/
	public static final int tag_animacy_inan=1;

	/**падеж*/
	public static final String[] tagDecoder_case_raw = {"None","nomn","gent","datv","accs","ablt","loct","voct","gen2","acc2","loc2","impf"};
	public static final String[] tagDecoder_case_raw_ru = {"None","им","рд","дт","вн","тв","пр","зв","рд2","вн2","пр2","impf"};
	/**именительный*/
	public static final int tag_case_nomn=0;
	/**родительный*/
	public static final int tag_case_gent=1;
	/**дательный*/
	public static final int tag_case_datv=2;
	/**винительный*/
	public static final int tag_case_accs=3;
	/**творительный*/
	public static final int tag_case_ablt=4;
	/**предложный*/
	public static final int tag_case_loct=5;
	/**звательный (Андрей, пойдём на дискуссию)*/
	public static final int tag_case_voct=6;
	/**второй родительный (ложка сахару)*/
	public static final int tag_case_gen2=7;
	/**второй винительный (записался в Диогены)*/
	public static final int tag_case_acc2=8;
	/**второй предложный (быть в долгу)*/
	public static final int tag_case_loc2=9;
	/**что то там с глаголами*/
	public static final int tag_case_impf=10;

	/**число*/
	public static final String[] tagDecoder_number_raw = {"None","sing","plur"};
	public static final String[] tagDecoder_number_raw_ru = {"None","ед","мн"};
	/**единственное число*/
	public static final int tag_number_sing=0;
	/**множественное число*/
	public static final int tag_number_plur=1;

	/**род*/
	public static final String[] tagDecoder_gender_raw = {"None","masc","femn","neut","ms-f"};
	public static final String[] tagDecoder_gender_raw_ru = {"None","мр","жр","ср","мж"};
	/**мужской род*/
	public static final int tag_gender_masc=0;
	/**женский род*/
	public static final int tag_gender_femn=1;
	/**средний род*/
	public static final int tag_gender_neut=2;
	/**мужской или женский род*/
	public static final int tag_gender_ms_f=3;

	/**совершенность глагола*/
	public static final String[] tagDecoder_aspect_raw = {"None","perf","impf"};
	public static final String[] tagDecoder_aspect_raw_ru = {"None","сов","несов"};
	/**совершенный глагол*/
	public static final int tag_aspect_perf=0;
	/**несовершенный глагол*/
	public static final int tag_aspect_impf=1;

	/**переходность глагола*/
	public static final String[] tagDecoder_transitivity_raw = {"None","tran","intr"};
	public static final String[] tagDecoder_transitivity_raw_ru = {"None","перех","неперех"};
	/**переходный глагол(должен иметь пассию)*/
	public static final int tag_transitivity_tran=0;
	/**непереходный глагол(может выражать действие сам по себе)*/
	public static final int tag_transitivity_intr=1;

	/**лицо глагола*/
	public static final String[] tagDecoder_person_raw = {"None","1per","2per","3per"};
	public static final String[] tagDecoder_person_raw_ru = {"None","1л","2л","3л"};
	/**первое лицо (говорю)*/
	public static final int tag_person_1per=0;
	/**второе лицо (говоришь)*/
	public static final int tag_person_2per=1;
	/**третье лицо (говорит)*/
	public static final int tag_person_3per=2;

	/**время глагола*/
	public static final String[] tagDecoder_tense_raw = {"None","pres","past","futr"};
	public static final String[] tagDecoder_tense_raw_ru = {"None","наст","прош","буд"};
	/**настоящее время*/
	public static final int tag_tense_pres=0;
	/**прошедшее время*/
	public static final int tag_tense_past=1;
	/**будущее время*/
	public static final int tag_tense_futr=2;

	/**наклонение (изъяв:делаешь,услов:делал бы,повел:делай)*/
	public static final String[] tagDecoder_mood_raw = {"None","indc","impr"};
	public static final String[] tagDecoder_mood_raw_ru = {"None","изъяв","повел"};
	/**изъявительное наклонение*/
	public static final int tag_mood_indc=0;
	/**повелительное наклонение*/
	public static final int tag_mood_impr=1;

	/**включённость говорящего в процесс*/
	public static final String[] tagDecoder_involvement_raw = {"None","incl","excl"};
	public static final String[] tagDecoder_involvement_raw_ru = {"None","вкл","выкл"};
	/**включён в процесс (пойдём работать)*/
	public static final int tag_involvement_incl=0;
	/**не включён в процесс (приходите в мой дом)*/
	public static final int tag_involvement_excl=1;

	/**залог*/
	public static final String[] tagDecoder_voice_raw = {"None","actv","pssv"};
	public static final String[] tagDecoder_voice_raw_ru = {"None","действ","страд"};
	/**действительный залог*/
	public static final int tag_voice_actv=0;
	/**страдательный залог*/
	public static final int tag_voice_pssv=1;
	/**third person - gender unknown*/
	
	//public static final int gender_3PER=3;

	/**часть речи*/
	public int tag_POS;
	/**одушевлённость*/
	public int tag_animacy;
	/**вид(совершенный, несовершенный)*/
	public int tag_aspect;
	/**падеж существительного*/
	public int tag_case;
	/**род (муж, жен, сред, мж)*/
	public int tag_gender;
	/**включённость существительного в глагол*/
	public int tag_involvement;
	/**наклонение (изъявительное, повелительное)*/
	public int tag_mood;
	/**число*/
	public int tag_number;
	/**лицо*/
	public int tag_person;
	/**время*/
	public int tag_tense;
	/**переходность*/
	public int tag_transitivity;
	/**залог (активный, пассивный)*/
	public int tag_voice;
	/**stores lemma index available in CommonData.textManager.textMonomialsToLemmes*/
	public int lemmaIndex;
	private boolean decodeTag_POS(int charSum) {
		return false;
	}
	private boolean decodeTag_Case(int charSum) {
		return false;
	}
	private boolean decodeTag_plur(int charSum) {
		return false;
	}
	private boolean decodeTag_gender(int charSum) {
		return false;
	}
	private String[][] tags = {tagDecoder_POS_raw, tagDecoder_animacy_raw, tagDecoder_aspect_raw, tagDecoder_case_raw, tagDecoder_gender_raw, tagDecoder_involvement_raw, tagDecoder_mood_raw, tagDecoder_number_raw, tagDecoder_person_raw, tagDecoder_tense_raw, tagDecoder_transitivity_raw, tagDecoder_voice_raw};

	 	private static ArrayList<String> unknownOCTagsFullStr = new ArrayList<String>();
	private static ArrayList<String> unknownOCTags = new ArrayList<String>();
	private void decodeOneTag(String tag, int tagNumber) {
		boolean result=false;
		String[] nowArray=tags[tagNumber];int nowLength=nowArray.length;
		int index=-2;
		for(int i=0;i<nowLength;++i)
			if(tag.equals(nowArray[i])) {index=i-1;break;}
		if(index==-2){if(unknownOCTags.indexOf(tag)==-1) {unknownOCTags.add(tag);System.out.println("?tag:"+tag);}return;}
		switch(tagNumber) {
		case 0:
			tag_POS=index;
			break;
		case 1:
			tag_animacy=index;
			break;
		case 2:
			tag_aspect=index;
			break;
		case 3:
			tag_case=index;
			break;
		case 4:
			tag_gender=index;
			break;
		case 5:
			tag_involvement=index;
			break;
		case 6:
			tag_mood=index;
			break;
		case 7:
			tag_number=index;
			break;
		case 8:
			tag_person=index;
			break;
		case 9:
			tag_tense=index;
			break;
		case 10:
			tag_transitivity=index;
			break;
		case 11:
			tag_voice=index;
			break;
		}
	}
	private int decodeNextTag(String tags, int tagNumber) {
		int commaIndex;
		commaIndex = tags.indexOf(',');
		if(commaIndex==-1) { decodeOneTag(tags, tagNumber);return -1;}
		decodeOneTag(tags.substring(0,commaIndex), tagNumber);
		return commaIndex;
	}
	private void decodeTagString(String tags) {
		int commaIndex=0;int i=0;
		do{
			commaIndex=decodeNextTag(tags, i);
			if(commaIndex==-1)return; else tags=tags.substring(commaIndex+1);
			++i;
		}while(commaIndex!=-1);
	}
	public OpenCorporaTag(String tag, int lemmaIndex) {
		decodeTagString(tag);
		this.lemmaIndex=lemmaIndex;
	}
	
}
