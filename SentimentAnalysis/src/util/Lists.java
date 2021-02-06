package util;

import java.util.ArrayList;

public class Lists {

	public static void getUniqueArrayList(ArrayList in,ArrayList out){
		int l=in.size(),l1=0;
		boolean match;
		for(int i=0;i<l;++i) {
			match=false;
			for(int j=0;j<l1;++j) {
				match=in.get(i).equals(out.get(j));
				if(match)break;}
			if(match)continue;
			out.add(in.get(i));
			l1+=1;}
		return;
	}
}
