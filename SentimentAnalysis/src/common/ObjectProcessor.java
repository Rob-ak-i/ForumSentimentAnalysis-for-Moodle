package common;

import java.util.ArrayList;
import java.util.HashMap;

public interface ObjectProcessor {
	public boolean process(String methodName, HashMap<String, Object> parameters);
	//public static ArrayList<ObjectProcessor> processors = new ArrayList<ObjectProcessor>();  
}
