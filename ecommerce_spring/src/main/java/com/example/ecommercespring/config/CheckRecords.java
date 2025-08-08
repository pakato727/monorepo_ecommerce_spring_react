package com.example.ecommercespring.config;

import com.example.ecommercespring.model.Account;
import com.example.ecommercespring.model.Cart;
import com.example.ecommercespring.model.Role;
import com.example.ecommercespring.model.User;
import com.example.ecommercespring.repo.AccountRepository;
import com.example.ecommercespring.repo.RoleRepository;
import com.example.ecommercespring.repo.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;

@Configuration
public class CheckRecords {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AccountRepository ar;
    
    @Autowired
    private UserRepository ur;
    
    @Autowired
    private RoleRepository rr;

    
    @PostConstruct
    public void init() {
        if (rr.findAll().size() == 0) {
            
        	 Role roleSAdmin = new Role();
             roleSAdmin.setRuolo("SADMIN");
             rr.save(roleSAdmin);
        	
            Role roleAdmin = new Role();
            roleAdmin.setRuolo("ADMIN");
            rr.save(roleAdmin);

            Role roleGuest = new Role();
            roleGuest.setRuolo("GUEST");
            rr.save(roleGuest);

            Role roleBanned = new Role();
            roleBanned.setRuolo("BANNED | DELETED");
            rr.save(roleBanned);
            
           
        }

        if (ar.findAll().size() == 0) {
            // Crea un account root
            Account account = new Account();
            account.setUsername("root");
            account.setPassword(passwordEncoder.encode("root"));
            account.setEmail("admin@gmail.com");
            account.setEnabled(true);

            Cart cart=new Cart();
            cart.setAccount(account);
            cart.setEmail(account.getEmail());

            
            Role adminRole = rr.findByRuolo("SADMIN"); 
            if (adminRole != null) {
                account.setRole(adminRole);
            }
            
            User user= new User();
            user.setName("admin");
            user.setSurname("admin");
            Date x = Date.valueOf("2016-10-10");
            user.setBirthDate(x);
            user.setAddress("admin");
            user.setCity("admin");
            user.setGender("admin");
            user.setProvince("admin");
            user.setZipCode(0000);
            user.setFiscalCode("0000000000");
            account.setUser(user);
            account.setCart(cart);
            user.setAccount(account);
            
            
            
            ur.save(user);
            ar.save(account);
           
        }
    }}
    
