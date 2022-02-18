import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CengPokeParser {

	public static ArrayList<CengPoke> parsePokeFile(String filename)
	{
		ArrayList<CengPoke> pokeList = new ArrayList<CengPoke>();

		// You need to parse the input file in order to use GUI tables.
		// TODO: Parse the input file, and convert them into CengPokes
		String curline;
		String[] parsed;

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			while ((curline = br.readLine()) != null) {
				parsed = curline.split("\\t");
				System.out.println(curline);
				pokeList.add(new CengPoke(Integer.parseInt(parsed[1]), parsed[2], parsed[3], parsed[4]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pokeList;
	}
	
	public static void startParsingCommandLine() throws IOException
	{
		// TODO: Start listening and parsing command line -System.in-.
		// There are 5 commands:
		// 1) quit : End the app. Print nothing, call nothing.
		// 2) add : Parse and create the poke, and call CengPokeKeeper.addPoke(newlyCreatedPoke).
		// 3) search : Parse the pokeKey, and call CengPokeKeeper.searchPoke(parsedKey).
		// 4) delete: Parse the pokeKey, and call CengPokeKeeper.removePoke(parsedKey).
		// 5) print : Print the whole hash table with the corresponding buckets, call CengPokeKeeper.printEverything().

		// Commands (quit, add, search, print) are case-insensitive.

		Scanner scanner = new Scanner(System.in);

		while (true) {
			String next_line = scanner.nextLine();
			String[] parsed = next_line.split("\\t");
			String command = parsed[0];
			command = command.toLowerCase();

			if (command.equals("quit")) {
				break;
			}
			else if (command.equals("add")) {
				CengPoke newPoke = new CengPoke(Integer.parseInt(parsed[1]), parsed[2], parsed[3], parsed[4]);
				CengPokeKeeper.addPoke(newPoke);
			}
			else if (command.equals("search")) {
				Integer parsedKey = Integer.parseInt(parsed[1]);
				CengPokeKeeper.searchPoke(parsedKey);
			}
			else if (command.equals("delete")) {
				Integer parsedKey = Integer.parseInt(parsed[1]);
				CengPokeKeeper.deletePoke(parsedKey);
			}
			else if (command.equals("print")) {
				CengPokeKeeper.printEverything();
			}
		}
	}
}
