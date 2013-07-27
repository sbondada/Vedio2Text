package Parser;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JApplet;
import javax.swing.JFrame;

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
				subnode[i1].connect(verbnode[j],1); //the value one only for the purpose of visualizing the graph.
			}
		}
		for(int i1=0;i1<actions.length;i1++)
		{
			for(int j=0;j<objects.length;j++)
			{
				verbnode[i1].connect(objnode[j],1); // the value one only for the purpose of visualizing the graph.
			}
		}
		SynsetGen g=new SynsetGen();
		LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		SVOextracter svo=new SVOextracter();
		EdgeWeightGen e = new EdgeWeightGen();
		svo.sentenceSplit(lp,"processedoutput/testsent.txt");
		// the out put is collected to a file for further review
		PrintStream out;
		try 
		{
			out = new PrintStream(new FileOutputStream("output.txt"));
			System.setOut(out);
			System.out.println("\n==========================================SUBJECT-VERB=============================================\n");
			Iterator<Entry<String,Integer>> SV=svo.subjverb.entrySet().iterator();
			while(SV.hasNext())
			{
				Entry<String,Integer> SVentry=SV.next();
				System.out.println(SVentry.getValue()+"\t\t\t"+SVentry.getKey());
			}
			System.out.println("\n==========================================VERB-OBJECT==============================================\n");
			Iterator<Entry<String,Integer>> VO=svo.verbobj.entrySet().iterator();
			while(VO.hasNext())
			{
				Entry<String,Integer> VOentry=VO.next();
				System.out.println(VOentry.getValue()+"\t\t\t"+VOentry.getKey());
			}
			System.out.println("\n======================================SUBJECT-VERB-OBJECT==========================================\n");
			Iterator<Entry<String,Integer>> SVO=svo.subjverbobj.entrySet().iterator();
			while(SVO.hasNext())
			{
				Entry<String,Integer> SVOentry=SVO.next();
				System.out.println(SVOentry.getValue()+"\t\t\t"+SVOentry.getKey());
			}
		} 
		catch (FileNotFoundException ex) 
		{
			ex.printStackTrace();
		}
		g.getSynset(svo, tpgraph);
		e.assignEdgeweight(tpgraph,g);
		// this the code to call the Applet
		ViewGraph v=new ViewGraph(tpgraph);
		JFrame f = new JFrame("ViewGraph");
		f.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		JApplet applet = v;
		f.getContentPane().add("Center", applet);
		applet.init();
		f.pack();
		f.setSize(new Dimension(2100,1050));
		f.setVisible(true);
		System.out.println(g.subjverb.size());
		System.out.println(g.verbobj.size());
		System.out.println(g.subjverbobj.size());
		
		
	}
}
