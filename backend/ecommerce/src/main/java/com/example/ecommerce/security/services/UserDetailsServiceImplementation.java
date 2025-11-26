public class UserDetailsServiceImplementatin implements UserDetailsService{


    @Override
    @Transactional  // Since roles are usually mapped with @OneToMany or @ManyToMany and often lazy-loaded, @Transactional prevents issues when accessing them outside the repository call.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found with username:"+username));

        return userDetailsImplementation.build(user);            
    }
    

}