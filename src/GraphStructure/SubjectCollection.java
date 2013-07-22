package GraphStructure;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class SubjectCollection extends CollectionClass {
	
LinkedHashMap<String,SubjectNode> NodeCollection;
	
	public SubjectCollection()
	{
		NodeCollection=new LinkedHashMap<String,SubjectNode>();
	}
	public SubjectNode addNode(String name)
	{
		SubjectNode subnode =new SubjectNode(name);
		NodeCollection.put(name,subnode);
		return subnode;
	}
    public void removeNode(String name)
    {
    	NodeCollection.remove(name);	
    }
    public Iterator<SubjectNode> listNodeCollection()
    {
    	Iterator<SubjectNode> subjnodecoll=NodeCollection.values().iterator();
    	return subjnodecoll;
    }
    
    public LinkedHashMap<String,SubjectNode> getNodeCollection()
    {
    	return NodeCollection;
    }
}
