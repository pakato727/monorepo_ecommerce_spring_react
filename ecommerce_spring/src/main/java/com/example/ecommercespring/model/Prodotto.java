package com.example.ecommercespring.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Table(name="products")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Prodotto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="nome_prodotto", nullable=false, length = 30)
	private String nomeProdotto;
	
	@Lob
    private byte[] data;
	
	@Column
	private String base64Image;
	
	@Column(nullable=false, length = 100)
	private String descrizione;
	
	@Column(columnDefinition="DECIMAL(6,2)", nullable=false)
	private Double prezzo;
	
	@Column(nullable=true)
	private int quantita;
	
	@Column(nullable=false, length = 30)
	private String categoria;
	
	@Column(nullable=false, length = 30)
	private String marca;

	@ManyToMany(mappedBy = "prodotti")
	private List<Cart> carts = new ArrayList<>();

}
