package com.example.comitserver.service.auth;

import com.example.comitserver.config.auth.CustomUserDetails;
import com.example.comitserver.entity.User;
import com.example.comitserver.repository.NewUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final NewUserRepository newUserRepository;

    public CustomUserDetailsService(NewUserRepository newUserRepository) {
        this.newUserRepository = newUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = newUserRepository.findByEmail(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + name));
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = newUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return new CustomUserDetails(user);
    }
}
