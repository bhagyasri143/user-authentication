package com.auth.user.service;

import com.auth.user.entities.User;
import com.auth.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void testGetUserById() {
        Long id = 1L;
        User expectedUser = new User(id, "testuser", "password");
        when(userRepository.findById(id)).thenReturn(expectedUser);

        User actualUser = userService.getUserById(id);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testGetUserByUsername() {
        String username = "testuser";
        User expectedUser = new User(1L, username, "password");
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        User actualUser = userService.getUserByUsername(username);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = Arrays.asList(
                new User(1L, "testuser1", "password1"),
                new User(2L, "testuser2", "password2")
        );
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1L, "testuser", "password");

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        Long id = 1L;

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}
