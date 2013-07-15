package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class ObjectNode implements Node {
	
	ArrayList<Connection> connectionList;
	public ObjectNode()
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
	
	}
	
}
