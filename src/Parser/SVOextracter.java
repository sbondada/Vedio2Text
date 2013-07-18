package Parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class SVOextracter 
{
	  HashMap<String,Integer> subjverb;
	  HashMap<String,Integer> verbobj;
	  HashMap<String,Integer> subjverbobj;
	  
	  public SVOextracter()
	  {
		  subjverb=new HashMap<>();
		  verbobj=new HashMap<>();
		  subjverbobj=new HashMap<>();
	  }
	  public static void main(String[] args) 
	  {
		  LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
		  SVOextracter s=new SVOextracter();
		  s.sentenceSplit(lp, args[0]);
//		  just for the purpose of viewing output
		  Iterator<String> SVOitr= s.subjverbobj.keySet().iterator();
		  while(SVOitr.hasNext())
		  {	
			  System.out.println("SVO triplet "+ SVOitr.next().toString());
		  }
		
	  }

	  public void sentenceSplit(LexicalizedParser lp, String filename) 
	  {
		  TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		  GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		  for (List<HasWord> sentence : new DocumentPreprocessor(filename)) 
		  {
			  Tree parse = lp.apply(sentence);
			  GrammaticalStructure gs = gsf.newGrammaticalStructure(parse); 
			  extractSVO(gs);
		  }
	  	}
	  public void extractSVO(GrammaticalStructure gs)
	  {
		  HashMap<String,String> SV =new HashMap<>();
		  HashMap<String,String> VO =new HashMap<>();
		  
//		  System.out.println("Total Dependencies  "+gs.typedDependencies());
//		  System.out.println("Relation  "+gs.typedDependencies().iterator().next());
//		  System.out.println("Governor  "+gs.typedDependencies().iterator().next().gov());
//		  System.out.println("Dependent  "+gs.typedDependencies().iterator().next().dep());
//		  System.out.println("Dependency type  "+gs.typedDependencies().iterator().next().reln());
		  Iterator<TypedDependency> Depitr = gs.typedDependencies().iterator();
		  while (Depitr.hasNext())
		  {
			 TypedDependency Dep = Depitr.next();
			 if(Dep.reln().getShortName().equals("nsubj"))
			 {
				 String[] subject=Dep.dep().toString().split("-");
				 String[] verb=Dep.gov().toString().split("-");
				 String key=subject[0]+"-"+verb[0];
				 SV.put(Dep.dep().toString(),Dep.gov().toString());
				 if(subjverb.containsKey(key))
				 {
					 subjverb.put(key,subjverb.get(key)+1);
				 }
				 else
				 {
					 subjverb.put(key,1);
				 }		
				 System.out.println("subjverb "+key);
			 }
			 if(Dep.reln().getShortName().equals("dobj"))
			 {
			 	String[] object=Dep.dep().toString().split("-");
			 	String[] verb=Dep.gov().toString().split("-");
			 	String key=verb[0]+"-"+object[0];
			 	VO.put(Dep.gov().toString(),Dep.dep().toString());
				 if(verbobj.containsKey(key))
				 {
					 verbobj.put(key,verbobj.get(key)+1);
				 }
				 else
				 {
					 verbobj.put(key,1);
				 }
				 System.out.println("verbonj "+key);
			 }
			 
		  }
		  Iterator<String> SVitr=SV.keySet().iterator();
		  while(SVitr.hasNext())
		  {
			  String s=SVitr.next();
			  String v=SV.get(s);
			  if (VO.containsKey(v))
			  {
				  String[] sparts=s.split("-");
				  String[] vparts=v.split("-");
				  if(subjverb.get(sparts[0]+"-"+vparts[0])-1==0)
				  {
				  subjverb.remove(sparts[0]+"-"+vparts[0]) ;
				  }
				  else
				  {
				  subjverb.put(sparts[0]+"-"+vparts[0],subjverb.get(sparts[0]+"-"+vparts[0])-1);
				  }
				  String[] oparts=VO.get(v).split("-");
				  if(verbobj.get(vparts[0]+"-"+oparts[0])-1==0)
				  {
					  verbobj.remove(vparts[0]+"-"+oparts[0]) ;
				  }
				  else
				  {
					  verbobj.put(vparts[0]+"-"+oparts[0],verbobj.get(vparts[0]+"-"+oparts[0])-1);
				  }
				  String SVOkey=sparts[0]+"-"+vparts[0]+"-"+oparts[0];
				  if(subjverbobj.containsKey(SVOkey))
				  {
					  subjverbobj.put(SVOkey,subjverbobj.get(SVOkey)+1);
				  }
				  else
				  {
					  subjverbobj.put(SVOkey,1);
				  }
			  }
		  }
	}
}
