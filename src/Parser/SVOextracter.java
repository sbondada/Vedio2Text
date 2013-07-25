package Parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Morphology;
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
		  //  just for the purpose of viewing output
		  //  responsible for viewing the output of SVO triplets
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
		  Morphology m=new Morphology();
		  
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
				 //we split the dependent and governor because it is of form "skipped-1" and so
				 String[] subject=Dep.dep().toString().split("-");
				 String[] verb=Dep.gov().toString().split("-");
				 //we try to convert the word to lower case and stem the word using the morphology class of Stanford parser
				 String key=m.stem(subject[0].toLowerCase())+"-"+m.stem(verb[0].toLowerCase());
				 /*we use an another hash map for fast finding of SVO triplets by  using two hash maps subject to verb and verb to object.we use these lists to 
				  *identify the SVO triplets by hashing from subject to verb and verb to object*/
				 SV.put(Dep.dep().toString(),Dep.gov().toString());
				 if(subjverb.containsKey(key))
				 {
					 subjverb.put(key,subjverb.get(key)+1);
				 }
				 else
				 {
					 subjverb.put(key,1);
				 }		
				 // responsible for the output of the subjverbs
//				 System.out.println("subjverb "+key);
			 }
			 if(Dep.reln().getShortName().equals("dobj"))
			 {
			 	String[] object=Dep.dep().toString().split("-");
			 	String[] verb=Dep.gov().toString().split("-");
			 	String key=m.stem(verb[0].toLowerCase())+"-"+m.stem(object[0].toLowerCase());
			 	VO.put(Dep.gov().toString(),Dep.dep().toString());
				 if(verbobj.containsKey(key))
				 {
					 verbobj.put(key,verbobj.get(key)+1);
				 }
				 else
				 {
					 verbobj.put(key,1);
				 }
				 // responsible for the output for verbobj
//				 System.out.println("verbonj "+key);
			 }
			 
		  }
		  Iterator<String> SVitr=SV.keySet().iterator();
		  while(SVitr.hasNext())
		  {
			  String s=SVitr.next();
			  //from the subject to verb hash map we try to get the verb of the subject
			  String v=SV.get(s);
			  // we match the verb to the verb to object hashmap.to find if there exist any case with same verb
			  if (VO.containsKey(v))
			  {
				  // we need to split because the subjects and verbs have their word order number still to it "helping-5" and so on
				  String[] sparts=s.split("-");
				  String[] vparts=v.split("-");
				  String subject=m.stem(sparts[0].toLowerCase());
				  String verb=m.stem(vparts[0].toLowerCase());
				  if(subjverb.get(subject+"-"+verb)-1==0)
				  {
				  subjverb.remove(subject+"-"+verb) ;
				  }
				  else
				  {
				  subjverb.put(subject+"-"+verb,subjverb.get(subject+"-"+verb)-1);
				  }
				  String[] oparts=VO.get(v).split("-");
				  String object=m.stem(oparts[0].toLowerCase());
				  if(verbobj.get(verb+"-"+object)-1==0)
				  {
					  verbobj.remove(verb+"-"+object) ;
				  }
				  else
				  {
					  verbobj.put(verb+"-"+object,verbobj.get(verb+"-"+object)-1);
				  }
				  String SVOkey=subject+"-"+verb+"-"+object;
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
