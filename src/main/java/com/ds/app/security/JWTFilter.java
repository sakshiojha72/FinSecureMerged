package com.ds.app.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ds.app.jwtutil.JWTUtil;
import com.ds.app.service.MyUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MyUserDetailService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
    	
    	String path = request.getServletPath();
    	
    	if(path.contains("swagger") || path.contains("api-docs")) {
    		filterChain.doFilter(request, response);
            return;
    	}

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        
        
        System.out.println("Token: " + token);

        String username = jwtUtil.extractUsername(token);
        System.out.println("Username: " + username);

        Integer userId = jwtUtil.extractUserId(token);
        System.out.println("UserId: " + userId);

        String role = jwtUtil.extractRole(token);
        System.out.println("Role: " + role);
     
        

        //checks if username is valid and authentication for current request is not set
        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(username);
            System.out.println("Authorities from DB: " + userDetails.getAuthorities());

            if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                //used to set details like ip address and sessionId(null in case of jwt)
                usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println("Authentication SET successfully");
            }
        }
        else {
        	System.err.println("Token is not validated");
        }

        filterChain.doFilter(request, response);
    }
    
    
}
