package ImageNets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class ImageNetDownload 
{
	public static void main(String[] args)
	{
			File file = new File("/home/kaushal/Dropbox/workspace/Vedio2Text/src/ImageNets/synset_list.txt");
	        BufferedReader reader = null;
	        HashMap<String,Integer> synset_list = new HashMap<>();
	        try 
	        {
	            reader = new BufferedReader(new FileReader(file));
	            String text = null;
	            // repeat until all lines is read
	            while ((text = reader.readLine()) != null) 
	            {
	            	synset_list.put(text,1);

	            }
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 
	        finally 
	        {
	            try 
	            {
	                if (reader != null) 
	                {
	                    reader.close();
	                }
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }

			System.setProperty("wordnet.database.dir", "/home/kaushal/Dropbox/github/vedio2test_package/Topdown_model/WordNet-3.0/dict/");
		
			String wordForm;
			//  Get the synsets containing the word form

			File file1 = new File("/home/kaushal/Dropbox/workspace/Vedio2Text/src/ImageNets/all_object.txt");
			BufferedReader reader1 = null;
	        try 
	        {
	            reader1 = new BufferedReader(new FileReader(file1));
	            String text = null;
	            // repeat until all lines is read
	            while ((text = reader1.readLine()) != null) 
	            {
	            		wordForm=text;
						WordNetDatabase database = WordNetDatabase.getFileInstance();
						//the way to get the baseforms(a way for stemming)
						Synset[] synsets = database.getSynsets(wordForm,SynsetType.NOUN,true);
						//  Display the word forms and definitions for synsets retrieved	
						String wnid= null;
						System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
						if (synsets.length > 0)
						{
							for (int i = 0; i < synsets.length; i++)
							{
								System.out.println(synsets[i].toString());
								String[] tempwords = synsets[i].toString().split("\\[");
//								System.out.println(tempwords[0]);
								String[] tempwidno = tempwords[0].split("@");
								if(tempwidno[1].toCharArray().length==7)
								{
									wnid="n0"+tempwidno[1];
//									System.out.println(wnid);
								}
								if(tempwidno[1].toCharArray().length==8)
								{
									wnid="n"+tempwidno[1];
//									System.out.println(wnid);
								}
								if(synset_list.containsKey(wnid))
								{
									System.out.println();
									System.out.println("file downloading "+wordForm+"_"+wnid+".tar");
									System.out.println();
									try 
									{
										URL website = new URL("http://image-net.org/download/synset?wnid="+wnid+"&username=kaushalb09&accesskey=7f6aadde78e78f1300e5996d257175feeaf1e7f8&release=latest&src=stanford");
										ReadableByteChannel rbc = Channels.newChannel(website.openStream());
										FileOutputStream fos = new FileOutputStream("/home/kaushal/Documents/Vedio2Text/imagenet_data/"+wordForm+"_"+wnid+".tar");
										fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
									} 
									catch (MalformedURLException e) 
									{
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
	            } 
	        }
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 
	        finally 
	        {
	            try 
	            {
	                if (reader1 != null) 
	                {
	                    reader1.close();
	                }
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	}
}
