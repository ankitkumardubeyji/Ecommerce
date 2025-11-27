package com.example.ecommerce.security.jwt;

import java.security.Key;
import java.sql.Date;

import javax.crypto.SecretKey;

import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils{

    /*
        // Usually header looks like: "Authorization: Bearer <token>", 
        public String getJwtFromHeader(HttpServletRequest request){ // method for extracting the jwt token part  from the request header  

                String bearerToken = request.getHeader("Authorization");
                logger.debug("Authorisation Header {}",bearerToken);

                if(bearerToken!=null && bearerToken.startsWith("Bearer")){
                        return bearerToken.substring(7);
                    }

                return null;
        
        }

    */



    public String getJwtFromCookies(HttpServletRequest request){ // method will be used to extract the jwt token from the cookie present in the incoming request 
        Cookie cookie = WebUtils.getCookie(request,jwtCookie); // passing the request , and cookie name as the parameter
        if(cookie!=null){
            return cookie.getValue();
        }
        return null;

    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal){
        String jwt = getTokenFromUsername(userPrincipal.getUsername()); // getting the string username from the custom UserDetails implementation and passing as the parameter 

        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt); // Passing the name of the cookie and value of the cookie 
                                        .path("/api") // setting the path for which the cookie is valid 
                                        .maxAge(24*60*60) // lifetime for cookie in seconds 
                                        .httpOnly(false) // controls wheteher cookie is httpOnly , false implies can be accessible via javascript 
                                        .secure(false) // controls whether cookie can be only sent through https, false implies can be sent over http too 
                                        .build(); // finalising the cookie creation and return responseCookie object

        return cookie;                                

    }

    
    
    public ResponseCookie getCleanJwtCookie( ) {
    	
    	ResponseCookie cookie = ResponseCookie.from(jwtCookie,null) // jwtCookie is the cookie name
    						.path("/api")
    						.build();
    	
    	return cookie;						
    }
    
    // generating the jwt token from the username 
    public String getTokenFromUsername(String username){
         return Jwts.builder()
                    .subject(username) // username is the unique identifier for the user 
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                    .signWith(key()) // sign with our secret key 
                    .compact() ; // build and convert to string token 
    }

    public String getUserNameFromJWTToken(String token){
        return jwts.parser()  // creating a jwt parser 
                    .verifyWith((SecretKey)key); // setting the secret key for verifying the signature 
                    .build()  // building the parser
                    .parseSignedClaims(token) // parses the jwt and verifies the signature 
                    .getPayload().getSubject(); // extract the subject username 
    }

    
    public Key key() { // the function converts secret string into cryptographic key
        // That key is used to sign and verify jwt token 
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret) // decode Base64 secret into raw bytes, hmacShaKey -> takes those bytes and converts into suitable secret key 
        );
    }


    /**
     * ✅ Validate JWT token.
     * Checks if:
     *  - token signature is valid
     *  - token is not expired
     *  - token format is correct
     *
     * If token is valid, returns true.
     * If token is invalid/expired/etc., logs error and returns false.
     */


    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");

         // Step 1: Create a JWT parser object
            Jwts.parser() 
                // Step 2: Provide the secret key to verify the token's signature
                .verifyWith((SecretKey) key()) 
                // Step 3: Build/finalize the parser. Now it's ready to parse and validate tokens
                .build() 
                // Step 4: Parse the token. This does two things:
                //         1. Verifies the token signature matches the secret key
                //         2. Extracts the claims (payload) if the signature is valid
                // If the signature is invalid or token is tampered, an exception is thrown here
                .parseSignedClaims(authToken); 

            // Step 5: If no exception was thrown so far, the token is valid
            return true;


        } catch (MalformedJwtException e) {
            // Thrown when token format is invalid (broken token string)
            logger.error("Invalid JWT Token: " + e.getMessage());

        } catch (ExpiredJwtException e) {
            // Thrown when token expiration date has passed
            logger.error("JWT Token is expired: " + e.getMessage());

        } catch (UnsupportedJwtException e) {
            // Thrown when token uses an unsupported JWT feature
            logger.error("JWT Token is unsupported: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            // Thrown when claims string is empty or null
            logger.error("JWT claims string is empty: " + e.getMessage());
        }

        // If any exception happened, token is invalid
        return false;
    }



}

