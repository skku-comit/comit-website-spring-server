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
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(userEntity);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return new CustomUserDetails(userEntity);
    }
}
