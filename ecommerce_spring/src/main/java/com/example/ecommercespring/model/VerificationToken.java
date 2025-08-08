package com.example.ecommercespring.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name="tokens")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique =true)
    private String token;

    @OneToOne
    @JoinColumn(referencedColumnName="id", name = "account_id")
    private Account account;

    private LocalDateTime expiryDate;



    public VerificationToken(Account account, String token) {
        this.account = account;
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusHours(24); // Scadenza dopo 24 ore
    }

}
