package Parser;

import java.util.Iterator;
import java.util.Map.Entry;

import GraphStructure.TriPartiteGraph;

public class EdgeWeightGen 
{
public void assignEdgeweight(TriPartiteGraph t,SynsetGen s)
{
	Iterator<Entry<String,Integer>> SV=s.subjverb.entrySet().iterator();
	while(SV.hasNext())
	{
		Entry<String,Integer> SVentry=SV.next();
		String[] key = SVentry.getKey().split("-");
		t.getsubcol().getNodeCollection().get(key[0]).getConnectionMap().get(SVentry.getKey()).setweight(SVentry.getValue());
	}
	Iterator<Entry<String,Integer>> VO=s.verbobj.entrySet().iterator();
	while(VO.hasNext())
	{
		Entry<String,Integer> VOentry=VO.next();
		String[] key = VOentry.getKey().split("-");
		t.getverbcol().getNodeCollection().get(key[0]).getConnectionMap().get(VOentry.getKey()).setweight(VOentry.getValue());
	}
}
}
