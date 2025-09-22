package com.booklify.config;

import com.booklify.domain.Admin;
import com.booklify.domain.RegularUser;
import com.booklify.service.impl.AdminService;
import com.booklify.service.impl.RegularUserService;
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
    private final RegularUserService regularUserService;

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil,@Lazy AdminService adminService, @Lazy RegularUserService regularUserService) {
        this.jwtUtil = jwtUtil;
        this.adminService = adminService;
        this.regularUserService = regularUserService;
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
                boolean authenticated = false;
                // Try admin authentication
                if (!authenticated) {
                    authenticated = adminService.findByEmail(email).map(admin -> {
                        if (jwtUtil.isTokenValid(token, admin.getEmail())) {
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(admin, null, null);
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            return true;
                        }
                        return false;
                    }).orElse(false);
                }
                // Try regular user authentication if not authenticated as admin
                if (!authenticated) {
                    regularUserService.findByEmail(email).ifPresent(user -> {
                        if (jwtUtil.isTokenValid(token, user.getEmail())) {
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(user, null, null);
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    });
                }
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
