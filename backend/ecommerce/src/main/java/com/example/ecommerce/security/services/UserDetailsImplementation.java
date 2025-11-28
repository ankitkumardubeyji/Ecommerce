package com.example.ecommerce.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.ecommerce.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class UserDetailsImplementation implements UserDetails{

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String email;

    @JsonIgnore // for ensuring the password doesnt get serialised in the json response 
    private String password;

    private Collection<?extends GrantedAuthority> authorities; // this collection can hold any class that implements GrantedAuthority 

    public static UserDetailsImplementation build(User user){

        // Converting user roles -> Spring security compatible authorities

        List<GrantedAuthority> authorities = user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()) // getRoleName will be returning the enum constant from which name is extracted via name()
                            ).collect(Collectors.toList());

        return new UserDetailsImplementation(
            user.getUserId(),
            user.getUserName(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );               

    }

   @Override // fullfilling the contract done with the interface 
   public  Collection<?extends GrantedAuthority> getAuthorities(){
    return authorities;
   } 


   @Override 
   public String getPassword(){
      return password; 
   }

   @Override 
   public String getUsername(){
        return username; 
   }

   @Override 
   public boolean isAccountNonExpired(){
        return true; 
   }

   @Override 
   public boolean isCredentialsNonExpired(){
        return true;
   }

    @Override 
   public boolean isEnabled(){
    return true;
   }

   public boolean equals(Object o){
        if(this==o){ // if both references refer to the same object no need to check 
            return true;
        }

        if(o== null && getClass()!= o.getClass()){
            return false;
        }

        UserDetailsImplementation  user = (UserDetailsImplementation)o;
        return Objects.equals(id,user.id);

   }


}
