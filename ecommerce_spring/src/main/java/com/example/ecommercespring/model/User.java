package com.example.ecommercespring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false, length = 30)
	private String name;
	
	@Column(nullable=false, length = 30)
	private String surname;
	
	@Column(name ="fiscal_code", nullable = false, length = 16)
	private String fiscalCode;
	
	@Column(name ="birth_date",  nullable = false)
	private Date birthDate;
	
	@Column(nullable=false, length = 30)
	private String gender;
	
	@Column(nullable=false, length = 30)
	private String address;
	
	@Column(name ="zip_code",nullable=false)
	private int zipCode;
	
	@Column(nullable=false, length = 30)
	private String city;
	
	@Column(nullable=false, length = 30)
	private String province;
	
	@JsonIgnore
	@OneToOne(mappedBy="user", cascade = CascadeType.ALL)
	private Account account;
	
}
