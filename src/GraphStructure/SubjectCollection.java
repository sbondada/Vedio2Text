package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class SubjectCollection {
ArrayList<SubjectNode> NodeCollection;
	
	public SubjectCollection()
	{
		NodeCollection=new ArrayList<SubjectNode>();
	}
	public SubjectNode addNode(String name)
	{
		SubjectNode subnode =new SubjectNode(name);
		NodeCollection.add(subnode);
		return subnode;
	}
    public void removeNode(String name)
    {
    	SubjectNode n=searchNode(name);
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
    public SubjectNode searchNode(String name)
    {
    	Iterator<SubjectNode> nodeItr=NodeCollection.iterator();
    	while(nodeItr.hasNext())
    	{
    		SubjectNode subnode=nodeItr.next();
    		if(subnode.name==name)
    		{
    			return subnode;
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
