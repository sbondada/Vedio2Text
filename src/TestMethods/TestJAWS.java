package TestMethods;

import edu.smu.tspell.wordnet.*;
import edu.smu.tspell.wordnet.SynsetType;

/**
 * Displays word forms and definitions for synsets containing the word form
 * specified on the command line. To use this application, specify the word
 * form that you wish to view synsets for, as in the following example which
 * displays all synsets containing the word form "airplane":
 * <br>
 * java TestJAWS airplane
 */
public class TestJAWS
{
	/**
	 * Main entry point. The command-line arguments are concatenated together
	 * (separated by spaces) and used as the word form to look up.
	 */
	public static void main(String[] args)
	{
		System.setProperty("wordnet.database.dir", "/home/kaushal/Dropbox/github/vedio2test_package/Topdown_model/WordNet-3.0/dict/");
		if (args.length > 0)
		{
			//  Concatenate the command-line arguments
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < args.length; i++)
			{
				buffer.append((i > 0 ? " " : "") + args[i]);
			}
			String wordForm = buffer.toString();
			//  Get the synsets containing the word form
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			//the way to get the baseforms(a way for stemming)
			Synset[] synsets = database.getSynsets(wordForm,SynsetType.NOUN,true);
			//  Display the word forms and definitions for synsets retrieved
			
			NounSynset nounSynset; 
			NounSynset[] hyponyms; 
			NounSynset[] hypernyms;
			
			if (synsets.length > 0)
			{
				for (int i = 0; i < synsets.length; i++)
				{
					nounSynset=(NounSynset)(synsets[i]);
					hyponyms=nounSynset.getInstanceHyponyms();
					hypernyms=nounSynset.getInstanceHypernyms();
					System.out.println("the hyponyms are as follow");
					for(int i1=0;i1<hyponyms.length;i1++)
					{
						System.out.println(hyponyms[i1]);
					}
					System.out.println(" ");
					System.out.println("the hypernyms are as follow");
					for(int i1=0;i1<hypernyms.length;i1++)
					{
						System.out.println(hypernyms[i1]);
					}
					System.out.println(" ");
				}
			}
			else
			{
				System.err.println("No synsets exist that contain " +
						"the word form '" + wordForm + "'");
			}
		}
		else
		{
			System.err.println("You must specify " +
					"a word form for which to retrieve synsets.");
		}
	}

}