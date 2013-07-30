package Parser;

// the modifications which can be done are instead of using comparator and sorting the list, you can use the there itself check and keep the track of the maximum similar word.
// add the subject ,verb pair or verb,object pair alone from the SVO triplets even if u are not able to find all three together for the first method.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import GraphStructure.TriPartiteGraph;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.process.Morphology;

public class SynsetGen 
{
	LinkedHashMap<String,Integer> subjverb;
	LinkedHashMap<String,Integer> verbobj;
	LinkedHashMap<String,Integer> subjverbobj;
	Comparator<Entry<String,Double>> comparator;

	public SynsetGen()
	{
		System.setProperty("wordnet.database.dir", "/home/kaushal/Dropbox/github/vedio2test_package/Topdown_model/WordNet-3.0/dict/");
		this.subjverb = new LinkedHashMap<>();
		this.verbobj=new LinkedHashMap<>();
		this.subjverbobj=new LinkedHashMap<>();
		comparator = new Comparator<Entry<String,Double>>() 
				{
			public int compare(Entry<String, Double> arg0,Entry<String, Double> arg1) 
			{
				return (int) ((arg1.getValue()-arg0.getValue())*10000); //multiplied with a large value so that there would be less loss while casting to int
			}
				};
	}

	public static void main(String Args[])
	{
		// main purpose is to test.

		SynsetGen g=new SynsetGen();
		ArrayList<Entry<String,Double>> sarray=g.getValuesSatisfyT("welfare",SynsetType.NOUN);
		int i=0;
		while (i<sarray.size())
		{
			System.out.println(sarray.get(i).getKey()+"  " +sarray.get(i).getValue());
			System.out.println(g.getSimilarityMeasure("person",sarray.get(i).getKey()));
			i=i+1;
		}
	}
	public void getSynset(SVOextracter s,TriPartiteGraph g,int method)
	{
		Iterator<Entry<String,Integer>> SV=s.subjverb.entrySet().iterator();
		Iterator<Entry<String,Integer>> VO=s.verbobj.entrySet().iterator();
		Iterator<Entry<String,Integer>> SVO=s.subjverbobj.entrySet().iterator();
		switch (method)
		{

		case 1:
			Morphology m=new Morphology();
			
			while(SV.hasNext())
			{
				int subjmatchind=-1,verbmatchind=-1;
				Entry<String,Integer> suve=SV.next();
				String[] svstr=suve.getKey().split("-");
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svstr[0],SynsetType.NOUN);
				if(sarray!=null)
				{
					for(int i=0;i<sarray.size();i++)
					{
						//this matches the list of subject collection to the synset and if matches the flag is raised.
						String key=m.stem(sarray.get(i).getKey().toLowerCase());
						if(g.getsubcol().getNodeCollection().containsKey(key))
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
						//this matches the list of verb collection to the synset and if matches the flag is raised.
						String key=m.stem(varray.get(i).getKey().toLowerCase());
						if(g.getverbcol().getNodeCollection().containsKey(key))
						{
							verbmatchind=i;
							break;
						}
					}
				}
				if(subjmatchind!=-1 && verbmatchind!=-1)
				{
					String key=sarray.get(subjmatchind).getKey()+"-"+varray.get(verbmatchind).getKey();
					// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(vostr[0],SynsetType.VERB);
				if(varray!=null)
				{
					for(int i=0;i<varray.size();i++)
					{
						//this matches the list of verb collection to the synset and if matches the flag is raised.
						String key=m.stem(varray.get(i).getKey().toLowerCase());
						if(g.getverbcol().getNodeCollection().containsKey(key))
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
						//this matches the list of objects collection to the synset and if matches the flag is raised.
						String key=m.stem(oarray.get(i).getKey().toLowerCase());
						if(g.getobjcol().getNodeCollection().containsKey(key))
						{
							objmatchind=i;
							break;
						}
					}
				}
				if(verbmatchind!=-1 && objmatchind!=-1)
				{
					String key=varray.get(verbmatchind).getKey()+"-"+oarray.get(objmatchind).getKey();
					// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svostr[0],SynsetType.NOUN);
				if(sarray!=null)
				{
					for(int i=0;i<sarray.size();i++)
					{
						//this matches the list of subject collection to the synset and if matches the flag is raised.
						String key=m.stem(sarray.get(i).getKey().toLowerCase());
						if(g.getsubcol().getNodeCollection().containsKey(key))
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
						//this matches the list of verb collection to the synset and if matches the flag is raised.
						String key=m.stem(varray.get(i).getKey().toLowerCase());
						if(g.getverbcol().getNodeCollection().containsKey(key))
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
						//this matches the list of objects collection to the synset and if matches the flag is raised.
						String key=m.stem(oarray.get(i).getKey().toLowerCase());
						if(g.getobjcol().getNodeCollection().containsKey(key))
						{
							objmatchind=i;
							break;
						}
					}
				}
				if(subjmatchind!=-1 && verbmatchind!=-1 && objmatchind!=-1)
				{
					String key=sarray.get(subjmatchind).getKey()+"-"+varray.get(verbmatchind).getKey()+"-"+oarray.get(objmatchind).getKey();
					// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
			break;

		case 2:

			double matchtreshold=0.5;
			Iterator<String> subcol;
			Iterator<String> verbcol;
			Iterator<String> objcol;
			while(SV.hasNext())
			{
				MyEntry<String, Double> maxsimsub= new MyEntry<>();


				Entry<String,Integer> suve=SV.next();
				String[] svstr=suve.getKey().split("-");
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svstr[0],SynsetType.NOUN); 
				// this has the list of matched words for each synset word and the whole graph list with the highest similarity measure
				ArrayList<Entry<String,Double>> smatchlist=new ArrayList<Entry<String,Double>>();
				if(sarray!=null)
				{
					for(int i=0;i<sarray.size();i++)
					{
						maxsimsub.setKey(" ");
						maxsimsub.setValue(0.0);
						subcol=g.getsubcol().getNodeCollection().keySet().iterator();
						while(subcol.hasNext())
						{
							String subcolvalue=subcol.next();
							Double simval=getSimilarityMeasure(sarray.get(i).getKey(),subcolvalue);
							if(maxsimsub.getValue()<simval)
							{
								maxsimsub.setKey(subcolvalue);
								maxsimsub.setValue(simval);
							}	
						}
						if(maxsimsub.getValue()!=0.0)
						{
							smatchlist.add(maxsimsub);
						}
					}
				}
				Collections.sort(smatchlist,comparator);
				
				ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(svstr[1],SynsetType.VERB);
				ArrayList<Entry<String,Double>> vmatchlist=new ArrayList<Entry<String,Double>>();

				MyEntry<String, Double> maxsimverb= new MyEntry<>();
				if(varray!=null)
				{
					for(int i=0;i<varray.size();i++)
					{
						maxsimverb.setKey(" ");
						maxsimverb.setValue(0.0);
						verbcol=g.getverbcol().getNodeCollection().keySet().iterator();
						while(verbcol.hasNext())
						{
							String verbcolvalue=verbcol.next();
							Double simval=getSimilarityMeasure(varray.get(i).getKey(),verbcolvalue);
							if(maxsimverb.getValue()<simval)
							{
								maxsimverb.setKey(verbcolvalue);
								maxsimverb.setValue(simval);
							}	
						}
						if(maxsimverb.getValue()!=0.0)
						{
							vmatchlist.add(maxsimverb);
						}
					}
				}
				Collections.sort(vmatchlist,comparator);
				
				if(smatchlist.size()>0 && vmatchlist.size()>0)
				{
					if(smatchlist.get(0).getValue() >= matchtreshold && vmatchlist.get(0).getValue() >= matchtreshold)
					{
						String key=smatchlist.get(0).getKey()+"-"+vmatchlist.get(0).getKey();
						// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
			}
			while(VO.hasNext())
			{
				MyEntry<String, Double> maxsimverb= new MyEntry<>();


				Entry<String,Integer> veobj=VO.next();
				String[] vostr=veobj.getKey().split("-");
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(vostr[0],SynsetType.VERB); 
				// this has the list of matched words for each synset word and the whole graph list with the highest similarity measure
				ArrayList<Entry<String,Double>> vmatchlist=new ArrayList<Entry<String,Double>>();
				if(varray!=null)
				{
					for(int i=0;i<varray.size();i++)
					{
						maxsimverb.setKey(" ");
						maxsimverb.setValue(0.0);
						verbcol=g.getverbcol().getNodeCollection().keySet().iterator();
						while(verbcol.hasNext())
						{
							String verbcolvalue=verbcol.next();
							Double simval=getSimilarityMeasure(varray.get(i).getKey(),verbcolvalue);
							if(maxsimverb.getValue()<simval)
							{
								maxsimverb.setKey(verbcolvalue);
								maxsimverb.setValue(simval);
							}	
						}
						if(maxsimverb.getValue()!=0.0)
						{
							vmatchlist.add(maxsimverb);
						}
					}
				}
				Collections.sort(vmatchlist,comparator);

				MyEntry<String, Double> maxsimobj= new MyEntry<>();

				ArrayList<Entry<String,Double>> oarray=getValuesSatisfyT(vostr[1],SynsetType.NOUN);
				ArrayList<Entry<String,Double>> omatchlist=new ArrayList<Entry<String,Double>>();

				if(oarray!=null)
				{
					for(int i=0;i<oarray.size();i++)
					{
						maxsimobj.setKey(" ");
						maxsimobj.setValue(0.0);
						objcol=g.getobjcol().getNodeCollection().keySet().iterator();
						while(objcol.hasNext())
						{
							String objcolvalue=objcol.next();
							Double simval=getSimilarityMeasure(oarray.get(i).getKey(),objcolvalue);
							if(maxsimobj.getValue()<simval)
							{
								maxsimobj.setKey(objcolvalue);
								maxsimobj.setValue(simval);
							}	
						}
						if(maxsimobj.getValue()!=0.0)
						{
							omatchlist.add(maxsimobj);
						}
					}
				}
				Collections.sort(omatchlist,comparator);

				if(vmatchlist.size()>0 && omatchlist.size()>0)
				{
					if(vmatchlist.get(0).getValue() >= matchtreshold && omatchlist.get(0).getValue() >= matchtreshold)
					{
						String key=vmatchlist.get(0).getKey()+"-"+omatchlist.get(0).getKey();
						// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
			}

			while(SVO.hasNext())
			{
				MyEntry<String, Double> maxsimsub= new MyEntry<>();


				Entry<String,Integer> suveobj=SVO.next();
				String[] svostr=suveobj.getKey().split("-");
				//this contains all the list of similar words with the similarity greater than the threshold
				ArrayList<Entry<String,Double>> sarray=getValuesSatisfyT(svostr[0],SynsetType.NOUN); 
				// this has the list of matched words for each synset word and the whole graph list with the highest similarity measure
				ArrayList<Entry<String,Double>> smatchlist=new ArrayList<Entry<String,Double>>();
				if(sarray!=null)
				{
					for(int i=0;i<sarray.size();i++)
					{
						maxsimsub.setKey(" ");
						maxsimsub.setValue(0.0);
						subcol=g.getsubcol().getNodeCollection().keySet().iterator();

						while(subcol.hasNext())
						{
							String subcolvalue=subcol.next();
							Double simval=getSimilarityMeasure(sarray.get(i).getKey(),subcolvalue);
							if(maxsimsub.getValue()<simval)
							{
								maxsimsub.setKey(subcolvalue);
								maxsimsub.setValue(simval);
							}	
						}
						if(maxsimsub.getValue()!=0.0)
						{
							smatchlist.add(maxsimsub);
						}
					}
				}
				Collections.sort(smatchlist,comparator);

				ArrayList<Entry<String,Double>> varray=getValuesSatisfyT(svostr[1],SynsetType.VERB);
				ArrayList<Entry<String,Double>> vmatchlist=new ArrayList<Entry<String,Double>>();

				MyEntry<String, Double> maxsimverb= new MyEntry<>();
				if(varray!=null)
				{
					for(int i=0;i<varray.size();i++)
					{
						maxsimverb.setKey(" ");
						maxsimverb.setValue(0.0);
						verbcol=g.getverbcol().getNodeCollection().keySet().iterator();

						while(verbcol.hasNext())
						{
							String verbcolvalue=verbcol.next();
							Double simval=getSimilarityMeasure(varray.get(i).getKey(),verbcolvalue);
							if(maxsimverb.getValue()<simval)
							{
								maxsimverb.setKey(verbcolvalue);
								maxsimverb.setValue(simval);
							}	
						}
						if(maxsimverb.getValue()!=0.0)
						{
							vmatchlist.add(maxsimverb);
						}
					}
				}
				Collections.sort(vmatchlist,comparator);

				MyEntry<String, Double> maxsimobj= new MyEntry<>();

				ArrayList<Entry<String,Double>> oarray=getValuesSatisfyT(svostr[2],SynsetType.NOUN);
				ArrayList<Entry<String,Double>> omatchlist=new ArrayList<Entry<String,Double>>();

				if(oarray!=null)
				{
					for(int i=0;i<oarray.size();i++)
					{
						maxsimobj.setKey(" ");
						maxsimobj.setValue(0.0);
						objcol=g.getobjcol().getNodeCollection().keySet().iterator();

						while(objcol.hasNext())
						{
							String objcolvalue=objcol.next();
							Double simval=getSimilarityMeasure(oarray.get(i).getKey(),objcolvalue);
							if(maxsimobj.getValue()<simval)
							{
								maxsimobj.setKey(objcolvalue);
								maxsimobj.setValue(simval);
							}	
						}
						if(maxsimobj.getValue()!=0.0)
						{
							omatchlist.add(maxsimobj);
						}
					}
				}
				Collections.sort(omatchlist,comparator);
				
				if(smatchlist.size()>0 && vmatchlist.size()>0 && omatchlist.size()>0)
				{
					if(smatchlist.get(0).getValue() >= matchtreshold && vmatchlist.get(0).getValue() >= matchtreshold &&  omatchlist.get(0).getValue() >= matchtreshold)
					{
						String key=smatchlist.get(0).getKey()+"-"+vmatchlist.get(0).getKey()+"-"+omatchlist.get(0).getKey();
						// this comparision is to find if there exist the same pair which has been already matched by some other synset combination.
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
			break;
			
			default: break;
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
			double threshold=0.5; // the threshold limit which must satisfy to be in the list of similar words
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
//			RelatednessCalculator rc =new JiangConrath(db);
//			RelatednessCalculator rc = new LeacockChodorow(db);
//			RelatednessCalculator rc = new Path(db);
			WS4JConfiguration.getInstance().setMFS(true);
			double s = rc.calcRelatednessOfWords(word1, word2);
			return s;
		}

	}


	class MyEntry<String, Double> implements Entry<String,Double> 
	{
		private String key;
		private Double value;

		public void setKey(String key)
		{
			this.key=key;
		}
		public Double setValue(Double value) 
		{
			this.value=value;
			return value;
		}
		public Double getValue() 
		{
			return this.value;
		}
		public String getKey() 
		{
			return this.key;
		}
	};