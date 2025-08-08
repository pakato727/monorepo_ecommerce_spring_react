package com.example.ecommercespring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommercespring.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role findByRuolo(String string);

}
