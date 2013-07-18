package GraphStructure;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class ObjectCollection extends CollectionClass 
{
	LinkedHashMap<String,ObjectNode> NodeCollection;
	
	public ObjectCollection()
	{
		NodeCollection=new LinkedHashMap<String,ObjectNode>();
	}
	public ObjectNode addNode(String name)
	{
		ObjectNode objnode =new ObjectNode(name);
		NodeCollection.put(name,objnode);
		return objnode;
	}
    public void removeNode(String name)
    {
    	NodeCollection.remove(name);
    }
    public Iterator<ObjectNode> listNodeCollection()
    {
    	Iterator<ObjectNode> objnodecoll=NodeCollection.values().iterator();
    	return objnodecoll;
    }
}
