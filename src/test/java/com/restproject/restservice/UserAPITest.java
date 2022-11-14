package com.restproject.restservice;

import com.restproject.restservice.domain.User;
import com.restproject.restservice.repository.IUserRepository;
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

        User user = new User();
        user.setName(rand+rand2+rand3);

        return user;
    }
    private void resetRepository() {
        userRepository.deleteAll();
    }

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void deleteAll_Test() {
        User user = getUser();
        userRepository.save(user);

        resetRepository();
        List<User> found = userRepository.findAll();
        assertEquals(0, found.size());
    }

    @Test
    public void save_Test() {
        User user = getUser();
        userRepository.save(user);

        User found = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user.getId(), found.getId());
    }

    @Test
    public void findAll_Test() {
        User user = getUser();
        userRepository.save(user);

        List<User> found = userRepository.findAll();
        assertFalse(found.isEmpty());
    }

    @Test
    public void findByName_Test() {
        User user = getUser();
        userRepository.save(user);

        List<User> found = userRepository.findByName(user.getName());
        assertFalse(found.isEmpty());
        assertEquals(user.getName(), found.get(0).getName());
    }

    @Test
    public void updateUser_Test(){
        User user = getUser();
        userRepository.save(user);

        user.setName(getUser().getName());
        userRepository.save(user);

        User found = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(user.getId(), found.getId());
        assertEquals(user.getName(), found.getName());
    }


}
