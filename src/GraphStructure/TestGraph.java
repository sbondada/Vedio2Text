package GraphStructure;

public class TestGraph {
	
	public static void main(String[] args)
	{
		TriPartiteGraph tpgraph = new TriPartiteGraph();
		Node n1=tpgraph.subcol.addNode("potter");
		Node n2=tpgraph.verbcol.addNode("ride");
		Node n3 =tpgraph.objcol.addNode("ema");
		Node n4=tpgraph.verbcol.addNode("love");
		n1.connect(n2, 1.3);
		n2.connect(n3, 3.2);
		n4.connect(n1, 1.1);
		n3.connect(n4, 2.4);
	}
}
