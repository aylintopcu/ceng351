import java.util.ArrayList;

public class CengHashRow {

	public CengBucket bucket;
	public boolean visited;
	// GUI-Based Methods
	// These methods are required by GUI to work properly.

	public CengHashRow()
	{
		this.bucket = new CengBucket();
		this.visited = false;
	}

	public CengHashRow(int local_depth)
	{
		this.bucket = new CengBucket(local_depth);
		this.visited = false;
	}
	
	public String hashPrefix()
	{
		// TODO: Return row's hash prefix (such as 0, 01, 010, ...)

		return " ";
	}
	
	public CengBucket getBucket()
	{
		// TODO: Return the bucket that the row points at.
		return bucket;
	}
	
	public boolean isVisited()
	{
		// TODO: Return whether the row is used while searching.
		return visited;
	}
	
	// Own Methods
}
