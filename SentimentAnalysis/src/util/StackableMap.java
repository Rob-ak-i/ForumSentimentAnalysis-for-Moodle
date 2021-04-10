package util;

import java.util.Map;
import java.util.HashMap;

public class StackableMap extends HashMap<String, Object>{
	/**was set by random seed*/
	private static final long serialVersionUID = -1589099301835372898L;
	public StackableMap pack(String key, Object value) {
		this.put(key, value);
		return this;
	}
	public String toString() {
		String result = super.toString();
		if(result.charAt(0)=='{')result=result.substring(1);
		if(result.charAt(result.length()-1)=='}')result=result.substring(0, result.length()-1);
		return result;
	}
}
