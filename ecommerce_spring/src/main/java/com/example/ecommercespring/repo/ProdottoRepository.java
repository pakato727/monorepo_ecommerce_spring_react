package com.example.ecommercespring.repo;

import com.example.ecommercespring.model.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;



@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Integer>{
    Page<Prodotto> findAll(Pageable pageable);
}
