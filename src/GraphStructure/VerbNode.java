package GraphStructure;

import java.util.Iterator;
import java.util.LinkedHashMap;


public class VerbNode implements Node{
	
	LinkedHashMap<String,Connection> connectionmap;
	String name;
	public VerbNode(String name)
	{
		connectionmap=new LinkedHashMap<String,Connection>();
		this.name=name;
	}
	public Iterator<Connection> listConnections()
	{
		Iterator<Connection> conItr=connectionmap.values().iterator();
		return conItr;
	}

	public void connect(Node n,int weight) 
	{
		
		if(n.getClass().toString().equals("class GraphStructure.SubjectNode"))
		{
		SubjectNode cn=(SubjectNode)n;
		Connection newCon = new Connection(this,n,weight);
		connectionmap.put(this.name+"-"+cn.name, newCon);
		cn.addConnection(cn.name+"-"+this.name,newCon);
		}
		else
		{
		ObjectNode cn=(ObjectNode)n;
		Connection newCon = new Connection(this,n,weight);
		connectionmap.put(this.name+"-"+cn.name, newCon);
		cn.addConnection(cn.name+"-"+this.name,newCon);
		}
		
	}
	
	public void addConnection(String conname,Connection c)
	{
		connectionmap.put(conname, c);
	}
	
	public void removeConnection(String conname)
	{
		connectionmap.remove(conname);
	}
	
	public void disconnect(Node n) 
	{
		if(n.getClass().toString().equals("class GraphStructure.SubjectNode"))
		{
		SubjectNode cn=(SubjectNode)n;
		connectionmap.remove(this.name+"-"+cn.name);
		cn.removeConnection(cn.name+"-"+this.name);
		}
		else
		{
		ObjectNode cn=(ObjectNode)n;
		connectionmap.remove(this.name+"-"+cn.name);
		cn.removeConnection(cn.name+"-"+this.name);
		}
		
	}
	
	public LinkedHashMap<String,Connection> getConnectionMap()
	{
		return connectionmap;
	}
}
