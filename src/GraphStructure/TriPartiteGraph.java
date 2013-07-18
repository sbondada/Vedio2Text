package GraphStructure;

public class TriPartiteGraph {
	
	SubjectCollection subcol;
	VerbCollection verbcol;
	ObjectCollection objcol;	
	
	public TriPartiteGraph() 
	{
	this.subcol=new SubjectCollection();
	this.verbcol=new VerbCollection();
	this.objcol=new  ObjectCollection();
	}

}
