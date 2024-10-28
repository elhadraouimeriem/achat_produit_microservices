package com.example.achat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Date date;
    private String currency;
    private Double total;

    @ElementCollection
    @CollectionTable(name = "achat_products", joinColumns = @JoinColumn(name = "achat_id"))
    private List<Long> productsIds;

}
