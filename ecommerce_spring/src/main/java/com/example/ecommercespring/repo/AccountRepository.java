package com.example.ecommercespring.repo;

import com.example.ecommercespring.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

	Optional<Account> findByEmail(String email);

	Account findByUsername(String username);
}

