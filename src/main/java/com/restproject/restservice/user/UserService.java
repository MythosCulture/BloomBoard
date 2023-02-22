package com.restproject.restservice.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private final IUserRepository userRepository;

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    //public List<User> findAllUsers() {
    //    return userRepository.findAll();
    //}

    //public List<User> findByName(String name) {
    //    return  userRepository.findByName(name);
    //}

    public User findById(long id){
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.orElseThrow();
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setLocked(false);
        newUser.setEnabled(true);
        newUser.setUsername(user.getUsername());

        return userRepository.save(newUser);
    }
}
