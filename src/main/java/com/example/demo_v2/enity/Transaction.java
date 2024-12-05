package com.example.demo_v2.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "id")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", referencedColumnName = "id")
    private Account receiverAccount;

    private Double amount;

    @Column(name = "created_at")
    private Date createdAt;

}
