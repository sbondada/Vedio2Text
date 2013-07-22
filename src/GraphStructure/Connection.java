package GraphStructure;

public class Connection {
	Node connectionLeft;
	Node connectionRight;
	int connectionWeight;
	public Connection (Node Left,Node Right,int weight)
	{
		this.connectionLeft=Left;
		this.connectionRight=Right;
		this.connectionWeight=weight;
	}
	public void setweight(int weight)
	{
		this.connectionWeight=weight;
	}
}
