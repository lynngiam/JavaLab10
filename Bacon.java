import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Bacon {
	private static Map<String, List<String>> aTM = new HashMap<String, List<String>>(); // Map with key as String actors
	// to value as List of
	// movies
	private static Map<String, List<String>> mTA = new HashMap<String, List<String>>(); // Map with key as String movie
	// to value as List of
	// actors
	private static Map<String, String> predecessor = new HashMap<String, String>();

	private static String center = "";

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
			// if (!actor.contains("(")) {
			// actor = actor + "(I)";
			// }
			String movie = data[1];
			if (aTM.containsKey(actor)) {
				List<String> currentList = aTM.get(actor);
				currentList.add(movie);
				aTM.put(actor, currentList);
			} else {
				movies = new ArrayList<String>();
				movies.add(movie);
				aTM.put(actor, movies);
			}
			if (mTA.containsKey(movie)) {
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
		if (args.length > 1) {
			for (int i = 1; i < args.length - 1; i++) {
				center += args[i] + " ";
			}
			center += args[args.length - 1];
			if (!aTM.containsKey(center)) {
				System.err.println(center + " is not in this data set.");
				System.exit(1);
			}
		} else if (!aTM.containsKey("Kevin Bacon (I)")) {
			center = aTM.keySet().toArray(new String[aTM.keySet().size()])[0];
		} else {
			center = "Kevin Bacon (I)";
		}
		// Add the center to the first element of the queue in find.
		System.out.println("Center is: " + center);
		findAllPredecessors();
		// Additional commands prompts
		help();
		Scanner commands = new Scanner(System.in);
		while (commands.hasNextLine()) {
			// Changed next() to nextLine().
			String input = commands.nextLine();
			if (input.equals("help")) {
				help();
			} else if (input.contains("find")) {
				String name = input.replaceFirst("find ", "");
				find(name);
				System.out.print("Enter a command: ");
			} else if (input.contains("recenter")) {
				String name = input.replaceFirst("recenter ", "");
				recenter(name);
				System.out.print("Enter a command: ");
			} else if (input.equals("avgdist")) {
				avgdist();
				System.out.print("Enter a command: ");
			} else if (input.equals("circles")) {
				circles();
				System.out.print("Enter a command: ");
			} else if (input.contains("topcenter")) {
				int n = Integer.parseInt(input.replaceFirst("topcenter ", ""));
				topcenter(n);
				System.out.print("Enter a command: ");
			} else if (input.equals("") || input.equals("quit")) {
				System.out.println("Thank you for using our Kevin Bacon program!");
				System.exit(0);
			} else if (input.contains("topactors")) {
				int n = Integer.parseInt(input.replaceFirst("topactors ", ""));
				topActors(n);
				System.out.print("Enter a command: ");
			} else {
				System.out.print("Please reenter a command: ");
			}
		}
	}

	private static void findAllPredecessors() {

		/*
		 * Finds all the nodes connected to the center and add entries to the hashmap
		 * predecessor.
		 */

		Queue<String> allConnectedNodes = new LinkedList<String>();
		Set<String> visited = new HashSet<String>();

		allConnectedNodes.add(center);

		while (!allConnectedNodes.isEmpty()) {
			String currentNode = allConnectedNodes.remove();

			// Mark the node visited.
			List<String> currentEdgesTo;

			// Get the appropriate list of vertices connected to target
			if ((currentEdgesTo = aTM.get(currentNode)) == null)
				currentEdgesTo = mTA.get(currentNode);

			// For each unvisited connected vertex, update predecessor mark add to queue.
			for (String vertex : currentEdgesTo) {
				if (!visited.contains(vertex)) {
					visited.add(vertex);
					predecessor.put(vertex, currentNode);
					allConnectedNodes.add(vertex);
				}

			}
		}
	}

    private static void topcenter(int n) {
	// TODO Auto-generated method stub
	Set<String> aTMKeysSet = aTM.keySet();
	String[] aTMKeys = aTMKeysSet.toArray(new String[aTMKeysSet.size()]);
	TreeMap<Double, String> results = new TreeMap<Double, String>();
	String previousCenter = center;
	for (int i = 0; i < aTMKeys.length; i++) {
	    String currentCenter = aTMKeys[i];
	    if (findBacon(currentCenter) != -1) {
		recenter(currentCenter);
		double currentAvgDist = avgDistNoPrint();
		results.put(currentAvgDist, center);
		recenter(previousCenter);
	    }
	}
	for (int j = 0; j < n; j++) {
	    Entry<Double, String> currentEntry = results.pollFirstEntry();
	    System.out.printf("%d. %-20s %.16f \n", (j + 1), currentEntry.getValue(), currentEntry.getKey());
	    // System.out.println("" + (j + 1) + ". " + currentEntry.getValue() + "\t" +
	    // "\t" + currentEntry.getKey()); // do
	    // printf
	}

    }

    private static void circles() {
	// TODO Auto-generated method stub
	// this ranks actors based on their bacon number from the assigned center
	Set<String> aTMKeys = aTM.keySet();
	TreeMap<Integer, List<String>> results = new TreeMap<Integer, List<String>>(); // the keys are bacon number and
										       // the values are actors with
										       // that bacon number from the
										       // center
	Iterator<String> it = aTMKeys.iterator(); // iterator loops through the list of actors
	while (it.hasNext()) {
	    String currentActor = it.next();
	    int currentBN = findBacon(currentActor); // for each actor, find their bacon number
	    if (currentBN >= 0) {
		if (!results.containsKey(currentBN)) {
		    // currentBN >=0 is necessary to avoid unreachable actors
		    List<String> value = new ArrayList<String>();
		    value.add(currentActor);
		    results.put(currentBN, value);
		} else { // if the results already contains this bacon number
		    results.get(currentBN).add(currentActor);
		}
	    } else {
		continue;
	    }
	}
	while (!results.isEmpty()) {
	    Entry<Integer, List<String>> currentEntry = results.pollFirstEntry(); // get the lowest bacon number
	    List<String> value = currentEntry.getValue(); // this is a list of actors with this bacon number
	    System.out.print("" + currentEntry.getKey() + "\t" + value.size() + "\t");
	    if (value.size() > 2) { // print a maximum of 3 actors
		List<String> truncatedValue = value.subList(0, 3);
		System.out.println(truncatedValue.toString());
	    } else {
		System.out.println(value.toString());
	    }
	}

    }

    private static double avgDistNoPrint() {
	// TODO Auto-generated method stub
	int reachable = 0;
	int unreachable = 0;
	Set<String> aTMKeysSet = aTM.keySet();
	Iterator<String> it = aTMKeysSet.iterator();
	double TotalBN = 0;
	while (it.hasNext()) {
	    // bacon number needed
	    String actorName = it.next();
	    int currentBN = findBacon(actorName);
	    if (currentBN == -1) { // test if target is reachable from center
		unreachable++;
	    } else {
		reachable++;
		TotalBN += currentBN;
	    }

	}
	double avgdist = TotalBN / reachable;

	return avgdist;

    }

    private static double avgdist() {
	// TODO Auto-generated method stub
	int reachable = 0;
	int unreachable = 0;
	Set<String> aTMKeysSet = aTM.keySet();
	Iterator<String> it = aTMKeysSet.iterator();
	double TotalBN = 0;
	while (it.hasNext()) {
	    // bacon number needed
	    String actorName = it.next();
	    int currentBN = findBacon(actorName);
	    if (currentBN == -1) { // test if target is reachable from center
		unreachable++;
	    } else {
		reachable++;
		TotalBN += currentBN;
	    }

	}
	double avgdist = TotalBN / reachable;
	System.out.println("" + avgdist + "\t" + center + " (" + reachable + "," + unreachable + ")");
	return avgdist;

    }

    private static void recenter(String name) {
		center = name;
		predecessor.clear();
		findAllPredecessors();
	}

	private static int findBacon(String name) {

		// If name is the center, return 0.
		if (name.equals(center)) {
			return 0;
		}

		if (predecessor.containsKey(name)) {
			String node = name;
			int count = 0;
			while (!node.equals(center)) {
				node = predecessor.get(node);
				count++;
			}
			// assign bacon to the count.
			int bacon = count / 2;
			return bacon;
		}

		// Returns the default -1 if connection not found.
		return -1;
	}

	private static void find(String name) {
		// SUGGESTION: factor this check out for private method?
		if (!aTM.containsKey(name)) {
			System.out.println(name + " is not in the database.");
			return;
		}

		int bacon = findBacon(name);
		// If name is the center, return 0.
		if (bacon < 0) {
			System.out.println(name + " is unreachable.");
		} else if (bacon == 0) {
			System.out.println(name + " (0)");
		} else {
			String node = name;
			while (!node.equals(center)) {
				System.out.print(node + " -> ");
				node = predecessor.get(node);
			}
			System.out.println(node + " (" + bacon + ") ");
		}
	}

	private static void topActors(int n) {
		class ReverseEntryComparator implements Comparator<Entry<String, Integer>> {

			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				int val1 = o1.getValue();
				int val2 = o2.getValue();
				return val2 - val1;
			}

		}

		Set<Entry<String, List<String>>> actors = aTM.entrySet();
		PriorityQueue<Entry<String, Integer>> actorByMovie = new PriorityQueue<Entry<String, Integer>>(
				new ReverseEntryComparator());

		for (Entry<String, List<String>> actor : actors) {
			String name = actor.getKey();
			int moviesNo = actor.getValue().size();
			actorByMovie.add(new AbstractMap.SimpleEntry<String, Integer>(name, moviesNo));
		}

		for (int i = 0; i < n; i++) {
			Entry<String, Integer> top = actorByMovie.remove();
			System.out.printf("%-10s %d\n", top.getKey(), top.getValue());
		}
	}

	private static void help() {
		System.out.println("Available commands include: ");
		System.out.println("   1. help");
		System.out.println("   2. quit");
		System.out.println("   3. find <name>");
		System.out.println("   4. recenter <name>");
		System.out.println("   5. avgdist");
		System.out.println("   6. circles");
		System.out.println("   7. topcenter <n>");
		System.out.println("   8. topactors <n>");
		System.out.print("Enter a command: ");

	}
}
