import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Bacon {

    public static void main(String[] args) {
	// TODO Auto-generated method stub
	Map<String, List> aTM = new HashMap(); // Map with key as String actors to value as List of movies
	Map<String, List> mTA = new HashMap(); // Map with key as String movie to value as List of actors
	Scanner s = null;
	String center;
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
	    if (!actor.contains("(")) {
		actor = actor + "(I)";
	    }
	    String movie = data[1];
	    if (aTM.containsKey(actor)) {
		List<String> currentList = mTA.get(actor);
		currentList.add(movie);
		aTM.put(actor, currentList);
	    } else {
		movies = new ArrayList<String>();
		movies.add(movie);
		aTM.put(actor, movies);
	    }
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
	System.out.println("Enter a command: ");
	Scanner commands = new Scanner(System.in);
	while (commands.hasNext()) {
	String input = commands.next();
	if (input.equals("help")) {
	    help(); 
	} else if (input.contains("find")) {
	    String name = input.substring(input.indexOf("<")+1, input.indexOf(">"));
	    find(name);
	} else if (input.contains("recenter")) {
	    String name = input.substring(input.indexOf("<")+1, input.indexOf(">"));
	    recenter(name);
	} else if (input.equals("avgdist")) {
	    avgdist();
	} else if (input.equals("centers")) {
	    centers();
	} else if (input.contains("topcenter <n> ")) {
	    int n = Integer.parseInt(input.substring(input.indexOf("<")+1, input.indexOf(">")));
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
	// TODO Auto-generated method stub

    }

    private static void find(String name) {
	// TODO Auto-generated method stub

    }

    private static void help() {
	// TODO Auto-generated method stub

    }
}