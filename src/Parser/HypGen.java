package Parser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import GraphStructure.TriPartiteGraph;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class HypGen {

	LinkedHashMap<String,Double> subj;
	LinkedHashMap<String,Double> verb;
	LinkedHashMap<String,Double> obj;

	public HypGen()
	{
		System.setProperty("wordnet.database.dir", "/home/kaushal/Dropbox/github/vedio2test_package/Topdown_model/WordNet-3.0/dict/");
		this.subj = new LinkedHashMap<>();
		this.verb=new LinkedHashMap<>();
		this.obj=new LinkedHashMap<>();
	}
	public static void main(String args[])
	{
		// the function yet to be completed
		HypGen h = new HypGen();
		Iterator<Entry<String,Double>> subitr=h.subj.entrySet().iterator();
		while(subitr.hasNext())
		{
			Entry<String,Double> sub=subitr.next();
			System.out.println(sub.getKey()+"----"+sub.getValue());
		}
		Iterator<Entry<String,Double>> verbitr=h.verb.entrySet().iterator();
		while(verbitr.hasNext())
		{
			Entry<String,Double> verb=verbitr.next();
			System.out.println(verb.getKey()+"----"+verb.getValue());
		}
	}

	public void getCollectionSimilarity(SVOextracter s,TriPartiteGraph t)
	{
		Iterator<Entry<String,Integer>> SV=s.subjverb.entrySet().iterator();
		Iterator<Entry<String,Integer>> VO=s.verbobj.entrySet().iterator();
		Iterator<Entry<String,Integer>> SVO=s.subjverbobj.entrySet().iterator();
		while(SV.hasNext())
		{
			Entry<String,Integer> suve=SV.next();
			String[] svstr=suve.getKey().split("-");
			LinkedHashMap<String,Integer>  shyparray=findwhat(svstr[0],SynsetType.NOUN,t,1);
			resetColSim(svstr[0], shyparray, t, 1,suve.getValue());
			LinkedHashMap<String,Integer>  vhyparray=findwhat(svstr[1],SynsetType.VERB,t,2);
			resetColSim(svstr[1], vhyparray, t, 2,suve.getValue());
		}
		while(VO.hasNext())
		{
			Entry<String,Integer> veobj=VO.next();
			String[] vostr=veobj.getKey().split("-");
			LinkedHashMap<String,Integer>  vhyparray=findwhat(vostr[0],SynsetType.VERB,t,2);
			resetColSim(vostr[0], vhyparray, t, 2,veobj.getValue());
			LinkedHashMap<String,Integer>  ohyparray=findwhat(vostr[1],SynsetType.NOUN,t,3);
			resetColSim(vostr[1], ohyparray, t, 3,veobj.getValue());
		}
		while(SVO.hasNext())
		{
			Entry<String,Integer> suveobj=SVO.next();
			String[] svostr=suveobj.getKey().split("-");
			LinkedHashMap<String,Integer>  shyparray=findwhat(svostr[0],SynsetType.NOUN,t,1);
			resetColSim(svostr[0], shyparray, t, 1,suveobj.getValue());
			LinkedHashMap<String,Integer>  vhyparray=findwhat(svostr[1],SynsetType.VERB,t,2);
			resetColSim(svostr[1], vhyparray, t, 2,suveobj.getValue());
			LinkedHashMap<String,Integer>  ohyparray=findwhat(svostr[2],SynsetType.NOUN,t,3);
			resetColSim(svostr[2], ohyparray, t, 3,suveobj.getValue());
		}
	}

	public LinkedHashMap<String,Integer>  findwhat(String word,SynsetType type,TriPartiteGraph t,int set)
	{
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets(word,type,true);
		LinkedHashMap<String,Integer> whatlist= new LinkedHashMap<>();
		Synset[] hypernyms;
		Synset[] hyponyms;
		if (synsets.length > 0)
		{
			for (int i = 0; i < synsets.length; i++)
			{
				if(set==1 || set==3)
				{
					NounSynset nounSynset; 
					nounSynset=(NounSynset)(synsets[i]);
					hypernyms=nounSynset.getHypernyms();
					hyponyms=nounSynset.getHyponyms();
				}
				else
				{
					VerbSynset verbSynset; 
					verbSynset=(VerbSynset)(synsets[i]);
					hypernyms=verbSynset.getHypernyms();
					hyponyms=new NounSynset[0];
				}
					
				for(int i1 =0;i1<hypernyms.length;i1++)
				{
					String[] hypnyms=hypernyms[i1].getWordForms();
					for(int i2=0;i2<hypnyms.length;i2++)
					{
						switch (set)
						{
						case 1:
							if(t.getsubcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],1);
							}
							break;
						case 2:
							if(t.getverbcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],1);
							}
							break;
						case 3:
							if(t.getobjcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],1);
							}
							break;
						default: break;
						}
					}
				}
				for(int i1 =0;i1<hyponyms.length;i1++)
				{
					String[] hypnyms=hyponyms[i1].getWordForms();
					for(int i2=0;i2<hypnyms.length;i2++)
					{
						switch (set)
						{
						case 1:
							if(t.getsubcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],2);
							}
							break;
						case 2:
							if(t.getverbcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],2);
							}
							break;
						case 3:
							if(t.getobjcol().getNodeCollection().containsKey(hypnyms[i2]))
							{
								whatlist.put(hypnyms[i2],2);
							}
							break;
						default: break;
						}
					}
				}

			}
		}
		return whatlist;
	}

	public void resetColSim(String word,LinkedHashMap<String,Integer> whatlist,TriPartiteGraph t,int set,int count)
	{
		switch (set)
		{
		case 1:
			Iterator<String> subcolitr=t.getsubcol().getNodeCollection().keySet().iterator();
			while(subcolitr.hasNext())
			{
				String colnode=subcolitr.next();
				if(this.subj.containsKey(colnode))
				{
				this.subj.put(colnode,(this.subj.get(colnode)+calculatescore(word, colnode, whatlist,count)));
				}
				else
				{
				this.subj.put(colnode,calculatescore(word, colnode, whatlist,count));
				}
			}
			break;
		case 2:
			Iterator<String> verbcolitr=t.getverbcol().getNodeCollection().keySet().iterator();
			while(verbcolitr.hasNext())
			{
				String colnode=verbcolitr.next();
				if(this.verb.containsKey(colnode))
				{
				this.verb.put(colnode,(this.verb.get(colnode)+calculatescore(word, colnode, whatlist,count)));
				}
				else
				{
				this.verb.put(colnode,calculatescore(word, colnode, whatlist,count));
				}
			}
			break;
		case 3:
			Iterator<String> objcolitr=t.getobjcol().getNodeCollection().keySet().iterator();
			while(objcolitr.hasNext())
			{
				String colnode=objcolitr.next();
				if(this.obj.containsKey(colnode))
				{
				this.obj.put(colnode,(this.obj.get(colnode)+calculatescore(word, colnode, whatlist,count)));
				}
				else
				{
				this.obj.put(colnode,calculatescore(word, colnode, whatlist,count));
				}
			}
			break;
		default: break;
		}
	}

	public Double calculatescore(String word,String colnode,LinkedHashMap<String,Integer> whatlist,int count)
	{
		Double bias=0.4;
		if(whatlist.containsKey(colnode))
		{
			Double cumsim;
			if(whatlist.get(colnode)==1)
			{
				cumsim=(getSimilarityMeasure(word,colnode)-bias);
				return (count*cumsim);
			}
			else
			{
				cumsim=(getSimilarityMeasure(word,colnode)+bias);
				return (count*cumsim);
			}
		}
		else
		{
			return (count*getSimilarityMeasure(word, colnode));
		}
		
	}
	
	public double getSimilarityMeasure(String word1,String word2)
	{
		ILexicalDatabase db = new NictWordNet();
		RelatednessCalculator rc = new WuPalmer(db);
		//		RelatednessCalculator rc =new JiangConrath(db);
		//		RelatednessCalculator rc = new LeacockChodorow(db);
		//		RelatednessCalculator rc = new Path(db);
		WS4JConfiguration.getInstance().setMFS(true);
		double s = rc.calcRelatednessOfWords(word1, word2);
		if(s>=1.7976931348623157E308) //if the word exactly matches the other word then reduce the similarity value to a bearable value
		{
			s=5.0;
		}
		return s;
	}
}
