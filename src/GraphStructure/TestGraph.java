package GraphStructure;

public class TestGraph {
	
	public static void main(String[] args)
	{
		TriPartiteGraph tpgraph = new TriPartiteGraph();
		Node n2=tpgraph.subcol.addNode("hello");
		Node n1=tpgraph.verbcol.addNode("ride");
		n1.connect(n2);
	}

}
