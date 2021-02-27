package languageprocessing;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.CommonData;
import common.MessageBank;
import common.Settings;
import util.DataTable;

public class LanguageProcessor {
	/**graphematical analysis*/
	public static void upgradeAndSpliceAppendedMessages(MessageBank messageBank) {
		messageBank.storageLevel=MessageBank.storageLevel_Lexemes;
		Message.delimitersIndexesFind();
		ArrayList<Message>messages=messageBank.textMessages;
		messageBank.storageLevel=MessageBank.storageLevel_Graphemes;
		for(int i=0;i<messages.size();++i) {
			messages.get(i).addLemmes();
			messages.get(i).spliceToGraphemes();
		}
	}
	/**morphological analysis - made by python script*/
	
	/**lexic analysis WIP*/

	/**grapheme simplification and anaphora resolution will be here*/
	/**pattern recognition will be here*/
	
	public static void doAllWIP(MessageBank messagesBank) {
		System.out.println("languageprocessing.LanguageProcessor.doAllWIP - have not yet done. deadline - beginning of march 2021");
		
		//resolveAnaphoras();
		//getActantPatternsFromGraphemes();
	}
}