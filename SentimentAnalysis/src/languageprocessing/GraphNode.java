package languageprocessing;

public interface GraphNode {
	public abstract GraphNode getParent();
	public abstract int getChildsCount();
	public abstract GraphNode getChild(int childIndex);
	public int getNumberOfLeaves();/* {
		int accumulator=0,count=getChildsCount();
		for(int i=0;i<count;++i)
			accumulator+=getChild(i).getNumberOfEdges();
		if(accumulator==0)accumulator++;
		return accumulator;
	}*/
}
