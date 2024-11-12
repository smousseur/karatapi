package com.smousseur.karatapi.example.api.model;

import com.smousseur.karatapi.example.api.model.entity.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResult {
  private String name;
  private String uuid;
  private String status;

  public static OrderResult map(Order order) {
    return OrderResult.builder()
        .name(order.getName())
        .uuid(order.getUuid())
        .name(order.getName())
        .status(order.getStatus())
        .build();
  }
}
