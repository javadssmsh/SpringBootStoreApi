package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductDto {
    public Long id ;
    public String name;
    public BigDecimal price;
}
