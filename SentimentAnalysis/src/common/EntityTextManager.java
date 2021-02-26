package common;

import java.util.ArrayList;

import gui.JComboBoxExt;
import languageprocessing.LanguageProcessor;
import languageprocessing.LowLevelTextProcessor;
import languageprocessing.OpenCorporaTag;
import languageprocessing.Sequence;
import util.DataTable;
import util.EntityAbstractManager;

public class EntityTextManager extends EntityAbstractManager<MessageBank>{
	/* MessageBank manager */
	
	public ArrayList<String> textMonomials;
	public ArrayList<String> textLemmes;
	public ArrayList<Integer> textMonomialsToLemmes;
	public ArrayList<OpenCorporaTag> textMonomialsToTags;
	public ArrayList<Sequence> sequenceRoots;
	public ArrayList<MessageBank> messageBanks;
	
	public EntityTextManager() {
		textMonomials = new ArrayList<String>();
		textLemmes = new ArrayList<String>();
		textMonomialsToTags = new ArrayList<OpenCorporaTag>();
		textMonomialsToLemmes = new ArrayList<Integer>();
		sequenceRoots=new ArrayList<Sequence>();
		messageBanks=new ArrayList<MessageBank>();	
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
	/**allows to create databank even from somePerson or someTextForum or RawText.
	 * makes messagesArray with word indexes, new words of which sends to externalLibraries to recognize*/
	public void addManagedElement(String tableIdentifier) {
		DataTable table = CommonData.dataManager.getManagedElement(tableIdentifier);if(table==null)return;
		MessageBank messageBank = languageprocessing.LowLevelTextProcessor.extractMessagesFromDataTable(table);if(messageBank==null)return;
		messageBank.name=tableIdentifier;
		super.addManagedElement(messageBank, tableIdentifier);
		/**updating TextManager mechanism*/
		appendKnowledgeBase();
		messageBank.storageLevel=MessageBank.storageLevel_Lexemes;
		//unifyTagsByLemmes(); - was made automatically by textManager
		LanguageProcessor.upgradeAndSpliceAppendedMessages(messageBank);//getGraphemesFromMessages();
	}
	private void appendKnowledgeBase() {if(textMonomials.size()-textMonomialsToLemmes.size()>0)LowLevelTextProcessor.appendKnowledgeBase(this.textMonomials.subList(textMonomialsToLemmes.size(), textMonomials.size()),Settings.appPath+Settings.linkDelimiter+"saves"+Settings.linkDelimiter+"words.txt");}

	@Override
	public Class<MessageBank> getManagedObjectClass() {
		// TODO Auto-generated method stub
		return MessageBank.class;
	}


}
