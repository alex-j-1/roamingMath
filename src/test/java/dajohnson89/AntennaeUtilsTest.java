package dajohnson89;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

@Test
public class AntennaeUtilsTest {
    private String pageExpression;
    private long expectedEvaluation;
    private LinkedList<Long> expectedShortestPath;
    /*
    @BeforeMethod
    private void setUp() throws Exception {

    }
*/

    @BeforeClass
    public void setUp() throws Exception {
        pageExpression = "abs(add(multiply(-1,abs(26881)),add(multiply(-1,43945),40)))";
        expectedEvaluation = 70786;

        expectedShortestPath = new LinkedList<>();
        expectedShortestPath.add(1l);
        expectedShortestPath.add(4l);
        expectedShortestPath.add(5l);
    }

    @Test
    public void testEvaluateExpression() throws Exception {
        long actualEvaluation = AntennaeUtils.evaluateExpression(pageExpression);
        assertThat(actualEvaluation).isEqualTo(expectedEvaluation);
    }

    @Test
    public void testCalculateShortestPath() throws Exception {
        Graph graph = createSampleGraph();
        LinkedList<Long> actualShortestPath;
        actualShortestPath = AntennaeUtils.calculateShortestPath(graph, 0l, 4l);
        assertThat(actualShortestPath).hasSize(expectedShortestPath.size());
        assertThat(actualShortestPath).isEqualTo(expectedShortestPath);
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
        for (Page page : pages) {
            pageSet.add(page);
        }

        Set<Link> linkSet = new HashSet<>(links.length);
        for (Link link : links) {
            linkSet.add(link);
        }
        return new Graph(linkSet, pageSet);
    }
}
