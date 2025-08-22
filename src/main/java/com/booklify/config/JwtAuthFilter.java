package com.booklify.config;

import com.booklify.domain.Admin;

import com.booklify.service.impl.AdminService;
import com.booklify.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil,@Lazy AdminService adminService) {
        this.jwtUtil = jwtUtil;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.debug("Extracted JWT token: '{}'", token);
            String email = jwtUtil.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                adminService.findByEmail(email).ifPresent(admin -> {
                    if (jwtUtil.isTokenValid(token, admin.getEmail())) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(admin, null, null);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/admins/create") ||
                path.equals("/api/admins/login") ||
                path.startsWith("/api/admins/deleteUser");

    }
}
