package util;

import java.util.ArrayList;

public class Message {
	public int userID=-1;
	public int postID=-1;
	public int parentPostID=-1;
	public int postTime=-1;
	public ArrayList<Integer>words=null;
	/** if 1 then stores vanilla string, if 2 then stores opencorporatag*/
	public int storageLevel=0;
}
