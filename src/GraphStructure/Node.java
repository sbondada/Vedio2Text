package GraphStructure;

import java.util.Iterator;

public interface Node 
{
public Iterator<Connection> listConnections();
public void addConnection(String conname,Connection c);
public void removeConnection(String conname);
public void connect(Node n,Double weight);
public void disconnect(Node n);
}
