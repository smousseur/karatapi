package com.smousseur.karatapi.example.api.controller;

import com.smousseur.karatapi.example.api.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
  @GetMapping
  public User getUser(@RequestParam Integer id) {
    return User.builder().id(id).name("User" + id).email("mail" + id + "@company.com").build();
  }

  @DeleteMapping
  public ResponseEntity<String> deleteUser(@RequestParam Integer id) {
    return ResponseEntity.ok("User " + id + " deleted");
  }
}
