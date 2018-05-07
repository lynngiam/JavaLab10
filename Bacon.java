import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Bacon {
	private static Map<String, List<String>> aTM = new HashMap<String, List<String>>(); // Map with key as String actors
																						// to value as List of
	// movies
	private static Map<String, List<String>> mTA = new HashMap<String, List<String>>(); // Map with key as String movie
																						// to value as List of
	// actors
	private static Set<String> visited = new HashSet<String>(); // Set of whether a string has been visited in a breadth
																// first search
	private static Map<String, String> predecessor = new HashMap();

	private static String center;

	public static void main(String[] args) {
		// @@@@ Added generic type <String> to Lists.
		Scanner s = null;
		if (args[0].contains("http:")) {
			try {
				s = new Scanner(new URL(args[0]).openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); // change error message?
			}
		} else {
			File file = new File(args[0]);
			try {
				s = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println(file + " is not found.");
			}
		}
		// This generates the aTM and mTA maps.
		List<String> actors;
		List<String> movies;
		while (s.hasNextLine()) {
			String[] data = s.nextLine().split("\\|");
			String actor = data[0];
			// ???? Why do we need this?
			if (!actor.contains("(")) {
				actor = actor + "(I)";
			}
			String movie = data[1];
			if (aTM.containsKey(actor)) {
				// ???? Line below should be aTM.get(actor)?
				List<String> currentList = mTA.get(actor);
				currentList.add(movie);
				aTM.put(actor, currentList);
			} else {
				movies = new ArrayList<String>();
				movies.add(movie);
				aTM.put(actor, movies);
			}
			// ???? Line below should be mTA.get(movie)?
			if (mTA.containsKey(actor)) {
				List<String> currentList = mTA.get(movie);
				currentList.add(actor);
				mTA.put(movie, currentList);
			} else {
				actors = new ArrayList<String>();
				actors.add(actor);
				mTA.put(movie, actors);
			}
		}
		// Determining center
		if (args[1] != "") {
			center = args[1];
		} else {
			center = "Kevin Bacon (I)";
		}
		// Additional commands prompts
		System.out.println("Available commands include: ");
		System.out.println("   1. help");
		System.out.println("   2. find <name>");
		System.out.println("   3. recenter <name>");
		System.out.println("   4. avgdist");
		System.out.println("   5. centers");
		System.out.println("   6. topcenter <n>");
		System.out.print("Enter a command: ");
		Scanner commands = new Scanner(System.in);
		while (commands.hasNext()) {
			String input = commands.next();
			if (input.equals("help")) {
				help();
			} else if (input.contains("find")) {
				String name = input.substring(input.indexOf("<") + 1, input.indexOf(">"));
				find(name);
			} else if (input.contains("recenter")) {
				String name = input.substring(input.indexOf("<") + 1, input.indexOf(">"));
				recenter(name);
			} else if (input.equals("avgdist")) {
				avgdist();
			} else if (input.equals("centers")) {
				centers();
			} else if (input.contains("topcenter <n> ")) {
				int n = Integer.parseInt(input.substring(input.indexOf("<") + 1, input.indexOf(">")));
				topcenter(n);
			} else if (input.equals("")) {
				System.out.println("Thank you for using our Kevin Bacon program!");
				System.exit(0);

			} else {
				System.out.println("Please reenter a command: ");
			}
		}
	}

	private static void topcenter(int n) {
		// TODO Auto-generated method stub

	}

	private static void centers() {
		// TODO Auto-generated method stub

	}

	private static void avgdist() {
		// TODO Auto-generated method stub

	}

	private static void recenter(String name) {
		center = name;

	}

	private static void find(String name) {
		boolean isActor = true;
		String target = null;
		Queue<String> allEdgesTo = new LinkedList<String>();
		allEdgesTo.add(name);

		while (target != center && !allEdgesTo.isEmpty()) {
			target = allEdgesTo.remove();
			if (!visited.contains(target)) {

				List<String> currentEdgesTo = new ArrayList<String>();

				// Get the appropriate list of vertices connected to target
				if (isActor)
					currentEdgesTo = aTM.get(target);
				else
					currentEdgesTo = mTA.get(target);
				isActor = !isActor;

				for (String vertex : currentEdgesTo) {
					allEdgesTo.add(vertex);
				}
			}
		}
	}

	private static findHelper(String target) {
		
	}

	private static void help() {
		System.out.println("Available commands include: ");
		System.out.println("   1. help");
		System.out.println("   2. find <name>");
		System.out.println("   3. recenter <name>");
		System.out.println("   4. avgdist");
		System.out.println("   5. centers");List
		System.out.println("   6. topcenter <n>");
		System.out.print("Enter a command: ");

	}
}
