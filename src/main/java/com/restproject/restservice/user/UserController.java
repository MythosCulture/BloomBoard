package com.restproject.restservice.user;

import com.restproject.restservice.user.User;
import com.restproject.restservice.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController //doesnt work with thymeleaf
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //@GetMapping("/all")
    //public List<User> getAllUsers() {
    //    return userService.findAllUsers();
    //}

    //@GetMapping("/{username}")
    //public List<User> getUsersByUsername(@PathVariable String username){
    //    return userService.findByUsername(username);
    //}

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
