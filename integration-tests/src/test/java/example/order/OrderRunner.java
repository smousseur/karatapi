package example.order;

import com.intuit.karate.junit5.Karate;

class OrderRunner {
  @Karate.Test
  Karate testOrder() {
    return Karate.run("order").relativeTo(getClass());
  }
}
