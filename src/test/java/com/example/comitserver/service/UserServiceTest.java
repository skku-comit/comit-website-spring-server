package com.example.comitserver.service;

import com.example.comitserver.domain.User;
import com.example.comitserver.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void createUser() {
        // given
        User  user1 = new User();
        user1.setName("spring1");
        user1.setEmail("abcd@gmail.com");
        //when
        userService.createUser(user1);
        //then
        User findUser = userService.findByEmail("abcd@gmail.com").get();
        Assertions.assertThat(user1.getName()).isEqualTo(findUser.getName());

    }

    @Test
    void findByName() {
        // given
        User user1 = new User();
        user1.setName("spring1");
        user1.setEmail("abcd@gmail.com");
        userService.createUser(user1);

        User user2 = new User();
        user2.setName("spring1");
        user2.setEmail("efgh@gmail.com");
        userService.createUser(user2);

        // when
        List<User> findUsers = userService.findByName("spring1");

        // then
        assertFalse(findUsers.isEmpty()); // Check that the list is not empty

        // Verify the users in the list match the created users
        assertTrue(findUsers.stream().anyMatch(user -> user.getEmail().equals("abcd@gmail.com")));
        assertTrue(findUsers.stream().anyMatch(user -> user.getEmail().equals("efgh@gmail.com")));

        // Additional assertions for the first user
        User findUser1 = findUsers.get(0);
        assertEquals("spring1", findUser1.getName());
        assertTrue(findUser1.getEmail().equals("abcd@gmail.com") || findUser1.getEmail().equals("efgh@gmail.com"));

    }

    @Test
    void findByEmail() {
        // given
        User user1 = new User();
        user1.setName("spring1");
        user1.setEmail("abcd@gmail.com");
        userService.createUser(user1);
        // when
        Optional<User> findUser = userService.findByEmail("abcd@gmail.com");
        // then
        assertTrue(findUser.isPresent());
        assertEquals(user1.getEmail(), findUser.get().getEmail());
    }

    @Test
    void findAllUsers() {
        // given
        User user1 = new User();
        user1.setName("spring1");
        user1.setEmail("abcd@gmail.com");
        userService.createUser(user1);
        User user2 = new User();
        user2.setName("spring2");
        user2.setEmail("efgh@gmail.com");
        userService.createUser(user2);
        // when
        List<User> allUsers = userService.findAllUsers();
        // then
        assertEquals(2, allUsers.size());
    }

    @Test
    void findAllStaff() {
        // given
        User user1 = new User();
        user1.setName("spring1");
        user1.setEmail("abcd@gmail.com");
        userService.createUser(user1);
        User user2 = new User();
        user2.setName("spring2");
        user2.setEmail("efgh@gmail.com");
        userService.createUser(user2);

        // Retrieve the saved user to get the generated ID
        User savedUser1 = userService.findByEmail("abcd@gmail.com").get();

        // when
        userService.updateUserStaffStatus(savedUser1.getId(), true);
        List<User> allStaff = userService.findAllStaff();
        // then
        assertEquals(1, allStaff.size());
    }

    @Test
    void updateUser() {
        // given
        User user1 = new User();
        user1.setName("originalName");
        user1.setEmail("originalEmail@gmail.com");
        user1.setStudentId("originalStudentId");
        user1.setPassword("originalPassword");
        userService.createUser(user1);

        // Retrieve the saved user to get the generated ID
        User savedUser1 = userService.findByEmail("originalEmail@gmail.com").get();

        // when
        User updatedUser = userService.updateUser(savedUser1.getId(), "newName", null, "newEmail@gmail.com", null);

        // then
        assertEquals("newName", updatedUser.getName());
        assertEquals("originalStudentId", updatedUser.getStudentId()); // Unchanged field
        assertEquals("newEmail@gmail.com", updatedUser.getEmail());
        assertEquals("originalPassword", updatedUser.getPassword()); // Unchanged field
    }


}