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
				System.out.print("\t"+(tempsub.getweight()+SVOentry.getValue())+"\t\t\t"+key[0]+"-"+key[1]);
				System.out.println("\t"+(tempverb.getweight()+SVOentry.getValue())+"\t\t\t"+key[1]+"-"+key[2]);
				System.out.println("...........................................");
				tempverb.setweight(tempverb.getweight()+SVOentry.getValue());

			}
			System.setOut(stdout);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
