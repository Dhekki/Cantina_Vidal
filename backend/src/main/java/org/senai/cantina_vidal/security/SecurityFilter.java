package org.senai.cantina_vidal.security;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.senai.cantina_vidal.entity.User;
import org.senai.cantina_vidal.service.TokenService;
import org.senai.cantina_vidal.repository.UserRepository;
import org.senai.cantina_vidal.exception.InvalidTokenException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            try {
                String email = tokenService.validateToken(token);

                User user = userRepository.findByEmailAndDeletedFalse(email).orElse(null);

                if (user != null) {
                    UserDetails userPrincipal = new UserPrincipal(user);
                    var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (InvalidTokenException e) {
                log.debug("Falha na validação do token: {}", e.getMessage());
            } catch (Exception e) {
                log.error("Erro inesperado na validação do token", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "accessToken");
        return cookie != null ? cookie.getValue() : null;
    }
}
