package Parser;


	import java.util.Collection;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;

public class SVOextracter {
	  /**
	   * The main method demonstrates the easiest way to load a parser.
	   * Simply call loadModel and specify the path, which can either be a
	   * file or any resource in the classpath.  For example, this
	   * demonstrates loading from the models jar file, which you need to
	   * include in the classpath for ParserDemo to work.
	   */
	  public static void main(String[] args) {
	    LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	    if (args.length > 0) {
	      demoDP(lp, args[0]);
//	    } else {
//	      demoAPI(lp);
	    }
	  }

	  /**
	   * demoDP demonstrates turning a file into tokens and then parse
	   * trees.  Note that the trees are printed by calling pennPrint on
	   * the Tree object.  It is also possible to pass a PrintWriter to
	   * pennPrint if you want to capture the output.
	   */
	  public static void demoDP(LexicalizedParser lp, String filename) {
	    // This option shows loading and sentence-segmenting and tokenizing
	    // a file using DocumentPreprocessor.
	    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    // You could also create a tokenizer here (as below) and pass it
	    // to DocumentPreprocessor
	    for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
	      Tree parse = lp.apply(sentence);
	     // parse.pennPrint();
	      System.out.println();

	      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	      // the main purpose to get the dependencies in Collection
	      Collection tdl = gs.typedDependenciesCCprocessed();
	      System.out.println("Total dependencies  "+gs.dependencies());
	      System.out.println("Relation  "+gs.dependencies().iterator().next());
	      System.out.println("Governor  "+gs.dependencies().iterator().next().governor());
	      System.out.println("Dependent  "+gs.dependencies().iterator().next().dependent());
	      System.out.println("name  "+gs.dependencies().iterator().next().name());
	      
	      System.out.println("--------------------------------------------------------------------------------------------------------");
	      //this seems to work
	      System.out.println("Total Dependencies  "+gs.typedDependencies());
	      System.out.println("Relation  "+gs.typedDependencies().iterator().next());
	      System.out.println("Governor  "+gs.typedDependencies().iterator().next().gov());
	      System.out.println("Dependent  "+gs.typedDependencies().iterator().next().dep());
	      System.out.println("Dependency type  "+gs.typedDependencies().iterator().next().reln());
	      
	    }
	  }

}