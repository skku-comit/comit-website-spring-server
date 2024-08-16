package com.example.comitserver.service;

import com.example.comitserver.dto.CustomUserDetails;
import com.example.comitserver.entity.UserEntity;
import com.example.comitserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByUsername(username);
        if (userData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증
            return new CustomUserDetails(userData);
        }

        return null;
    }
}