package TestMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;


public class TestComparator {
	
	Comparator<Entry<String,Double>> comparator;
	
	public TestComparator()
	{
		comparator = new Comparator<Entry<String,Double>>() 
				{
			public int compare(Entry<String, Double> arg0,Entry<String, Double> arg1) 
			{
				return (int) ((arg1.getValue()-arg0.getValue())*100000); //multiplied with a large value so that there would be less loss while casting to int
			}
				};
	}
	
	public  void test()
	{
		ArrayList<Entry<String,Double>> smatchlist=new ArrayList<Entry<String,Double>>();

		MEntry<String, Double> maxsimsub= new MEntry<>();
		maxsimsub.setKey("hi");
		maxsimsub.setValue(3.98872);
		smatchlist.add(maxsimsub);
		MEntry<String, Double> maxsimsub1= new MEntry<>();
		maxsimsub1.setKey("hello");
		maxsimsub1.setValue(3.98874);
		smatchlist.add(maxsimsub1);
		
		Collections.sort(smatchlist,comparator);
		
		for(int i=0;i<smatchlist.size();i++)
		{
			System.out.println("ss  "+smatchlist.get(i).getKey());
		}
	}
	public static void main(String Args[])
	{
	TestComparator t = new TestComparator();
	t.test();
	}
}

@SuppressWarnings("hiding")
class MEntry<String, Double> implements Entry<String,Double> 
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