package util;

public class GenericObject {
	byte[] hashData;
	public GenericObject(String hashData) {
		
	}
	public GenericObject() {
		hashData=null;
	}
	@Override
	public String toString() {
		if(hashData==null)return"";
		int l=hashData.length;
		if(l==0)return"";
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<l;++i)sb.append(Integer.toString(hashData[i],16));
		return sb.toString();
	}
}
