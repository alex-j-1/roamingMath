package dajohnson89;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

@Test
public class MathUtilsTest {
    private String pageExpression;
    private long expectedEvaluation;
    private LinkedList<Long> expectedShortestPath;

    @BeforeClass
    public void setUp() throws Exception {
        pageExpression = "abs(add(multiply(-1,abs(26881)),add(multiply(-1,43945),40)))";
        expectedEvaluation = 70786;

        expectedShortestPath = new LinkedList<>();
        expectedShortestPath.add(0l);
        expectedShortestPath.add(3l);
        expectedShortestPath.add(4l);
    }

    @Test
    public void testEvaluateExpression() throws Exception {
        long actualEvaluation = MathUtils.evaluateExpression(pageExpression);
        assertThat(actualEvaluation).isEqualTo(expectedEvaluation);
    }

    @Test
    public void testCalculateShortestPath() throws Exception {
        Graph graph = createSampleGraph();
        LinkedList<Long> actualShortestPath;
        actualShortestPath = MathUtils.calculateShortestPath(graph, 0l, 4l);
        assertThat(actualShortestPath).hasSize(expectedShortestPath.size());
        assertThat(actualShortestPath).isEqualTo(expectedShortestPath);
    }

    @Test
    public void testCountAllCycles() throws Exception {
        Graph graph = createSampleGraph();

        /*let's add a cycle from 0 to 3 to 4 to 0.
        This actually makes the entire graph strongly connected.
        A better test case would be nice.
         */
        Page page0 = graph.getPageFromLong(0l);
        Page page4 = graph.getPageFromLong(4l);
        page4.getOutgoingList().add(new Link(page4.getNumericValue(), page0.getNumericValue()));
        graph.addEdge(new Link(page4.getNumericValue(), page0.getNumericValue()));

        int cycleCount = MathUtils.countCycles(graph);
        assertThat(cycleCount).isEqualTo(1);
    }

    private Graph createSampleGraph() {
        Page[] pages = {
                new Page(0l),
                new Page(1l),
                new Page(2l),
                new Page(3l),
                new Page(4l),
        };
        Link[] links = {
                new Link(0l, 1l),
                new Link(1l, 2l),
                new Link(2l, 4l),
                new Link(0l, 3l),
                new Link(3l, 4l)
        };

        pages[0].getOutgoingList().add(links[0]);
        pages[1].getOutgoingList().add(links[1]);
        pages[2].getOutgoingList().add(links[2]);
        pages[0].getOutgoingList().add(links[3]);
        pages[3].getOutgoingList().add(links[4]);

        Set<Page> pageSet = new HashSet<>(5);
        Collections.addAll(pageSet, pages);

        Set<Link> linkSet = new HashSet<>(links.length);
        Collections.addAll(linkSet, links);

        return new Graph(linkSet, pageSet);
    }
}
