package com.example.achat.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatDTO {
    private Long id;
    private Date date;
    private String currency;
    private Double total;
    private List<ProductDTO> products;
}
