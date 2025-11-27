package com.ecommerce.example.security.jwt 


@Component
public class AuthTokenFilter extends OncePerRequestFilter{

    @Autowired 
    private JwtUtils jwtUtils;

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