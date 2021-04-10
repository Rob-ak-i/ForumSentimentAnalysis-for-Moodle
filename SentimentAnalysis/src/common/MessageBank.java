package common;

import java.util.ArrayList;

import languageprocessing.Message;
import languageprocessing.Sequence;
import languageprocessing.SyntaxProcessor;
import util.ManagedObject;

public class MessageBank implements ManagedObject{
	/** if 0 then stores vanilla string, if 1 then stores OpenCorporaTag, if 2 then stores one grapheme and link to nextGrapheme*/
	public static final int storageLevel_Vanilla=0;
	public static final int storageLevel_Lexemes=1;
	public static final int storageLevel_Graphemes=2;
	
	public static final int storageLevel_Syntax=3;
	/**ниже приведены уровни упрощения сообщения на пути к извлечению необходимых признаков*/
	/**уровень, при котором разрешаются анафоры*/
	//public static final int storageLevel_DePersonized=3;
	public int storageLevel=0;
	public String name;
	public ArrayList<Message> textMessages;
	public MessageBank() {
		textMessages = new ArrayList<Message>();
		name="";
	}
	public void free() {
		clear();
		textMessages=null;
		name=null;
	}
	@Override
	public void clear() {
		if(textMessages!=null)for(int i=0;i<textMessages.size();++i)textMessages.get(i).clear();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getMeasurableParameter() {
		//if(textMessages==null)return 0;
		return textMessages.size();
	}
	
	public void upgradeToLevel3_Syntax() {
		if(storageLevel!=storageLevel_Graphemes)return;
		Message nowMessage;
		for(int i=0;i<textMessages.size();++i) {
			nowMessage=textMessages.get(i);
			while(nowMessage!=null) {
				SyntaxProcessor.processMessage(nowMessage);
				nowMessage=nowMessage.nextPartOfMessage;
			}
		}
		storageLevel=storageLevel_Syntax;
	}
}
