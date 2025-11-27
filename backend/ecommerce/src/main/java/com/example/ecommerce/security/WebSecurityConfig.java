
package com.example.ecommerce.security;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity // for activating the filter chain mechanism
public class WebSecurityConfig{

/*
In Spring Security, filters are not automatically added to the filter chain just because they’re beans. You need to register them in the security filter chain.

By exposing AuthTokenFilter as a @Bean in your WebSecurityConfig, you can then reference it inside your SecurityFilterChain configuration and insert it at the right position (e.g., before UsernamePasswordAuthenticationFilter).

*/

    @Bean 
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }


    // DaoAuthenticationProvider is a class that knows how to authenticate user against database.
    // It uses UserDetailsService to load the user information
    // It uses passwordEncoder to check the password provided during the login ,against the hashed password
    // So its basically the bridge between the login form and the database 

    @Bean 
    public DaoAuthenticationProvider authenticationProvider(){
         
         DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);

         authenticationProvider.setPasswordEncoder();

         return authenticationProvider;

    }


    // AuthenticationManager is the centeral interface that handles the centeral interface in spring security that handles authentication
    // When the user tries to login, spring security passes the credentials to the authentication manager
    // The authenticationManager delegeates this to one or more authenticationProviders like in our case here DaoAuthenticaionProvider to actually check the credentials
    // So its basically the entry point for the authentication in spring security

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager(passwordEncoder());
    }



    public PasswordEncoder passwordEncoder(){
        return new BcryptPasswordEncoder();
    }
	
      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            http.authorizeHttpRequests(authorizeRequests -> 
                                            authorizeRequests 
                                                  .requestMatchers("/api/**").permitAll()
                                                  .requestMatchers("/api/users**").authenticated()
                                                  .requestMatchers("/v3/api-docs/**").permitAll()
                                                  .requestMatchers("/api/public/**").permitAll()
                                                  .requestMatchers("/api/test/**").permitAll()
                                                  .requestMatchers("/images/**").permitAll()
                                                  .requestMatchers("/swagger-ui/**").permitAll()
                                                  .requestMatchers("/h2-console/**").permitAll()
                                                  .requestMatchers("/favicon.ico").permitAll()
                                                  .anyRequest().authenticated());

        // Making our app as stateless , ie every request must bring its own jwt 
        http.sessionManagement(session ->
                                        session.sessionCreationPolicy(
                                            SessionCreationPolicy.STATELESS
                                        ));


        http.exceptionHandling(exception -> exception.authenticationEntryPoint(uanuthorizedHandler));

        // h2-console loads in iframe , By default spring security blocks iframe view for security concerns so enabling

        http.headers(headers -> headers.
                                        frameOptions -> frameOptions.sameOrigin());


        http.cors({}); // allowing your backend to accept request from different domains.
        http.csrf(csrf.disable()); // cross-site request forgery is disabled for stateless api like rest apis using jwt, where server dont rely on cookie for authentication


        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(),UsernamePasswordAuthenticationFilter.class);

        return http.build();                                                                

      }
	 
	 @Bean
	 public WebSecurityCustomizer webSecurityCustomizer() {
	     return web -> web.ignoring().requestMatchers(
	             "/v2/api-docs",
	             "/configuration/ui",
	             "/swagger-resources/**",
	             "/configuration/security",
	             "/swagger-ui.html",
	             "/swagger-ui/**",
	             "/webjars/**",
	             "/subjects/**"
	     );
	 }
	 
	 @Bean
	    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        return args -> {
	            // Retrieve or create roles
	            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
	                    .orElseGet(() -> {
	                        Role newUserRole = new Role(AppRole.ROLE_USER);
	                        return roleRepository.save(newUserRole);
	                    });

	            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
	                    .orElseGet(() -> {
	                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
	                        return roleRepository.save(newSellerRole);
	                    });

	            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
	                    .orElseGet(() -> {
	                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
	                        return roleRepository.save(newAdminRole);
	                    });

	            Set<Role> userRoles = Set.of(userRole);
	            Set<Role> sellerRoles = Set.of(sellerRole);
	            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


	            // Create users if not already present
	            if (!userRepository.existsByUserName("user1")) {
	                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
	                userRepository.save(user1);
	            }

	            if (!userRepository.existsByUserName("seller1")) {
	                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
	                userRepository.save(seller1);
	            }

	            if (!userRepository.existsByUserName("admin")) {
	                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
	                userRepository.save(admin);
	            }

	            // Update roles for existing users
	            userRepository.findByUserName("user1").ifPresent(user -> {
	                user.setRoles(userRoles);
	                userRepository.save(user);
	            });


	            userRepository.findByUserName("seller1").ifPresent(seller -> {
	                seller.setRoles(sellerRoles);
	                userRepository.save(seller);
	            });

	            userRepository.findByUserName("admin").ifPresent(admin -> {
	                admin.setRoles(adminRoles);
	                userRepository.save(admin);
	            });
	            
	        };
	    }
    



}