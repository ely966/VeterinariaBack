package com.example.demo.security;


import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userRes = userRepo.findByEmail(email);
        if(userRes == null)  /**Esta vacío**///userRes.isEmpty()
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        /**Si existe**/
        User user = userRes.get();
        if(user.getRole().compareTo("ADMIN") ==0) {//Si el role que tiene es admin
        	 return new org.springframework.security.core.userdetails.User(
                     email,
                     user.getPassword(),
                     Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }else if (user.getRole().compareTo("VETERINARIO") ==0) {//Si el role que tiene es veterinario
       	 return new org.springframework.security.core.userdetails.User(
                 email,
                 user.getPassword(),
                 Collections.singletonList(new SimpleGrantedAuthority("ROLE_VETERINARIO")));
    }
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE")));
       // }
    }
}
//else if (userRes.get().getRol().equals("admin") ){
//User user = userRes.get();
//return new org.springframework.security.core.userdetails.User(
//      email,
 //     user.getPassword(),
  //    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//}/**falta añadir el de veterinario**/
//else 