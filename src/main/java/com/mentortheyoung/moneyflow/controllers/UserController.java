package com.mentortheyoung.moneyflow.controllers;

import com.mentortheyoung.moneyflow.entities.User;
import com.mentortheyoung.moneyflow.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
    logger.info("Getting user by id: " + userId);
    return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        logger.info("Creating new user");
        return userService.saveUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody User user) {
        logger.info("Updating user with id: " + userId);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public  void  deleteUser(@PathVariable Integer userId) {
        logger.info("Deleting user with id: " + userId);
        userService.deleteUserById(userId);
    }
}
