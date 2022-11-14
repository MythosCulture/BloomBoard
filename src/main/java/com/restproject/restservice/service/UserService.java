package com.restproject.restservice.service;

import com.restproject.restservice.domain.User;
import com.restproject.restservice.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findByName(String name) {
        return  userRepository.findByName(name);
    }

    public User findById(long id){
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.orElseThrow();
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());

        return userRepository.save(newUser);
    }
}
