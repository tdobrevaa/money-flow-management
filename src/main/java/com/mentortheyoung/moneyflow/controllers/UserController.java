package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.services.AuthService;
import com.mentortheyoung.moneyflow.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity <User> createUser(@RequestBody User user) {
        logger.info("Creating new user");
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        String token = authService.verify(username, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/test")
    public String test() {
        return "You are logged in!";
    }
}
