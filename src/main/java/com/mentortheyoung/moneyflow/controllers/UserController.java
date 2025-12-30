package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity <User> createUser(@RequestBody User user) {
        logger.info("Creating new user");
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.loginUser(username, password);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/test")
    public String test() {
        return "You are logged in!";
    }
}
