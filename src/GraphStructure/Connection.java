package GraphStructure;

public class Connection {
	Node connectionLeft;
	Node connectionRight;
	Double connectionWeight;
	public Connection (Node Left,Node Right,Double weight)
	{
		connectionLeft=Left;
		connectionRight=Right;
		connectionWeight=weight;
	}
}
