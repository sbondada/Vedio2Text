package GraphStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class VerbCollection {
ArrayList<VerbNode> NodeCollection;

public VerbCollection()
{
	NodeCollection=new ArrayList<VerbNode>();
}
	
public VerbNode addNode(String name)
{
	VerbNode vernode =new VerbNode(name);
	NodeCollection.add(vernode);
	return vernode;
}
public void removeNode(String name)
{
	VerbNode n=searchNode(name);
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
public VerbNode searchNode(String name)
{
	Iterator<VerbNode> nodeItr=NodeCollection.iterator();
	while(nodeItr.hasNext())
	{
		VerbNode verbnode=nodeItr.next();
		if(verbnode.name==name)
		{
			return verbnode;
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
