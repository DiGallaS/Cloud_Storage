package ru.netology.cloudserver.server;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netology.cloudserver.security.JwtTokenUtil;
import ru.netology.cloudserver.model.AuthorizationRequest;
import ru.netology.cloudserver.model.AuthorizationResponse;
import ru.netology.cloudserver.repository.AuthorizationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final AuthorizationRepository authorizationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil tokenUtil;
    private final UserService userService;

//    @Autowired
//    public AuthorizationService(AuthorizationRepository authorizationRepository, AuthenticationManager authenticationManager, JwtTokenUtil tokenUtil) {
//        this.authorizationRepository = authorizationRepository;
//        this.authenticationManager = authenticationManager;
//        this.tokenUtil = tokenUtil;
//    }

    public AuthorizationResponse login(AuthorizationRequest authorizationRequest) {
        final String username = authorizationRequest.getLogin();
        final String password = authorizationRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userService.loadUserByUsername(username);
        String token = tokenUtil.generateToken(userDetails);
        authorizationRepository.putTokenAndUsername(token, username);
        log.info("User {} is authorized", username);
        return new AuthorizationResponse(token);
    }

    public void logout(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final String username = authorizationRepository.getUserNameByToken(authToken);
        log.info("User {} logout", username);
        authorizationRepository.removeTokenAndUsernameByToken(authToken);

    }
}
