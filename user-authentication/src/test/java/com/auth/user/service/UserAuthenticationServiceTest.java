package com.auth.user.service;

import com.auth.user.entities.User;
import com.auth.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserAuthenticationService userAuthenticationService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        userAuthenticationService = new UserAuthenticationService(userRepository, passwordEncoder);
    }

    @Test
    public void testRegisterSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        userAuthenticationService.register(user);

        verify(userRepository).save(any(User.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRegisterUserAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        userAuthenticationService.register(user);
    }


    @Test
    public void testLogin() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

        User result = userAuthenticationService.login("testuser", "password");

        assertEquals(user, result);
    }

    @Test(expected = RuntimeException.class)
    public void testLoginInvalidUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        userAuthenticationService.login("testuser", "password");
    }

    @Test(expected = RuntimeException.class)
    public void testLoginInvalidPassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(false);

        userAuthenticationService.login("testuser", "password");
    }
}



