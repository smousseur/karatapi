package com.smousseur.karatapi.example.api.controller;

import com.smousseur.karatapi.example.api.model.OrderCreated;
import com.smousseur.karatapi.example.api.model.OrderModified;
import com.smousseur.karatapi.example.api.model.OrderResult;
import com.smousseur.karatapi.example.api.model.PostOrder;
import com.smousseur.karatapi.example.api.model.entity.Order;
import com.smousseur.karatapi.example.api.repository.OrderRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderRepository repository;

  @PostMapping
  @Transactional
  public ResponseEntity<OrderCreated> createOrder(@RequestBody PostOrder postOrder) {
    String uuid = UUID.randomUUID().toString();
    Order order = Order.builder().name(postOrder.getName()).status("CREATED").uuid(uuid).build();
    repository.save(order);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(OrderCreated.builder().orderId(uuid).build());
  }

  @PutMapping
  @Transactional
  public ResponseEntity<OrderResult> modifyStatus(@RequestBody OrderModified orderModified) {
    return repository
        .findByUuid(orderModified.getUuid())
        .map(
            order -> {
              order.setStatus(orderModified.getStatus());
              return ResponseEntity.ok().body(OrderResult.map(order));
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<OrderResult> getOrder(@RequestParam("uuid") String uuid) {
    return repository
        .findByUuid(uuid)
        .map(OrderResult::map)
        .map(order -> ResponseEntity.ok().body(order))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping
  public ResponseEntity<Object> deleteOrder(@RequestParam String uuid) {
    return repository
        .findByUuid(uuid)
        .map(
            order -> {
              repository.delete(order);
              return ResponseEntity.noContent().build();
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/list")
  public List<OrderResult> getOrders() {
    return repository.findAll().stream().map(OrderResult::map).toList();
  }
}
