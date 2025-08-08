package com.example.ecommercespring.service;

import com.example.ecommercespring.model.Account;
import com.example.ecommercespring.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        if (!account.isEnabled()) {
            throw new DisabledException("L'account non è abilitato.");
        }

        return User.withUsername(account.getEmail())
                .password(account.getPassword()) 
                .roles(account.getRole().getRuolo())
                .build();
    }
}
