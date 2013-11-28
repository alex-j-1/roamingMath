package dajohnson89;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Roamer {
    public static final String BASE_URL = "http://www.crunchyroll.com/tech-challenge/roaming-math/dajohnson89@gmail.com";
    private static final String GOAL = "GOAL";
    private static final String DEADEND = "DEADEND";

    private Graph graph;
    private final Set<Link> encounteredLinks = new HashSet<>();
    private final Set<Page> encounteredPages = new HashSet<>();

    /**
     * Create a graph from the specified entry point
     * @param url The starting point of the graph we want to explore
     * @return The traversed graph
     */
    public Graph explore(URL url) {
        traverse(url);
        setGraph(new Graph(encounteredLinks, encounteredPages));
        return getGraph();
    }

    /**
     * For internal use only. Traverse the graph and extract the raw data (vertices and edges)
     * @param url The starting point of the graph we want to explore
     */
    private void traverse(URL url) {
        String path = url.getPath();
        Long sourceID = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

        List<String> entries = getEntriesFromURL(url);

        //DEADENDs and GOAL deserve special treatment. Still, add them to the graph under construction.
        if (entries.contains(GOAL) || entries.contains(DEADEND)) {
            Page endPage = handleSpecialPage(entries, sourceID);
            encounteredPages.add(endPage);
        } else {
            //for non-special pages, recursively traverse every entry.
            Page page = new Page(sourceID);
            //create a new (directed) link between the source page and every node in the entry set.
            for (String entry : entries) {
                Long destinationID = MathUtils.evaluateExpression(entry);
                Link pageLink = new Link(sourceID, destinationID);
                page.getOutgoingList().add(pageLink);
                //skip already-visited pages.
                if (!encounteredLinks.add(pageLink)) {
                    System.out.println("Roamer encountered an already-visited node. Ignoring.");
                } else {
                    URL newURL = createURL(sourceID, destinationID);
                    //recursive step.
                    traverse(newURL);
                }
            }
            encounteredPages.add(page);
        }
    }

    private URL createURL(Long sourceID, Long destinationID) {
        URL newURL = null;
        String rawString = BASE_URL + '/' + destinationID;
        try {
            newURL = new URL(rawString);
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL. [sourceID, destinationID] = ["+ sourceID + " " + destinationID+']');
            e.printStackTrace();
        }
        return newURL;
    }

    /**
     * Visit the URL, and return a list of every line on the web page.
     * @param url
     * @return
     */
    private final List<String> getEntriesFromURL(URL url) {
        final List<String> entries = new ArrayList<>();
        //BufferedReader br = null;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())))  {
            String line;
            while ((line = br.readLine()) != null) {
                // Process each line.
                entries.add(line);
            }
        } catch(IOException e) {
            System.out.println("Error reading from URL: " + url.toString());
        }
        return entries;
    }

    /**
     * DEADENDs and GOAL get special treatment
     * @param entries The list of entries on the web page
     * @param id The ID of this particular page
     * @return
     */
    private final Page handleSpecialPage(List<String> entries, Long id) {
        if(entries.size() > 1) {
            throw new IllegalStateException("DEADENDs or GOALs should have only one entry!");
        }
        Page page = new Page(id);
        if(entries.contains(GOAL)) {
            System.out.println("Goal reached at #" + id);
            page.setIsGoal(true);
        } else {
            page.setIsDeadEnd(true);
        }
        return page;
    }

    /**
     * Set the graph for this Roamer.
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * @return The graph associated with this roamer.
     */
    public Graph getGraph() {
        return graph;
    }
}
