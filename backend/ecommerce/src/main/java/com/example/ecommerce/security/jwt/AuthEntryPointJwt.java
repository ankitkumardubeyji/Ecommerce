package com.example.ecommerce.security.jwt 

@Component 
public class AuthEntryPointJwt extends AuthenticationEntryPoint { // used to define behaviour of the application when unauthenticated user tries to access the private resource 
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
    @Override // This is used for defining the behaviour when the unauthenticated user is trying to access the private resource 
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException, ServletException{
        logger.error("Unauthorised error:{}",authException.getMessage());

        // setting the content type to json as we are building an api
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String,Object> body = new HashMap<>();

        body.put("status",HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error","Unauthorized");
        body.put("message",authException.getMessage());

        body.put("path",request.getServletPath);

        final ObjectMapper mapper = new ObjectMapper(); // objectMapper is a java jackson main class for converting between javaobjects and json 

        mapper.writeValue(response.getOutputStream(),body); // serialises java object into json , and writes it directly to the given output stream

        
    }


}