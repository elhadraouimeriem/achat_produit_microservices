package com.example.achat.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchatReq {
    //private Date date;
    private String currency;
    private List<Long> productsIds;
}
