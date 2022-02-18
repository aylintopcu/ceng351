import java.util.ArrayList;

public class CengBucket {

	public int local_depth;
	public ArrayList<CengPoke> bucket;


	public CengBucket()
	{
		this.local_depth = 0;
		this.bucket = new ArrayList<CengPoke>(1);
	}

	public CengBucket(int local_depth)
	{
		this.local_depth = local_depth;
		this.bucket = new ArrayList<CengPoke>(1);
	}

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	
	public int pokeCount()
	{
		// TODO: Return the pokemon count in the bucket.
		return this.bucket.size();
	}
	
	public CengPoke pokeAtIndex(int index)
	{
		// TODO: Return the corresponding pokemon at the index.
		return this.bucket.get(index);
	}
	
	public int getHashPrefix()
	{
		// TODO: Return hash prefix length.
		return this.local_depth;
	}
	
	public Boolean isVisited()
	{
		// TODO: Return whether the bucket is found while searching.
		return false;
	}
	
	// Own Methods
}
