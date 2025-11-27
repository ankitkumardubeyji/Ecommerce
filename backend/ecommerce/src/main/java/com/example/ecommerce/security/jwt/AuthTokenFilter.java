package com.ecommerce.example.security.jwt 


public class AuthTokenFilter extends OncePerRequestFilter{

    @Autowired 
    private JwtUtils jwtUtils;
    
    @Override // This method is once called per http request by the filter chain
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// logging the incoming request uri
		logger.debug("AuthTokenFilter called for URI:{}",request.getRequestURI());
		
		try {
			
			// Extract jwt token string from the Authorisation header (actual extraction happens in parseJwt())
			String jwt = parseJwt(request);
			
			// If there is token and it validates successfully continue to setup Spring Authentication
			if(jwt!=null && jwtUtils.validateJwtToken(jwt)) {
				// Extracting the username
				String username = jwtUtils.getUserNameFromJWTToken(jwt);
				
				// Loading the full userDetails: (password hash,roles,enabled flags) from configured userDetailsService
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				
				// Create an authentication object representing the authenticated user 
				// We pass userDetails(principal),null for credentials(we dont keep the password) and authorities.
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( // This is designed for simple presentation of username and password
						userDetails,null,userDetails.getAuthorities()
						); 
				
				// Attach additional details to the authentication (like remote address,session id)
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// Put the Authentication into SecurityContextHolder(thread local storage), from now on *within this request* the user is considered authenticated
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				// Debug log roles 
				logger.debug("Roles from JWT:{}",userDetails.getAuthorities());
			}
		}
		catch(Exception e) {
			logger.error("Cannot set user authentication: ",e);
		}
		
		
		// Important : continute the filter chain to let request reach controller or other security filters.
		filterChain.doFilter(request, response);
		
	}

    @Override 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        // logging the incoming request uri
        logger.debug("AuthTokenFilter created , {}",request.getRequestURI);

        try{
            String jwt = parseJwt(request); // extracting the jwt token from the request header

            if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

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