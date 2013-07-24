package Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import GraphStructure.TriPartiteGraph;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class SynsetGen 
{
	HashMap<String,Integer> subjverb;
	HashMap<String,Integer> verbobj;
	HashMap<String,Integer> subjverbobj;

	public SynsetGen()
	{
		System.setProperty("wordnet.database.dir", "/home/kaushal/Dropbox/github/vedio2test_package/Topdown_model/WordNet-3.0/dict/");
		this.subjverb = new HashMap<>();
		this.verbobj=new HashMap<>();
		this.subjverbobj=new HashMap<>();
	}

	public static void main(String Args[])
	{
		// main purpose is to test.

		SynsetGen g=new SynsetGen();
		ArrayList<Entry<String,Double>> sarray=g.getValuesSatisfyT("person",SynsetType.NOUN);
		int i=0;
		while (i<sarray.size())
		{
			System.out.println(sarray.get(i).getKey());
			i=i+1;
		}
	}
	public void getSynset(SVOextracter s,TriPartiteGraph g)
	{
		Iterator<Entry<String,Integer>> SV=s.subjverb.entrySet().iterator();
		Iterator<Entry<String,Integer>> VO=s.verbobj.entrySet().iterator();
		Iterator<Entry<String,Integer>> SVO=s.subjverbobj.entrySet().iterator();
		while(SV.hasNext())
		{
			int subjmatchind=-1,verbmatchind=-1;
			Entry<String,Integer> suve=SV.next();
			String[] svstr=suve.getKey().split("-");
			ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svstr[0],SynsetType.NOUN);
			if(sarray!=null)
			{
				for(int i=0;i<sarray.size();i++)
				{
					if(g.getsubcol().getNodeCollection().containsKey(sarray.get(i)))
					{
						subjmatchind=i;
						break;
					}
				}
			}
			ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(svstr[1],SynsetType.VERB);
			if(varray!=null)
			{
				for(int i=0;i<varray.size();i++)
				{
					if(g.getverbcol().getNodeCollection().containsKey(varray.get(i)))
					{
						verbmatchind=i;
						break;
					}
				}
			}
			if(subjmatchind!=-1 && verbmatchind!=-1)
			{
				String key=sarray.get(subjmatchind).getKey()+"-"+varray.get(verbmatchind).getKey();
				if(subjverb.containsKey(key))
				{
					this.subjverb.put(key,this.subjverb.get(key)+suve.getValue());
				}
				else
				{
					this.subjverb.put(key, suve.getValue());
				}
			}
		}
		while(VO.hasNext())
		{
			int verbmatchind=-1,objmatchind=-1;
			Entry<String,Integer> veobj=VO.next();
			String[] vostr=veobj.getKey().split("-");
			ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(vostr[0],SynsetType.VERB);
			if(varray!=null)
			{
				for(int i=0;i<varray.size();i++)
				{
					if(g.getverbcol().getNodeCollection().containsKey(varray.get(i)))
					{
						verbmatchind=i;
						break;
					}
				}
			}
			ArrayList<Entry<String,Double>> oarray=getValuesSatisfyT(vostr[1],SynsetType.NOUN);
			if(oarray!=null)
			{
				for(int i=0;i<oarray.size();i++)
				{
					if(g.getobjcol().getNodeCollection().containsKey(oarray.get(i)))
					{
						objmatchind=i;
						break;
					}
				}
			}
			if(verbmatchind!=-1 && objmatchind!=-1)
			{
				String key=varray.get(verbmatchind).getKey()+"-"+oarray.get(objmatchind).getKey();
				if(verbobj.containsKey(key))
				{
					this.verbobj.put(key,this.verbobj.get(key)+veobj.getValue());
				}
				else
				{
					this.verbobj.put(key, veobj.getValue());
				}

			}
		}
		while(SVO.hasNext())
		{
			int subjmatchind=-1,verbmatchind=-1,objmatchind=-1;
			Entry<String,Integer> suveobj=SVO.next();
			String[] svostr=suveobj.getKey().split("-");
			ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svostr[0],SynsetType.NOUN);
			if(sarray!=null)
			{
				for(int i=0;i<sarray.size();i++)
				{
					if(g.getsubcol().getNodeCollection().containsKey(sarray.get(i)))
					{
						subjmatchind=i;
						break;
					}
				}
			}
			ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(svostr[1],SynsetType.VERB);
			if(varray!=null)
			{
				for(int i=0;i<varray.size();i++)
				{
					if(g.getverbcol().getNodeCollection().containsKey(varray.get(i)))
					{
						verbmatchind=i;
						break;
					}
				}
			}
			ArrayList<Entry<String,Double>> oarray=getValuesSatisfyT(svostr[2],SynsetType.NOUN);
			if(oarray!=null)
			{
				for(int i=0;i<oarray.size();i++)
				{
					if(g.getobjcol().getNodeCollection().containsKey(oarray.get(i)))
					{
						objmatchind=i;
						break;
					}
				}
			}
			if(subjmatchind!=-1 && verbmatchind!=-1 && objmatchind!=-1)
			{
				String key=sarray.get(subjmatchind).getKey()+"-"+varray.get(verbmatchind).getKey()+"-"+oarray.get(objmatchind).getKey();
				if(subjverbobj.containsKey(key))
				{
					this.subjverbobj.put(key,this.subjverbobj.get(key)+suveobj.getValue());
				}
				else
				{
					this.subjverbobj.put(key, suveobj.getValue());
				}			
			}
		}
	}

	public LinkedHashMap<String,Double> genSynset(String s,SynsetType type)
	{
		String word= s;
		String[] wordForms=null;
		LinkedHashMap<String,Double> wordMap=null;
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(word,type,true);
		if (synsets.length > 0)
		{
			wordForms =new  String[0];
			for (int i = 0; i < synsets.length; i++)
			{
				wordForms=Append(wordForms,synsets[i].getWordForms());
			}
		}
		if(wordForms!=null)
		{
			wordMap=removeDupGetSim(wordForms,s);
		}
		return wordMap;
	}

	public ArrayList<Entry<String,Double>> getValuesSatisfyT(String word,SynsetType type)
	{
		Comparator<Entry<String,Double>> comparator = new Comparator<Entry<String,Double>>() 
				{
			public int compare(Entry<String, Double> arg0,Entry<String, Double> arg1) 
			{
				return (int) ((arg1.getValue()-arg0.getValue())*10000); //multiplied with a large value so that there would be less loss while casting to int
			}
				};

				double threshold=0.5;
				ArrayList<Entry<String,Double>> ar=new ArrayList<>();
				LinkedHashMap<String,Double> similarset=genSynset(word,type);
				if(similarset!=null)
				{
					ar.addAll(similarset.entrySet());
					Collections.sort(ar,comparator);
					for(int i=ar.size()-1;i>0;i--)
					{
						double simval=ar.get(i).getValue();
						if(simval<threshold)
						{
							ar.remove(i);
						}
					}
					return ar;
				}
				return null;
	}
	private String[] Append(String[] A, String[] B) 
	{
		int aLen = A.length;
		int bLen = B.length;
		String[] C= new String[aLen+bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	private LinkedHashMap<String,Double> removeDupGetSim(String[] s,String word)
	{
		LinkedHashMap<String,Double> unqmap=new LinkedHashMap<>();


		for(int i=0;i<s.length;i++)
		{
			if(!unqmap.containsKey(s[i]))
			{
				unqmap.put(s[i],getSimilarityMeasure(word, s[i]));
			}
		}

		return unqmap;
	}

	public double getSimilarityMeasure(String word1,String word2)
	{
		ILexicalDatabase db = new NictWordNet();
		RelatednessCalculator rc = new WuPalmer(db);
		WS4JConfiguration.getInstance().setMFS(true);
		double s = rc.calcRelatednessOfWords(word1, word2);
		return s;
	}

}
