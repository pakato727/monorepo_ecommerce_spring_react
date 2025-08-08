package com.example.ecommercespring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommercespring.model.Role;
import com.example.ecommercespring.model.User;
import com.example.ecommercespring.repo.AccountRepository;
import com.example.ecommercespring.repo.RoleRepository;
import com.example.ecommercespring.repo.UserRepository;

@Service
public class RoleService {
	@Autowired
	UserRepository ur;
	
	@Autowired
	AccountRepository ar;
	
	@Autowired
	RoleRepository rr;
	
	public boolean insertRole( Role role) {
		try {
	        // Salva l'utente nel database
	        rr.save(role);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
