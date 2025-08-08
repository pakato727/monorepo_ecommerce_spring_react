package com.example.ecommercespring.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name="carts")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    private Account account;

    @Column(name="email")
    private String email;

    @ManyToMany
    @JoinTable(
            name = "carrello_prodotto",
            joinColumns = @JoinColumn(name = "carrello_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();
}