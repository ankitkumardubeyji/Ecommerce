package com.example.ecommerce.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.UserRepository;

import jakarta.transaction.Transactional;


public class UserDetailsServiceImplementation implements UserDetailsService{

	@Autowired
	private UserDetailsImplementation userDetailsImplementation;
	
	
	@Autowired
	private UserRepository userRepository;

    @Override
    @Transactional  // Since roles are usually mapped with @OneToMany or @ManyToMany and often lazy-loaded, @Transactional prevents issues when accessing them outside the repository call.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUserName(username)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found with username:"+username));

        return userDetailsImplementation.build(user);            
    }
    

}
