package util;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class AdditionalDataObjects {
	/**value format:"key:value;key=value,...; key:value;"*/
	public static HashMap<String,String> getFunctions(String value) {
		HashMap<String, String> properties=new HashMap<String, String>();
		if(value.charAt(value.length()-1)=='}')value=value.substring(0, value.length()-1);
		if(value.charAt(0)=='{')value=value.substring(1, value.length());
		if(value.charAt(value.length()-1)!=';')value=value.concat(";");
		int l=value.length();
		char c;
		StringBuilder sb=new StringBuilder();
		String k=null,v=null;boolean isKeyRead=true;
		for(int i=0;i<l;++i) {
			c=value.charAt(i);
			switch(c) {
			case ':':case '=':
				k=(sb.toString());
				sb=new StringBuilder();
				isKeyRead=false;
				break;
			case ';':case ',':
				v=sb.toString();
				properties.put(k, v);
				sb=new StringBuilder();
				isKeyRead=true;
				break;
			default:
				if(isKeyRead&&c==' ')break;//имя переменной не должно содержать пробелов
				sb.append(c);
			}
		}
		return properties;
	}
	public static StackableMap pack(String key, Object value) {
		StackableMap result = new StackableMap(); 
		result.put(key, value);
		return (StackableMap)result;
	}
}
