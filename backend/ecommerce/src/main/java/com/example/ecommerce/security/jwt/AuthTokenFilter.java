package com.example.ecommerce.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ecommerce.security.services.UserDetailsServiceImplementation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
    @Autowired 
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        // logging the incoming request uri
        logger.debug("AuthTokenFilter created , {}",request.getRequestURI());

        try{
            String jwt = parseJwt(request); // extracting the jwt token from the request header

            if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUserNameFromJWTToken(jwt);

                // loading the full user details like user ,hashed password and all
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
                // Creating an authenticationObject representing the authenticatedUser
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities() 
                );

                // adding the additional details to the authentication like session id,and remote address and all
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Putting the authentication object into the Security Context ie the local thread storage, once this is set in the securityContext from now onwards the user is considered as authenticated 
                SecurityContextHolder.getContext().setAuthentication(authentication);



            }
        }
        catch(Exception e){
            logger.error("Cannot set user authentication",e);
        }

        // Important : continue the filter chain, to let the request reach to the controllers or the other security filters 
        filterChain.doFilter(request,response);
    }


// Helper method to extract the jwt from the cookie:-
    private String parseJwt(HttpServletRequest request){
        String jwt = jwtUtils.getJwtFromCookies(request); // this will run every request
        logger.debug("AuthTokenFilter.java {}",jwt);
        return jwt;
    }
}