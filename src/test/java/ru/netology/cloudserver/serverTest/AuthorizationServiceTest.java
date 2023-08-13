package ru.netology.cloudserver.serverTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.cloudserver.security.JwtTokenUtil;
import ru.netology.cloudserver.model.AuthorizationRequest;
import ru.netology.cloudserver.model.AuthorizationResponse;
import ru.netology.cloudserver.repository.AuthorizationRepository;
import ru.netology.cloudserver.server.AuthorizationService;
import ru.netology.cloudserver.server.UserService;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthorizationServiceTest {
    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private AuthorizationRepository authorizationRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil tokenProvider;

    @Mock
    private UserService userService;

    public static final String TOKEN = "token";
    public static final String LOGIN_1 = "login1";
    public static final String PASSWORD_1 = "11111";
    public static final AuthorizationResponse AUTHORIZATION_RESPONSE = new AuthorizationResponse(TOKEN);
    public static final AuthorizationRequest AUTHORIZATION_REQUEST = new AuthorizationRequest(LOGIN_1, PASSWORD_1);
    public static final UsernamePasswordAuthenticationToken USERNAME_PASSWORD_AUTHENTICATION_TOKEN = new UsernamePasswordAuthenticationToken(LOGIN_1, PASSWORD_1);
    public static final UserDetails USER_DETAILS_1 = User.builder()
            .username(LOGIN_1)
            .password(PASSWORD_1)
            .authorities(new ArrayList<>())
            .build();

    @Test
    void login() {
        Mockito.when(userService.loadUserByUsername(LOGIN_1)).thenReturn(USER_DETAILS_1);
        Mockito.when(tokenProvider.generateToken(USER_DETAILS_1)).thenReturn(TOKEN);
        assertEquals(AUTHORIZATION_RESPONSE, authorizationService.login(AUTHORIZATION_REQUEST));
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(USERNAME_PASSWORD_AUTHENTICATION_TOKEN);
        Mockito.verify(authorizationRepository, Mockito.times(1)).putTokenAndUsername(TOKEN, LOGIN_1);
    }

    @Test
    void logout() {
        Mockito.when(authorizationRepository.getUserNameByToken(TOKEN)).thenReturn(LOGIN_1);
        authorizationService.logout(TOKEN);
        Mockito.verify(authorizationRepository, Mockito.times(1)).getUserNameByToken(TOKEN);
        Mockito.verify(authorizationRepository, Mockito.times(1)).removeTokenAndUsernameByToken(TOKEN);
    }
}
