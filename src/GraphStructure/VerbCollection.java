package GraphStructure;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class VerbCollection extends CollectionClass{
LinkedHashMap<String,VerbNode> NodeCollection;

public VerbCollection()
{
	NodeCollection=new LinkedHashMap<String,VerbNode>();
}
	
public VerbNode addNode(String name)
{
	VerbNode vernode =new VerbNode(name);
	NodeCollection.put(name,vernode);
	return vernode;
}
public void removeNode(String name)
{	
	NodeCollection.remove(name);
}
public Iterator<VerbNode> listNodeCollection()
{
	Iterator<VerbNode> verbnodecoll=NodeCollection.values().iterator();
	return verbnodecoll;
}
}
