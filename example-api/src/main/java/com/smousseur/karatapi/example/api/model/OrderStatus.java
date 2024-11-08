package com.smousseur.karatapi.example.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStatus {
  private String status;
}
