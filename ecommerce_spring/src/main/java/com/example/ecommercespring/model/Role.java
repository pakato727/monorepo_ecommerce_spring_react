package com.example.ecommercespring.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(unique=true, name="role")
	private String ruolo;
	
	@JsonIgnore
	@OneToMany(mappedBy="role", cascade=CascadeType.ALL)
	private List<Account> account;

}
