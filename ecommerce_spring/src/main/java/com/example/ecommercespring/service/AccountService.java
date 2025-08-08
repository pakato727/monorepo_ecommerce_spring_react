package com.example.ecommercespring.service;

import com.example.ecommercespring.model.Account;
import com.example.ecommercespring.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {
	
	
	@Autowired
	AccountRepository ar;
	
	
	public boolean insertAccount( Account account) {
		try {
	        ar.save(account);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public List<Account> readAll(){
		return ar.findAll();
	}

	public Account getAccount(int id) {
		return ar.findById(id).orElse(null);
	}
}
