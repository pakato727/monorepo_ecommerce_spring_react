package com.example.ecommercespring.service;

import com.example.ecommercespring.model.Prodotto;
import com.example.ecommercespring.model.User;
import com.example.ecommercespring.repo.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;



import java.util.List;

@Service
public class ProdottoService {
	@Autowired
	ProdottoRepository pr;
	
	public boolean insertProdotto(Prodotto prodotto) {
		if(prodotto != null) {
			pr.save(prodotto);
			return true;
		}
		return false;
	}
	
	public List<Prodotto> readAll(){
		return pr.findAll();
	}
	
	public Prodotto getProdotto(int id) {
        return pr.findById(id).orElse(null);
    }


	public Page<Prodotto> findAllPageProd(Pageable pageable) {
		return pr.findAll(pageable);
	}
}
