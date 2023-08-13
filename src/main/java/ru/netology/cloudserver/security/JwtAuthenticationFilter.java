package ru.netology.cloudserver.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloudserver.server.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final UserService userService;
        private final JwtTokenUtil tokenUtil;

        @Autowired
        public JwtAuthenticationFilter(UserService userService, JwtTokenUtil jwtTokenUtil) {
            this.userService = userService;
            this.tokenUtil = jwtTokenUtil;
        }

        @Override
        protected void doFilterInternal(@NotNull HttpServletRequest request,
                                        @NotNull HttpServletResponse response,
                                        @NotNull FilterChain chain) throws IOException, ServletException {
            final String header = request.getHeader("auth-token");
            String username = null;
            String authToken = null;
            if (header != null && header.startsWith("Bearer ")) {
                authToken = header.substring(7);
                try {
                    username = tokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    log.error("Error: jwt token not received", e);
                } catch (ExpiredJwtException e) {
                    log.warn("jwt token is expired", e);
                }
            } else {
                log.warn("couldn't find bearer string");
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(username);
                if (tokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);
        }

    }
