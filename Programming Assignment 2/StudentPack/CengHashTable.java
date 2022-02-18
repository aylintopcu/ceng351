import java.util.ArrayList;

public class CengHashTable {

	public int global_depth;
	public ArrayList<CengHashRow> hashTable;

	public CengHashTable()
	{
		// TODO: Create a hash table with only 1 row.
		this.hashTable = new ArrayList<CengHashRow>(1);
		this.global_depth = 0;
		CengHashRow row = new CengHashRow();
		this.hashTable.add(row);
	}

	public void deletePoke(Integer pokeKey)
	{
		// TODO: Empty Implementation
		int index = this.hashFunction(pokeKey);
		int empty_buckets = 0;

		CengBucket bucket = this.hashTable.get(index).getBucket();

		for (int i = 0; i < bucket.bucket.size(); i++)
		{
			if (bucket.bucket.get(i).pokeKey().equals(pokeKey))
			{
				bucket.bucket.remove(i);
				break;
			}
		}

		int i = 0;

		while (i < this.hashTable.size())
		{
			if (this.hashTable.get(i).bucket.bucket.size() == 0)
			{
				empty_buckets++;

				if (this.hashTable.get(i).bucket.local_depth != this.global_depth)
				{
					i += (int) Math.pow(2, this.global_depth - this.hashTable.get(i).bucket.local_depth);
				}
				else
				{
					i++;
				}
			}
			else
			{
				i++;
			}
		}

		System.out.println("\"delete\": {");
		System.out.println("\t\"emptyBucketNum\": " + empty_buckets);
		System.out.println("}");
	}

	public void addPoke(CengPoke poke)
	{			
		// TODO: Empty Implementation
		int index = this.hashFunction(poke.pokeKey());
		CengBucket bucket = this.hashTable.get(index).getBucket();

		bucket.bucket.add(poke);

		if (bucket.bucket.size() > CengPokeKeeper.getBucketSize())
		{
			if (bucket.local_depth == this.global_depth)
			{
				this.global_depth++;
				bucket.local_depth++;

				int size = this.hashTable.size();

				for (int i = 0; i < 2 * size; i += 2)
				{
					this.hashTable.add(i + 1, this.hashTable.get(i));
				}

				int new_index = this.hashFunction(poke.pokeKey());
				String newIndexHashPrefix = Integer.toBinaryString(new_index);
				bucket = this.hashTable.get(new_index).getBucket();

				while (newIndexHashPrefix.length() < this.global_depth)
				{
					newIndexHashPrefix = "0" + newIndexHashPrefix;
				}

				newIndexHashPrefix = newIndexHashPrefix.substring(0, bucket.local_depth);

				ArrayList<CengPoke> temp = new ArrayList<CengPoke>();

				for (int i = 0; i < bucket.bucket.size(); i++)
				{
					temp.add(bucket.bucket.get(i));
				}

				bucket.bucket.clear();

				CengHashRow new_row = new CengHashRow(bucket.local_depth);

				for (int i = 0; i < this.hashTable.size(); i++)
				{
					String hashPrefix = Integer.toBinaryString(i);

					while (hashPrefix.length() < this.global_depth)
					{
						hashPrefix = "0" + hashPrefix;
					}

					String indexString = hashPrefix.substring(0, bucket.local_depth);

					if (indexString.equals(newIndexHashPrefix))
					{
						this.hashTable.set(i, new_row);
					}
				}

				//this.hashTable.set(new_index, new_row);

				for (int i = 0; i < temp.size(); i++)
				{
					this.addPoke(temp.get(i));
				}
			}
			else if (bucket.local_depth < this.global_depth)
			{
				bucket.local_depth++;
				//String bitsInHash = Integer.toBinaryString(this.global_depth);
				CengHashRow new_row = new CengHashRow(bucket.local_depth);
				String indexHashPrefix = Integer.toBinaryString(index);

				while (indexHashPrefix.length() < this.global_depth)
				{
					indexHashPrefix = "0" + indexHashPrefix;
				}

				indexHashPrefix = indexHashPrefix.substring(0, bucket.local_depth);

				for (int i = 0; i < this.hashTable.size(); i++)
				{
					String hashPrefix = Integer.toBinaryString(i);

					while (hashPrefix.length() < this.global_depth)
					{
						hashPrefix = "0" + hashPrefix;
					}

					String indexString = hashPrefix.substring(0, bucket.local_depth);

					if (indexString.equals(indexHashPrefix))
					{
						this.hashTable.set(i, new_row);
					}
				}

				//this.hashTable.set(index, new_row);

				ArrayList<CengPoke> temp = new ArrayList<CengPoke>();

				for (int i = 0; i < bucket.bucket.size(); i++)
				{
					temp.add(bucket.bucket.get(i));
				}

				bucket.bucket.clear();

				for (int i = 0; i < temp.size(); i++)
				{
					this.addPoke(temp.get(i));
				}
			}
		}
	}
	
	public void searchPoke(Integer pokeKey)
	{
		// TODO: Empty Implementation
		ArrayList<CengHashRow> found = new ArrayList<CengHashRow>();
		ArrayList<String> hashPrefixes = new ArrayList<String>();
		int index = this.hashFunction(pokeKey);
		int table_size = this.hashTable.size();
		CengHashRow row = this.hashTable.get(index);

		for (int i = 0; i < table_size; i++)
		{
			if (this.hashTable.get(i).equals(row))
			{
				CengBucket bucket = this.hashTable.get(i).bucket;

				for (int j = 0; j < bucket.bucket.size(); j++)
				{
					if (bucket.bucket.get(j).pokeKey().equals(pokeKey))
					{
						found.add(this.hashTable.get(i));

						String binary = Integer.toBinaryString(i);

						if (this.global_depth == 0)
						{
							binary = "0";
						}
						else
						{
							while (binary.length() < this.global_depth)
							{
								binary = "0" + binary;
							}
						}
						hashPrefixes.add(binary);
					}
				}
			}
		}

		System.out.println("\"search\": {");

		for (int i = 0; i < found.size(); i++)
		{
			System.out.println("\t\"row\": {");
			System.out.println("\t\t\"hashPref\": " + hashPrefixes.get(i) + ",");

			CengBucket bucket = found.get(i).getBucket();
			int bucket_size = bucket.bucket.size();

			System.out.println("\t\t\"bucket\": {");
			System.out.println("\t\t\t\"hashLength\": " + bucket.local_depth + ",");
			System.out.println("\t\t\t\"pokes\": [");

			for (int j = 0; j < bucket_size; j++)
			{
				System.out.println("\t\t\t\t\"poke\": {");
				System.out.println("\t\t\t\t\t\"hash\": " + this.getHashValue(bucket.bucket.get(j).pokeKey()) + ",");
				System.out.println("\t\t\t\t\t\"pokeKey\": " + bucket.bucket.get(j).pokeKey() + ",");
				System.out.println("\t\t\t\t\t\"pokeName\": " + bucket.bucket.get(j).pokeName() + ",");
				System.out.println("\t\t\t\t\t\"pokePower\": " + bucket.bucket.get(j).pokePower() + ",");
				System.out.println("\t\t\t\t\t\"pokeType\": " + bucket.bucket.get(j).pokeType());

				if (j == bucket_size - 1)
				{
					System.out.println("\t\t\t\t}");
				}
				else
				{
					System.out.println("\t\t\t\t},");
				}

			}

			System.out.println("\t\t\t]");
			System.out.println("\t\t}");

			if (i == found.size() - 1)
			{
				System.out.println("\t}");
			}
			else
			{
				System.out.println("\t},");
			}

		}

		System.out.println("}");
	}
	
	public void print()
	{
		// TODO: Empty Implementation
		System.out.println("\"table\": {");

		int table_size = this.hashTable.size();

		for (int i = 0; i < table_size; i++)
		{
			String binary = Integer.toBinaryString(i);

			if (this.global_depth == 0)
			{
				binary = "0";
			}
			else
			{
				while (binary.length() < this.global_depth)
				{
					binary = "0" + binary;
				}
			}

			System.out.println("\t\"row\": {");
			System.out.println("\t\t\"hashPref\": " + binary + ",");

			CengBucket bucket = this.hashTable.get(i).getBucket();
			int bucket_size = bucket.bucket.size();

			System.out.println("\t\t\"bucket\": {");
			System.out.println("\t\t\t\"hashLength\": " + bucket.local_depth + ",");
			System.out.println("\t\t\t\"pokes\": [");

			for (int j = 0; j < bucket_size; j++)
			{
				System.out.println("\t\t\t\t\"poke\": {");
				System.out.println("\t\t\t\t\t\"hash\": " + this.getHashValue(bucket.bucket.get(j).pokeKey()) + ",");
				System.out.println("\t\t\t\t\t\"pokeKey\": " + bucket.bucket.get(j).pokeKey() + ",");
				System.out.println("\t\t\t\t\t\"pokeName\": " + bucket.bucket.get(j).pokeName() + ",");
				System.out.println("\t\t\t\t\t\"pokePower\": " + bucket.bucket.get(j).pokePower() + ",");
				System.out.println("\t\t\t\t\t\"pokeType\": " + bucket.bucket.get(j).pokeType());

				if (j == bucket_size - 1)
				{
					System.out.println("\t\t\t\t}");
				}
				else
				{
					System.out.println("\t\t\t\t},");
				}

			}

			System.out.println("\t\t\t]");
			System.out.println("\t\t}");

			if (i == table_size - 1)
			{
				System.out.println("\t}");
			}
			else
			{
				System.out.println("\t},");
			}

		}

		System.out.println("}");
	}

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	
	public int prefixBitCount()
	{
		// TODO: Return table's hash prefix length.
		return this.global_depth;
	}
	
	public int rowCount()
	{
		// TODO: Return the count of HashRows in table.
		return this.hashTable.size();
	}
	
	public CengHashRow rowAtIndex(int index)
	{
		// TODO: Return corresponding hashRow at index.
		return this.hashTable.get(index);
	}
	
	// Own Methods

	public int hashFunction(int pokeKey)
	{
		String binaryHashValue = getHashValue(pokeKey);
		String indexString = binaryHashValue.substring(0, this.global_depth);

		if (indexString.equals(""))
		{
			indexString = "0";
		}

		int index = Integer.parseInt(indexString,2);

		return index;
	}

	public String getHashValue(int pokeKey)
	{
		int hashValue = pokeKey % CengPokeKeeper.getHashMod();
		String binaryHashValue = Integer.toBinaryString(hashValue);
		String bitsInHash = Integer.toBinaryString(CengPokeKeeper.getHashMod());

		while (binaryHashValue.length() < (bitsInHash.length() - 1))
		{
			binaryHashValue = "0" + binaryHashValue;
		}

		return binaryHashValue;
	}
}
