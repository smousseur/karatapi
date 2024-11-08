package com.smousseur.karatapi.example.api.controller;

import com.smousseur.karatapi.example.api.model.OrderCreated;
import com.smousseur.karatapi.example.api.model.OrderStatus;
import com.smousseur.karatapi.example.api.model.PostOrder;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/order")
public class OrderController {
  private static final Set<String> orderIds = new HashSet<>();
  private static final Random random = new Random();

  @PostMapping
  public OrderCreated createOrder(
      @RequestBody PostOrder postOrder, @RequestHeader Map<String, String> headers) {
    if (!headers.containsKey("authorization")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    if (!headers.get("authorization").equals("Permitted")) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
    }
    String orderId = UUID.randomUUID().toString();
    orderIds.add(orderId);
    return OrderCreated.builder().orderId(orderId).build();
  }

  @GetMapping
  public OrderStatus getOrder(
      @RequestParam String orderId, @RequestHeader Map<String, String> headers) {
    String status = random.nextFloat(1.f) < 0.3f ? "COMPLETE" : "IN_PROGRESS";
    return orderIds.stream()
        .filter(id -> id.equals(orderId))
        .map(id -> OrderStatus.builder().status(status).build())
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
  }
}
