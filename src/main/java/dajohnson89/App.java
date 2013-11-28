package dajohnson89;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Main application class for the roaming-math challenge
 */
public class App
{
    private static URL startURL;
    private static long startID;

    public static void main(String[] args) {
        init();

        Roamer roamer = new Roamer();
        Graph graph = roamer.explore(startURL);

        Solution solution = getSolution(graph);
        serializeAndWriteToDisk(solution);
    }

    private static Solution getSolution(Graph graph) {
        Long goalID = graph.getGoalPage().getNumericValue();
        int nodeCount = graph.getPageSet().size();
        List<Long> shortestPath = MathUtils.calculateShortestPath(graph, startID, goalID);
        int directedCycleCount = MathUtils.countCycles(graph);

        return new Solution(goalID, nodeCount, shortestPath, directedCycleCount);
    }

    //Encode the solution into JSON and write to disk for submission
    private static void serializeAndWriteToDisk(Solution solution) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String solutionGson = gson.toJson(solution);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("solution.json");
            writer.print(solutionGson);
        } catch (IOException e) {
            System.out.println("Encountered problem writing JSON solution to local disk");
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
            writer.close();
            }
        }
    }

    //set various fields safely
    private static void init() {
        //change this value to use a different point of entry into the graph
        startID = 70786l;
        try {
            startURL = new URL(Roamer.BASE_URL + '/' + startID);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error Creating URL from supplied String",e);
        }
    }

    //POJO for representing the solution
    private static class Solution {
        private long goal;
        private int node_count;
        private List<Long> shortest_path;
        private int directed_cycle_count;

        Solution(long goal, int node_count, List<Long> shortest_path, int directed_cycle_count) {
            this.goal = goal;
            this.node_count = node_count;
            this.shortest_path = shortest_path;
            this.directed_cycle_count = directed_cycle_count;
        }
    }
}
