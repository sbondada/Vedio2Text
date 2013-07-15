package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class VerbCollection {
ArrayList<Node> NodeCollection;
	
	public void addNode(Node n)
	{
		NodeCollection.add(n);
	}
    public void removeNode(Node n)
    {
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

}
