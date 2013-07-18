package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class SubjectNode implements Node{
	
	ArrayList<Connection> connectionList;
	String name;
	public SubjectNode(String name)
	{
		connectionList=new ArrayList<Connection>();
		this.name=name;
	}
	public Iterator<Connection> listConnections()
	{
		Iterator<Connection> conItr=connectionList.iterator();
		return conItr;
	}

	public void connect(Node n,Double weight) 
	{
		Connection newCon = new Connection(this,n,weight);
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
				break;
			}
		}
		n.removeConnection(next);
	}
		
}
