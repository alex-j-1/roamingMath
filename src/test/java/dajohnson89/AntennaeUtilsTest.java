package dajohnson89;

import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

@Test
public class AntennaeUtilsTest {
    private String pageExpression;
    private long expectedEvaluation;
    /*
    @BeforeMethod
    private void setUp() throws Exception {

    }
*/
    @Test
    public void testEvaluateExpression() throws Exception {
        pageExpression = "abs(add(multiply(-1,abs(26881)),add(multiply(-1,43945),40)))";
        expectedEvaluation = 70786;

        long actualEvaluation = AntennaeUtils.evaluateExpression(pageExpression);

        assertThat(actualEvaluation).isEqualTo(expectedEvaluation);

    }
}
