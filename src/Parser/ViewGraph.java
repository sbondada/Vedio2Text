package Parser;

//all the color codings are extracted from http://www.tayloredmktg.com/rgb/ and http://www.rapidtables.com/web/color/RGB_Color.html

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JApplet;
import javax.swing.JFrame;

import GraphStructure.Connection;
import GraphStructure.Node;
import GraphStructure.ObjectNode;
import GraphStructure.SubjectNode;
import GraphStructure.TriPartiteGraph;
import GraphStructure.VerbNode;

public class ViewGraph extends JApplet {
	private static final long serialVersionUID = 1L;
	TriPartiteGraph tpgraph;
	public ViewGraph(TriPartiteGraph tpgraph)
	{
		this.tpgraph=tpgraph;
	}

	public void init() 
	{
		setBackground(Color.white);
		setForeground(Color.black);
	}
	public void paint(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		int basex=200;
		int basey=150;
		int step=40;
		// drawing the nodes for the collection of subjects.
		Iterator<Entry<String,SubjectNode>> subitr=tpgraph.getsubcol().getNodeCollection().entrySet().iterator();
		int inc=0;
		while(subitr.hasNext())
		{	
		DrawEllipse(g2,subitr.next().getKey(),basex, basey+(inc*step));
		inc=inc+1;
		}
		
		Iterator<Entry<String,VerbNode>> verbitr=tpgraph.getverbcol().getNodeCollection().entrySet().iterator();
		inc=0;
		int shiftx=0;
		int shifty=0;
		while(verbitr.hasNext())
		{	
		DrawEllipse(g2,verbitr.next().getKey(),basex+600+shiftx, basey+shifty-125+(inc*step));
		inc=inc+1;
		if (inc==25)
		{
			inc=0;
			shiftx=150;
			shifty=25;
		}
		}
		
		Iterator<Entry<String,ObjectNode>> objitr=tpgraph.getobjcol().getNodeCollection().entrySet().iterator();
		inc=0;
		while(objitr.hasNext())
		{	
		DrawEllipse(g2,objitr.next().getKey(),basex+1350, basey+(inc*step));
		inc=inc+1;
		}
		
		Iterator<Entry<String,SubjectNode>> subjconitr = tpgraph.getsubcol().getNodeCollection().entrySet().iterator();
		int incs=0;
		int incv=0;
		g2.setPaint(Color.red);
		int subjx=basex+100;
		int subjy=basey+15;
		int verbx=basex+600;
		int verby=basey-125+15;
		shiftx=0;shifty=0;
		while(subjconitr.hasNext())
		{
			Iterator<Entry<String,Connection>> conitr = subjconitr.next().getValue().getConnectionMap().entrySet().iterator();
			incv=0;
			shiftx=0;shifty=0;
			while (conitr.hasNext())
			{
				Entry<String,Connection> entry = conitr.next();
				g2.setStroke(new BasicStroke(setstrokeprop(g2,entry.getValue().getweight())/2));
				if(entry.getValue().getweight()!=0)
				{
					g2.draw(new Line2D.Double((float)subjx,(float)subjy+(incs*step),(float)verbx+shiftx,(float)verby+(incv*step)+shifty));

				}
				incv=incv+1;
				if(incv==25)
				{
					incv=0;
					shiftx=150;
					shifty=25;
				}
			}
			incs=incs+1;
		}
		
		Iterator<Entry<String,ObjectNode>> objconitr = tpgraph.getobjcol().getNodeCollection().entrySet().iterator();
		incv=0;
		int inco=0;
		g2.setPaint(Color.red);
		verbx=basex+600+100;  // 100 as the length of the ellipse also contributes plus 600 the position of the verb node collections
		verby=basey-125+15;   
		int objx=basex+1350;
		int objy=basey+15;
		shiftx=0;shifty=0;
		while(objconitr.hasNext())
		{
			Iterator<Entry<String,Connection>> conitr = objconitr.next().getValue().getConnectionMap().entrySet().iterator();
			incv=0;
			shiftx=0;shifty=0;
			while (conitr.hasNext())
			{
				Entry<String,Connection> entry = conitr.next();
				g2.setStroke(new BasicStroke(setstrokeprop(g2,entry.getValue().getweight())/2));
				if(entry.getValue().getweight()!=0)
				{
				g2.draw(new Line2D.Double((float)objx,(float)objy+(inco*step),(float)verbx+shiftx,(float)verby+(incv*step)+shifty));
				}
				incv=incv+1;
				if(incv==25)
				{
					incv=0;
					shiftx=150;
					shifty=25;
				}
			}
			inco=inco+1;
		}
		
	}
	public void DrawEllipse(Graphics2D g2,String text,double x,double y)
	{
		GradientPaint lg2white= new GradientPaint((float)x,(float)y,Color.gray,(float)x+60f,(float)y,Color.white);
		g2.setPaint(lg2white);
		Ellipse2D.Double e=new Ellipse2D.Double(x, y,100, 30); //orginally it was supposed to be 80
		g2.fill(e);
		g2.setPaint(Color.black);
		g2.draw(e);
		// the whole arrangement is to align the text inside the ellipse.
		int diff=5-text.length();
		g2.drawString(text, (float)x+25+(3*diff), (float)y+20);
	}
	
	public int setstrokeprop(Graphics2D g,int weight)
	{
		if(weight<25)
		{
			g.setPaint(new Color(250,128,114)); //salmon
			return weight;
		}
		if(weight >=25 && weight<50)
		{
			g.setPaint(new Color(255,140,0));  //dark orange
			return weight-25;
		}
		if(weight>=50 && weight <75)
		{
			g.setPaint(new Color(255,0,0)); //red

			return weight-50;
		}
		if(weight>=75 && weight <100)
		{
			g.setPaint(new Color(128,0,0));  //Maroon
			
			return weight-75;
		}
		if(weight>=100 && weight <125)
		{
			g.setPaint(new Color(240,230,140)); //khaki
			
			return weight-100;
		}
		if(weight>=125 && weight <150)
		{
			g.setPaint(new Color(255,255,0)); // yellow
		
			return weight-125;
		}
		if(weight>=150 && weight <175)
		{
			g.setPaint(new Color(124,252,0)); //lawn green
			
			return weight-150;
		}
		if(weight>=175 && weight <200)
		{
			g.setPaint(new Color(0,255,127));  //spring green
		
			return weight-175;
		}
		if(weight>=200 && weight <225)
		{
			g.setPaint(new Color(0,100,0)); //dark green
			
			return weight-200;
		}
		if(weight>=225 && weight <250)
		{
			g.setPaint(new Color(188,143,143));  //rosy brown
			
			return weight-225;
		}
		if(weight>=250 && weight <275)
		{
			g.setPaint(new Color(139,69,19)); //saddle brown

			return weight-250;
		}
		if(weight>=275 && weight <300)
		{
			g.setPaint(new Color(0,255,255)); //cyan

			return weight-275;
		}
		if(weight>=300 && weight <325)
		{
			g.setPaint(new Color(0,128,128)); //Teal

			return weight-300;
		}
		if(weight>=325 && weight <350)
		{
			g.setPaint(new Color(0,0,128));  //navy blue

			return weight-325;
		}
		if(weight>=350 && weight <375)
		{
			g.setPaint(new Color(255,182,193));  //light pink

			return weight-350;
		}
		if(weight>=375 && weight <400)
		{
			g.setPaint(new Color(255,20,147)); //deep pink

			return weight-375;
		}
		if(weight>=400 && weight <425)
		{
			g.setPaint(new Color(148,0,211)); //dark violet

			return weight-400;
		}
		if(weight>=425 && weight <450)  
		{
			g.setPaint(new Color(105,105,105)); //dim gray

			return weight-425;
		}
		if(weight>=450 && weight <475)
		{
			g.setPaint(new Color(0,0,0)); //black
			return weight-450;
		}
		if(weight>=475 && weight <500)
		{
			g.setPaint(new Color(0,0,0)); //black
			return weight-475;
		}
		if(weight>=500 && weight <525)
		{
			g.setPaint(new Color(0,0,0)); //black
			return weight-500;
		}
		else
			
		{
			g.setPaint(new Color(0,51,51)); //strange shade of blue
			return weight-525;
		}
	}
	public static void main(String s[]) 
	{
		// the whole purpose is to let it work with the graph.
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
		
		
		JFrame f = new JFrame("ViewGraph");
		f.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) {System.exit(0);}
		});
		JApplet applet = new ViewGraph(tpgraph);
		f.getContentPane().add("Center", applet);
		applet.init();
		f.pack();
		f.setSize(new Dimension(2100,1050));
		f.setVisible(true);
	}
	
}
