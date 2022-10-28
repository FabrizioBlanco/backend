package com.proyecto.integrador.Security.security.userpincal;

import com.proyecto.integrador.Security.model.Users;
import com.proyecto.integrador.Security.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found -> user name or password"+username));
        return UserPrinciple.build(users);
    }
}
