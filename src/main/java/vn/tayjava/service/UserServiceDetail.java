package vn.tayjava.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.tayjava.repository.UserRepository;

@Service
public record UserServiceDetail(UserRepository userRepository) {

    public UserDetailsService userDetailsService() {
        return userRepository::findByUsername;
    }
}
