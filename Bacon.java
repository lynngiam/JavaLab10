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
			// Changed next() to nextLine().
			String input = commands.nextLine();
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
	String[] aTMKeys = (String[]) aTM.keySet().toArray();
	TreeMap<Double, String> results = new TreeMap<Double, String>();
	String previousCenter = center;
	for (int i = 0; i < aTMKeys.length; i++) {
	    center = aTMKeys[i];
	    double currentAvgDist = avgdist();
	    results.put(currentAvgDist, center);
	    }
	center = previousCenter;
	for (int j = 0; j<n; j++) {
	    Entry<Double, String> currentEntry = results.pollLastEntry();
	    System.out.println(""+(j+1)+". "+currentEntry.getValue()+"\t"+currentEntry.getKey());
	    }

	}


    private static void circles() {
	// TODO Auto-generated method stub
	Set<String> aTMKeys = aTM.keySet();
	TreeMap<Integer, List<String>> results = new TreeMap<Integer, List<String>>();
	aTMKeys.remove(center);
	Iterator<String> it = aTMKeys.iterator();
	while (it.hasNext()){
	    String currentActor = it.next();
	    int currentBN = find(currentActor);
	    if(!results.containsKey(currentBN)) {
		List<String> value = new ArrayList<String>();
		value.add(currentActor);
		results.put(currentBN, value);
	    }
	    else {
		results.get(currentBN).add(currentActor);
	    }
	for (int j = 0; j<10; j++) {
	    Entry<Integer, List<String>> currentEntry = results.pollFirstEntry();
	    List<String> truncatedValue = currentEntry.getValue().subList(0,2);
	    System.out.print(""+currentEntry.getKey()+"\t"+currentEntry.getValue().size()+"\t"+truncatedValue.toString());
	    }

	}

    }

    private static double avgdist() {
	// TODO Auto-generated method stub
	int reachable = 0;
	int unreachable = 0;
	String[] aTMKeys = (String[]) aTM.keySet().toArray();
	Set<String> aTMKeysSet = aTM.keySet();
	aTMKeysSet.remove(center);
	Iterator<String> it = aTMKeysSet.iterator();
	int TotalBN = 0;
	while (it.hasNext()) {
	    // bacon number needed
	    int currentBN = find(it.next());
	    if (currentBN == -1) { // test if target is reachable from center
		unreachable++;
	    } else {
		reachable++;
		TotalBN += currentBN;
	    }

	}
	double avgdist = TotalBN / reachable;
	System.out.println(""+avgdist+"\t"+center+" ("+reachable+","+unreachable+")");
	return avgdist;

    }
	
	private static void recenter(String name) {
		center = name;
		visited.clear();
		predecessor.clear();
	}

	private static int find(String name) {
		// SUGGESTION: factor this check out for private method?
		if (!aTM.containsKey(name)) {
			System.out.println(name + " is not in the database.");
			return -1;
		}
		int bacon = -1;
		String target = center;
		Queue<String> allEdgesTo = new LinkedList<String>();
		allEdgesTo.add(center);

		while (!target.equals(name) && !allEdgesTo.isEmpty()) {
			target = allEdgesTo.remove();

			// Mark the node visited.
			visited.add(target);
			List<String> currentEdgesTo;

			// Get the appropriate list of vertices connected to target
			if ((currentEdgesTo = aTM.get(target)) == null)
				currentEdgesTo = mTA.get(target);

			// For each unvisited connected vertex, update predecessor mark add to queue.
			for (String vertex : currentEdgesTo) {
				if (!visited.contains(vertex)) {
					predecessor.put(vertex, target);
					allEdgesTo.add(vertex);
				}

			}
		}

		if (visited.contains(name)) {
			String node = name;
			int count = 0;
			while (!node.equals(center)) {
				System.out.print(node + " -> ");
				node = predecessor.get(node);
				count++;
			}

			bacon = count / 2;
			System.out.println(node + " (" + bacon + ") ");
		} else {
			System.out.println(name + " is unreachable.");
		}

		// Returns the default -1 if connection not found.
		return bacon;
	}

	private static void help() {
		System.out.println("Available commands include: ");
		System.out.println("   1. help");
		System.out.println("   2. find <name>");
		System.out.println("   3. recenter <name>");
		System.out.println("   4. avgdist");
		System.out.println("   5. centers");
		System.out.println("   6. topcenter <n>");
		System.out.print("Enter a command: ");

	}
}
