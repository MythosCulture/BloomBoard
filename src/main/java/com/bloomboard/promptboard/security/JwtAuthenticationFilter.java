package com.bloomboard.promptboard.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        logger.info("JwtAuthenticationFilter is being applied for URI: {}", request.getServletPath());

        final String authHeader = request.getHeader("Authorization");
        logger.info("Authorization header value: {}", authHeader);
        final String jwt;
        final String userName;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("No JWT token found in request headers. Skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userName = JwtUtil.extractUsername(jwt);//extract userName from JWT token
        logger.info("Extracted username from JWT token: {}", userName);

        //check if userName isn't null, and if the user is authenticated
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            logger.info("User details retrieved from UserDetailsService: {}", userDetails);

            if (JwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request) );
                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info("JWT token is valid. Authentication successful for user: {}", userName);
            } else {
                logger.info("JWT token is not valid. Authentication failed for user: {}", userName);
            }
        }
        filterChain.doFilter(request, response);
        logger.debug("JwtAuthenticationFilter completed processing for URI: {}", request.getServletPath());
    }

}
