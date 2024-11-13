package example;

import static org.junit.jupiter.api.Assertions.*;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ExampleIntegrationTest {

  static final Network network = Network.newNetwork();

  static Consumer<CreateContainerCmd> cmd =
      e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(3307), new ExposedPort(3307)));

  @Container
  static final MySQLContainer<?> mySQLContainer =
      new MySQLContainer<>("mysql:latest")
          .withInitScript("init.sql")
          .withDatabaseName("test")
          .withUsername("caca51")
          .withPassword("Smousse+51")
          .withNetwork(network)
          .withNetworkAliases("db-mysql")
          .withCreateContainerCmdModifier(cmd)
          .waitingFor(Wait.forListeningPort());

  @Container
  static GenericContainer<?> exempleApp =
      new GenericContainer<>("smousseur/karatapi:latest")
          .dependsOn(mySQLContainer)
          .withExposedPorts(8080)
          .withNetwork(network)
          .withNetworkAliases("backend-app")
          .waitingFor(Wait.forHttp("/example/actuator/health"))
          .withEnv(
              Map.of(
                  "SPRING_DATASOURCE_URL",
                  "jdbc:mysql://db-mysql:3307/test",
                  "SPRING_DATASOURCE_USERNAME",
                  mySQLContainer.getUsername(),
                  "SPRING_DATASOURCE_PASSWORD",
                  mySQLContainer.getPassword()));

  @BeforeAll
  static void setup() {
    System.setProperty("appPort", String.valueOf(exempleApp.getMappedPort(8080)));
    System.setProperty("appHost", exempleApp.getHost());
  }

  @Test
  void testParallel() {
    Results results =
        Runner.path("classpath:example")
            // .outputCucumberJson(true)
            .parallel(5);
    assertEquals(0, results.getFailCount(), results.getErrorMessages());
  }
}
