package com.example.ecommercespring.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommercespring.model.User;
import com.example.ecommercespring.repo.AccountRepository;
import com.example.ecommercespring.repo.RoleRepository;
import com.example.ecommercespring.repo.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository ur;

    @Autowired
    AccountRepository ar;

    @Autowired
    RoleRepository rr;

    public boolean insertUser(User user) {
        try {
            ur.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> readAll() {
		List<User> users = ur.findAll().stream()
				.filter(user -> user.getAccount().getRole().getId() != 4)
				.collect(Collectors.toList());
        return users;
    }

    public User getUser(int id) {
        return ur.findById(id).orElse(null);

    }

    public Page<User> findAllPageUsers(Pageable pageable) {
        return ur.findAll(pageable);
    }

    ;


}
