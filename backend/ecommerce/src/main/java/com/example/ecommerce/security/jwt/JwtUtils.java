package com.example.ecommerce.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.util.WebUtils;

import com.example.ecommerce.security.services.UserDetailsImplementation;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils{
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${spring.app.jwtCookieName}")
	private String jwtCookie;
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${spring.app.jwtExpirationMS}")
	private long jwtExpirationMs;
	
	

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


    public ResponseCookie generateJwtCookie(UserDetailsImplementation userPrincipal){
        String jwt = getTokenFromUsername(userPrincipal.getUsername()); // getting the string username from the custom UserDetails implementation and passing as the parameter 

        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt) // Passing the name of the cookie and value of the cookie 
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
                    .signWith(key()) // sign with our secret key , that is cryptographed 
                    .compact() ; // build and convert to string token 
    }

    public String getUserNameFromJWTToken(String token){
        return Jwts.parser()  // creating a jwt parser 
                    .verifyWith((SecretKey)key()) // setting the secret key for verifying the signature 
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

    public boolean validateJwtToken(String authToken){
        
        try{
                 Jwts.parser()
                    .verifyWith((SecretKey)key())
                    .build()
                    .parseSignedClaims(authToken);

                    return true;

        }
        catch(MalformedJwtException e){
            // throw when token format is invalid or broken string
            logger.error("Invalid Jwt Token:"+e.getMessage());
        }

        catch(ExpiredJwtException e){
            // thrown when expiration data has passed.
            logger.error("Jwt token is expired ",e.getMessage());
        }

        catch(UnsupportedJwtException e){
            // throw when token uses unsupported jwt token feature.
            logger.error("Token uses unsupported  jwt feature"+e.getMessage());
        }

        catch(IllegalArgumentException e){
            // thrown when claim string in token is empty 
            logger.error("Jwt claims string is empty",e.getMessage());
        }

       return false; // if any exception happended token is invalid 
    }



}

