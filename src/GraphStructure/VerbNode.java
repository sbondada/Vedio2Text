package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;


public class VerbNode implements Node{
	
	ArrayList<Connection> connectionList;
	public VerbNode()
	{
		connectionList=new ArrayList<Connection>();
	}
	public Iterator<Connection> listConnections()
	{
		Iterator<Connection> conItr=connectionList.iterator();
		return conItr;
	}

	public void connect(Node n) 
	{
		Connection newCon = new Connection(this,n,1.0);
		connectionList.add(newCon);
		n.addConnection(newCon);
	}
	
	public void addConnection(Connection c)
	{
		connectionList.add(c);
	}
	
	public void removeConnection(Connection c)
	{
		connectionList.remove(c);
	}
	
	public void disconnect(Node n) 
	{
		Iterator<Connection> Itr =connectionList.iterator();
		Connection next=null;
		while (Itr.hasNext())
		{
			next=Itr.next();
			if(next.connectionRight.equals(n))
			{
				connectionList.remove(next);
			}
		}
		n.removeConnection(next);
	}

}
