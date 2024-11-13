package example;

import static org.junit.jupiter.api.Assertions.*;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.dockerclient.DockerClientConfigUtils;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class ExampleIntegrationTest {

  static final Network network = Network.newNetwork();

  @Container
  static final MySQLContainer<?> mySQLContainer =
      (MySQLContainer<?>)
          new MySQLContainer("mysql:latest") {
            @Override
            public String getJdbcUrl() {
              if (DockerClientConfigUtils.IN_A_CONTAINER) {
                final String ipAddress = getContainerInfo().getNetworkSettings().getIpAddress();
                final String port = getExposedPorts().get(0).toString();
                String additionalUrlParams = constructUrlParameters("?", "&");
                return "jdbc:mysql://"
                    + ipAddress
                    + ":"
                    + port
                    + "/"
                    + getDatabaseName()
                    + additionalUrlParams;
              }
              return super.getJdbcUrl();
            }
          }.withDatabaseName("test")
              .withUsername("caca51")
              .withPassword("Smousse+51")
              .withInitScript("init.sql")
              .withNetwork(network)
              .withNetworkAliases("db-mysql")
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
                  "jdbc:mysql://db-mysql:3306/test",
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
