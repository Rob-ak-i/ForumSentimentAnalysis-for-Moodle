import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import common.Settings;
import languageprocessing.OpenCorporaTag;

public class TestLexicThings {
	static int wlen,tlen;
	static ArrayList<String> lemmes;
	static ArrayList<OpenCorporaTag> tags;
	static String[] words={"перед","тем",",","как","принять","участие","в","дискуссии","ответьте","пожалуйста","на","вопросы","этой","анкеты"};
	
	public static void prepare() throws Exception {
		Settings.initialize();
		lemmes=new ArrayList<String>();
		tags=new ArrayList<OpenCorporaTag>();
		Scanner sw=new Scanner(new File(Settings.appPathFull+"saves"+Settings.linkDelimiter+"words_normalForm.txt"));
		Scanner st=new Scanner(new File(Settings.appPathFull+"saves"+Settings.linkDelimiter+"words_tag.txt"));
		
		for(int i=0;i<30;++i) {
			tags.add(new OpenCorporaTag(st.nextLine(), lemmes.size()));
			lemmes.add(sw.nextLine());
		}
		tlen=words.length;
		wlen=lemmes.size();
		sw.close();st.close();
	}
	public static ArrayList<Integer> getNouns(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0;i<wlen;++i)
			if(tags.get(i).tag_POS==OpenCorporaTag.tag_POS_NOUN)result.add(i);
		return result;
	}
	public static ArrayList<Integer> getElementsByPOS(int POSNumber){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0;i<wlen;++i)
			if(tags.get(i).tag_POS==OpenCorporaTag.tag_POS_NOUN)result.add(i);
		return result;
	}
	public static void printIntList(ArrayList<Integer> indexes) {
		for(int i=0;i<indexes.size();++i)
			System.out.print(lemmes.get(indexes.get(i))+' ');
	}
	public static ArrayList<Point> getConnections(ArrayList<Integer> activeElements, ArrayList<Integer> passiveElements) {
		ArrayList<Point>connections = new ArrayList<Point>();
		
		return connections;
	}
	public static void main(String[] args) throws Exception {
		prepare();
		ArrayList<Integer> nouns=getNouns();
		printIntList(nouns);
		ArrayList<Integer> verbs=getElementsByPOS(OpenCorporaTag.tag_POS_VERB);
		printIntList(verbs);
		ArrayList<Point>connections=getConnections(verbs, nouns);
		
		
	}

}
