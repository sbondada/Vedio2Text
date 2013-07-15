package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectNode implements Node {
	
	ArrayList<Connection> connectionList;
	String name;
	public ObjectNode(String name)
	{
		connectionList=new ArrayList<Connection>();
		this.name=name;
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
			if(next.connectionLeft.equals(n)|| next.connectionRight.equals(n))
			{
				connectionList.remove(next);
				break;
			}
		}
		n.removeConnection(next);
	}
	
}
