package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectCollection extends CollectionClass 
{
	ArrayList<ObjectNode> NodeCollection;
	
	public ObjectCollection()
	{
		NodeCollection=new ArrayList<ObjectNode>();
	}
	public ObjectNode addNode(String name)
	{
		ObjectNode objnode =new ObjectNode(name);
		NodeCollection.add(objnode);
		return objnode;
	}
    public void removeNode(String name)
    {
    	ObjectNode n=searchNode(name);
    	if (n==null)
    	{
    		System.out.println("No Node of such name exist");
    	}
    	Iterator<Connection> Itr = n.listConnections();
    	while(Itr.hasNext())
    	{
    		Connection con=Itr.next();
    		if(!con.connectionLeft.equals(n))
    		{
    			con.connectionLeft.disconnect(n);
    			n.removeConnection(con);
    		}
    		else
    		{
    			con.connectionRight.disconnect(n);
    			n.removeConnection(con);
    		}
    	}
    	NodeCollection.remove(n);
    }
    public ObjectNode searchNode(String name)
    {
    	Iterator<ObjectNode> nodeItr=NodeCollection.iterator();
    	while(nodeItr.hasNext())
    	{
    	    ObjectNode objnode=nodeItr.next();
    		if(objnode.name==name)
    		{
    			return objnode;
    		}
    		else
    		{
    			System.out.println("No node of such name exists");
    			return null;
    		}
    	}
		return null;
    }
}
