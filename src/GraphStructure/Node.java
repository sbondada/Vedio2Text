package GraphStructure;

import java.util.Iterator;

public interface Node 
{
public Iterator<Connection> listConnections();
public void addConnection(Connection c);
public void removeConnection(Connection c);
public void connect(Node n,Double weight);
public void disconnect(Node n);
}
