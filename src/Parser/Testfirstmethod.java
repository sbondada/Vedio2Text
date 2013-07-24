package Parser;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import GraphStructure.Node;
import GraphStructure.TriPartiteGraph;

public class Testfirstmethod {
	public static void main(String args[])
	{
		TriPartiteGraph tpgraph = new TriPartiteGraph();
		String[] objects={"aeroplane","bicycle","bird","boat","bottle","person","car","bus","dog","cat","chair","table","cow","table","horse","bike","plant","sheep","sofa","monitor"};
		String[] actions= {"smile","laugh","chew","talk","smake","eat","drink","cartwheel","clap","climb","climb stairs","dive","fall","backhand flip","handstand","jump","pull up","push up","run","sit down","sit up","somersault","stand up","turn","walk","wave","brush","catch","draw sword","dribble","golf","hit","kick","pick","pour","push","ride","shoot ball","shoot bow","shoot gun","swing bat","exercise","throw","fence","hug","kiss","punch","shake hands","sword fight"};
		Node[] subnode=new Node[objects.length];
		Node[] verbnode=new Node[actions.length];
		Node[] objnode=new Node[objects.length];
		int i=0;
		while(i<objects.length)
		{
			subnode[i]=tpgraph.getsubcol().addNode(objects[i]);
			i=i+1;
		}
		i=0;
		while(i<actions.length)
		{
			verbnode[i]=tpgraph.getverbcol().addNode(actions[i]);
			i=i+1;
		}
		i=0;
		while(i<objects.length)
		{
			objnode[i]=tpgraph.getobjcol().addNode(objects[i]);
			i=i+1;
		}
		for(int i1=0;i1<objects.length;i1++)
		{
			for(int j=0;j<actions.length;j++)
			{
				subnode[i1].connect(verbnode[j],0);
			}
		}
		for(int i1=0;i1<actions.length;i1++)
		{
			for(int j=0;j<objects.length;j++)
			{
				verbnode[i1].connect(objnode[j],0);
			}
		}
		SynsetGen g=new SynsetGen();
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		SVOextracter svo=new SVOextracter();
		svo.sentenceSplit(lp,"descriptions.txt");
		g.getSynset(svo, tpgraph);
		System.out.println(g.subjverb.size());
		System.out.println(g.verbobj.size());
		System.out.println(g.subjverbobj.size());
		
	}
}
