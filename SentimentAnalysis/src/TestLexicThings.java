import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import common.Settings;
import languageprocessing.OpenCorporaTag;

public class TestLexicThings {
	static int wlen,tlen;
	static ArrayList<String> lemmesStorage;
	static ArrayList<String> lemmes;
	static ArrayList<String> wordsStorage;
	static ArrayList<OpenCorporaTag> tagsStorage;
	static ArrayList<OpenCorporaTag> tags;
	static ArrayList<String> words;
	static String[] words0={"перед","тем",",","как","принять","участие","в","дискуссии","ответьте","пожалуйста","на","вопросы","этой","анкеты"};
	static ArrayList<Integer> positions;
	
	public static void prepare() throws Exception {
		Settings.initialize();
		lemmes=new ArrayList<String>();
		positions=new ArrayList<Integer>();
		tags=new ArrayList<OpenCorporaTag>();
		wordsStorage=new ArrayList<String>();
		lemmesStorage=new ArrayList<String>();
		tagsStorage=new ArrayList<OpenCorporaTag>();
		Scanner sw0=new Scanner(new File(Settings.appPathFull+"saves"+Settings.linkDelimiter+"words.txt"));
		Scanner sw=new Scanner(new File(Settings.appPathFull+"saves"+Settings.linkDelimiter+"words_normalForm.txt"));
		Scanner st=new Scanner(new File(Settings.appPathFull+"saves"+Settings.linkDelimiter+"words_tag.txt"));
		words = new ArrayList<String>();
		for(int i=0;i<words0.length;++i)words.add(words0[i]);
		for(int i=0;i<30;++i) {
			tagsStorage.add(new OpenCorporaTag(st.nextLine(), lemmesStorage.size()));
			lemmesStorage.add(sw.nextLine());
			wordsStorage.add(sw0.nextLine());
		}
		int index=-1;
		for(int i=0;i<words0.length;++i) {
			index=wordsStorage.indexOf(words0[i]);
			lemmes.add(lemmesStorage.get(index));
			tags.add(tagsStorage.get(index));
			positions.add(i);
		}
		tlen=words0.length;
		wlen=lemmes.size();
		sw0.close();sw.close();st.close();
	}
	public static WordList getElementsByPOS(int POSNumber){
		WordList result = new WordList();
		for(int i=0;i<wlen;++i)
			if(tags.get(i).tag_POS==POSNumber)result.add(new Point(i, positions.get(i) ));
		return result;
	}
	public static void printIntList(WordList indexes, ArrayList<String> elementsStorage) {
		for(int i=0;i<indexes.size();++i)
			System.out.print(elementsStorage.get(indexes.get(i).x)+' ');
		System.out.println();
	}
	public static void connectFirstEpoch(ArrayList<OpenCorporaTag> tags) {
		int lastNoun=-1,lastVerb=-1,lastPrep=-1;
		/**нужно для сохранения перечисления*/
		int lastNounCase=-1;
		int isLastVERB=-1;
		
		OpenCorporaTag tag;
		
		for(int i=0;i<tags.size();++i) {
			tag=tags.get(i);
			switch(tag.tag_POS) {
			case OpenCorporaTag.tag_POS_NOUN:
				lastNoun=i;
				lastNounCase=tag.tag_case;
				
				break;
			case OpenCorporaTag.tag_POS_VERB:case OpenCorporaTag.tag_POS_INFN:
				if(tag.tag_POS==OpenCorporaTag.tag_POS_INFN) {
					if(isLastVERB==0) {
						//если через запятую, то перечисление инфинитивов, иначе соединение
					}if(isLastVERB==1) {
						//соединение глагола с текущим инфинитивом
					}
					isLastVERB=0;
				}else
					isLastVERB=1;
				lastVerb=i;
				break;
			case OpenCorporaTag.tag_POS_PREP:
				lastPrep=i;
				break;
			}
		}
	}
	public static ArrayList<Point> getConnections(WordList activeElements, WordList passiveElements) {
		WordList allElements=new WordList();
		ArrayList<Boolean> classes= new ArrayList<Boolean>();
		allElements.addAll(activeElements);
		for(int i=0;i<activeElements.size();++i) {classes.add(true);}
		allElements.addAll(passiveElements);
		for(int i=activeElements.size();i<allElements.size();++i) {classes.add(false);}
		ArrayList<Boolean> classChangings= new ArrayList<Boolean>();
		for(int i=0;)
		ArrayList<Point>connections = new ArrayList<Point>();
		
		return connections;
	}
	public static void main(String[] args) throws Exception {
		prepare();
		WordList nouns=getElementsByPOS(OpenCorporaTag.tag_POS_NOUN);
		System.out.print("    существительные: ");
		printIntList(nouns, words);
		System.out.print("    глаголы: ");
		WordList verbs=getElementsByPOS(OpenCorporaTag.tag_POS_VERB);
		verbs.addAll(getElementsByPOS(OpenCorporaTag.tag_POS_INFN));
		printIntList(verbs, words);
		WordList preps = getElementsByPOS(OpenCorporaTag.tag_POS_PREP);
		System.out.print("    предлоги: ");
		printIntList(preps, words);
		WordList adjectives = getElementsByPOS(OpenCorporaTag.tag_POS_ADJF);
		adjectives.addAll(getElementsByPOS(OpenCorporaTag.tag_POS_ADJS));
		System.out.print("    прилагательные: ");
		printIntList(adjectives, words);

		
		System.out.print("    соединения предлог-существительное: ");
		ArrayList<Point>connections1=getConnections(verbs, nouns);
		for(Point connection:connections1) {
			System.out.println(words.get(connection.x)+"-"+words.get(connection.y));
		}
		System.out.print("    соединения глагол-существительное: ");
		ArrayList<Point>connections=getConnections(verbs, nouns);
		for(Point connection:connections) {
			System.out.println(words.get(connection.x)+"-"+words.get(connection.y));
		}
	}

}
class WordList extends ArrayList<Point>{
	
}
