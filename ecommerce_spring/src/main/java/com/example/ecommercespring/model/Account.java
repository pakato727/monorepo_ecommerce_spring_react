package com.example.ecommercespring.model;


import jakarta.persistence.*;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class Account{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false, length = 30)
	private String username;
	
	@Column(nullable=false, length = 50)
	private String email;
	
	@Column(nullable=false, length = 255)
	private String password;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_user", referencedColumnName = "id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="id_role", referencedColumnName = "id")
    private Role role;

	@Column(nullable = false, name = "autenticato")
	private boolean enabled=false;

	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Cart cart;


	

}
