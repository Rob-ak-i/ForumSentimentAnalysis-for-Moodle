package common;

import java.util.ArrayList;

import util.OpenCorporaTag;

public class EntityTextManager {
	public ArrayList<String> textMonomials;
	public ArrayList<String> textLemmes;
	public ArrayList<Integer> textMonomialsToLemmes;
	public ArrayList<OpenCorporaTag> textMonomialsToTags;
	
	public EntityTextManager() {
		textMonomials = new ArrayList<String>();
		textLemmes = new ArrayList<String>();
		textMonomialsToLemmes = new ArrayList<Integer>();
		textMonomialsToTags = new ArrayList<OpenCorporaTag>();
	}
	
	public int addWord(String word) {
		int index = textMonomials.indexOf(word);
		if(index!=-1)return index;
		textMonomials.add(word);
		return textMonomials.size()-1;
	}
	private int addLemmaInner(String word) {
		int index = textLemmes.indexOf(word);
		if(index!=-1)return index;
		textLemmes.add(word);
		return textLemmes.size()-1;
	}
	public void addLemmaAndTag(String lemma, String tagStr) {
		int lemmaIndex=addLemmaInner(lemma);
		textMonomialsToLemmes.add(lemmaIndex);
		OpenCorporaTag tag = new OpenCorporaTag(tagStr,lemmaIndex);
		textMonomialsToTags.add(tag);
		
	}
}
