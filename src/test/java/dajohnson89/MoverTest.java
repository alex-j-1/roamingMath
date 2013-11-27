/*
package dajohnson89;

import org.testng.annotations.Test;

import java.net.URL;
import java.util.List;

@Test
public class MoverTest {


    @Test
    public void debugMover() throws Exception {
        Mover m = new Mover();
        URL starter = new URL("http://www.crunchyroll.com/tech-challenge/roaming-math/dajohnson89@gmail.com/70786");
        Graph graph =  m.explore(starter);
        System.out.println("Let's look at the graph.");
        Page goalPage = graph.getGoalPage();
        List<Long> shortestPath = AntennaeUtils.calculateShortestPath(graph, 70786l, goalPage.getNumericValue());
        System.out.println("Shortest path from start to finish: " + shortestPath);
        System.out.println("Cycles count: "+ m.getCycleCount());
    }
*/
/*
    private Graph<Page, Link> expectedAcyclicGraph;
    private Graph<Page, Link> expectedCyclicGrpah;


    @BeforeClass
    public void setUp() throws Exception {
        Page centerPage = new Page(0l);
        Set<Page> pageSet = new HashSet<>(5);
        pageSet.add(centerPage);

        //create the fringe nodes and link them to the center node
        List<Link> linkList = new ArrayList<>(4);
        for (long i = 0; i < 4; i++) {
            pageSet.add(new Page(i+1));
            centerPage.getOutgoingList().add(new Link(0l, i+1));

        }

        //todo[dj]: Hacky. It just so happens i can do this.
        expectedAcyclicGraph = new Graph<>(centerPage.getOutgoingList(), pageSet);
    }

    @Test
    public void testMoverCreatesStarGraph() throws Exception {
        Mover mockedMover = mock(Mover.class);
        when(mockedMover.

    }
    *//*


}
*/
