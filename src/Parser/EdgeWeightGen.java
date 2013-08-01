package Parser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map.Entry;

import GraphStructure.Connection;
import GraphStructure.TriPartiteGraph;

public class EdgeWeightGen 
{
	public void assignEdgeweight(TriPartiteGraph t,SynsetGen s)
	{
		PrintStream out;
		PrintStream stdout=System.out;
		try 
		{
			out = new PrintStream(new FileOutputStream("edgewtoutput.txt"));
			System.setOut(out);
			Iterator<Entry<String,Integer>> SV=s.subjverb.entrySet().iterator();
			System.out.println("\n======================================SUBJECT-VERB EDGE==============================================\n");
			System.out.println("\tWeight\t\t\tConnection name\n");

			while(SV.hasNext())
			{
				Entry<String,Integer> SVentry=SV.next();
				String[] key = SVentry.getKey().split("-");
				Connection tempsub=t.getsubcol().getNodeCollection().get(key[0]).getConnectionMap().get(SVentry.getKey());
				System.out.println("\t"+(tempsub.getweight()+SVentry.getValue())+"\t\t\t"+SVentry.getKey());
				tempsub.setweight(tempsub.getweight()+SVentry.getValue());
			}
			Iterator<Entry<String,Integer>> VO=s.verbobj.entrySet().iterator();
			System.out.println("\n======================================VERB-OBJECT EDGE==============================================\n");
			System.out.println("\tWeight\t\t\tConnection name\n");

			while(VO.hasNext())
			{
				Entry<String,Integer> VOentry=VO.next();
				String[] key = VOentry.getKey().split("-");
				Connection tempverb = t.getverbcol().getNodeCollection().get(key[0]).getConnectionMap().get(VOentry.getKey());
				System.out.println("\t"+(tempverb.getweight()+VOentry.getValue())+"\t\t\t"+VOentry.getKey());
				tempverb.setweight(tempverb.getweight()+VOentry.getValue());

			}
			Iterator<Entry<String,Integer>> SVO=s.subjverbobj.entrySet().iterator();
			System.out.println("\n==================================SUBJECT-VERB-OBJECT EDGE==========================================\n");
			System.out.println("\tWeight\t\t\tConnection name\n");

			while(SVO.hasNext())
			{
				Entry<String,Integer> SVOentry=SVO.next();
				String[] key = SVOentry.getKey().split("-");
				Connection tempsub=t.getsubcol().getNodeCollection().get(key[0]).getConnectionMap().get(key[0]+"-"+key[1]);
				tempsub.setweight(tempsub.getweight()+SVOentry.getValue());
				Connection tempverb = t.getverbcol().getNodeCollection().get(key[1]).getConnectionMap().get(key[1]+"-"+key[2]);
				System.out.println("\t"+(tempsub.getweight()+SVOentry.getValue())+"\t\t\t"+key[0]+"-"+key[1]);
				System.out.println("\t"+(tempverb.getweight()+SVOentry.getValue())+"\t\t\t"+key[1]+"-"+key[2]);
				System.out.println("\t...........................................");
				tempverb.setweight(tempverb.getweight()+SVOentry.getValue());

			}
			System.setOut(stdout);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	public void assignEdgeweight(TriPartiteGraph t,HypGen h)
	{
		PrintStream out;
		PrintStream stdout=System.out;
		try 
		{
			out = new PrintStream(new FileOutputStream("edgewtoutput.txt"));
			System.setOut(out);
			System.out.println("\n======================================SUBJECT-VERB EDGE==============================================\n");
			System.out.println("\tWeight\t\t\tConnection name\n");
			Iterator<Entry<String,Double>> S = 	h.subj.entrySet().iterator();
			while(S.hasNext())
			{
				Entry<String,Double> sub = S.next();
				Iterator<Entry<String,Double>> V =h.verb.entrySet().iterator();
				while(V.hasNext())
				{
					Entry<String,Double> verb= V.next();
					t.getsubcol().getNodeCollection().get(sub.getKey()).getConnectionMap().get(sub.getKey()+"-"+verb.getKey()).setweight((int)(sub.getValue()*verb.getValue()));
					System.out.println("\t"+(int)(sub.getValue()*verb.getValue())+"\t\t\t"+(sub.getKey()+"-"+verb.getKey()));
				}
			}
			System.out.println("\n======================================VERB-OBJECT EDGE==============================================\n");
			System.out.println("\tWeight\t\t\tConnection name\n");
			Iterator<Entry<String,Double>> V = 	h.verb.entrySet().iterator();
			while(V.hasNext())
			{
				Entry<String,Double> verb = V.next();
				Iterator<Entry<String,Double>> O =h.obj.entrySet().iterator();
				while(O.hasNext())
				{
					Entry<String,Double> obj= O.next();
					t.getverbcol().getNodeCollection().get(verb.getKey()).getConnectionMap().get(verb.getKey()+"-"+obj.getKey()).setweight((int)(verb.getValue()*obj.getValue()));
					System.out.println("\t"+(int)(verb.getValue()*obj.getValue())+"\t\t\t"+(verb.getKey()+"-"+obj.getKey()));
				}
			}
			System.setOut(stdout);
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
}
