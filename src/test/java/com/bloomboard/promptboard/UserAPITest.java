package com.bloomboard.promptboard;

import com.bloomboard.promptboard.security.model.RegisterRequest;
import com.bloomboard.promptboard.security.model.User;
import com.bloomboard.promptboard.security.repository.IUserRepository;
import com.bloomboard.promptboard.security.service.UserServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserAPITest {
    private Random random = new Random();
    private static String[] nameParts = {
            "Best", "Red", "Cowboy", "Rave", "Jazz", "Wolf", "Blueberry", "Gold", "Funny", "Blaze",
            "Steel", "Sword", "Lizard", "Laser", "XX", "_", "Creepy", "Ligma", "Water", "Pastry", "Hero"
    };
    private User getUser() {
        String rand = nameParts[random.nextInt(nameParts.length)];
        String rand2 = nameParts[random.nextInt(nameParts.length)];
        String rand3 = nameParts[random.nextInt(nameParts.length)];
        String username = rand+rand2+rand3;
        User user = new User(username, username+"@gmail.com","password123");

        return user;
    }
    private void resetRepository() {
        userRepository.deleteAll();
    }

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void deleteAll_Test() {
        User user = getUser();
        userRepository.save(user);

        resetRepository();
        List<User> found = userRepository.findAll();
        assertEquals(0, found.size());
    }

    @Test
    public void Register_Test() {
        User user = getUser();

        RegisterRequest request = new RegisterRequest();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setPassword("password123");
        User newUser = new User(request.getUsername(),request.getEmail(),request.getPassword());

        userService.save(newUser);

        User found = userRepository.findByUsernameIgnoreCase(user.getUsername()).orElseThrow();
        assertEquals(newUser.getUsername(), found.getUsername());
    }

    /*
    @Test
    public void RegisterAndAuthenticate_Test() {
        User user = getUser();

        RegisterRequest request = new RegisterRequest();
        request.setUsername(user.getUsername());
        request.setEmail(user.getEmail());
        request.setPassword("password123");
        service.register(request);

        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setUsername(user.getUsername());
        authRequest.setPassword("password123");
        AuthenticationResponse authToken = service.authenticate(authRequest);

        System.out.println(user.toString());
        System.out.println(authToken);
        assertTrue(authToken.getToken() != null);
    }
     */

    @Test
    public void findAll_Test() {
        User user = getUser();
        userRepository.save(user);

        List<User> found = userRepository.findAll();
        assertFalse(found.isEmpty());
    }

    /*@Test
    public void findByName_Test() {
        User user = getUser();
        userRepository.save(user);

        List<User> found = userRepository.findByName(user.getName());
        assertFalse(found.isEmpty());
        assertEquals(user.getName(), found.get(0).getName());
    } */

    /*
    @Test
    public void updateUser_Test(){
        User user = getUser();
        userRepository.save(user);

        user.setName(getUser().getName());
        userRepository.save(user);

        User found = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user.getId(), found.getId());
        assertEquals(user.getName(), found.getName());
    } */


}
