package example;

import static org.junit.jupiter.api.Assertions.*;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

public class ExampleTest {
  @Test
  void testParallel() {
    Results results =
        Runner.path("classpath:example")
            // .outputCucumberJson(true)
            .parallel(5);
    assertEquals(0, results.getFailCount(), results.getErrorMessages());
  }
}
