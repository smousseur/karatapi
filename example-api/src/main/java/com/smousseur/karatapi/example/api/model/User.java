package com.smousseur.karatapi.example.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
  private Integer id;
  private String name;
  private String email;
}
