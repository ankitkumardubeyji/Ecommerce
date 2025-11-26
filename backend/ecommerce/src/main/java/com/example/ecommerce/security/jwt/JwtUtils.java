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
    

    /**
     * ✅ Generate JWT token for a given user.
     * We store username as subject (sub claim) of the token.
     */
    public String generateTokenFromUsername(String username) {
    // UserDetails is the interface : that tells ekk user object me kaunse details hone chahiye for authentication and authorisation,has methods like getUsername()
    // 	UserDetails : spring security khud deta hai jab tum login authenticate krte ho,tumhara userdatilservice jasie JDBCUserdetailManager database se user fetch krta hai :Fir wo user ko object ke form
    // me return krta hai,jo userDetail ko implement krta hain.	
    	
    	
        // Get username from UserDetails object
        //String username = userDetails.getUsername();

        // Build JWT token:
        return Jwts.builder()
                .subject(username) // set the 'sub' claim = username,in most simple JWT setups, storing just the username is enough because it uniquely identifies the user, and authentication on every request can be done using this claim plus token validation.
                .issuedAt(new Date()) // current time = issue time
                .expiration(new Date(new Date().getTime() + jwtExpirationMs)) // expiration = now + jwtExpirationMs
                .signWith(key()) // sign with our secret key
                .compact(); // build and convert to string token
    }
    
    

    /**
     * ✅ Extract username (subject) from JWT token.
     * We parse the token and read its 'sub' (subject) claim.
     */
    public String getUserNameFromJWTToken(String token) {
        return Jwts.parser() // create parser object: that will be used to read and verify jwt tokens
                .verifyWith((SecretKey) key()) // configures the parser with our secret key that was used to sign jwt,ensures the token has not been tampered with ,if token modified or signature doesnt match parsing fail.
                .build() // building the parser with key and settings ,after this parser is ready to validate and read tokens
                .parseSignedClaims(token) // takes the jwt string,and parses into signedJwtObject ,essentially it extracts the payload and header from the token after verifying the signature
                .getPayload().getSubject(); // retrieves the payload section of the jwt ,this contains all the claims - like sub:username,iat:issuedat
        					   // getSubject : will return the subject claim from the payload ,in our case this is the username that uniquely identifies the user.
    
    }

    /**
     * ✅ Generate signing key from our secret.
     * jwtSecret is a Base64 encoded string. 
     * We decode it, then use it to create a SecretKey for signing/validating JWT.
     */
    public Key key() {

        // jwtSecret is a Base64-encoded string, e.g., "U2VjcmV0S2V5Rm9ySl..."
        // Base64 is an encoding technique that converts binary data into text,
        // making it safe to store or transfer as a string.

        // JWT libraries expect a secret key that is at least 256 bits strong for security.

        // Decoders.BASE64.decode(jwtSecret) 
        // → Converts the Base64-encoded secret string back into raw bytes.
        //   These bytes represent the actual secret key material.

        // Keys.hmacShaKeyFor(...) 
        // → Takes the raw secret key bytes and creates a SecretKey object.
        //   This SecretKey can be used by cryptographic algorithms to:
        //     - Sign JWTs when creating tokens
        //     - Verify JWTs when validating tokens

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret) // decode Base64 secret into bytes
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